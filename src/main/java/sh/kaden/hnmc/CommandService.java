package sh.kaden.hnmc;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.bukkit.parsers.PlayerArgument;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import sh.kaden.hnmc.menu.MenuService;

import java.util.Optional;
import java.util.function.Function;

/**
 * Responsible for the creation and registration of commands.
 */
public class CommandService {

    private static final @NonNull MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final @NonNull String HN_ADMIN_PERMISSION = "hnmc.command.use";
    private final @NonNull JavaPlugin plugin;
    private final @NonNull MenuService menuService;
    private final @NonNull ItemService itemService;
    private final @NonNull PaperCommandManager<CommandSender> manager;

    /**
     * Constructs {@code CommandService}.
     *
     * @param plugin      the plugin
     * @param menuService the {@link MenuService}
     * @param itemService the {@link ItemService}
     */
    public CommandService(final @NonNull JavaPlugin plugin,
                          final @NonNull MenuService menuService,
                          final @NonNull ItemService itemService) {
        this.plugin = plugin;
        this.menuService = menuService;
        this.itemService = itemService;
        try {
            final @NonNull Function<CommandSender, CommandSender> mapper = Function.identity();

            this.manager = new PaperCommandManager<>(
                    this.plugin,
                    AsynchronousCommandExecutionCoordinator.simpleCoordinator(),
                    mapper,
                    mapper
            );

            if (this.manager.queryCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
                this.manager.registerAsynchronousCompletions();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize CommandService", e);
        }

    }

    /**
     * Registers commands.
     */
    protected void registerCommands() {
        final Command.@NonNull Builder<CommandSender> builder = this.manager.commandBuilder("hn");

        this.manager.command(builder
                .permission(HN_ADMIN_PERMISSION)
                .handler(this::handleRoot));

        this.manager.command(builder
                .permission(HN_ADMIN_PERMISSION)
                .literal("open")
                .argument(PlayerArgument.optional("player"))
                .handler(this::handleOpen));

        this.manager.command(builder
                .permission(HN_ADMIN_PERMISSION)
                .literal("get")
                .handler(this::handleGet));

        this.manager.command(builder
                .permission(HN_ADMIN_PERMISSION)
                .literal("give")
                .argument(PlayerArgument.optional("player"))
                .handler(this::handleGet));
    }

    private void handleRoot(final @NonNull CommandContext<@NonNull CommandSender> ctx) {
        final CommandSender sender = ctx.getSender();
        sender.sendMessage(MINI_MESSAGE.deserialize("<#ff6600><bold>Hacker News</bold></#ff6600> <gray>v" + this.plugin.getDescription().getVersion() + " by</gray> <gradient:#5ecddb:#5ea9db>bluely"));
        sender.sendMessage(Component.empty());
        sender.sendMessage(MINI_MESSAGE.deserialize("<dark_gray> - <#d6d6d6>/hn <dark_gray>- <gray>help menu"));
        sender.sendMessage(MINI_MESSAGE.deserialize("<dark_gray> - <#d6d6d6>/hn open <dark_gray>- <gray>opens HN"));
        sender.sendMessage(MINI_MESSAGE.deserialize("<dark_gray> - <#d6d6d6>/hn open [player]<dark_gray>- <gray>opens HN for a player"));
        sender.sendMessage(MINI_MESSAGE.deserialize("<dark_gray> - <#d6d6d6>/hn get <dark_gray>- <gray>gets an HN book"));
        sender.sendMessage(MINI_MESSAGE.deserialize("<dark_gray> - <#d6d6d6>/hn give <player> <dark_gray>- <gray>gives an HN book to a player"));
    }

    private void handleOpen(final @NonNull CommandContext<@NonNull CommandSender> ctx) {
        final CommandSender sender = ctx.getSender();

        final Optional<Player> playerArgOpt = ctx.getOptional("player");

        if (playerArgOpt.isEmpty() && sender instanceof Player player) {
            this.menuService.openBrowse(player);
            return;
        }

        if (playerArgOpt.isEmpty()) {
            sender.sendMessage(Component.text("You must provide a valid player name to use this command from the console.", NamedTextColor.RED));
            return;
        }

        final Player player = playerArgOpt.get();
        this.menuService.openBrowse(player);
    }

    private void handleGet(final @NonNull CommandContext<@NonNull CommandSender> ctx) {
        final CommandSender sender = ctx.getSender();

        final Optional<Player> playerArgOpt = ctx.getOptional("player");

        if (playerArgOpt.isEmpty() && sender instanceof Player player) {
            final ItemStack itemStack = this.itemService.book();
            player.getInventory().addItem(itemStack);
            player.sendMessage(MINI_MESSAGE.deserialize("<gray>You have been given a Hacker News book. Enjoy!"));
            return;
        }

        if (playerArgOpt.isEmpty()) {
            sender.sendMessage(Component.text("You must provide a valid player name to use this command from the console.", NamedTextColor.RED));
            return;
        }

        final Player player = playerArgOpt.get();

        final ItemStack itemStack = this.itemService.book();
        player.getInventory().addItem(itemStack);
        sender.sendMessage(MINI_MESSAGE.deserialize("<gray>You have given <aqua>" + player.getName() + " a Hacker News book."));
    }

}
