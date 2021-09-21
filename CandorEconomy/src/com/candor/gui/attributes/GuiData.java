package com.candor.gui.attributes;

import java.util.List;

import org.bukkit.Sound;

public class GuiData extends AbstractedGuiData{

	private String name;
	private InteractionSounds interactionSound;
	private boolean canModify;
	private int size;
	
	private List<GuiItem> items;

	public GuiData(String name, InteractionSounds interactionSound, boolean canModify, int size, List<GuiItem> items) {
		super(name, interactionSound, canModify, size);
		this.name = name;
		this.interactionSound = interactionSound;
		this.canModify = canModify;
		this.size = size;
		this.items = items;
	}
	
	public static GuiData getDefaultData(String name, int size, List<GuiItem> items) {
		
		com.candor.gui.attributes.Sound open = new com.candor.gui.attributes.Sound(Sound.BLOCK_LEVER_CLICK, 1f, 1f);
		com.candor.gui.attributes.Sound close = new com.candor.gui.attributes.Sound(Sound.BLOCK_LEVER_CLICK, 1f, 1f);
		
		InteractionSounds sounds = new InteractionSounds(open, close);
		
		GuiData data = new GuiData(name, sounds, false, size, items);
		return data;
	}

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
		return items;
	}

	public void setItems(List<GuiItem> items) {
		this.items = items;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.MUTABLE_GUI;
	}
	

	

}
