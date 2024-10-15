package sh.kaden.hnmc.menu;

import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Handles the creation and viewing of menus.
 */
public class MenuService {

    private final @NonNull BrowseMenu browseMenu;
    private final @NonNull StoryMenu storyMenu;

    /**
     * Constructs {@code MenuService}.
     *
     * @param hnService the HNService
     */
    public MenuService(final @NonNull HNService hnService) {
        this.browseMenu = new BrowseMenu(this, hnService);
        this.storyMenu = new StoryMenu(this, hnService);
    }

    /**
     * Opens the browse menu for a player.
     *
     * @param player the player
     */
    public void openBrowse(final @NonNull Player player) {
        this.browseMenu.open(player);
    }

    /**
     * Opens an item for a player.
     *
     * @param player the player
     * @param item   the item
     */
    public void openItem(final @NonNull Player player,
                         final @NonNull HNItem item) {
        if (item instanceof HNStory story) {
            this.storyMenu.open(player, story);
            return;
        }

        throw new UnsupportedOperationException("Could not find appropriate menu to display for type '"+item.getClass().getSimpleName()+"'.");
    }

}
