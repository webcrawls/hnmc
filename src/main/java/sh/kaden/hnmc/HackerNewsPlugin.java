package sh.kaden.hnmc;

import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import sh.kaden.hnmc.menu.MenuService;

/**
 * The Paper entrypoint for the HNMC plugin.
 */
public final class HackerNewsPlugin extends JavaPlugin {

    private @MonotonicNonNull ItemService itemService;

    @Override
    public void onEnable() {
        // Initialization
        this.itemService = new ItemService(this);

        // Setup
        this.itemService.registerRecipe();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
