package sh.kaden.hnmc.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.TextElement;
import org.incendo.interfaces.paper.type.BookInterface;
import sh.kaden.hnmc.hn.HNService;
import sh.kaden.hnmc.hn.HNStory;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The menu used to view a single story.
 */
public class StoryMenu implements Menu {

    private static final @NonNull MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final @NonNull SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("d/M/y");
    private static final @NonNull SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("d/M/y m:H a");
    private final @NonNull MenuService menuService;
    private final @NonNull HNService hnService;

    /**
     * Constructs {@code StoryMenu}.
     *
     * @param menuService the menu service
     * @param hnService   the hn service
     */
    public StoryMenu(final @NonNull MenuService menuService,
                     final @NonNull HNService hnService) {
        this.menuService = menuService;
        this.hnService = hnService;
    }

    /**
     * Opens the menu for the player. This method will throw an error.
     *
     * @param player the player
     * @throws UnsupportedOperationException this method is not implemented. Please provide an
     *                                       {@link sh.kaden.hnmc.hn.HNItem} using {@link #open(Player, HNStory)}
     */
    @Override
    public void open(final @NonNull Player player) {
        throw new UnsupportedOperationException("Unsupported operation. Please use #open(Player, HNItem) instead.");
    }

    /**
     * Opens the browse menu for a player.
     *
     * @param player the player
     * @param story  the item to open
     */
    public void open(final @NonNull Player player,
                     final @NonNull HNStory story) {
        URL _url = null;

        try {
            if (story.text().startsWith("http")) {
                _url = new URL(story.text());
            }
        } catch (final Exception e) {
            _url = null;
        }

        final boolean isUrl = _url != null;
        final URL pageUrl = _url;

        BookInterface.builder()
                .addTransform((pane, view) -> {
                    return pane.add(TextElement.of(
                            Component.text()
                                    .append(Menu.header())
                                    .append(Component.text("by " + story.by() + " / ")
                                            .append(Component.text(DATE_FORMAT.format(Date.from(story.time())))
                                                    .hoverEvent(HoverEvent.showText(Component.text(DATE_TIME_FORMAT.format(Date.from(story.time()))))))
                                    )
                                    .append(Component.newline())
                                    .append(isUrl ?
                                            Component.text()
                                                    .append(Component.text(story.text()))
                                                    .hoverEvent(HoverEvent.showText(Component.text(story.text(), TextColor.fromCSSHexString("#0645AD"), TextDecoration.UNDERLINED)))
                                                    .clickEvent(ClickEvent.openUrl(pageUrl))
                                                    .build() :
                                            Component.text()
                                                    .append(Component.text(story.text()))
                                                    .build())
                                    .build()
                    ));
                })
                .build()
                .open(PlayerViewer.of(player));
    }
}
