
package com.candor.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.candor.builder.PlaceholderFactory;
import com.candor.database.Databases;
import com.candor.database.Money;
import com.candor.database.Money.Account;
import com.candor.main.Candor;

public class Balance extends SubCommand {

    @SuppressWarnings("deprecation")
	@Override
    public void onCommand(Player player, String[] args) {

    	Money database = (Money)Candor.getInstance().configManager.sqlLoader.getDatabase(Databases.ACCOUNTS);
    	
    	if(args.length != 2) {
        	Account user = database.getUser(player.getUniqueId());
        	long balance = user.getBalance();
        	
    		Candor.getInstance().configManager.messages.send(player, "balance", PlaceholderFactory.createPlaceholder("%balance%", balance+"").get());

    		return;
    	}
    	
    	OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
    	
    	if(target == null || !database.userExists(target.getUniqueId())) {
    		Candor.getInstance().configManager.messages.send(player, "invalid_player", PlaceholderFactory.createPlaceholder("%player%", args[1]).get());
    		return;
    	}
    	
    	Account user = database.getUser(target.getUniqueId());
    	long balance = user.getBalance();
    	
		Candor.getInstance().configManager.messages.send(player, "balance", PlaceholderFactory.createPlaceholder("%balance%", balance+"").get());

    }

    @Override

    public String name() {
        return "balance";
    }

    @Override
    public String info() {
        return "set a users' balance";
    }

    @Override
    public String[] aliases() {
        return new String[] {"bal", "money", "finance"};
    }

	@Override
	public String permission() {
		return Candor.getInstance().configManager.permissions.balance;	
	}
	
	@Override
	public AutoComplete autoComplete(CommandSender sender) {
		AutoComplete tabCompleter = new AutoComplete();
		return tabCompleter;
	}
	

}