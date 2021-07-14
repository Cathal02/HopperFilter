package org.cathal02.hopperfilter.managers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.cathal02.hopperfilter.HopperFilter;
import org.cathal02.hopperfilter.data.HopperData;
import org.cathal02.hopperfilter.data.HopperUtils;
import org.cathal02.hopperfilter.gui.holders.HopperFilterHolder;
import org.cathal02.hopperfilter.gui.HopperGUI;

import java.util.*;

public class HopperManager implements Listener {

    Map<UUID, Location> hopperLocations = new HashMap<>();
    public Map<Location, HopperData> hoppers;
    private List<Location> openHoppers = new ArrayList<>();

    public Map<UUID,Location> getHopperLocations(){return hopperLocations; }
    private final HopperGUI hopperGUI;
    private final HopperUtils hopperUtils;
    private final HopperFilter plugin;

    public HopperUtils getHopperUtils(){return hopperUtils;}
    public HopperManager(HopperFilter plugin) {
        hopperGUI = plugin.getGuiHandler();
        hopperUtils = new HopperUtils(this, plugin);
        this.hoppers = plugin.hoppers;
        this.plugin = plugin;

    }

    @EventHandler
    public void onPlayerClickHopper(PlayerInteractEvent e) {
        Block clickedBlock = e.getClickedBlock();
        Player player = e.getPlayer();

        if (!e.getPlayer().hasPermission("hopperfilter.use")) return;
        if (clickedBlock == null) return;
        if (!clickedBlock.getType().equals(Material.HOPPER)) return;
        if (!player.isSneaking()) return;
        if(!e.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;
        if(!allowedOpenHopper(player, clickedBlock.getLocation()))
        {
            MessageManager.CannotOpenHopper(player);
            return;
        }


        if(openHoppers.contains(clickedBlock.getLocation()))
        {
            e.getPlayer().sendMessage(ChatColor.RED + "A player is already interacting with this hopper!");
            return;
        }

        Location loc = clickedBlock.getLocation();
        Player p = e.getPlayer();

        hopperLocations.put(p.getUniqueId(),loc);
        hopperGUI.openHopperGUI(player, hopperUtils.initHopperData(p,loc));
        openHoppers.add(loc);
    }

    private boolean allowedOpenHopper(Player player, Location loc) {
        if(!hoppers.containsKey(loc))return true;
        return hoppers.get(loc).allowedOpenHopper(player);
    }

    @EventHandler
    public void onHopperClose(InventoryCloseEvent e) {
        if(!(e.getInventory().getHolder() instanceof HopperFilterHolder)) return;

        if (hopperLocations.containsKey(e.getPlayer().getUniqueId())) {
            Location invLocation = hopperLocations.get(e.getPlayer().getUniqueId());

            HopperData currentData = hoppers.get(invLocation);
            ItemStack[] items = new ItemStack[currentData.allowedItems];
            for(int i = 0; i < currentData.allowedItems; i++)
            {
                if(e.getInventory().getItem(i) != null)
                {
                    items[i] = (e.getInventory().getItem(i));
                }
            }
            currentData.hopperItems = items;
            hoppers.put(invLocation, currentData);
            openHoppers.remove(invLocation);
        }
    }

    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent e) {
        Inventory destinationInventory = e.getDestination();
        if(destinationInventory.getHolder() == null) return;
        if(destinationInventory.getHolder() instanceof BlockState)
        {
            Location hopperLocation = ((BlockState)destinationInventory.getHolder()).getLocation();
            if (hoppers.containsKey(hopperLocation)) {
                e.setCancelled(hopperUtils.cancelItemTransferEvent(hopperLocation, e.getItem()));
            }
        }
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof  Player)) return;
        if(e.getClickedInventory() == null)return;

        Player p = (Player)e.getWhoClicked();
        int allowedItems = hopperUtils.getAllowedItems(e.getClickedInventory(), p);

        if(e.getClickedInventory().getHolder() instanceof HopperFilterHolder &&e.getSlot() >= allowedItems) e.setCancelled(true);

    }

    @EventHandler
    public void onInventoryPickupItem(InventoryPickupItemEvent e)
    {
        if(!e.getInventory().getType().equals(InventoryType.HOPPER)) return;
        if(e.getInventory().getHolder() instanceof BlockState)
        {
            e.setCancelled(hopperUtils.cancelItemTransferEvent(((BlockState)e.getInventory().getHolder()).getLocation(), e.getItem().getItemStack()));
        }
    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent e)
    {
        if(!e.getBlock().getType().equals(Material.HOPPER)) return;
        if(e.getPlayer().isSneaking())
        {
            e.setCancelled(true);
            return;
        }

        if(openHoppers.contains(e.getBlock().getLocation()))
        {
            MessageManager.cannotBreakOpenHopper(e.getPlayer());
            e.setCancelled(true);
        }
        else
        {
            Location loc = e.getBlock().getLocation();

            if(hoppers.containsKey(loc))
            {
                HopperData data = hoppers.get(loc);
                if(plugin.getConfig().getBoolean("enableHopperBreakProtection"))
                {
                    if(!data.allowedOpenHopper(e.getPlayer()))
                    {
                        MessageManager.cannotBreakHopper(e.getPlayer());
                        e.setCancelled(true);
                        return;
                    }
                }

               hopperUtils.dropItems(hoppers.get(loc));
                hoppers.remove(loc);
            }

        }
    }

    public HopperData getHopperData(Player p) {return hopperUtils.getHopperData(p); }

    public void initHoppers(HopperData[] hopperData) {
        for(HopperData data : hopperData)
        {
            if(data.location != null)
            {
              hoppers.put(data.location, data);
            }
        }
    }

}
