package com.lucacando.impactMines.shop;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Shop {

    private Inventory shopGUI;
    private HashMap<ItemStack, ArrayList<ItemStack>> trades;
    private String id;

    public Shop(Inventory shopGUI, HashMap<ItemStack, ArrayList<ItemStack>> trades, String id) {
        this.shopGUI = shopGUI;
        this.trades = trades;
        this.id = id;
    }

    public String getId() {
        return id;
    }
    public Inventory getShopGUI() {
        return shopGUI;
    }
    public Set<ItemStack> getItems() {
        return trades.keySet();
    }
    public ArrayList<ItemStack> getTradeItems(ItemStack itemToBuy) {
        return trades.get(itemToBuy);
    }
    public HashMap<ItemStack, ArrayList<ItemStack>> getTrades() {
        return trades;
    }
    public void addTrade(ItemStack item, ArrayList<ItemStack> tradeItems) {
        trades.put(item, tradeItems);
    }
}
