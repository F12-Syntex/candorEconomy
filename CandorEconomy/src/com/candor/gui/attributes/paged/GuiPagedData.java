package com.candor.gui.attributes.paged;

import java.util.List;

import org.bukkit.Sound;

import com.candor.gui.attributes.AbstractedGuiData;
import com.candor.gui.attributes.GuiItem;
import com.candor.gui.attributes.InteractionSounds;
import com.candor.gui.attributes.Type;

public class GuiPagedData extends AbstractedGuiData{

	public static GuiPagedData getDefaultData(String name, int size, List<GuiItem> items) {
		
		com.candor.gui.attributes.Sound open = new com.candor.gui.attributes.Sound(Sound.BLOCK_LEVER_CLICK, 1f, 1f);
		com.candor.gui.attributes.Sound close = new com.candor.gui.attributes.Sound(Sound.BLOCK_LEVER_CLICK, 1f, 1f);
		
		InteractionSounds sounds = new InteractionSounds(open, close);
		
		GuiPagedData data = new GuiPagedData(name, sounds, false, size, items);
		return data;
	}

	public GuiPagedData(String name, InteractionSounds interactionSound, boolean canModify, int size, List<GuiItem> items) {
		super(name, interactionSound, canModify, size);
		this.name = name;
		this.interactionSound = interactionSound;
		this.canModify = canModify;
		this.size = size;
		this.guiItems = items;
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
		return guiItems;
	}

	public void setItems(List<GuiItem> items) {
		this.guiItems = items;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.MUTABLE_PAGED_GUI;
	}
	

	

}
