package me.sentientp.lives;

import org.bukkit.plugin.java.JavaPlugin;

public final class Lives extends JavaPlugin {

    private static Lives plugin;

    @Override
    public void onEnable() {
        plugin = this;
        System.out.println("Hello World!");
        getServer().getPluginManager().registerEvents(new PlayerKill(), this);
        getCommand("stats").setExecutor(new Commands());
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
