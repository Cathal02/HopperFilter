package org.cathal02.hopperfilter.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.cathal02.hopperfilter.HopperFilter;
import org.cathal02.hopperfilter.data.HopperData;
import org.cathal02.hopperfilter.gui.holders.HopperManagementHolder;

public class HopperSettingsGUI {

    HopperFilter plugin;

    public HopperSettingsGUI(HopperFilter plugin)
    {
        this.plugin = plugin;
    }

    public void openGUI(Player p)
    {
        Inventory inventory = Bukkit.createInventory(new HopperManagementHolder(), 27, "Hopper Settings");
        setupGUI(inventory, p);

        p.openInventory(inventory);
    }

    private void setupGUI(Inventory inventory, Player player) {
        for(int i = 0; i < inventory.getSize(); i++)
        {
            inventory.setItem(i, plugin.getInventoryUtils().getBlackGlass());
        }


        HopperData data = plugin.getHopperManager().getHopperData(player);
        inventory.setItem(10, plugin.getInventoryUtils().getToggleEnabledButton(data.isPublic));
        inventory.setItem(12, plugin.getInventoryUtils().getAddFriendsItem(player));
        inventory.setItem(14, plugin.getInventoryUtils().getManageFriendsItem());
        inventory.setItem(16, plugin.getInventoryUtils().getAdvancedFilterButton(data.advancedFilter));
        inventory.setItem(18, plugin.getInventoryUtils().getBackwardArrow());
    }

    public void updateToggleButton(Inventory inventory, boolean newState)
    {
        ItemStack updatedItem = plugin.getInventoryUtils().getToggleEnabledButton(newState);
        inventory.setItem(10, updatedItem);
    }


    public void toggleAdvancedFilter(Inventory inventory, boolean newState) {
        inventory.setItem(16, plugin.getInventoryUtils().getAdvancedFilterButton(newState));
    }
}
