package com.candor.config;

import java.util.List;

import com.candor.utils.ComponentBuilder;

public class Credentials extends Config{

	public String host = "localhost";
	public String port = "3306";
	public String database = "candor";
	public String username = "root";
	public String password = "";
	
	public Credentials(String name, double version) {
		super(name, version);
		this.items.add(new ConfigItem("Settings.credentials.host", this.host));
		this.items.add(new ConfigItem("Settings.credentials.port", this.port));
		this.items.add(new ConfigItem("Settings.credentials.database", this.database));
		this.items.add(new ConfigItem("Settings.credentials.username", this.username));
		this.items.add(new ConfigItem("Settings.credentials.password", this.password));
	
	}
	
	@Override
	public void initialize() {
	
		this.host = this.getConfiguration().getString("Settings.credentials.host");
		this.port = this.getConfiguration().getString("Settings.credentials.port");
		this.database = this.getConfiguration().getString("Settings.credentials.database");
		this.username = this.getConfiguration().getString("Settings.credentials.username");
		this.password = this.getConfiguration().getString("Settings.credentials.password");
		
	}

	@Override
	public Folder folder() {
		return Folder.DEFAULT;
	}

	@Override
	public List<String> header() {
		// TODO Auto-generated method stub
		return ComponentBuilder.createLore("Credentials for your database");
	}
}
