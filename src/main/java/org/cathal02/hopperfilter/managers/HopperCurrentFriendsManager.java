package org.cathal02.hopperfilter.managers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.cathal02.hopperfilter.HopperFilter;
import org.cathal02.hopperfilter.NBTEditor;
import org.cathal02.hopperfilter.gui.holders.CurrentFriendsHolder;

public class HopperCurrentFriendsManager implements Listener{

    HopperFilter plugin;
    public HopperCurrentFriendsManager(HopperFilter plugin)
    {
        this.plugin = plugin;
    }


    @EventHandler
    public void onClick(InventoryClickEvent e)
    {
        if(!(e.getInventory().getHolder() instanceof CurrentFriendsHolder))return;
        if(e.getCurrentItem() == null)return;
        if(e.getCurrentItem().getType().equals(Material.AIR))return;

        if(!(e.getWhoClicked() instanceof Player))return;
        e.setCancelled(true);

        ItemStack item = e.getCurrentItem();
        Player player = (Player)e.getWhoClicked();

        if(e.getSlot() == 9)
        {
            plugin.getGuiHandler().getHopperSettingsGUI().openGUI(player);
        }


        if(NBTEditor.contains(item,"player"))
        {
            String playerName = NBTEditor.getString(item, "player");
            plugin.getHopperManager().getHopperUtils().removeFriend((Player)e.getWhoClicked(), playerName);
            plugin.getCurrentFriendsGUI().openGUI((Player)e.getWhoClicked());
        }


    }

}
