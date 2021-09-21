package com.candor.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.candor.gui.attributes.Sound;

public class SoundUtils {

	public static void playSound(Player player, Sound sound) {
		World world = player.getWorld();
		Location targetLocation = player.getLocation();
		world.playSound(targetLocation, sound.getSound(), sound.getVolume(), sound.getPitch());
	}
	
	public static void playSound(Location location, Sound sound) {
		World world = location.getWorld();
		world.playSound(location, sound.getSound(), sound.getVolume(), sound.getPitch());
	}
	
}
