package sh.kaden.hnmc;

import broccolai.corn.paper.item.PaperItemBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * Handles the construction of the recipe and item.
 */
public class ItemService {

    private static final @NonNull MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private final @NonNull JavaPlugin plugin;
    private final @NonNull NamespacedKey craftingKey;

    /**
     * Constructs {@code ItemService}.
     *
     * @param plugin the plugin
     */
    public ItemService(final @NonNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.craftingKey = NamespacedKey.fromString("hn_book", this.plugin);
    }

    /**
     * Returns an instance of the Hacker News book item.
     *
     * @return the HN book
     */
    public @NonNull ItemStack book() {
        return PaperItemBuilder.ofType(Material.KNOWLEDGE_BOOK)
                .name(MINI_MESSAGE.deserialize("<#ff6600><bold>Hacker News"))
                .lore(List.of(MINI_MESSAGE.deserialize("<gray>Click to view Hacker News")))
//                .setData() // book id
                .build();
    }

    /**
     * Registers the book recipe.
     */
    protected void registerRecipe() {
        final ShapedRecipe recipe = new ShapedRecipe(this.craftingKey, this.book());
        recipe.shape("#R#", "CBZ", "#J#");
        recipe.setIngredient('#', Material.IRON_BLOCK);
        recipe.setIngredient('R', Material.REDSTONE_BLOCK);
        recipe.setIngredient('C', Material.CLOCK);
        recipe.setIngredient('J', Material.COMPASS);
        recipe.setIngredient('Z', Material.COMPARATOR);
        recipe.setIngredient('B', Material.BOOK);
        Bukkit.addRecipe(recipe);
        this.plugin.getLogger().info("Registered hn_book recipe.");
    }

}
