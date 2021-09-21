package com.candor.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.candor.database.Databases;
import com.candor.database.MySQLLoader;

public class ConfigManager {

    public ArrayList<Config> config = new ArrayList<Config>();
	
    public Messages messages;
    public Permissions permissions;
    public Cooldown cooldown;
    public Configs configs;
    public Settings settings;
    
    public MySQLLoader sqlLoader;
    
    public void setup(Plugin plugin) {
    	
    	this.messages = new Messages("messages", 1.7);
    	this.permissions = new Permissions("permissions", 1.7);
    	this.cooldown = new Cooldown("cooldown", 1.4);
    	this.configs = new Configs("configs", 1.4);
    	this.settings = new Settings("settings", 1.4);
    	
    	this.config.clear();

    	this.config.add(messages);
    	this.config.add(permissions);
    	this.config.add(cooldown);
    	this.config.add(configs);
    	this.config.add(settings);
    	
    	
    	//register guis
    	GuiConfigRegister guis = new GuiConfigRegister();
    	guis.initiate();
    	this.config.addAll(guis.getGuiConfig());
    	
    	
    	
    	//register mysql databases
    	this.sqlLoader = new MySQLLoader();
    	sqlLoader.loadDatabases();
    	this.config.addAll(sqlLoader.getConfiguration());
    	
    	this.configure(plugin, config);
    	
    	this.sqlLoader.connect();
    }

    public void configure(Plugin plugin, List<Config> configs) {
    	for(int i = 0; i < config.size(); i++) {
        	
    		Config currentConfig = config.get(i);
    		
    		currentConfig.setup();
    		
    		if(currentConfig.getConfiguration().contains("identity.version") && (currentConfig.getConfiguration().getDouble("identity.version") == currentConfig.getVerison())) {
    			currentConfig.initialize();
        		continue;
    		}
    		
	    		File file = currentConfig.backup();
    			
    			if(currentConfig.folder() == Folder.DEFAULT) {
	    			new File(plugin.getDataFolder(), currentConfig.getName() + ".yml").delete();
		    		plugin.saveResource(currentConfig.getName() + ".yml", false);
	    		}else {
	    			File old = new File(plugin.getDataFolder(), currentConfig.folder().getPath());
	    			new File(old, currentConfig.getName() + ".yml").delete();
		    		plugin.saveResource(currentConfig.getName() + ".yml", false);
    			}
	    	
       			if(file != null) {
   					final FileConfiguration oldConfig = YamlConfiguration.loadConfiguration(file);
   					
   					oldConfig.getKeys(true).forEach(o -> {
   						if(currentConfig.getConfiguration().contains(o)) {
   							currentConfig.getConfiguration().set(o, oldConfig.get(o));
   						}
   					});
   		    
   					currentConfig.setDefault();
   					
   		    		currentConfig.getConfiguration().set("identity.version", currentConfig.getVerison());
   					
   		    		currentConfig.save();
   		    		currentConfig.initialize();
   		    		
   				}

    	}
    }
    
    public GUIConfig getGuiConfig(String name) {
    	for(Config config : this.config) { 
    		if(config instanceof GUIConfig) {
    			GUIConfig guiConfig = (GUIConfig)config;
    			if(guiConfig.getName().equals(name)) {
    				return guiConfig;
    			}
    		}
    	}
		return null;
    }
    
    
	public DatabaseConfig getDatabaseConfig(Databases database) {
    	for(Config config : this.config) { 
    		if(config instanceof DatabaseConfig) {
    			DatabaseConfig guiConfig = (DatabaseConfig)config;
    			if(guiConfig.getName().equals(database.name().toLowerCase())) {
    				return guiConfig;
    			}
    		}
    	}
		return null;
	}
    
}
