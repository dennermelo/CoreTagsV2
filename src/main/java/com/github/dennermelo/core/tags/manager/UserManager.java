package com.github.dennermelo.core.tags.manager;

import com.github.dennermelo.core.tags.CoreTags;
import com.github.dennermelo.core.tags.model.Tag;
import com.github.dennermelo.core.tags.model.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private final List<User> users;
    private final CoreTags instance;

    public UserManager(CoreTags instance) {
        users = new ArrayList<>();
        this.instance = instance;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public User getUser(String name) {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public boolean hasUser(String name) {
        return getUser(name) != null;
    }

    public List<User> getUsers() {
        return users;
    }

    public void load() {
        PreparedStatement stm = null;
        try {
            createTable();
            stm = CoreTags.getSqlManager().getConnection().prepareStatement("SELECT * FROM `coretags_players`");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                String username = rs.getString("name");
                List<Tag> usingTags = new ArrayList<>();
                List<Tag> tags = new ArrayList<>();
                for (String usingTagName : rs.getString("using_tags").split(",")) {
                    if (CoreTags.getTagManager().getTag(usingTagName) != null) {
                        usingTags.add(CoreTags.getTagManager().getTag(usingTagName));
                    }
                }
                for (String tagName : rs.getString("tags").split(",")) {
                    if (CoreTags.getTagManager().getTag(tagName) != null) {
                        tags.add(CoreTags.getTagManager().getTag(tagName));
                    }
                }
                User user = new User(username);
                user.setTags(tags);
                user.setTagsInUse(usingTags);
                this.addUser(user);
                stm.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!hasUser(player.getName())) {
                User user = new User(player.getName());
            }
        }

    }

    public void save() {
        PreparedStatement stm = null;
        try {
            stm = CoreTags.getSqlManager().getConnection().prepareStatement("DELETE FROM `coretags_players`");
            stm.executeUpdate();
            stm.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (User user : users) {
            try {
                stm = CoreTags.getSqlManager().getConnection().prepareStatement("INSERT INTO `coretags_players` (`name`, `using_tags`, `tags`) VALUES (?, ?, ?)");
                stm.setString(1, user.getName());
                stm.setString(2, user.getTagsInUse().stream().map(Tag::getName).reduce((s1, s2) -> s1 + "," + s2).orElse(""));
                stm.setString(3, user.getTags().stream().map(Tag::getName).reduce((s1, s2) -> s1 + "," + s2).orElse(""));
                stm.executeUpdate();
                stm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void createTable() {
        PreparedStatement stm = null;
        try {
            stm = CoreTags.getSqlManager().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `coretags_players` (`name` VARCHAR(16) NOT NULL, `using_tags` VARCHAR(255) NOT NULL, `tags` VARCHAR(255) NOT NULL, PRIMARY KEY (`name`))");
            stm.executeUpdate();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
