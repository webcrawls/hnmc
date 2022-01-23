package sh.kaden.hnmc.menu;

import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a menu that can be shown to a player.
 */
public interface Menu {

    /**
     * Opens the menu for the player.
     *
     * @param player the player
     */
    void open(final @NonNull Player player);

}
