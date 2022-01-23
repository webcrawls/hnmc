package sh.kaden.hnmc.menu;

import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import sh.kaden.hnmc.hn.HNService;

/**
 * Handles the creation and viewing of menus.
 */
public class MenuService {

    private final @NonNull BrowseMenu browseMenu;

    /**
     * Constructs {@code MenuService}.
     *
     * @param hnService the HNService
     */
    public MenuService(final @NonNull HNService hnService) {
        this.browseMenu = new BrowseMenu(hnService);
    }

    /**
     * Opens the browse menu for a player.
     *
     * @param player the player
     */
    public void openBrowse(final @NonNull Player player) {
        this.browseMenu.open(player);
    }

}
