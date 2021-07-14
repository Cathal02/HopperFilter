package org.cathal02.hopperfilter.gui;

import com.google.gson.internal.$Gson$Preconditions;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.cathal02.hopperfilter.NBTEditor;
import org.cathal02.hopperfilter.XMaterial;
import org.cathal02.hopperfilter.managers.MessageManager;
import sun.tools.asm.CatchData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryUtils {

    ItemStack glass;
    private ItemStack enabledButton;
    private ItemStack disabledButton;
    private final ItemStack manageFriends;
    private final ItemStack forwardArrow;
    private final ItemStack backArrow;
    private final ItemStack noPageLeft;
    private final ItemStack searchIcon;
    private ItemStack whitelistButton;
    private ItemStack blacklistButton;
    private ItemStack advancedFilterOn;
    private ItemStack advancedFilterOff;
    private ItemStack itemNotAvailable;

    Map<String, ItemStack> skulls = new HashMap<>();

    public InventoryUtils()
    {
        glass = XMaterial.BLACK_STAINED_GLASS_PANE.parseItem();
        ItemMeta meta = glass.getItemMeta();
        meta.setDisplayName(" ");
        glass.setItemMeta(meta);

        manageFriends = XMaterial.BOOK.parseItem();
        ItemMeta friendsMeta = manageFriends.getItemMeta();
        friendsMeta.setDisplayName(MessageManager.getItemName("manageFriends"));
        manageFriends.setItemMeta(friendsMeta);
        setupToggleEnableButton();

        forwardArrow = XMaterial.ARROW.parseItem();
        ItemMeta forwardArrowItemMeta = forwardArrow.getItemMeta();
        forwardArrowItemMeta.setDisplayName(MessageManager.getItemName("nextPage"));
        forwardArrow.setItemMeta(forwardArrowItemMeta);

        backArrow = XMaterial.ARROW.parseItem();
        ItemMeta backArrowItemMeta = backArrow.getItemMeta();
        backArrowItemMeta.setDisplayName(MessageManager.getItemName("previousPage"));
        backArrow.setItemMeta(backArrowItemMeta);

        noPageLeft = XMaterial.BARRIER.parseItem();
        ItemMeta noPageLeftMeta = noPageLeft.getItemMeta();
        noPageLeftMeta.setDisplayName(MessageManager.getItemName("noPagesRemaining"));
        noPageLeft.setItemMeta(noPageLeftMeta);

        searchIcon = XMaterial.BEACON.parseItem();
        ItemMeta searchmeta = searchIcon.getItemMeta();
        searchmeta.setDisplayName(MessageManager.getItemName("clickToSearch"));
        searchIcon.setItemMeta(searchmeta);

        itemNotAvailable = XMaterial.RED_STAINED_GLASS_PANE.parseItem();
        ItemMeta itemMeta = itemNotAvailable.getItemMeta();
        itemMeta.setDisplayName(MessageManager.getItemName("slotNotAvailable"));
        itemNotAvailable.setItemMeta(itemMeta);

        setupWhitelistButtons();
        setupAdvancedFilterButton();
    }

    private void setupAdvancedFilterButton() {
        advancedFilterOn = XMaterial.NETHER_STAR.parseItem();
        List<String> lore = new ArrayList<>();
        lore.add(MessageManager.getItemName("filterLoreOne"));
        lore.add(MessageManager.getItemName("filterLoreTwo"));
        ItemMeta meta = advancedFilterOn.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName(MessageManager.getItemName("filterEnabled"));
        advancedFilterOn.setItemMeta(meta);

        advancedFilterOff = advancedFilterOn.clone();
        ItemMeta _meta = advancedFilterOff.getItemMeta();
        _meta.setDisplayName(MessageManager.getItemName("filterDisabled"));
        advancedFilterOff.setItemMeta(_meta);

    }


    private void setupWhitelistButtons() {
        whitelistButton = XMaterial.IRON_BLOCK.parseItem();
        ItemMeta whitelistMeta = whitelistButton.getItemMeta();
        whitelistMeta.setDisplayName(MessageManager.getItemName("whitelistEnabled"));

        List<String> whitelistLore = new ArrayList<>();
        whitelistLore.add(MessageManager.getItemName("whitelistDesc"));
        whitelistMeta.setLore(whitelistLore);
        whitelistButton.setItemMeta(whitelistMeta);
        whitelistButton = NBTEditor.set(whitelistButton, 0, "enableToggle");

        blacklistButton = XMaterial.COAL_BLOCK.parseItem();
        ItemMeta blacklistMeta = blacklistButton.getItemMeta();
        List<String> blacklistLore = new ArrayList<>();
        blacklistLore.add(MessageManager.getItemName("blacklistDesc"));
        blacklistMeta.setLore(blacklistLore);

        blacklistMeta.setDisplayName(MessageManager.getItemName("blacklistEnabled"));

        blacklistButton.setItemMeta(blacklistMeta);
        blacklistButton = NBTEditor.set(blacklistButton ,0, "enableToggle");
    }

    private void setupToggleEnableButton() {
        enabledButton = XMaterial.EMERALD_BLOCK.parseItem();
        ItemMeta meta = enabledButton.getItemMeta();
        meta.setDisplayName(MessageManager.getItemName("publicHopperStatus"));
        enabledButton.setItemMeta(meta);
        enabledButton = NBTEditor.set(enabledButton, 0, "enableToggle");

        disabledButton = XMaterial.REDSTONE_BLOCK.parseItem();
        ItemMeta disabledMeta = disabledButton.getItemMeta();
        disabledMeta.setDisplayName(MessageManager.getItemName("privateHopperStatus"));
        disabledButton.setItemMeta(disabledMeta);
        disabledButton = NBTEditor.set(disabledButton, 1, "enableToggle");
    }

    public ItemStack getBlackGlass() {
        return glass;
    }

    public ItemStack getPlayerHead(OfflinePlayer player) {
        ItemStack head = XMaterial.PLAYER_HEAD.parseItem();
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        if (XMaterial.isNewVersion()) meta.setOwningPlayer(player);
        else meta.setOwner(player.getName());

        meta.setDisplayName(ChatColor.GREEN + player.getName());
        head.setItemMeta(meta);

        head = NBTEditor.set(head, player.getUniqueId().toString(), "player");
        return head;
    }
    public ItemStack getToggleEnabledButton(boolean enabled)
    {
        return  enabled ? enabledButton : disabledButton;
    }


    public ItemStack getItemNotAvailable(){return itemNotAvailable;}
    public ItemStack getManageFriendsItem() {
        return manageFriends;
    }

    public ItemStack getFriendItem(OfflinePlayer player)
    {
        ItemStack head = getPlayerHead(player);
        ItemMeta meta = head.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW + "Click to remove friend");
        meta.setLore(lore);
        head.setItemMeta(meta);

        return head;
    }
    public ItemStack getAddFriendsItem(Player player)
    {
        ItemStack head = getPlayerHead(player);
        ItemMeta meta = head.getItemMeta();

        meta.setDisplayName(MessageManager.getItemName("addFriends"));
        head.setItemMeta(meta);
        return head;
    }

    public ItemStack getFriendsIcon(Player player)
    {
        if(skulls.containsKey(player.getUniqueId().toString())) return skulls.get(player.getUniqueId().toString());
        ItemStack skull = getPlayerHead(player);
        ItemMeta meta = skull.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(MessageManager.getItemName("clickToAdd"));
        meta.setLore(lore);
        skull.setItemMeta(meta);
        skulls.put(player.getUniqueId().toString(), skull);
        return skull;
    }


    public ItemStack getForwardArrow() {
        return forwardArrow;
    }

    public ItemStack getBackwardArrow()
    {
        return backArrow;
    }

    public ItemStack getNoPageLeft()
    {
        return noPageLeft;
    }

    public ItemStack getSearchIcon() {
        return searchIcon;
    }

    public ItemStack getBlackListButton(boolean blacklistEnabled) {
        return blacklistEnabled ? blacklistButton : whitelistButton;
    }

    public ItemStack getAdvancedFilterButton(boolean enabled)
    {
        return enabled ? advancedFilterOn : advancedFilterOff;
    }
}
