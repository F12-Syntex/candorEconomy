
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

public class Pay extends SubCommand {

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
    	
    	Account userPaying = database.getUser(player.getUniqueId());
    	Account userToPay = database.getUser(target.getUniqueId());
    	int amountToPay = Integer.parseInt(args[2]);
    	
    	if(userPaying.getBalance() < amountToPay) {
    		Candor.getInstance().configManager.messages.send(player, "insufficient_funds", PlaceholderFactory.createPlaceholder("%amount%", ""+(amountToPay-userPaying.getBalance())).get());
    		return;
    	}
    	
    	userPaying.takeBalance(amountToPay);
    	userToPay.addBalance(amountToPay);
    	
		Candor.getInstance().configManager.messages.send(player, "transaction_complete", PlaceholderFactory.createPlaceholder("%reciver%", args[1]).addPlaceholder("%amount%", amountToPay+"").get());

    }

    @Override

    public String name() {
        return "pay";
    }

    @Override
    public String info() {
        return "pay a user a certain amont of money";
    }

    @Override
    public String[] aliases() {
        return new String[] {"give", "send", "payment"};
    }

	@Override
	public String permission() {
		return Candor.getInstance().configManager.permissions.pay;	
	}
	
	@Override
	public AutoComplete autoComplete(CommandSender sender) {
		AutoComplete tabCompleter = new AutoComplete();
		return tabCompleter;
	}
	

}