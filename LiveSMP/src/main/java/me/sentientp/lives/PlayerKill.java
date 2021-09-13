package me.sentientp.lives;

import org.bukkit.*;
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
                    player.sendTitle(ChatColor.YELLOW+victim.Name, ChatColor.RED+"has perished.", 1, 200, 1);
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 2.0F, 1.0F);

                }
                Person.setGameMode(GameMode.SPECTATOR);
            } else {
                for (Player player:Bukkit.getServer().getOnlinePlayers()) {
                    player.playSound(Person.getLocation(), Sound.ITEM_TOTEM_USE, 2.0F, 1.0F);
                }
            }
            Kira.updateServerMember();
            victim.updateServerMember();


        } catch (NullPointerException | UnknownHostException e) {
            serverMember person = new serverMember(Person);
            person.lives-=1;
            person.deaths+=1;
            Person.sendMessage(ChatColor.RED+"You have "+person.lives+" lives left. dont lose any more!!");
            if (person.lives<=0) {
                for (Player player:Bukkit.getServer().getOnlinePlayers()) {
                    player.sendTitle(ChatColor.YELLOW+person.Name, ChatColor.RED+"has perished.", 1, 200, 1);
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 2.0F, 1.0F);
                }
                Person.setGameMode(GameMode.SPECTATOR);
            } else {
                for (Player player:Bukkit.getServer().getOnlinePlayers()) {
                    player.playSound(Person.getLocation(), Sound.ITEM_TOTEM_USE, 2.0F, 1.0F);
                }
            }
            person.updateServerMember();
        }

    }




    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        serverMember member = new serverMember(p);
        if (p.hasPlayedBefore()) {
            Bukkit.broadcastMessage(ChatColor.GREEN+"Greetings, "+p.getName());
            if (member.lives>0 && p.getGameMode()==GameMode.SPECTATOR) {
                p.setGameMode(GameMode.SURVIVAL);
                if (p.getBedSpawnLocation()!=null) {
                    p.teleport(p.getBedSpawnLocation());
                } else {
                    p.teleport(p.getWorld().getSpawnLocation());
                    p.sendMessage(ChatColor.RED + "You didnt have a bed, so you were sent to spawn");
                }
                for (Player player:Bukkit.getServer().getOnlinePlayers()) {
                    player.sendTitle(ChatColor.YELLOW+p.getName(), ChatColor.GREEN+"has been revived.", 1, 200, 1);
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.0F, 1.0F);
                }
            }

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
