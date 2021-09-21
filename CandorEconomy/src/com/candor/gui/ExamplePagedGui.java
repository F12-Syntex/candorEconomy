package com.candor.gui;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import com.candor.builder.ActionsBuilder;
import com.candor.builder.ExecutionBuilder;
import com.candor.builder.ItemGenerator;
import com.candor.gui.attributes.GuiItem;
import com.candor.gui.attributes.paged.Condition;
import com.candor.gui.attributes.paged.GuiPagedData;
import com.candor.gui.attributes.paged.Slot;

public class ExamplePagedGui extends MutablePagedGUI{

	@Override
	public String config() {
		// TODO Auto-generated method stub
		return "paged_exmaple";
	}

	@Override
	public GuiPagedData configuration() {

		List<GuiItem> items = this.getPageBar();

		items.add(new GuiItem(ItemGenerator.getInstance(Material.BLACK_WOOL).setName("%player%").get(), Slot.pagedSlot(0, 1), "wool:2"));
		items.add(new GuiItem(ItemGenerator.getInstance(Material.BLACK_WOOL).setName("%player%").get(), Slot.pagedSlot(1, 2), "wool:2"));
		items.add(new GuiItem(ItemGenerator.getInstance(Material.BLACK_WOOL).setName("%player%").get(), Slot.pagedSlot(2, 2), "wool:2"));

		GuiPagedData data = GuiPagedData.getDefaultData("&6Example", 54, items);
		
		return data;
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
				}).get();
	}

	@Override
	public boolean loadFromConfig() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 54;
	}
	
	@Override
	public Map<String, Condition> onActions(Player payer) {
		return ActionsBuilder.empty();
	}
	
}