package sh.kaden.hnmc.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import sh.kaden.hnmc.util.Colours;

/**
 * Represents a menu that can be shown to a player.
 */
public interface Menu {

    int LINES_PER_BOOK_PAGE = 15;
    int HEADER_LINE_COUNT = 2;
    @NonNull MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    /**
     * Returns the menu header.
     *
     * @return the header
     */
    static @NonNull Component header() {
        return MINI_MESSAGE.deserialize("<" + Colours.ORANGE.asHexString() + ">▋▌▍▎▏</" + Colours.ORANGE.asHexString() + "> <dark_gray>Hacker News</dark_gray> <" + Colours.ORANGE.asHexString() + ">▏▎▍▌▋</" + Colours.ORANGE.asHexString() + ">")
                .append(Component.newline())
                .append(Component.text("top | ask | show | jobs"))
                .append(Component.newline());
    }

    /**
     * Opens the menu for the player.
     *
     * @param player the player
     */
    void open(final @NonNull Player player);

}
