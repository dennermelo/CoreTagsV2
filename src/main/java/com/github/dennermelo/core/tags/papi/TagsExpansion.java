package com.github.dennermelo.core.tags.papi;

import com.github.dennermelo.core.tags.CoreTags;
import com.github.dennermelo.core.tags.model.Tag;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class TagsExpansion extends PlaceholderExpansion {


    @Override
    public @NotNull String getIdentifier() {
        return "CoreTags";
    }

    @Override
    public @NotNull String getAuthor() {
        return "String";
    }

    @Override
    public @NotNull String getVersion() {
        return CoreTags.getInstance().getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("first_tag")) {
            if (CoreTags.getUserManager().hasUser(player.getName())
                    && CoreTags.getUserManager().getUser(player.getName()).getTagsInUse().size() > 0) {
                // Get the first tag from user
                return CoreTags.getUserManager().getUser(player.getName()).getTagsInUse().get(0).getFormat();
            }
            return "Nenhuma";
        }
        if (params.equalsIgnoreCase("last_tag")) {
            if (CoreTags.getUserManager().hasUser(player.getName())
                    && CoreTags.getUserManager().getUser(player.getName()).getTagsInUse().size() > 0) {
                // Get the last tag from user
                return CoreTags.getUserManager().getUser(player.getName()).getTagsInUse().get(CoreTags.getUserManager().getUser(player.getName()).getTagsInUse().size() - 1).getFormat();
            }
            return "Nenhuma";
        }
        if (params.equalsIgnoreCase("tags")) {
            if (CoreTags.getUserManager().hasUser(player.getName())) {
                // Get using tags from user
                return CoreTags.getUserManager().getUser(player.getName()).getTagsInUse().stream().map(Tag::getFormat).collect(Collectors.joining(" "));
            }
            return "Nenhuma";
        }
        return super.onRequest(player, params);
    }
}
