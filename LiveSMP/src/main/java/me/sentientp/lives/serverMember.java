package me.sentientp.lives;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.net.UnknownHostException;
import java.util.Objects;

public class serverMember {
    String Name;
    int deaths;
    int kills;
    int lives;
    int donations;
    serverMember(Player p) {
        NamespacedKey killsKey = new NamespacedKey(Lives.getPlugin(), "kills");
        NamespacedKey deathsKey = new NamespacedKey(Lives.getPlugin(), "deaths");
        NamespacedKey livesKey = new NamespacedKey(Lives.getPlugin(), "lives");
        NamespacedKey donationsKey = new NamespacedKey(Lives.getPlugin(), "donations");
        PersistentDataContainer data = p.getPersistentDataContainer();
        this.kills = data.get(killsKey, PersistentDataType.INTEGER);
        this.deaths =data.get(deathsKey, PersistentDataType.INTEGER);
        this.lives =data.get(livesKey, PersistentDataType.INTEGER);
        this.donations =data.get(donationsKey, PersistentDataType.INTEGER);
        this.Name = p.getName();
    }

    public void updateServerMember () throws UnknownHostException {
        NamespacedKey killsKey = new NamespacedKey(Lives.getPlugin(), "kills");
        NamespacedKey deathsKey = new NamespacedKey(Lives.getPlugin(), "deaths");
        NamespacedKey livesKey = new NamespacedKey(Lives.getPlugin(), "lives");
        NamespacedKey donationsKey = new NamespacedKey(Lives.getPlugin(), "donations");
        PersistentDataContainer data = Objects.requireNonNull(Bukkit.getPlayer(this.Name)).getPersistentDataContainer();
        data.set(killsKey, PersistentDataType.INTEGER, this.kills);
        data.set(deathsKey, PersistentDataType.INTEGER, this.deaths);
        data.set(livesKey, PersistentDataType.INTEGER, this.lives);
        data.set(donationsKey, PersistentDataType.INTEGER, this.donations);
    }


}
