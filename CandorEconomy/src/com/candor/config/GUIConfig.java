package com.candor.config;

import org.bukkit.entity.Player;

import com.candor.gui.attributes.GuiData;

public abstract class GUIConfig extends Config{

	public GuiData guiData;
	
	private Player player;
	
	public GUIConfig(String name, double version) {
		super(name, version);
	}

	public GuiData getGuiData() {
		return guiData;
	}

	public void setGuiData(GuiData guiData) {
		this.guiData = guiData;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
