package com.candor.gui.attributes;

import java.util.Map;

import org.bukkit.entity.Player;

public interface PlaceholderArguments {
	public abstract Map<String, String> load(Player player);
}
