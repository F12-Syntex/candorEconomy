
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
import com.candor.utils.MessageUtils;
import com.candor.utils.Numbers;

public class Set extends SubCommand {

    @SuppressWarnings("deprecation")
	@Override
    public void onCommand(Player player, String[] args) {

    	if(args.length != 3) {
    		MessageUtils.sendMessage(player, "/" + Candor.getInstance().CommandManager.main[0] + " " + this.name() + " <player> <amount>");
    		return;
    	}
    	
    	Money database = (Money)Candor.getInstance().configManager.sqlLoader.getDatabase(Databases.ACCOUNTS);
    	
    	OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
    	
    	if(target == null || !database.userExists(target.getUniqueId())) {
    		Candor.getInstance().configManager.messages.send(player, "invalid_player", PlaceholderFactory.createPlaceholder("%player%", args[1]).get());
    		return;
    	}
    
    	if(!Numbers.isNumber(args[2])){
    		Candor.getInstance().configManager.messages.send(player, "invalid_number", PlaceholderFactory.createPlaceholder("%number%", args[2]).get());
    		return;
    	}
    	
    	Account userToPay = database.getUser(target.getUniqueId());
    	int amountToPay = Integer.parseInt(args[2]);
    	
    	userToPay.setBalance(amountToPay);
    	
		Candor.getInstance().configManager.messages.send(player, "transaction_set_complete", PlaceholderFactory.createPlaceholder("%reciver%", args[1]).addPlaceholder("%amount%", amountToPay+"").get());

    }

    @Override

    public String name() {
        return "set";
    }

    @Override
    public String info() {
        return "set a users' balance";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

	@Override
	public String permission() {
		return Candor.getInstance().configManager.permissions.set;	
	}
	
	@Override
	public AutoComplete autoComplete(CommandSender sender) {
		AutoComplete tabCompleter = new AutoComplete();
		return tabCompleter;
	}
	

}