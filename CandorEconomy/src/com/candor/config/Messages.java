package com.candor.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import com.candor.utils.ComponentBuilder;
import com.candor.utils.MessageUtils;

public class Messages extends Config{

	public String prefix = "&c[&6SyntexCore&c]";
	public String error = "%prefix% sorry an error has accured!";;
	public String invalid_syntax = "%prefix% &cInvalid syntax";
	public String invalid_permission = "%prefix% &cYou cant do that!";
	public String invalid_entitiy = "%prefix% &cplayers only!";
	public String invalid_help_command = "%prefix% &c%command% is not a command!";
	public String invalid_configure_command = "%prefix% &c%config% is not a valid config!";
	public List<String> help_format = ComponentBuilder.createLore("%prefix% &b%command%&7: &c%description%", "%prefix% &bpermissions&7: &c%permission%");

	
	//public String insufficient_balance = "%prefix% &6Sorry you dont have enough money!";
	//public String purchase_successful = "%prefix% &6Your pet has been added to your battle inventory!";
	
	public List<HashMap<String, String>> messages = new ArrayList<HashMap<String, String>>();
	
	public Map<Integer, String> key = new HashMap<Integer, String>();
	public List<String> data = new ArrayList<String>();

	public Messages(String name, double version) {
		super(name, version);
		
		this.items.add(new ConfigItem("Messages.prefix", prefix));
		this.items.add(new ConfigItem("Messages.error", error));
		this.items.add(new ConfigItem("Messages.invalid_syntax", invalid_syntax));
		this.items.add(new ConfigItem("Messages.invalid_permission", invalid_permission));
		this.items.add(new ConfigItem("Messages.invalid_entitiy", invalid_entitiy));
		this.items.add(new ConfigItem("Messages.help.invalid_command", invalid_help_command));
		this.items.add(new ConfigItem("Messages.configure.invalid_command", invalid_configure_command));
		this.items.add(new ConfigItem("Messages.help.command_help_format", help_format));
		
		
		//plugin_reload
		this.updateMessages("plugin_reload", "%prefix% &creloaded!");
		this.updateMessages("invalid_player", "%prefix% &c%player% &7does not exist!");
		this.updateMessages("invalid_number", "%prefix% &c%number% &7is not a valid number!");
		this.updateMessages("insufficient_funds", "%prefix% &7You need &c%amount%$ &7to complete this transaction!");
		this.updateMessages("transaction_complete", "%prefix% &7You have paid &c%reciver%$ &a%amount%$&7!");
		this.updateMessages("transaction_set_complete", "%prefix% &7You have set &c%reciver%'s &7balance to &a%amount%$&7!");
		this.updateMessages("balance", "%prefix% &7You have &a%balance%$&7!");
		
		//transaction_complete

		this.items.add(new ConfigItem("Messages.contents", messages));
		
	}
	
	private void updateMessages(String key, String data) {
		HashMap<String, String> message = new HashMap<String, String>();
		message.put(key, data);
		this.messages.add(message);
	}
	

	@Override
	public Folder folder() {
		// TODO Auto-generated method stub
		return Folder.DEFAULT;
	}
	
	@Override
	public void initialize() {
		this.prefix = MessageUtils.translateAlternateColorCodes(this.getConfiguration().getString("Messages.prefix"));
		this.error = MessageUtils.translateAlternateColorCodes(this.getConfiguration().getString("Messages.error").replace("%prefix%", prefix));
		this.invalid_syntax = MessageUtils.translateAlternateColorCodes(this.getConfiguration().getString("Messages.invalid_syntax").replace("%prefix%", prefix));
		this.invalid_permission = MessageUtils.translateAlternateColorCodes(this.getConfiguration().getString("Messages.invalid_permission").replace("%prefix%", prefix));
		this.invalid_entitiy = MessageUtils.translateAlternateColorCodes(this.getConfiguration().getString("Messages.invalid_entitiy").replace("%prefix%", prefix));
		this.invalid_help_command = MessageUtils.translateAlternateColorCodes(this.getConfiguration().getString("Messages.help.invalid_command").replace("%prefix%", prefix));
		this.help_format = ComponentBuilder.createLore(this.getConfiguration().getStringList("Messages.help.command_help_format"));
		this.invalid_configure_command = MessageUtils.translateAlternateColorCodes(this.getConfiguration().getString("Messages.configure.invalid_command").replace("%prefix%", prefix));
	
		List<Map<?, ?>> mapping = this.getConfiguration().getMapList("Messages.contents");
		
		for(Map<?, ?> i : mapping) {
			String key = String.valueOf(i.keySet().stream().findFirst().get());
			String data = i.get(key).toString();
			this.data.add(MessageUtils.translateAlternateColorCodes(data).replace("%prefix%", prefix));
			int index = (this.data.size() - 1);
			this.key.put(index, key);
		}
		
	
	}
	
	
	public void send(Player player, String key) {
		MessageUtils.sendRawMessage(player, this.getMessage(key));
	}
	
	public void send(Player player, String key, Map<String, String> pairs) {
		String message = this.getMessage(key);
		
		for(String placeholder : pairs.keySet()) {
			message = message.replace(placeholder, pairs.get(placeholder));
		}

		MessageUtils.sendRawMessage(player, message);
	}
	
	public String getMessage(String key) {
		
		for(Integer i : this.key.keySet()) {
			if(this.key.get(i).equalsIgnoreCase(key)) {
				return this.data.get(i);
			}
		}

		String data = this.prefix + " &cPlease update your \"messages.yml\" for the setting &7[&c" + key + "&7]";
		
		HashMap<String, String> message = new HashMap<String, String>();
		message.put(key, data);
		List<Map<?, ?>> mapping = this.getConfiguration().getMapList("Messages.contents");
		mapping.add(message);
		this.getConfiguration().set("Messages.contents", mapping);
		this.save();
		
		this.data.add(data);
		this.key.put(this.key.size(), key);
		
		return MessageUtils.translateAlternateColorCodes(data);
	}
	
	@Override
	public List<String> header() {
		// TODO Auto-generated method stub
		return ComponentBuilder.createLore("All messages can be modified here.");
	}

	
}
