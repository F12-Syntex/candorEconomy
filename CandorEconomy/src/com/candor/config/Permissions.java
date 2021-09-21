package com.candor.config;

import java.util.List;

import com.candor.utils.ComponentBuilder;

public class Permissions extends Config{

	public String basic = "bukkit.command.help";
	public String admin = "candor.admin";
	public String bypass = "candor.timer.bypass";
	public String reload = "candor.admin.reload";
	public String configure = "candor.admin.configure";
	
	public String set = "candor.set";
	public String pay = "candor.pay";
	public String balance = "candor.balance";
	
	
	public Permissions(String name, double version) {
		super(name, version);

		this.items.add(new ConfigItem("Permissions.everyone.basic", basic));
		
		this.items.add(new ConfigItem("Permissions.administration.admin", admin));
		this.items.add(new ConfigItem("Permissions.administration.timer_bypass", bypass));
		this.items.add(new ConfigItem("Permissions.administration.reload", reload));
		this.items.add(new ConfigItem("Permissions.administration.configure", configure));
		
		this.items.add(new ConfigItem("Permissions.administration.set", set));
		this.items.add(new ConfigItem("Permissions.administration.pay", set));
		this.items.add(new ConfigItem("Permissions.administration.balance", set));
		
	}

	@Override
	public Folder folder() {
		// TODO Auto-generated method stub
		return Folder.DEFAULT;
	}
	
	@Override
	public void initialize() {
		this.basic = this.getConfiguration().getString("Permissions.everyone.basic");
		this.admin = this.getConfiguration().getString("Permissions.administration.admin");
		this.bypass = this.getConfiguration().getString("Permissions.administration.timer_bypass");
		this.reload = this.getConfiguration().getString("Permissions.administration.reload");
		this.configure = this.getConfiguration().getString("Permissions.administration.configure");
		
		this.set = this.getConfiguration().getString("Permissions.administration.set");
		this.pay = this.getConfiguration().getString("Permissions.administration.pay");
		this.balance = this.getConfiguration().getString("Permissions.administration.balance");
	}

	@Override
	public List<String> header() {
		// TODO Auto-generated method stub
		return ComponentBuilder.createLore("you can change the permissions here");
	}
	
}
