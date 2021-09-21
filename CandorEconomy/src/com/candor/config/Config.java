package com.candor.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.candor.main.Candor;

public abstract class Config {

	protected String name;
	protected double verison;
	protected FileConfiguration configuration;
	protected File config;
	protected Candor base;
	public List<ConfigItem> items;
	
	public abstract Folder folder();
	
	public Config(String name, double version) {
		this.name = name;
		this.verison = version;
		this.base = Candor.getInstance();
		this.items = new ArrayList<ConfigItem>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getVerison() {
		return verison;
	}

	public void setVerison(double verison) {
		this.verison = verison;
	}

	public FileConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(FileConfiguration configuration) {
		this.configuration = configuration;
	}
	
	public void setup() {
		config = new File(this.base.getDataFolder().getAbsolutePath(), this.name + ".yml");
		
		if(folder() != Folder.DEFAULT) {
			File old = new File(this.base.getDataFolder().getAbsolutePath(), folder().getPath());
			config = new File(old, this.name + ".yml");
		}
		
		this.configuration = YamlConfiguration.loadConfiguration(config);
		
		if(!config.exists()) {
		
			this.configuration.set("identity.version", this.verison);
			
			String header = "";
			
			List<String> headerLore = this.header();
			
			if(!headerLore.isEmpty()) {
				for(int i = 0; i < headerLore.size(); i++) {
					if((i+1) < headerLore.size()) {
						header += headerLore.get(i) + "\n";	
					}else {
						header += headerLore.get(i);
					}
				}
			}
			
			this.configuration.options().header(header);
			
			for(ConfigItem i : this.items) {
				this.configuration.set(i.getPath(), i.getData());
			}
			
			
		}
		
		
		this.save();
		
	}

	public void setDefault() {
		this.configuration.set("identity.version", this.verison);
		
		for(ConfigItem i : this.items) {
			this.configuration.set(i.getPath(), i.getData());
		}
	}
	
    public void save() {
		try {
			this.configuration.save(this.config);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public File getConfig() {
		return config;
	}

	public void setConfig(File config) {
		this.config = config;
	}
	
	public abstract void initialize();
	public abstract List<String> header();
	
	public File backup() {
		
		File depreciated = new File(this.base.getDataFolder(), "depreciated");
		File backups = new File(depreciated, this.name);

		Candor.info("Backing up...");
		
		if(backups.mkdirs() || backups.exists()) {

			Candor.info("created");
			
			if(!this.getConfig().exists()) return null;
			
				int files = backups.listFiles().length + 1;
				
				File backup = new File(backups, this.name + "(" + files + ")" + ".yml");
				
				final File tempConfig = this.getConfig();
				
				boolean success = this.getConfig().renameTo(backup);
				
				if(success) {
					Candor.info("&a" + this.name + ".yml has been backed up!");	
				}else {
					Candor.info("&cCouldnt backup!");
				}
			
				return tempConfig;
			
		}else if(!backups.exists()) {
			Candor.info("&cCouldnt create backup folder!");
		}
		
		return null;
		
	}
	
	
}
