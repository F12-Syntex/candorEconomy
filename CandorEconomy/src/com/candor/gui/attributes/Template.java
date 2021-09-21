package com.candor.gui.attributes;

import org.bukkit.inventory.ItemStack;

public abstract class Template implements PlaceholderArguments {

	private String name;
	private ItemStack template;
	
	public Template(String name, ItemStack stack) {
		this.name = name;
		this.template = stack;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ItemStack getTemplate() {
		return template;
	}

	public void setTemplate(ItemStack template) {
		this.template = template;
	}
	
}
