package org.cathal02.hopperfilter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.cathal02.hopperfilter.data.DataManager;
import org.cathal02.hopperfilter.data.HopperData;
import org.cathal02.hopperfilter.gui.HopperCurrentFriendsGUI;
import org.cathal02.hopperfilter.gui.HopperGUI;
import org.cathal02.hopperfilter.gui.InventoryUtils;
import org.cathal02.hopperfilter.managers.*;

import java.util.HashMap;
import java.util.Map;

public final class HopperFilter extends JavaPlugin {

    private HopperGUI guiHandler;
    private HopperManager hopperManager;
    private DataManager dataManager;
    private Utils utils;
    private InventoryUtils inventoryUtils;
    private HopperCurrentFriendsGUI hopperCurrentFriendsGUI;

    public Map<Location, HopperData> hoppers = new HashMap<>();


    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        new MessageManager(this);

        utils = new Utils(this);
        inventoryUtils = new InventoryUtils();
        guiHandler = new HopperGUI(this);
        dataManager = new DataManager(this);
        hopperManager = new HopperManager(this);
        hopperCurrentFriendsGUI = new HopperCurrentFriendsGUI(this);

        hopperManager.initHoppers(dataManager.getHopperData());

        Bukkit.getPluginManager().registerEvents(hopperManager, this);
        Bukkit.getPluginManager().registerEvents(guiHandler, this);
        Bukkit.getPluginManager().registerEvents(new HopperSettingsManager(this), this);
        Bukkit.getPluginManager().registerEvents(new HopperFriendsManager(this), this);
        Bukkit.getPluginManager().registerEvents(new HopperCurrentFriendsManager(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        dataManager.saveHoppers(hoppers.values());
    }


    public HopperGUI getGuiHandler() {
        return guiHandler;
    }

    public HopperCurrentFriendsGUI getCurrentFriendsGUI(){return hopperCurrentFriendsGUI;}

    public HopperManager getHopperManager() {
        return hopperManager;
    }


    public Utils getUtils() {
        return utils;
    }

    public InventoryUtils getInventoryUtils(){return inventoryUtils;}

}
