package org.cathal02.hopperfilter.managers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.cathal02.hopperfilter.HopperFilter;
import org.cathal02.hopperfilter.NBTEditor;
import org.cathal02.hopperfilter.data.HopperData;
import org.cathal02.hopperfilter.gui.holders.HopperManagementHolder;

public class HopperSettingsManager implements Listener{

    HopperFilter plugin;
    public HopperSettingsManager(HopperFilter plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        if(!(e.getInventory().getHolder() instanceof HopperManagementHolder)) return;
        if(!(e.getWhoClicked() instanceof  Player)) return;
        if(e.getCurrentItem() == null)return;

        Player player = (Player)e.getWhoClicked();
        e.setCancelled(true);


        handleToggleHopperIsPublic(player, e);

        switch (e.getSlot())
        {
            case 12:
                plugin.getGuiHandler().getHopperFriendsGUI().openFriendsGUI(player);
                break;
            case 14:
                plugin.getCurrentFriendsGUI().openGUI(player);
                break;
            case 16:
                HopperData data = plugin.getHopperManager().getHopperData(player);
                plugin.getGuiHandler().getHopperSettingsGUI().toggleAdvancedFilter(e.getInventory(),!data.advancedFilter);
                plugin.getHopperManager().getHopperUtils().toggleAdvancedFilter(data);
                break;
            case 18:
                plugin.getGuiHandler().openHopperGUI(player, plugin.getHopperManager().getHopperData(player));
                break;
        }



    }



    private void handleToggleHopperIsPublic(Player player, InventoryClickEvent e) {
        ItemStack clickedItem = e.getCurrentItem();
        if(NBTEditor.contains(clickedItem, "enableToggle"))
        {
            boolean newState = !(NBTEditor.getInt(clickedItem, "enableToggle") == 0);
            plugin.getGuiHandler().getHopperSettingsGUI().updateToggleButton(e.getInventory(), newState);
            plugin.getHopperManager().getHopperUtils().updateHopperIsPublic(player, newState);
        }
    }
}
