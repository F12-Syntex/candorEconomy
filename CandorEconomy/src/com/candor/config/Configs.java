package com.candor.config;

import java.util.List;

import org.bukkit.Material;

import com.candor.utils.ComponentBuilder;

public class Configs extends Config{
	
	public String Sections = Material.PAPER.name();
	public String Text = Material.STRING.name();
	public String BooleanON = Material.LIME_DYE.name();
	public String BooleanOFF = Material.GRAY_DYE.name();
	public String Numbers = Material.STICK.name();
	public String Lists = Material.REPEATER.name();

	public Configs(String name, double version) {
		super(name, version);
		
		this.items.add(new ConfigItem("Configs.Sections.Material", Sections));
		this.items.add(new ConfigItem("Configs.Values.Text", Text));
		this.items.add(new ConfigItem("Configs.Values.Boolean.true", BooleanON));
		this.items.add(new ConfigItem("Configs.Values.Boolean.false", BooleanOFF));
		this.items.add(new ConfigItem("Configs.Values.Numbers", Numbers));
		this.items.add(new ConfigItem("Configs.Values.Lists", Lists));
	
	}

	@Override
	public void initialize() {
		this.Sections = this.getConfiguration().getString("Configs.Sections.Material");
		this.Text = this.getConfiguration().getString("Configs.Values.Text");
		this.BooleanON = this.getConfiguration().getString("Configs.Values.Boolean.true");
		this.BooleanOFF = this.getConfiguration().getString("Configs.Values.Boolean.false");
		this.Numbers = this.getConfiguration().getString("Configs.Values.Numbers");
		this.Lists = this.getConfiguration().getString("Configs.Values.Lists");
	}

	@Override
	public Folder folder() {
		// TODO Auto-generated method stub
		return Folder.DEFAULT;
	}

	@Override
	public List<String> header() {
		// TODO Auto-generated method stub
		return ComponentBuilder.createLore("these are the default material values for specific data types",
				"for example, if you want to change a text value, the material shown will be a String.");
	}
	
}
