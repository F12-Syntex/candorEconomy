package com.candor.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.candor.builder.ItemGenerator;
import com.candor.gui.ConfigurableGui;
import com.candor.gui.GuiStorage;
import com.candor.gui.attributes.AbstractedGuiData;
import com.candor.gui.attributes.GuiData;
import com.candor.gui.attributes.GuiItem;
import com.candor.gui.attributes.InteractionSounds;
import com.candor.gui.attributes.Type;
import com.candor.gui.attributes.paged.Action;
import com.candor.gui.attributes.paged.Actions;
import com.candor.gui.attributes.paged.Slot;
import com.candor.main.Candor;
import com.candor.utils.ComponentBuilder;
import com.candor.utils.MessageUtils;
import com.candor.utils.Numbers;

public class GuiConfigRegister {

	private ArrayList<GUIConfig> guiConfig;
	
	private GuiStorage storage;
	
	public GuiConfigRegister() {
		this.guiConfig = new ArrayList<GUIConfig>();
	}
	
    public void initiate() {
		
    	this.storage = Candor.getInstance().guiStorage;
    	
    	List<ConfigurableGui> data = storage.getGuis();
    	
    	for(ConfigurableGui gui : data) {
    	
    		AbstractedGuiData configurationData = gui.configuration();
			
    		GUIConfig config = this.generateGuiConfig(gui);
			
			config.items.add(new ConfigItem("GUI.name", configurationData.getName()));
			
			config.items.add(new ConfigItem("GUI.sounds.open.sound", configurationData.getInteractionSound().getOpen().getSound().name()));
			config.items.add(new ConfigItem("GUI.sounds.open.volume", configurationData.getInteractionSound().getOpen().getVolume()));
			config.items.add(new ConfigItem("GUI.sounds.open.pitch", configurationData.getInteractionSound().getOpen().getPitch()));
			
			config.items.add(new ConfigItem("GUI.sounds.close.sound", configurationData.getInteractionSound().getClose().getSound().name()));
			config.items.add(new ConfigItem("GUI.sounds.close.volume", configurationData.getInteractionSound().getClose().getVolume()));
			config.items.add(new ConfigItem("GUI.sounds.close.pitch", configurationData.getInteractionSound().getClose().getPitch()));
			
			config.items.add(new ConfigItem("GUI.mutable", configurationData.isCanModify()));
			config.items.add(new ConfigItem("GUI.size", configurationData.getSize()));
			
			
			int counter = 1;
			
			for(GuiItem item : configurationData.getItems()) {
				
				ItemStack object = item.getItem();
				
				String path = "GUI.items." + counter;
				
				config.items.addAll(this.getDefaultSerialisedItemStackPath(path + ".item", object));

				if(item.getSlot().isPaged()) {
					config.items.add(new ConfigItem("GUI.items." + counter + ".slot.slot", item.getSlot().getSlot()));
					config.items.add(new ConfigItem("GUI.items." + counter + ".slot.page", item.getSlot().getPageAsString()));
				}else{
					config.items.add(new ConfigItem("GUI.items." + counter + ".slot", item.getSlot().getSlot()));
				}
				
				config.items.add(new ConfigItem("GUI.items." + counter + ".onClick", item.getExecutable()));
				config.items.add(new ConfigItem("GUI.items." + counter + ".permission", item.getPermission()));
				
				if(!item.getActions().isEmpty()) {
					for(Action action : item.getActions().getActions()) {
						String dir = "GUI.items." + counter + ".actions." + action.getCondition() + ".item";
						config.items.addAll(this.getDefaultSerialisedItemStackPath(dir, action.getBlock()));
					}
				}
				
				counter++;
				
			}
			
			this.guiConfig.add(config);			
    	}
    	
    }
    
    public List<ConfigItem> getDefaultSerialisedItemStackPath(String path, ItemStack item) {
		
		String name = "";
		Material material = item.getType();
		int amount = item.getAmount();
		
		List<ConfigItem> paths = new ArrayList<>();
		
		List<String> lore = new ArrayList<>();
		Map<Enchantment, Integer> enchantments = new HashMap<>();
		List<String> flags = new ArrayList<String>();
		
		if(item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			lore = MessageUtils.removeColourCodeSymbol(meta.getLore());
			enchantments = meta.getEnchants();
			flags = meta.getItemFlags().stream().map(i -> i.name()).collect(Collectors.toList());
			name = meta.getDisplayName();
		}
    	
		paths.add(new ConfigItem(path + ".name", name.replace("§", "&")));
		paths.add(new ConfigItem(path+ ".type", material.name()));
		paths.add(new ConfigItem(path + ".amount", amount));
		
		paths.add(new ConfigItem(path + ".lore", lore));
		
		paths.add(new ConfigItem(path + ".flags", flags));
		
		for (java.util.Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
			paths.add(new ConfigItem(path + ".enchants." + entry.getKey().getKey().getKey(), entry.getValue()));
		}
		
		return paths;
    }
    
    public ItemStack getItemStackFromSection(ConfigurationSection attributes) {
    	
		String ItemName = attributes.getString("name");
		Material type = Material.valueOf(attributes.getString("type"));
		int amount = attributes.getInt("amount");

		
		List<String> lore = attributes.getStringList("lore");						
		List<ItemFlag> flags = attributes.getStringList("flags").stream().map(i -> ItemFlag.valueOf(i)).collect(Collectors.toList());						
		
		Map<Enchantment, Integer> enchants = new HashMap<>();						
		
		if(attributes.isConfigurationSection("enchants")) {
			for(String enchant : attributes.getConfigurationSection("enchants").getKeys(false)) {
				enchants.put(Enchantment.getByKey(NamespacedKey.minecraft(enchant)), attributes.getInt("enchants." + enchant));
			}
		}
		
		
		ItemStack item = ItemGenerator.getInstance(type)
				.setName(ItemName)
				.setAmount(amount)
				.setLore(lore)
				.addFlag(flags)
				.addEnchants(enchants)
				.get();
    	
    	
    	return item;
    }

    public GUIConfig generateGuiConfig(ConfigurableGui gui) {
    	
    	AbstractedGuiData configurationData = gui.configuration();
    	
    	GUIConfig config = new GUIConfig(gui.config(), 1) {
		
		@Override
		public void initialize() {
			
			Sound open = Sound.valueOf(this.getConfiguration().getString("GUI.sounds.open.sound"));
			
			float openVoume = (float)this.getConfiguration().getDouble("GUI.sounds.open.volume");
			float openPitch = (float)this.getConfiguration().getDouble("GUI.sounds.open.pitch");
			
			Sound close = Sound.valueOf(this.getConfiguration().getString("GUI.sounds.close.sound"));
			
			float closeVoume = (float)this.getConfiguration().getDouble("GUI.sounds.close.volume");
			float closePitch = (float)this.getConfiguration().getDouble("GUI.sounds.close.pitch");
			
			com.candor.gui.attributes.Sound openSound = new com.candor.gui.attributes.Sound(open, openVoume, openPitch);
			com.candor.gui.attributes.Sound closeSound = new com.candor.gui.attributes.Sound(close, closeVoume, closePitch);
			
			String name = this.getConfiguration().getString("GUI.name");
			InteractionSounds sounds = new InteractionSounds(openSound, closeSound);
			boolean mutable = this.getConfiguration().getBoolean("GUI.size");
			int size = this.getConfiguration().getInt("GUI.size");
			
			List<GuiItem> items = new ArrayList<>();
			
			if(this.getConfiguration().getConfigurationSection("GUI").isConfigurationSection("items")) {

				ConfigurationSection itemSection = this.getConfiguration().getConfigurationSection("GUI.items");
				
				for(String key : itemSection.getKeys(false)) {
					ConfigurationSection attributes = itemSection.getConfigurationSection(key + ".item");
					
					Slot slot = Slot.slot(0);
					
					if(configurationData.getType() == Type.MUTABLE_GUI) {
						
						slot.setSlot(itemSection.getIntegerList(key + ".slot"));
						
					}else {
					
						slot.setSlot(itemSection.getIntegerList(key + ".slot.slot"));
						
						String page = itemSection.getString(key + ".slot.page");
						
						if(page.equalsIgnoreCase("ALL")) {
							slot.allPages = true;	
						}else {
							slot.setPage(Numbers.buildFromString(page));
						}
					}
					
					
					String executable = itemSection.getString(key + ".onClick");
					String permission = itemSection.getString(key + ".permission");
					
					ItemStack item = getItemStackFromSection(attributes);
					
					Actions actions = new Actions();
					
					if(itemSection.isConfigurationSection(key + ".actions")) {
						ConfigurationSection actionsSection = itemSection.getConfigurationSection(key + ".actions");
						for(String action : actionsSection.getKeys(false)) {
							ItemStack actionItem = getItemStackFromSection(actionsSection.getConfigurationSection(action + ".item"));
							Action data = new Action(action, actionItem);
							actions.addAction(data);
						}	
					}

					GuiItem guiItem = new GuiItem(item, slot, executable, permission);
					guiItem.setActions(actions);
					
					 
					items.add(guiItem);

				}
			}
			
			this.guiData = new GuiData(name, sounds, mutable, size, items);
		}
		
		@Override
		public Folder folder() {
			return Folder.GUI;
		}

		@Override
		public List<String> header() {
			// TODO Auto-generated method stub
			return ComponentBuilder.createLore("Modify your gui here");
		}
		
	};
	
		return config;
    }
    
	public ArrayList<GUIConfig> getGuiConfig() {
		return guiConfig;
	}

	public void setGuiConfig(ArrayList<GUIConfig> guiConfig) {
		this.guiConfig = guiConfig;
	}
    
}
