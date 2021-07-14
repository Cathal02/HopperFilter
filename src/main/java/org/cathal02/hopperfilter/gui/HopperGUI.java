package org.cathal02.hopperfilter.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.cathal02.hopperfilter.HopperFilter;
import org.cathal02.hopperfilter.ItemBuilder;
import org.cathal02.hopperfilter.XMaterial;
import org.cathal02.hopperfilter.data.HopperData;
import org.cathal02.hopperfilter.gui.holders.HopperFilterHolder;
import org.cathal02.hopperfilter.managers.MessageManager;

public class HopperGUI implements Listener {

    HopperFilter plugin;
    ItemStack barrier = null;
    HopperFriendsGUI hopperFriendsGUI;
    HopperSettingsGUI hopperSettingsGUI;
    int size;

    public HopperGUI(HopperFilter plugin) {
        this.plugin = plugin;
        if (plugin.getConfig().contains("hopperFilterSize")) {
            size = plugin.getConfig().getInt("hopperFilterSize");
        } else {
            size = 54;
        }

        barrier = XMaterial.BARRIER.parseItem();
        hopperFriendsGUI = new HopperFriendsGUI(plugin, this);
        hopperSettingsGUI = new HopperSettingsGUI(plugin);
    }

    public void openHopperGUI(Player player, HopperData hopperData) {
        int allowedItems = hopperData.allowedItems;
        ItemStack[] filterItems = hopperData.hopperItems;

        String title = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("hopperFilterName"));

        Integer size = plugin.getUtils().getInventorySize(allowedItems);
        Inventory inventory = Bukkit.createInventory(new HopperFilterHolder(), size, title);
        if (filterItems != null) {
            inventory.setContents(filterItems);
        }

        initHotbar(inventory, allowedItems, hopperData);
        player.openInventory(inventory);
    }

    private void initHotbar(Inventory inventory,int allowedSlots, HopperData hopperData) {

        for (int i = 0; i < inventory.getSize(); i++) {
            if (i < allowedSlots) continue;
            if(i<inventory.getSize()-9)
            {
                inventory.setItem(i, plugin.getInventoryUtils().getItemNotAvailable());
                continue;
            }
            inventory.setItem(i, plugin.getInventoryUtils().getBlackGlass());
        }

        int lastRowFirstSlot = inventory.getSize() - 9;
        ItemStack barrier = new ItemBuilder(XMaterial.BARRIER.parseMaterial(), 1)
                .setName(MessageManager.getHopperSettingsName())
                .toItemStack();

        inventory.setItem(lastRowFirstSlot + 4, barrier);

        inventory.setItem(inventory.getSize()-1, plugin.getInventoryUtils().getBlackListButton(hopperData.blacklistEnabled));
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e) {
        ItemStack clickedItem = e.getCurrentItem();

        if (e.getInventory().getHolder() instanceof HopperFilterHolder) {
            Player player = (Player) e.getWhoClicked();
            HopperData data = plugin.getHopperManager().getHopperData(player);

            if (!data.owner.equalsIgnoreCase(e.getWhoClicked().getUniqueId().toString()))
            {
                MessageManager.onlyHopperOwnerCanDoThis(player);
                return;
            }
            if (clickedItem == null) return;

            if (clickedItem.getType().equals(barrier.getType())) {
                e.setCancelled(true);
                hopperSettingsGUI.openGUI(player);
            }

            int slot = e.getInventory().getSize()-1;
            if(e.getSlot() == slot)
            {
                e.setCancelled(true);
                data.blacklistEnabled = !data.blacklistEnabled;
                e.getInventory().setItem(slot, plugin.getInventoryUtils().getBlackListButton(data.blacklistEnabled));
            }
        }

    }

    public HopperSettingsGUI getHopperSettingsGUI(){return hopperSettingsGUI;}
    public HopperFriendsGUI getHopperFriendsGUI(){return hopperFriendsGUI;}
}
