package me.sentientp.lives;

import me.sentientp.lives.Lives;
import me.sentientp.lives.PlayerKill;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;


public class Commands implements CommandExecutor, Listener {

    public ItemStack getPlayerHead(OfflinePlayer playerstring) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        assert meta != null;
        meta.setOwningPlayer(playerstring);
        meta.setDisplayName(playerstring.getName());
        item.setItemMeta(meta);
        return item;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("stats")) {
            NamespacedKey killsKey = new NamespacedKey(Lives.getPlugin(), "kills");
            NamespacedKey deathsKey = new NamespacedKey(Lives.getPlugin(), "deaths");
            NamespacedKey livesKey = new NamespacedKey(Lives.getPlugin(), "lives");
            NamespacedKey donationsKey = new NamespacedKey(Lives.getPlugin(), "donations");
            PersistentDataContainer data = p.getPersistentDataContainer();
            int kills = data.get(killsKey, PersistentDataType.INTEGER);
            int deaths =data.get(deathsKey, PersistentDataType.INTEGER);
            int lives =data.get(livesKey, PersistentDataType.INTEGER);
            int donations =data.get(donationsKey, PersistentDataType.INTEGER);

            Inventory gui = Bukkit.createInventory(null, 9, "stats");

            ItemStack killitem = new ItemStack(Material.DIAMOND_SWORD);
            ItemStack deathsitem = new ItemStack(Material.SKELETON_SKULL);
            ItemStack livesitem = new ItemStack(Material.TOTEM_OF_UNDYING);
            ItemStack donateitem = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);

            ItemMeta killmeta = killitem.getItemMeta();
            killmeta.setDisplayName(ChatColor.YELLOW+p.getName()+"'s kills");
            ArrayList<String> killLore = new ArrayList<>();
            killLore.add(ChatColor.WHITE+" The amout of kills you have:"+kills);
            killmeta.setLore(killLore);
            killitem.setItemMeta(killmeta);

            ItemMeta deathsmeta = deathsitem.getItemMeta();
            deathsmeta.setDisplayName(ChatColor.WHITE+p.getName()+"'s Deaths");
            ArrayList<String> deathsLore = new ArrayList<>();
            deathsLore.add(ChatColor.WHITE+" Total deaths:"+deaths);
            deathsmeta.setLore(deathsLore);
            deathsitem.setItemMeta(deathsmeta);

            ItemMeta livesmeta = livesitem.getItemMeta();
            livesmeta.setDisplayName(ChatColor.RED+p.getName()+"'s Lives");
            ArrayList<String> livesLore = new ArrayList<>();
            livesLore.add(ChatColor.WHITE+" Current amount of lives:"+lives);
            livesmeta.setLore(livesLore);
            livesitem.setItemMeta(livesmeta);

            ItemMeta donationsmeta = donateitem.getItemMeta();
            donationsmeta.setDisplayName(ChatColor.AQUA+p.getName()+"'s Donations");
            ArrayList<String> donateLore = new ArrayList<>();
            donateLore.add(ChatColor.WHITE+" Total Donations:"+donations);
            donationsmeta.setLore(donateLore);
            donateitem.setItemMeta(donationsmeta);


            ItemStack[] menuitems = {killitem, deathsitem, livesitem, donateitem};
            gui.setContents(menuitems);
            p.openInventory(gui);

        }
        if (cmd.getName().equalsIgnoreCase("donate")) {
             serverMember person = new serverMember(p);
             if (person.lives<=0) {
                 ExampleGui newgui = new ExampleGui(36, "donate!");
                 for (Player onlinePlayer:Bukkit.getOnlinePlayers()) {
                     OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(onlinePlayer.getUniqueId());
                     ItemStack theirskull = getPlayerHead(offlinePlayer);
                     newgui.initializeitemstack(theirskull);

                 }
                 newgui.openInventory(p);

             }

        }
        if (cmd.getName().equalsIgnoreCase("getlives")) {
            if (NumberUtils.isNumber(args[0])) {
                int livesToAdd = Integer.parseInt(args[0]);
                Player recieverPlayer = Bukkit.getPlayer(args[1]);
                serverMember recieveroflife = new serverMember(recieverPlayer);
                recieveroflife.lives+=livesToAdd;
                System.out.println(livesToAdd+recieveroflife.Name);
                try {
                    recieveroflife.updateServerMember();
                    p.sendMessage("added "+livesToAdd+" lives to "+p.getName());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }


            } else {
                System.out.println("cant parse integer, this is ther error");
            }
        }
        return true;


    }
}
