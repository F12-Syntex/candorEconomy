package com.candor.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import com.candor.builder.ActionsBuilder;
import com.candor.builder.ExecutionBuilder;
import com.candor.builder.ItemGenerator;
import com.candor.gui.attributes.GuiData;
import com.candor.gui.attributes.GuiItem;
import com.candor.gui.attributes.paged.Condition;
import com.candor.utils.Numbers;

public class ExampeGui extends MutableGUI{

	@Override
	public GuiData configuration() {
		
		List<GuiItem> items = new ArrayList<>();

		items.add(new GuiItem(ItemGenerator.getInstance(Material.BLACK_WOOL).setName("&c%player%").get(), Numbers.of(0, 54), "wool"));

		GuiData data = GuiData.getDefaultData("&cEmpty", 54, items);
		
		return data;
	}

	@Override
	public String config() {
		// TODO Auto-generated method stub
		return "example";
	}

	@Override
	public Map<String, Runnable> onExecution(Player payer) {
		return ExecutionBuilder
				.createExecution("wool:2", () -> {
					player.getInventory().addItem(ItemGenerator.getInstance(Material.BLACK_WOOL)
							.setName("&6" + player.getName())
							.addEnchant(Enchantment.DURABILITY, 10)
							.addEnchant(Enchantment.DAMAGE_ALL, 10)
							.addFlag(ItemFlag.HIDE_ATTRIBUTES)
							.addFlag(ItemFlag.HIDE_PLACED_ON)
							.addLore("&cHi")
							.get());
				}).addExecution("wool", () -> {
					player.getInventory().addItem(ItemGenerator.getInstance(Material.CYAN_WOOL).get());
				}).get();
	}

	@Override
	public boolean loadFromConfig() {
		return false;
	}

	@Override
	public Map<String, Condition> onActions(Player payer) {
		return ActionsBuilder.empty();
	}

}
