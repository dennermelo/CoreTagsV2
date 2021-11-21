package com.github.dennermelo.core.tags.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final String name;
    private final List<Tag> tags, tagsInUse;

    public User(String name) {
        this.name = name;
        this.tags = new ArrayList<>();
        this.tagsInUse = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags.clear();
        this.tags.addAll(tags);
    }

    public List<Tag> getTagsInUse() {
        return tagsInUse;
    }

    public void setTagsInUse(List<Tag> tagsInUse) {
        this.tagsInUse.clear();
        this.tagsInUse.addAll(tagsInUse);
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void addTagInUse(Tag tag) {
        tagsInUse.add(tag);
    }

    public void removeTagInUse(Tag tag) {
        tagsInUse.remove(tag);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    public Tag getTagByName(String name) {
        for (Tag tag : tags) {
            if (tag.getName().equalsIgnoreCase(name)) {
                return tag;
            }
        }
        return null;
    }

    public Tag getInUseTagByName(String name) {
        for (Tag tag : tagsInUse) {
            if (tag.getName().equalsIgnoreCase(name)) {
                return tag;
            }
        }
        return null;
    }

}
