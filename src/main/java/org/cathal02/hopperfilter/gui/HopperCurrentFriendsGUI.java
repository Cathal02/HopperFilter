package org.cathal02.hopperfilter.gui;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.cathal02.hopperfilter.HopperFilter;
import org.cathal02.hopperfilter.XMaterial;
import org.cathal02.hopperfilter.data.HopperData;
import org.cathal02.hopperfilter.gui.holders.CurrentFriendsHolder;
import sun.awt.OSInfo;

import java.util.List;
import java.util.UUID;

public class HopperCurrentFriendsGUI{

    HopperFilter plugin;
    public HopperCurrentFriendsGUI(HopperFilter plugin)
    {
        this.plugin = plugin;
    }

    public void openGUI(Player player)
    {
        HopperData data = plugin.getHopperManager().getHopperData(player);
        if(data == null)return;
        List<String> friends = data.friends;

        Inventory inventory = Bukkit.createInventory(new CurrentFriendsHolder(), plugin.getUtils().getInventorySize(friends.size()),"Manage Friends");
        setupGUI(friends, inventory);

        player.openInventory(inventory);
    }

    private void setupGUI(List<String> friends, Inventory inventory) {
        for(int i = 0; i <inventory.getSize()-9; i++)
        {
            if(i >= friends.size())break;
            if(friends.get(i).length() < 5)continue;
            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(friends.get(i)));
            inventory.setItem(i, plugin.getInventoryUtils().getFriendItem(player));
        }

        for(int i = inventory.getSize()-9; i<inventory.getSize(); i++)
        {
            inventory.setItem(i, plugin.getInventoryUtils().getBlackGlass());
        }
        inventory.setItem(inventory.getSize()-9,plugin.getInventoryUtils().getBackwardArrow());

    }


}
