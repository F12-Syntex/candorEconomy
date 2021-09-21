package com.candor.gui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.candor.builder.PlaceholderFactory;
import com.candor.config.Messages;
import com.candor.gui.attributes.AbstractedGuiData;
import com.candor.gui.attributes.GuiData;
import com.candor.gui.attributes.GuiItem;
import com.candor.gui.attributes.paged.Action;
import com.candor.gui.attributes.paged.Condition;
import com.candor.main.Candor;
import com.candor.utils.ItemHelper;
import com.candor.utils.SoundUtils;

public abstract class ConfigurableGui implements Listener{

	protected Player player;
	protected Inventory inv;
	protected Messages messages;
	
	protected Map<String, String> placeholders;
	
	public ConfigurableGui() {}
	
	@EventHandler()
	public void onOpen(InventoryOpenEvent e) {
		if(e.getPlayer().getUniqueId().compareTo(this.player.getUniqueId()) != 0) return;
	}
	
	@EventHandler()
	public void onClose(InventoryCloseEvent e) {
		if(e.getPlayer().getUniqueId().compareTo(this.player.getUniqueId()) != 0) return;
		
		HandlerList.unregisterAll(this);
		
		GuiData contents = Candor.getInstance().configManager.getGuiConfig(this.config()).getGuiData();
		
		SoundUtils.playSound(player, contents.getInteractionSound().getClose());
		
	}

	public abstract String config();
	
	public abstract AbstractedGuiData configuration();
	
	public void refresh() {
		
		this.inv.clear();
		
		GuiData contents = Candor.getInstance().configManager.getGuiConfig(this.config()).getGuiData();
		
		for(GuiItem item : contents.getItems()) {
		
			ItemStack invItem = this.applyDefaultPlaceholders(this.getItemStack(item, this.player));

			for(int slot : item.getSlot().getSlot()) {
				this.inv.setItem(slot, invItem);	
			}
			
		}
		
	}
	
	public ItemStack applyDefaultPlaceholders(ItemStack invItem) {
		Map<String, String> placeholders = this.placeholders;
		
		String serialisedData = ItemHelper.itemToStringBlob(invItem);
		
		for(String placeholder : placeholders.keySet()) {
			String replaceTo = placeholders.get(placeholder);
			serialisedData = serialisedData.replace(placeholder, replaceTo);
		}
		
		return ItemHelper.stringBlobToItem(serialisedData);
	}
	
	protected boolean authenticateClick(InventoryClickEvent e) {
		if(e.getWhoClicked().getUniqueId().compareTo(this.player.getUniqueId()) != 0) return false;
		if(e.getClickedInventory() == null) return false;
		if(e.getCurrentItem() == null) return false;
		if(e.getClick() == ClickType.DOUBLE_CLICK) return false;
		return true;
	}
	
	public ItemStack getItemStack(GuiItem item, Player player) {
		
		Map<String, Condition> executions = this.onParentActions(player);
		
		for(Action action : item.getActions().getActions()) {
			if(executions.get(action.getCondition()).valid(this)) {
				return action.getBlock();
			}
		}
		
		return item.getItem();
	}
	
	public abstract boolean loadFromConfig();
	
	public abstract Map<String, Runnable> onParentExecution(Player payer);
	
	public Map<String, Condition> onParentActions(Player player){
		Map<String, Condition> actions = new HashMap<>();
		
		actions.put("last_page", (ConfigurableGui instance) -> {
			if(instance instanceof MutablePagedGUI) { 
				MutablePagedGUI gui = (MutablePagedGUI)instance;
				if(gui.page.getPage() == gui.getMaxPage()) {
					return true;
				}
			}
			return false;
		});
		
		actions.put("first_page", (ConfigurableGui instance) -> {
			if(instance instanceof MutablePagedGUI) {
				MutablePagedGUI gui = (MutablePagedGUI)instance;
				if(gui.page.getPage() == 1) {
					return true;
				}
			}
			return false;
		});
		
		Map<String, Condition> guiActions = onActions(player);
		
		for(String key : guiActions.keySet()) {
			actions.put(key, guiActions.get(key));
		}
		
		return actions;
	}
	
	public void initialisePlaceholders(Player player){
		this.placeholders = PlaceholderFactory
				.createPlaceholder("%player%", player.getName())
				.addPlaceholder("%prefix%", Candor.getInstance().configManager.messages.prefix)
				.get();
	}
	
	public abstract Map<String, Condition> onActions(Player payer);
	
}
