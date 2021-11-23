package com.github.dennermelo.core.tags.listener.player.legendchat;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import com.github.dennermelo.core.tags.CoreTags;
import com.github.dennermelo.core.tags.model.Tag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerLegendChatListener implements Listener {

    @EventHandler
    public void onChat(ChatMessageEvent event) {

        if (event.getTags().contains("coretags_using")) {
            List<String> tags = new ArrayList<>();
            if (CoreTags.getUserManager().getUser(event.getSender().getName()).getTagsInUse().size() > 0) {
                tags = CoreTags.getUserManager().getUser(event.getSender().getName()).getTagsInUse().stream().map(Tag::getFormat).collect(Collectors.toList());
                event.setTagValue("coretags_using", String.join(" ", tags));
            }
        }
        if (event.getTags().contains("coretags_first_tag")) {
            if (CoreTags.getUserManager().hasUser(event.getSender().getName())
                    && CoreTags.getUserManager().getUser(event.getSender().getName()).getTagsInUse().size() > 0) {
                event.setTagValue("coretags_first_tag", CoreTags.getUserManager().getUser(event.getSender().getName()).getTagsInUse().get(0).getFormat());
            }
        }
        if (event.getTags().contains("coretags_last_tag")) {
            if (CoreTags.getUserManager().hasUser(event.getSender().getName())
                    && CoreTags.getUserManager().getUser(event.getSender().getName()).getTagsInUse().size() > 0) {
                event.setTagValue("coretags_last_tag", CoreTags.getUserManager().getUser(event.getSender().getName()).getTagsInUse().get(CoreTags.getUserManager().getUser(event.getSender().getName()).getTagsInUse().size() - 1).getFormat());
            }
        }
    }
}
