package sh.kaden.hnmc.menu;

import broccolai.corn.paper.item.special.BookBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.MultiTextElement;
import org.incendo.interfaces.paper.element.TextElement;
import org.incendo.interfaces.paper.type.BookInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * The main menu for accessing stories on Hacker News.
 */
public class BrowseMenu implements Menu {

    private static final @NonNull MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final int MAX_STORIES_PER_PAGE = LINES_PER_BOOK_PAGE - HEADER_LINE_COUNT;
    private final @NonNull MenuService menuService;
    private final @NonNull HNService hnService;

    /**
     * Constructs {@code BrowseMenu}.
     *
     * @param menuService the menu service
     * @param hnService the hn service
     */
    public BrowseMenu(final @NonNull MenuService menuService,
                      final @NonNull HNService hnService) {
        this.menuService = menuService;
        this.hnService = hnService;
    }

    @Override
    public void open(final @NonNull Player player) {
        final int stories = this.hnService.getTopStories().size();
        final int pages = stories % MAX_STORIES_PER_PAGE;

        final List<TextElement> pageElements = new ArrayList<>();

        for (int i = 0; i < pages; i++) {
            pageElements.add(this.createBrowsePage(i, player));
        }

        BookInterface
                .builder()
                .addTransform((pane, view) -> {
                    for (final TextElement page : pageElements) {
                        pane = pane.add(page);
                    }

                    return pane;
                })
                .build()
                .open(PlayerViewer.of(player));
    }

    /**
     * Constructs a page containing stories listed in the given index.
     * The index is used to determine what pages to show. This is based off of the Minecraft book page size.
     *
     * @param page   the page number
     * @param viewer the viewer of this page
     * @return the page
     */
    private @NonNull TextElement createBrowsePage(final int page,
                                                  final @NonNull Player viewer) {
        int firstStory = page * MAX_STORIES_PER_PAGE;
        int lastStory = firstStory + MAX_STORIES_PER_PAGE;

        final List<HNStory> stories = this.hnService.getTopStories(firstStory, lastStory);
        final List<TextElement> pageElements = new ArrayList<>();

        pageElements.add(TextElement.of(Menu.header()));

        for (final HNStory story : stories) {
            String title = story.title();

            if (title.length() > 19) {
                title = title.substring(0, 19) + "...";
            }

            final boolean wasTruncated = story.title().length() != title.length();
            final TextComponent.Builder hoverBuilder = Component.text();

            if (wasTruncated) {
                hoverBuilder.append(Component.text(story.title()));
                hoverBuilder.append(Component.newline());
                hoverBuilder.append(Component.newline());
            }

            hoverBuilder.append(Component.text("Click to open").decorate(TextDecoration.ITALIC));

            pageElements.add(TextElement.of(
                    TextElement.of(Component.text(title)
                            .append(Component.newline())
                            .hoverEvent(HoverEvent.showText(hoverBuilder))),
                    (ctx) -> this.menuService.openItem(viewer, story)
            ));
        }

        return new MultiTextElement(pageElements);
    }
}
