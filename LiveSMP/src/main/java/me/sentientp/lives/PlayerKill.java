package me.sentientp.lives;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.net.UnknownHostException;
import java.util.Objects;

public class PlayerKill implements Listener {



    public PlayerKill() {
    }


    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) throws NullPointerException, UnknownHostException {
        Player Person = event.getEntity();
        Player Killer = event.getEntity().getKiller();
        try {
            assert Killer != null;
            String KillerName = Killer.getName();
            String PersonName = Person.getName();
            event.setDeathMessage(ChatColor.RED+KillerName+ " whooped "+PersonName+"'s ass");
            Bukkit.broadcastMessage(KillerName+" stole a life from "+PersonName);

            serverMember Kira = new serverMember(Killer);
            serverMember victim = new serverMember(Person);
            Kira.lives+=1;
            victim.lives-=1;

            Kira.kills+=1;
            victim.deaths+=1;

            Person.sendMessage(ChatColor.RED+"You have "+victim.lives+" left. dont lose any more!!");
            Killer.sendMessage(ChatColor.GREEN+"You have "+Kira.lives+" total lives! kill more players to get more!");
            if (victim.lives<=0) {
                for (Player player:Bukkit.getServer().getOnlinePlayers()) {
                    player.sendTitle(Person.getName()+" perished.", "they can be revived if they are donated lives", 1, 50, 1);
                }
                Objects.requireNonNull(Bukkit.getPlayer(Person.getName())).setGameMode(GameMode.SPECTATOR);
            }
            Kira.updateServerMember();
            victim.updateServerMember();


        } catch (NullPointerException | UnknownHostException e) {
            serverMember person = new serverMember(Person);
            person.lives-=1;
            Person.sendMessage(ChatColor.RED+"You have "+person.lives+" left. dont lose any more!!");
            if (person.lives<=0) {
                for (Player player:Bukkit.getServer().getOnlinePlayers()) {
                    player.sendTitle(person.Name, "has parished.", 1, 20, 1);
                }
                Person.setGameMode(GameMode.SPECTATOR);
            }
            person.updateServerMember();


        }

    }




    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if (p.hasPlayedBefore()) {
            Bukkit.broadcastMessage(ChatColor.GREEN+"Greetings, "+p.getName());
        }
        NamespacedKey killsKey = new NamespacedKey(Lives.getPlugin(), "kills");
        NamespacedKey deathsKey = new NamespacedKey(Lives.getPlugin(), "deaths");
        NamespacedKey livesKey = new NamespacedKey(Lives.getPlugin(), "lives");
        NamespacedKey donationsKey = new NamespacedKey(Lives.getPlugin(), "donations");
        PersistentDataContainer data = p.getPersistentDataContainer();

        if (!data.has(killsKey, PersistentDataType.INTEGER)) {
            data.set(killsKey, PersistentDataType.INTEGER, 0);
        }
        if (!data.has(deathsKey, PersistentDataType.INTEGER)) {
            data.set(deathsKey, PersistentDataType.INTEGER, 0);
        }
        if (!data.has(livesKey, PersistentDataType.INTEGER)) {
            data.set(livesKey, PersistentDataType.INTEGER, 5);
        }
        if (!data.has(donationsKey, PersistentDataType.INTEGER)) {
            data.set(donationsKey, PersistentDataType.INTEGER, 0);
        }




}
}
