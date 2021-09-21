
package com.candor.commands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.candor.config.Config;
import com.candor.configs.gui.ConfigGUI;
import com.candor.configs.gui.ConfigSpecific;
import com.candor.configs.gui.ConfigsView;
import com.candor.main.Candor;
import com.candor.utils.MessageUtils;

public class Configure extends SubCommand {

    @Override
    public void onCommand(Player player, String[] args) {
    	
    	if(args.length == 1) {
        	ConfigsView gui = new ConfigsView(player, null, null);
        	gui.open();	
        	return;
    	}
    	
    	ArrayList<Config> config = Candor.getInstance().configManager.config;
    	
    	for(Config i : config) {
    		if(i.getName().equalsIgnoreCase(args[1])) {
    			player.closeInventory();
				ConfigGUI gui = new ConfigSpecific(player, i, null, null);
				gui.open();
				return;
    		}
    	}
    	
    	MessageUtils.sendRawMessage(player, Candor.getInstance().configManager.messages.invalid_configure_command.replace("%config%", args[1]));
    	
    }

    @Override

    public String name() {
        return "configure";
    }

    @Override
    public String info() {
        return "Modify configs in game!";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

	@Override
	public String permission() {
		return Candor.getInstance().configManager.permissions.configure;
	}

	@Override
	public AutoComplete autoComplete(CommandSender sender) {
		AutoComplete tabCompleter = new AutoComplete();
		
		for(Config i : Candor.getInstance().configManager.config) {
			tabCompleter.createEntry(i.getName());
		}
		
		return tabCompleter;
	}

	

}