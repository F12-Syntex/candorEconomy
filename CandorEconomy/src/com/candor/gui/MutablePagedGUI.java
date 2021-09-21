package com.candor.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.candor.builder.ExecutionBuilder;
import com.candor.builder.ItemGenerator;
import com.candor.gui.attributes.GuiData;
import com.candor.gui.attributes.GuiItem;
import com.candor.gui.attributes.paged.Action;
import com.candor.gui.attributes.paged.Actions;
import com.candor.gui.attributes.paged.Condition;
import com.candor.gui.attributes.paged.Page;
import com.candor.gui.attributes.paged.Slot;
import com.candor.main.Candor;
import com.candor.utils.MessageUtils;
import com.candor.utils.Numbers;
import com.candor.utils.SoundUtils;

public abstract class MutablePagedGUI extends ConfigurableGui implements Listener{
	
	protected Page page;
	protected GuiData contents;
	
	@Override
	public void refresh() {
		
		this.inv.clear();
		
		this.contents = Candor.getInstance().configManager.getGuiConfig(this.config()).getGuiData();
		
		List<GuiItem> items = contents.getItems();
		
		for(GuiItem item : items) {
		
			ItemStack invItem = this.applyDefaultPlaceholders(this.getItemStack(item, this.player));
			
			for(int slot : item.getSlot().getSlot()) {
				
				if(item.getSlot().allPages || Numbers.of(item.getSlot().getPage()).contains(this.page.getPage())) {
					this.inv.setItem(slot, invItem);		
				}
			}
			
		}
		
	}
	
	
	@EventHandler()
	public void onClick(InventoryClickEvent e) {

		if(!super.authenticateClick(e)) return;
		
		e.setCancelled(!contents.isCanModify());

		List<GuiItem> amount = contents.getItems()
				.stream()
				.filter(i -> i.getSlot().getSlot().contains(e.getSlot()))
				.filter(i -> i.getSlot().pageIncluded(this.page.getPage()))
				.collect(Collectors.toList());
		
		if(!amount.isEmpty()) {
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
				
				this.onParentExecution(this.player).get(item.getExecutable()).run();
			});
		}
	}
	
	//public abstract GuiPagedData configuration();
	
	public List<GuiItem> getPageBar() {
		
		int size = 54;
		
		List<GuiItem> items = new ArrayList<GuiItem>();
		
		List<Integer> slots = IntStream.range((size-18), (size-9)).boxed().collect(Collectors.toList());
		
		if(size > 18) {
			items.add(new GuiItem(ItemGenerator.getInstance(Material.WHITE_STAINED_GLASS_PANE).setName("").get(), Slot.defaultSlot(slots), "empty"));
		}

		Actions nextActions = new Actions();
		Actions prevActions = new Actions();
		
		nextActions.addAction(new Action("last_page", ItemGenerator.getInstance(Material.RED_STAINED_GLASS_PANE).setName("&cNext").get()));
		prevActions.addAction(new Action("first_page", ItemGenerator.getInstance(Material.RED_STAINED_GLASS_PANE).setName("&cBack").get()));
		
		items.add(new GuiItem(ItemGenerator.getInstance(Material.GREEN_STAINED_GLASS_PANE).setName("&aNext").get(), Slot.defaultSlot(Numbers.of(size-1)), "pane:next", nextActions));
		items.add(new GuiItem(ItemGenerator.getInstance(Material.GREEN_STAINED_GLASS_PANE).setName("&aBack").get(), Slot.defaultSlot(Numbers.of(size-9)), "pane:back", prevActions));
		
		return items;
	}

	public abstract int size();
	
	public void open(Player player, int page) {

		this.player = player;
		
		this.initialisePlaceholders(player);
		
		GuiData configuration = Candor.getInstance().configManager.getGuiConfig(this.config()).getGuiData();
		
		Candor.getInstance().getServer().getPluginManager().registerEvents(this, Candor.getInstance());
		
		this.inv = Bukkit.createInventory(null, configuration.getSize(), MessageUtils.translateAlternateColorCodes(configuration.getName()));
	
		this.page = new Page(configuration.getSize(), page);
		
		this.messages = Candor.getInstance().configManager.messages;
		
		SoundUtils.playSound(player, configuration.getInteractionSound().getOpen());
	    
		this.refresh();
		
	    player.openInventory(inv);
	}
	
	@Override
	public Map<String, Runnable> onParentExecution(Player payer){
		return ExecutionBuilder
				.createExecution("empty", () -> {})
				
				.addExecution("pane:next", () -> {
					if(this.page.getPage() < this.getMaxPage()) {
						this.page.nextPage();
						this.refresh();
					}else {
						
						
						
						//last page
						
					}
				})
				
				.addExecution("pane:back", () -> {
					
					if(this.page.getPage() > 1) {
						this.page.prevPage();
						this.refresh();
					}else {
						
						//first page
						
					}
				
				})
				
				.include(this.onExecution(payer))
				.getPairs();
	}
	
	public abstract Map<String, Runnable> onExecution(Player payer);
	
	public int getMaxPage() {
		List<GuiItem> items = this.contents.getItems();
		if(items.isEmpty()) return 0;
		
		int max = 1;
		
		for(GuiItem item : items) {
			if(item.getSlot().allPages) continue;
			int var = Numbers.max(item.getSlot().getPage());
			if(max < var) {
				max = var;
			}
		}
		
		return max;
		
	}
	
	public abstract Map<String, Condition> onActions(Player payer);
	
}
