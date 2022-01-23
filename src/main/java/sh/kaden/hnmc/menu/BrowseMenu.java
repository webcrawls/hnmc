package sh.kaden.hnmc.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.TextElement;
import org.incendo.interfaces.paper.type.BookInterface;
import sh.kaden.hnmc.hn.HNService;
import sh.kaden.hnmc.hn.HNStory;

import java.util.List;

public class BrowseMenu implements Menu {

    private final @NonNull HNService hnService;

    public BrowseMenu(final @NonNull HNService hnService) {
        this.hnService = hnService;
    }

    @Override
    public void open(@NonNull Player player) {
        final List<HNStory> stories = this.hnService.getTopStories();

        BookInterface.builder()
                .addTransform((pane, view) -> {
                    int i = 0;
                    for (final HNStory story : stories) {
                        if (i >= 15) {
                            break;
                        }
                        pane = pane.add(TextElement.of(Component.text(story.title())));
                        i++;
                    }

                    return pane;
                })
                .build()
                .open(PlayerViewer.of(player));
    }
}
