package com.candor.gui;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.candor.builder.ExecutionBuilder;
import com.candor.gui.attributes.GuiData;
import com.candor.gui.attributes.GuiItem;
import com.candor.gui.attributes.paged.Condition;
import com.candor.main.Candor;
import com.candor.utils.MessageUtils;
import com.candor.utils.SoundUtils;

public abstract class MutableGUI extends ConfigurableGui implements Listener{

	@EventHandler()
	public void onClick(InventoryClickEvent e) {
		
		if(!super.authenticateClick(e)) return;
		
		GuiData contents = Candor.getInstance().configManager.getGuiConfig(this.config()).getGuiData();
		
		if(!contents.isCanModify()) {
			e.setCancelled(true);
		}
		
		List<GuiItem> amount = contents.getItems().stream().filter(i -> i.getSlot().getSlot().contains(e.getSlot())).collect(Collectors.toList());
		
		if(!amount.isEmpty() && e.getClickedInventory() == super.inv) {
			Bukkit.getScheduler().runTask(Candor.getInstance(), () -> {
				GuiItem item = amount.get(0);
				
				if(!this.player.hasPermission(item.getPermission())) {
					String error = Candor.getInstance().configManager.messages.invalid_permission;
					MessageUtils.sendRawMessage(player, error);
					return;
				}
				
				if(!this.onParentExecution(player).containsKey(item.getExecutable())) {
					
					String error_message = "&cERROR IN CONFIG -> " + this.config() + ".yml invalid executable \"&7" + amount.get(0).getExecutable() + "&c\"";
					
					Candor.info(error_message);
					MessageUtils.sendMessage(player, error_message);
					
					return;
				}
				
				this.onParentExecution(this.player).get(amount.get(0).getExecutable()).run();
			});
		}
	}
	
	public void open(Player player) {

		this.player = player;
		
		this.initialisePlaceholders(player);
		
		GuiData configuration =  Candor.getInstance().configManager.getGuiConfig(this.config()).getGuiData();
		
		Candor.getInstance().getServer().getPluginManager().registerEvents(this, Candor.getInstance());
		
		inv = Bukkit.createInventory(null, configuration.getSize(), MessageUtils.translateAlternateColorCodes(configuration.getName()));
		this.messages = Candor.getInstance().configManager.messages;
		
		SoundUtils.playSound(player, configuration.getInteractionSound().getOpen());
	    
		this.refresh();
		
	    player.openInventory(inv);
	}
	
	@Override
	public Map<String, Runnable> onParentExecution(Player payer){
		return ExecutionBuilder
				.createExecution("empty", () -> {})
				.include(this.onExecution(payer))
				.getPairs();
	}

	public abstract Map<String, Runnable> onExecution(Player payer);
	public abstract Map<String, Condition> onActions(Player payer);
}
