package sh.kaden.hnmc.hn;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.Instant;

/**
 * A HackerNews story.
 */
public class HNStory extends HNItem {

    private final @NonNull String by;
    private final @NonNull String url;
    private final @NonNull String title;
    private final @NonNull Instant time;
    private final int score;

    /**
     * Constructs {@code HNStory}.
     *
     * @param by    the author
     * @param url   the url
     * @param title the title
     * @param time  the time
     * @param score the score
     */
    public HNStory(final @NonNull String by,
                   final @NonNull String url,
                   final @NonNull String title,
                   final @NonNull Instant time,
                   final int score) {
        this.by = by;
        this.url = url;
        this.title = title;
        this.time = time;
        this.score = score;
    }

    /**
     * Returns the author of this story.
     *
     * @return the author
     */
    public @NonNull String by() {
        return this.by;
    }

    /**
     * Returns the URL this story links to.
     *
     * @return the URL
     */
    public @NonNull String url() {
        return this.url;
    }

    /**
     * Returns the title of this story.
     *
     * @return the title
     */
    public @NonNull String title() {
        return this.title;
    }

    /**
     * Returns the {@link Instant} this story was created at.
     *
     * @return the creation time
     */
    public @NonNull Instant time() {
        return this.time;
    }

    /**
     * Returns the score of this story.
     *
     * @return the score
     */
    public int score() {
        return this.score;
    }

    @Override
    public @NonNull String toString() {
        return "HNStory{" +
                "by=" + this.by + "," +
                "url=" + this.url + "," +
                "title=" + this.title + "," +
                "time=" + this.time.toString() + "," +
                "score=" + this.score +
                "}";
    }

}
