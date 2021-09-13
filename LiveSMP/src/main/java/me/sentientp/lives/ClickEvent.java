package me.sentientp.lives;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.net.UnknownHostException;

public class ClickEvent implements Listener {
    @EventHandler
    public void clickEvent(InventoryClickEvent e) throws UnknownHostException {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase("stats")) {
            e.setCancelled(true);
        }
        if (e.getView().getTitle().equalsIgnoreCase("donate!")) {
            switch (e.getCurrentItem().getType()){
                case PLAYER_HEAD:
                    SkullMeta meta = (SkullMeta) e.getCurrentItem().getItemMeta();
                    String playername = meta.getOwningPlayer().getName();
                    meta.setDisplayName(ChatColor.GREEN+" Confirm gift to "+playername);

                    ExampleGui newgui = new ExampleGui(9, "Confirm");
                    newgui.initializeitemstack(e.getCurrentItem());
                    newgui.initializeItems(Material.RED_STAINED_GLASS_PANE, "Cancel", " ");
                    newgui.openInventory(p);
            }
            e.setCancelled(true);
        }
        if (e.getView().getTitle().equalsIgnoreCase("confirm")) {

            switch (e.getCurrentItem().getType()){
                case PLAYER_HEAD:
                    SkullMeta meta = (SkullMeta) e.getCurrentItem().getItemMeta();
                    assert meta != null;
                    Player playername = meta.getOwningPlayer().getPlayer();
                    serverMember player = new serverMember(p);
                    serverMember reciever = new serverMember(playername);

                    player.lives-=1;
                    reciever.lives+=1;
                    player.donations+=1;

                    player.updateServerMember();
                    reciever.updateServerMember();

                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0F, 1.0F);
                    playername.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0F, 1.0F);

                    p.sendMessage(ChatColor.GREEN +"You gave "+reciever.Name+" 1 life");
                    playername.sendMessage(ChatColor.GREEN +"You recieved 1 life from "+player.Name);
                    if (playername.getGameMode()!= GameMode.SURVIVAL) {
                        playername.sendMessage(ChatColor.GREEN + "rejoin the server to start playing again!" + reciever.Name);
                    }
                    if (player.lives <= 0) {
                        for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                            pl.sendTitle(ChatColor.YELLOW + pl.getName(), ChatColor.RED + "has perished.", 1, 200, 1);
                            pl.playSound(pl.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 2.0F, 1.0F);
                        }
                        p.setGameMode(GameMode.SPECTATOR);
                    }
                case RED_STAINED_GLASS_PANE:
                    p.closeInventory();
                    serverMember person = new serverMember(p);
                    if (person.lives>0) {
                        ExampleGui newgui = new ExampleGui(36, "donate!");
                        for (Player onlinePlayer: Bukkit.getOnlinePlayers()) {
                            if (onlinePlayer.getName()!=p.getName()) {
                                try {
                                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(onlinePlayer.getUniqueId());
                                    ItemStack item = new ItemStack(Material.PLAYER_HEAD);
                                    SkullMeta metaa = (SkullMeta) item.getItemMeta();
                                    assert metaa != null;
                                    metaa.setOwningPlayer(offlinePlayer);
                                    metaa.setDisplayName(offlinePlayer.getName());
                                    item.setItemMeta(metaa);
                                    ItemStack theirskull = item;
                                    newgui.initializeitemstack(theirskull);
                                } catch (Exception er) {
                                    ItemStack item = new ItemStack(Material.PLAYER_HEAD);
                                    SkullMeta metaA = (SkullMeta) item.getItemMeta();
                                    metaA.setOwningPlayer(Bukkit.getOfflinePlayer(onlinePlayer.getName()));
                                    metaA.setDisplayName(p.getName());
                                    item.setItemMeta(metaA);
                                    newgui.initializeitemstack(item);
                                }
                            }
                        }
                        newgui.openInventory(p);

                    }
            }
            e.setCancelled(true);
        }
    }

}
