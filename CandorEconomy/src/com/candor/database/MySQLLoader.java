package com.candor.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.reflections.Reflections;

import com.candor.config.Config;
import com.candor.config.ConfigItem;
import com.candor.config.DatabaseConfig;
import com.candor.config.Folder;
import com.candor.main.Candor;
import com.candor.utils.ComponentBuilder;

public class MySQLLoader {

	private List<MySql> databases;
	private List<Config> configuration;
	
	public MySQLLoader() {
		this.databases = new ArrayList<>();
		this.configuration = new ArrayList<>();
	}
	
	
	public void loadDatabases() {
	
    	try {
	    	//Automatically register all databases.
	        Reflections reflections = new Reflections(this.getClass().getPackage().getName());    
	        Set<Class<? extends MySql>> classes = reflections.getSubTypesOf(MySql.class);
	        
	        for(Class<? extends MySql> databases : classes) {
	        		@SuppressWarnings("deprecation")
					MySql database = databases.newInstance();
					this.databases.add(database);
					
					Config config = registerCredentialsConfig(database);
					
					this.configuration.add(config);
			}
	        
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}
	
	public void connect() {
		for(MySql database : this.databases) {
			try {
				database.loadConfig(Candor.getInstance().configManager.getDatabaseConfig(database.config()));
				database.connect();
				database.setup();
				Candor.inform("&a" + database.config().name().toLowerCase() + ".sql has connected!");
			} catch (SQLException e) {
				Candor.inform("&c" + e.getLocalizedMessage());
				Candor.inform("&cDisabling (" + database.config().name().toLowerCase() + ".sql)");
				Bukkit.getServer().shutdown();
			}
		}
	}
	
	public void disconnect() {
		for(MySql database : this.databases) {
			database.disconnect();
		}
	}

	public Config registerCredentialsConfig(MySql database) {
		
		Config config = new DatabaseConfig(database.config(), 1.0) {
			
			@Override
			public void initialize() {
				
				ConfigurationSection credentialSection = this.getConfiguration().getConfigurationSection("database.credentials");
				ConfigurationSection updaterSection = this.getConfiguration().getConfigurationSection("database.updater");
				
				MySQLCredentials credentials = new MySQLCredentials(credentialSection.getString("host"),
																	credentialSection.getString("port"),
																	credentialSection.getString("database"),
																	credentialSection.getString("username"),
																	credentialSection.getString("password"));
				
				MySQLUpdater updater = new MySQLUpdater(updaterSection.getLong("ticks"));
				
				this.setCredentials(credentials);
				this.setUpdater(updater);
				
			}
			
			@Override
			public Folder folder() {
				return Folder.DATABASE;
			}
			
			@Override
			public List<String> header() {
				// TODO Auto-generated method stub
				return ComponentBuilder.createLore("Edit your credentials here.");
			}
			
		};
		
		
		config.items.add(new ConfigItem("database.credentials.host", "localhost"));
		config.items.add(new ConfigItem("database.credentials.port", "3306"));
		config.items.add(new ConfigItem("database.credentials.database", config.getName()));
		config.items.add(new ConfigItem("database.credentials.username", "root"));
		config.items.add(new ConfigItem("database.credentials.password", ""));
		
		//default time is 72000L or 1 hour.
		config.items.add(new ConfigItem("database.updater.ticks", 20L*60*60));
		
		return config;
		
	}
	
	public List<MySql> getDatabases() {
		return databases;
	}


	public void setDatabases(List<MySql> databases) {
		this.databases = databases;
	}


	public List<Config> getConfiguration() {
		return configuration;
	}


	public void setConfiguration(List<Config> configuration) {
		this.configuration = configuration;
	}

	public MySql getDatabase(Databases config) {
		return this.databases.stream()
							.filter(i -> i.config() == config)
							.findFirst()
							.get();
	}
	
	
}
