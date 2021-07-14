package org.cathal02.hopperfilter.managers;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.cathal02.hopperfilter.HopperFilter;
import org.cathal02.hopperfilter.NBTEditor;
import org.cathal02.hopperfilter.XMaterial;
import org.cathal02.hopperfilter.data.HopperData;
import org.cathal02.hopperfilter.gui.holders.FriendsHolder;

public class HopperFriendsManager implements Listener{

    public HopperFilter plugin;
    public HopperFriendsManager(HopperFilter plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        if(e.getCurrentItem() == null)return;
        if(!(e.getWhoClicked() instanceof Player))return;

        Player player = (Player)e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        if (e.getInventory().getHolder() instanceof FriendsHolder) {
            e.setCancelled(true);
            if (clickedItem.getType().equals(XMaterial.PLAYER_HEAD.parseMaterial())) {
                HopperData data = plugin.getHopperManager().getHopperData(player);

                if (data != null) {
                    if (NBTEditor.contains(clickedItem, "player")) {
                        String uuid = NBTEditor.getString(clickedItem, "player");
                        addFriend(uuid, player, data);
                    }
                }
            }

            else if(clickedItem.getType().equals(XMaterial.ARROW.parseMaterial()))
            {
                if(e.getSlot() == e.getInventory().getSize()-1)
                {
                    plugin.getGuiHandler().getHopperFriendsGUI().nextPage(player,e.getInventory());
                } else if(e.getSlot() == e.getInventory().getSize()-9)
                {
                    if(!plugin.getGuiHandler().getHopperFriendsGUI().backPage(player,e.getInventory()))
                    {
                        plugin.getGuiHandler().getHopperSettingsGUI().openGUI(player);
                    }
                }
            } else if(clickedItem.getType().equals(XMaterial.BEACON.parseMaterial()))
            {
                new AnvilGUI.Builder()
                        .onComplete((p, text) -> {           //called when the inventory output slot is clicked
                            Player newFriend = Bukkit.getPlayer(text);
                            if(newFriend == null)
                            {
                                return AnvilGUI.Response.text("Player does not exist");
                            }
                            else
                            {
                                HopperData data = plugin.getHopperManager().getHopperData(p);
                                if(addFriend(newFriend.getUniqueId().toString(), player, data))
                                {
                                    plugin.getGuiHandler().openHopperGUI(player, plugin.getHopperManager().getHopperData(player));
                                    return AnvilGUI.Response.close();
                                }
                                else
                                {
                                    return AnvilGUI.Response.text("Invalid Player!");
                                }
                            }
                        })
                        .text("Enter Player Name")     //sets the text the GUI should start with
                        .item(new ItemStack(XMaterial.PAPER.parseMaterial())) //use a custom item for the first slot
                        .title("Enter your answer.")              //set the title of the GUI (only works in 1.14+)
                        .plugin(plugin)                 //set the plugin instance
                        .open(player);
            }
        }
    }

    private boolean addFriend(String uuid, Player player, HopperData data)
    {
        if(uuid.equalsIgnoreCase(player.getUniqueId().toString()))
        {
            MessageManager.cannotBeFriendsWithSelf(player);
            return false;
        }
        if (data.friends.contains(uuid)){
            MessageManager.alreadyFriends(player);
            return false;
        }

        data.friends.add(uuid);
        plugin.getHopperManager().hoppers.put(data.location, data);

        MessageManager.friendAdded(player);
        return true;
    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e)
    {
        if(e.getInventory() instanceof FriendsHolder)
        {
            plugin.getGuiHandler().getHopperFriendsGUI().removeFromMap((Player)e.getPlayer());
        }
    }


}
