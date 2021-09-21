package com.candor.gui;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.candor.config.Messages;
import com.candor.main.Candor;

public abstract class GUI implements Listener{

	protected Player player;
	protected Inventory inv;
	protected Messages messages;
	
	public GUI(Player player) {
		this.player = player;
		Candor.getInstance().getServer().getPluginManager().registerEvents(this, Candor.getInstance());
		inv = Bukkit.createInventory(this.player, size(), name());
		this.messages = Candor.getInstance().configManager.messages;
	}
	
	@EventHandler()
	public void onOpen(InventoryOpenEvent e) {
		if(e.getPlayer().getUniqueId().compareTo(this.player.getUniqueId()) != 0) return;

		onInventoryOpen(e);
	}
	
	@EventHandler()
	public void onClose(InventoryCloseEvent e) {
		if(e.getPlayer().getUniqueId().compareTo(this.player.getUniqueId()) != 0) return;
		
		this.onInventoryClose(e);
		HandlerList.unregisterAll(this);
	}
	
	@EventHandler()
	public void onClick(InventoryClickEvent e) {
		if(e.getWhoClicked().getUniqueId().compareTo(this.player.getUniqueId()) != 0) return;
		if(e.getClickedInventory() == null) return;
		if(e.getCurrentItem() == null) return;
		
		e.setCancelled(!canTakeItems());
		
		this.onInventoryClick(e);
	}
	
	public abstract String name();
	
	public abstract int size();
	
	public abstract Sound sound();
	public abstract float soundLevel();
	public abstract boolean canTakeItems();
	
	public abstract void onInventoryClick(InventoryClickEvent e);
	public abstract void onInventoryOpen(InventoryOpenEvent e);
	public abstract void onInventoryClose(InventoryCloseEvent e);
	public abstract void Contents(Inventory inv);
	
	public void open() {
		player.getWorld().playSound(player.getLocation(), this.sound(), this.soundLevel(), this.soundLevel());
	    Contents(inv);
	    player.openInventory(inv);
	}
	
	public void addItem(int index, ItemStack item) {
		inv.setItem(index, item);
	}
	
	public void fillEmpty(ItemStack stack) {
		for(int i = 0; i < this.size(); i++) {
			if(this.inv.getItem(i) == null) {
				this.inv.setItem(i, stack);
			}
		}
	}
	
	
}
