package com.candor.gui.attributes;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.candor.gui.attributes.paged.Actions;
import com.candor.gui.attributes.paged.Slot;
import com.candor.main.Candor;
import com.candor.utils.Numbers;

public class GuiItem{

	private Template template;
	private ItemStack item;
	private Slot slot;
	private String executable;
	private String permission = Candor.getInstance().configManager.permissions.basic;
	private Actions actions = new Actions();

	public GuiItem(ItemStack item, Slot slot, String executable, String permission) {
		this.item = item;
		this.slot = slot;
		this.executable = executable;
		this.setPermission(permission);
	}
	
	/*
	public GuiItem(Template template, Slot slot, String executable, String permission) {
		this.template = template;
		this.slot = slot;
		this.executable = executable;
		this.setPermission(permission);
	}
		*/
	
	public GuiItem(ItemStack item, List<Integer> slot, String executable, String permission) {
		this.item = item;
		this.slot = new Slot(slot);
		this.executable = executable;
		this.setPermission(permission);
	}
	
	/*
	public GuiItem(Template template, List<Integer> slot, String executable, String permission) {
		this.template = template;
		this.slot = new Slot(slot);
		this.executable = executable;
		this.setPermission(permission);
	}
		*/
	
	public GuiItem(ItemStack item, int slot, String executable, String permission) {
		this.item = item;
		this.slot = new Slot(Numbers.of(slot));
		this.executable = executable;
		this.setPermission(permission);
	}
	
	/*
	public GuiItem(Template template, int slot, String executable, String permission) {
		this.template = template;
		this.slot = new Slot(Numbers.of(slot));
		this.executable = executable;
		this.setPermission(permission);
	}
		*/
	
	public GuiItem(ItemStack item, Slot slot, String executable) {
		this.item = item;
		this.slot = slot;
		this.executable = executable;
	}
	
	/*
	public GuiItem(Template template, Slot slot, String executable) {
		this.template = template;
		this.slot = slot;
		this.executable = executable;
	}
		*/
	
	public GuiItem(ItemStack item, List<Integer> slot, String executable) {
		this.item = item;
		this.slot = new Slot(slot);
		this.executable = executable;
	}
	
	/*
	public GuiItem(Template template, List<Integer> slot, String executable) {
		this.template = template;
		this.slot = new Slot(slot);
		this.executable = executable;
	}
		*/
	
	public GuiItem(ItemStack item, int slot, String executable) {
		this.item = item;
		this.slot = new Slot(Numbers.of(slot));
		this.executable = executable;
	}
	
	/*
	public GuiItem(Template template, int slot, String executable) {
		this.template = template;
		this.slot = new Slot(Numbers.of(slot));
		this.executable = executable;
	}
		*/
	

	public GuiItem(ItemStack item, Slot slot, String executable, Actions actions) {
		this.item = item;
		this.slot = slot;
		this.executable = executable;
		this.actions = actions;
	}
	
	/*
	public GuiItem(Template template, Slot slot, String executable, Actions actions) {
		this.template = template;
		this.slot = slot;
		this.executable = executable;
		this.actions = actions;
	}
		*/
	
	public GuiItem(ItemStack item, List<Integer> slot, String executable, Actions actions) {
		this.item = item;
		this.slot = new Slot(slot);
		this.executable = executable;
		this.actions = actions;
	}
	
	/*
	public GuiItem(Template template, List<Integer> slot, String executable, Actions actions) {
		this.template = template;
		this.slot = new Slot(slot);
		this.executable = executable;
		this.actions = actions;
	}
		*/
	
	public GuiItem(ItemStack item, int slot, String executable, Actions actions) {
		this.item = item;
		this.slot = new Slot(Numbers.of(slot));
		this.executable = executable;
		this.actions = actions;
	}
	
	/*
	public GuiItem(Template template, int slot, String executable, Actions actions) {
		this.template = template;
		this.slot = new Slot(Numbers.of(slot));
		this.executable = executable;
		this.actions = actions;
	}
	*/
	
	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public Slot getSlot() {
		return slot;
	}

	public void setSlot(Slot slot) {
		this.slot = slot;
	}

	public String getExecutable() {
		return executable;
	}

	public void setExecutable(String executable) {
		this.executable = executable;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Actions getActions() {
		return actions;
	}

	public void setActions(Actions actions) {
		this.actions = actions;
	}
	
}
