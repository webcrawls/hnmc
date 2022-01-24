package sh.kaden.hnmc.hn;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.Instant;

/**
 * A HackerNews story.
 */
public class HNStory extends HNItem {

    private final @NonNull String by;
    private final @NonNull String text;
    private final @NonNull String title;
    private final @NonNull Instant time;
    private final int score;

    /**
     * Constructs {@code HNStory}.
     *
     * @param by    the author
     * @param text   the text
     * @param title the title
     * @param time  the time
     * @param score the score
     */
    public HNStory(final @NonNull String by,
                   final @NonNull String text,
                   final @NonNull String title,
                   final @NonNull Instant time,
                   final int score) {
        this.by = by;
        this.text = text;
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
     * Returns the text contained within this story. This may be a URL or the story text.
     *
     * @return the URL
     */
    public @NonNull String text() {
        return this.text;
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
                "url=" + this.text + "," +
                "title=" + this.title + "," +
                "time=" + this.time.toString() + "," +
                "score=" + this.score +
                "}";
    }

}
