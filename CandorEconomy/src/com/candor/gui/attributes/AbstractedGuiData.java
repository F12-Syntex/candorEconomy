package com.candor.gui.attributes;

import java.util.List;

public abstract class AbstractedGuiData {
	
	protected String name;
	protected InteractionSounds interactionSound;
	protected boolean canModify;
	protected int size;
	
	protected List<GuiItem> guiItems;
	//protected List<GuiPagedtem> guiPagedItems;

	public AbstractedGuiData(String name, InteractionSounds interactionSound, boolean canModify, int size) {
		this.name = name;
		this.interactionSound = interactionSound;
		this.canModify = canModify;
		this.size = size;
	}

	public abstract Type getType();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InteractionSounds getInteractionSound() {
		return interactionSound;
	}

	public void setInteractionSound(InteractionSounds interactionSound) {
		this.interactionSound = interactionSound;
	}

	public boolean isCanModify() {
		return canModify;
	}

	public void setCanModify(boolean canModify) {
		this.canModify = canModify;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<GuiItem> getItems() {
		return guiItems;
	}

	public void setItems(List<GuiItem> guiItems) {
		this.guiItems = guiItems;
	}

	/*
	public List<GuiPagedtem> getGuiPagedItems() {
		return guiPagedItems;
	}

	public void setGuiPagedItems(List<GuiPagedtem> guiPagedItems) {
		this.guiPagedItems = guiPagedItems;
	}
	*/
	
}
