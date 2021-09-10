package me.sentientp.lives;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
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
    public void clickEvent(InventoryClickEvent e, Inventory inv) throws UnknownHostException {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase("stats")) {
            e.setCancelled(true);
        }
        if (e.getView().getTitle().equalsIgnoreCase("donate")) {
            switch (e.getCurrentItem().getType()){
                case PLAYER_HEAD:
                    SkullMeta meta = (SkullMeta) e.getCurrentItem().getItemMeta();
                    String playername = meta.getOwningPlayer().getName();

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
                    Player playername = meta.getOwningPlayer().getPlayer();
                    serverMember player = new serverMember(p);
                    serverMember reciever = new serverMember(playername);

                    player.lives-=1;
                    reciever.lives+=1;

                    player.updateServerMember();
                    reciever.updateServerMember();

                    p.sendMessage(ChatColor.GREEN +"You gave "+reciever.Name+" 1 life");
                    playername.sendMessage(ChatColor.GREEN +"You recieved 1 life from "+reciever.Name);
                    if (reciever.lives==1) {
                        playername.sendMessage(ChatColor.GREEN +"rejoin the server to start playing again!"+reciever.Name);

                    }
                case RED_STAINED_GLASS_PANE:
                    serverMember person = new serverMember(p);
                    if (person.lives<=0) {
                        ExampleGui newgui = new ExampleGui(36, "donate!");
                        for (Player onlinePlayer: Bukkit.getOnlinePlayers()) {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(onlinePlayer.getUniqueId());
                            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
                            SkullMeta metaa = (SkullMeta) item.getItemMeta();
                            assert metaa != null;
                            metaa.setOwningPlayer(offlinePlayer);
                            metaa.setDisplayName(offlinePlayer.getName());
                            item.setItemMeta(metaa);
                            ItemStack theirskull = item;
                            newgui.initializeitemstack(theirskull);

                        }
                        newgui.openInventory(p);

                    }


            }
            e.setCancelled(true);
        }
    }

}
