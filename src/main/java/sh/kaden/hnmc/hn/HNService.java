package sh.kaden.hnmc.hn;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Responsible for fetching the latest posts from HackerNews.
 */
public class HNService {

    private static final @NonNull Gson GSON = new Gson();
    private static final @NonNull String API_URL = "https://hacker-news.firebaseio.com/v0/";
    private static final int UPDATE_MINS = 10;
    private static final int MAX_TOP_STORIES_TO_LOAD = 15; // how many top stories should be loaded from the API
    private static final int UPDATE_TICKS = UPDATE_MINS * 20 * 60; // multiply by 20 as 20 ticks per sec in MC
    private final @NonNull JavaPlugin plugin;
    private final @NonNull Executor httpExecutor;
    private final @NonNull Executor cfExecutor;
    private final @NonNull HttpClient http;
    private final @NonNull List<HNItem> topStories;

    /**
     * Constructs {@code HNService}.
     * <p>
     * Constructing this class will register a repeating {@link BukkitRunnable} against {@code plugin} to update
     * the HN stories.
     *
     * @param plugin the plugin
     */
    public HNService(final @NonNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.httpExecutor = Executors.newSingleThreadExecutor();
        this.cfExecutor = Executors.newFixedThreadPool(2);
        this.http = HttpClient
                .newBuilder()
                .executor(this.httpExecutor)
                .build();
        this.topStories = new ArrayList<>();
        new BukkitRunnable() {
            @Override
            public void run() {
                topStories.clear();
                loadTopStoryIds()
                        .thenAccept(ids -> {
                            for (final int id : ids) {
                                try {
                                    final HNItem story = loadItem(id).join();
                                    topStories.add(story);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .exceptionally(err -> {
                            err.printStackTrace();
                            return null;
                        });
            }
        }.runTaskTimer(this.plugin, 0, UPDATE_TICKS);
    }

    /**
     * Returns all top stories currently loaded.
     *
     * @return the top stories
     */
    public @NonNull List<HNStory> getTopStories() {
        final List<HNStory> stories = new ArrayList<>();

        for (final HNItem item : this.topStories) {
            stories.add((HNStory) item);
        }

        return stories;
    }

    /**
     * Returns all top stories between the given indexes.
     *
     * @param min the min index
     * @param max the max index
     * @return the list of top stories
     */
    public @NonNull List<HNStory> getTopStories(final int min,
                                                final int max) {
        return this.getTopStories().subList(min, max);
    }

    /**
     * Retrieves the current top stories from HackerNews and returns it in a {@link CompletableFuture}.
     *
     * @return a list containing the IDs of the top stories.
     */
    private @NonNull CompletableFuture<@NonNull List<Integer>> loadTopStoryIds() {
        return CompletableFuture.supplyAsync(() -> {
            final HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(API_URL + "topstories.json"))
                    .build();

            final HttpResponse<String> resp;
            try {
                resp = this.http.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                this.plugin.getLogger().severe("Encountered an error while parsing topstories.json:");
                e.printStackTrace();
                return List.of();
            }
            final String body = resp.body();
            final JsonArray idArray = GSON.fromJson(body, JsonArray.class);

            final List<Integer> ids = new ArrayList<>();

            for (final JsonElement element : idArray) {
                if (!(element instanceof JsonPrimitive primitive)) {
                    throw new JsonParseException("Couldn't parse ID element.");
                }

                if (!primitive.isNumber()) {
                    throw new JsonParseException("Couldn't parse ID element");
                }

                final int number = primitive.getAsInt();
                ids.add(number);

                if (ids.size() >= MAX_TOP_STORIES_TO_LOAD) {
                    break;
                }
            }

            return ids;
        }, this.cfExecutor);
    }

    /**
     * Retrieves an HNItem with the given ID.
     *
     * @param id the id of the item
     * @return a list containing the IDs of the top stories.
     */
    private @NonNull CompletableFuture<@NonNull HNItem> loadItem(final int id) {
        return CompletableFuture.supplyAsync(() -> {
            final HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(API_URL + "item/" + id + ".json"))
                    .build();

            final HttpResponse<String> resp;
            try {
                resp = this.http.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            final String body = resp.body();
            this.plugin.getLogger().info("Loaded JSON from API:");
            this.plugin.getLogger().info(body);
            final JsonObject itemObject = GSON.fromJson(body, JsonObject.class);

            final String type = itemObject.get("type").getAsString();

            if (type.equals("story")) {
                final String by = itemObject.get("by").getAsString();
                final String title = itemObject.get("title").getAsString();
                final int score = itemObject.get("score").getAsInt();
                final long time = itemObject.get("time").getAsLong();
                final String text = itemObject.has("url") ?
                        itemObject.get("url").getAsString() :
                        itemObject.has("text") ?
                                itemObject.get("text").getAsString() :
                                "hnmc failed to load the text for this story.";

                final Instant instant = Instant.ofEpochSecond(time);

                return new HNStory(by, text, title, instant, score);
            } else {
                throw new UnsupportedOperationException("Unimplemented type: " + type);
            }
        }, this.cfExecutor);
    }

}
