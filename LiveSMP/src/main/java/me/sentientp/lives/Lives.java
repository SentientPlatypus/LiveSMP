package me.sentientp.lives;

import me.sentientp.lives.Commands;
import me.sentientp.lives.PlayerKill;
import org.bukkit.plugin.java.JavaPlugin;

public final class Lives extends JavaPlugin {

    private static Lives plugin;

    @Override
    public void onEnable() {
        plugin = this;
        System.out.println("Hello World!");
        getServer().getPluginManager().registerEvents(new PlayerKill(), this);
        getCommand("stats").setExecutor(new Commands());
        getCommand("donate").setExecutor(new Commands());
        getCommand("getlives").setExecutor(new Commands());
        getCommand("reset").setExecutor(new Commands());

        getServer().getPluginManager().registerEvents(new ClickEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static Lives getPlugin() {
        return plugin;
    }
}
