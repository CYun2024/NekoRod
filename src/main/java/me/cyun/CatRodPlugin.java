package me.cyun;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CatRodPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // 注册事件
        Bukkit.getPluginManager().registerEvents(new CatRodListener(this), this);
        getLogger().info("CatRod 已加载！");
    }
}