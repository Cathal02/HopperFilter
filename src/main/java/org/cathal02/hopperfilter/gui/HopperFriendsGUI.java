package org.cathal02.hopperfilter.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.cathal02.hopperfilter.HopperFilter;
import org.cathal02.hopperfilter.XMaterial;
import org.cathal02.hopperfilter.data.HopperData;
import org.cathal02.hopperfilter.gui.holders.FriendsHolder;

import java.util.*;

public class HopperFriendsGUI {
    Player[] players;

    HopperGUI guiHandler;
    HopperData hopperData;
    HopperFilter plugin;

    Map<UUID, FriendsGUIData> friendsGUIDataMap = new HashMap<>();
    public HopperFriendsGUI(HopperFilter plugin, HopperGUI guiHandler)
    {
        this.guiHandler =guiHandler;
        this.plugin = plugin;
    }

    public void openFriendsGUI(Player p)
    {
        Collection<? extends Player> _players = Bukkit.getOnlinePlayers();
        players = _players.toArray(new Player[0]);
        Inventory inventory = Bukkit.createInventory(new FriendsHolder(), 54, "Add Friends");
        hopperData = plugin.getHopperManager().getHopperData(p);

        FriendsGUIData friendsData = new FriendsGUIData((players.length/45)+1);
        friendsGUIDataMap.put(p.getUniqueId(), friendsData);

        inventory.setItem(inventory.getSize()-5, plugin.getInventoryUtils().getSearchIcon());
        updateGUI(p,inventory);
        p.openInventory(inventory);
    }


    void updateGUI(Player player, Inventory inventory)
    {
        int page = getData(player).page;

       int baseIndex = page == 0 ? 0 : page*54;
        updateArrows(player, inventory);

        for(int i = 0; i < 45; i++)
       {
           int currentPlayer = baseIndex == 0 ? i : baseIndex+i;
           if(currentPlayer  >= players.length || players[currentPlayer] == null)
           {
               setGlass(i, inventory);
               break;
           }
           inventory.setItem(i, plugin.getInventoryUtils().getFriendsIcon(players[currentPlayer]));
       }
    }

    void setGlass(int start, Inventory inventory)
    {
        for(int i = start; i < 54; i++)
        {
            if(inventory.getItem(i) == null)
            {
                inventory.setItem(i,plugin.getInventoryUtils().getBlackGlass());
            }
        }
    }


    private void updateArrows(Player player, Inventory inventory) {
        FriendsGUIData  data = getData(player);
        int page = data.page;
        int maxPages = data.maxPage;


        ItemStack forwardArrow = page == (maxPages-1) ? plugin.getInventoryUtils().getNoPageLeft() : plugin.getInventoryUtils().getForwardArrow();
        inventory.setItem(53, forwardArrow);

        inventory.setItem(45, plugin.getInventoryUtils().getBackwardArrow());

       setGlass(46, inventory);
    }

    public void nextPage(Player player, Inventory inventory)
    {
        FriendsGUIData  data = getData(player);
        int page = data.page;
        int maxPages = data.maxPage;

        if(page+1 > maxPages)return;

        data.page++;
        friendsGUIDataMap.put(player.getUniqueId(), data);

        updateGUI(player, inventory);
    }

    public boolean backPage(Player player, Inventory inventory)
    {
        FriendsGUIData  data = getData(player);
        int page = data.page;

        if(page-1 < 0)return false;
        data.page--;
        friendsGUIDataMap.put(player.getUniqueId(), data);
        updateGUI(player,inventory);
        return true;
    }

    public void removeFromMap(Player player) {
        friendsGUIDataMap.remove(player.getUniqueId());
    }

    private class FriendsGUIData {
        public FriendsGUIData( int maxPage)
        {
            this.maxPage = maxPage;
        }

        int page = 0;
        int maxPage = 0;
    }

    private FriendsGUIData getData(Player player)
    {
        if(!friendsGUIDataMap.containsKey(player.getUniqueId()))
            openFriendsGUI(player);
        return friendsGUIDataMap.get(player.getUniqueId());
    }

}

