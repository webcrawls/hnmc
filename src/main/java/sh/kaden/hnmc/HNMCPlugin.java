package sh.kaden.hnmc;

import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import sh.kaden.hnmc.hn.HNService;
import sh.kaden.hnmc.menu.MenuService;

/**
 * The Paper entrypoint for the HNMC plugin.
 */
public final class HNMCPlugin extends JavaPlugin {

    private @MonotonicNonNull ItemService itemService;
    private @MonotonicNonNull HNService hnService;
    private @MonotonicNonNull MenuService menuService;
    private @MonotonicNonNull CommandService commandService;

    @Override
    public void onEnable() {
        // Initialization
        this.itemService = new ItemService(this);
        this.hnService = new HNService(this);
        this.menuService = new MenuService(this.hnService);
        this.commandService = new CommandService(this, this.menuService, this.itemService);

        // Setup
        this.itemService.registerRecipe();
        this.commandService.registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
