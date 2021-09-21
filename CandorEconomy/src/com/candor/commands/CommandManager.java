package com.candor.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.reflections.Reflections;

import com.candor.config.Cooldown;
import com.candor.config.Messages;
import com.candor.config.Permissions;
import com.candor.cooldown.CooldownUser;
import com.candor.main.Candor;
import com.candor.tags.TagFactory;
import com.candor.utils.MessageUtils;
import com.candor.utils.StringMinipulation;

public class CommandManager implements CommandExecutor {

    private List<SubCommand> commands = new ArrayList<SubCommand>();
    private Messages messages = Candor.getInstance().configManager.messages;
    private Permissions permissions = Candor.getInstance().configManager.permissions;
    private Cooldown cooldowns = Candor.getInstance().configManager.cooldown;
    
    private Candor plugin;

    //Sub Commands
    public String[] main = {"base"};
    
    @SuppressWarnings("deprecation")
	public void setup(Candor plugin) {
    	this.setPlugin(plugin);
    	
    	
    	//Automatic tab completer 
    	TabCompleter tabCompleter = new TabCompleter() {
			@Override
			public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
				
				if(args.length == 1) {
				
					List<String> tabCommands = new ArrayList<String>();
					
					for(SubCommand i : commands) {
						tabCommands.add(i.name());
					}
					return tabCommands;
				}
	  
				SubCommand parent = get(args[0]);
				
				AutoComplete completer = null;
				
				try {
					completer = parent.autoComplete(sender);
				}catch (Exception e) {
					return new ArrayList<String>();
				}
				
				if(completer == null) {
					return new ArrayList<String>();
				}
				
				String filter = "";
				
				for(int i = 0; i < args.length - 1; i++) {
					filter += args[i] + ".";	
				}
				
				List<String> values = completer.filter(StringMinipulation.removeLastCharOptional(filter, 1));
				
				return values;
			}
		};
    	
    	
    	
		//register the command.
    	for(String mainCommand : main) {
    		plugin.getCommand(mainCommand).setExecutor(this);
            plugin.getCommand(mainCommand).setTabCompleter(tabCompleter);
    	}    	
    	
    	try {
	    	//Automatically register all commands.
	        Reflections reflections = new Reflections(this.getClass().getPackage().getName());    
	        Set<Class<? extends SubCommand>> classes = reflections.getSubTypesOf(SubCommand.class);
	        
	        for(Class<? extends SubCommand> command : classes) {
					SubCommand cmd = command.newInstance();
					this.commands.add(cmd);
	        }
	        
		} catch (Throwable e) {
			e.printStackTrace();
		}
        
    }


    public List<SubCommand> getCommands(){
    	return commands;
    }

    
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) {

            sender.sendMessage(messages.invalid_entitiy);

            return true;

        }

        Player player = (Player) sender;

    	try {

    		
    	
        if(Arrays.asList(this.main).stream().filter(i -> i.equalsIgnoreCase(command.getName())).count() > 0) {

            if (args.length == 0) {
            	
            	CooldownUser user = Candor.getInstance().cooldownManager.getUser(player.getUniqueId());
            	
            	SubCommand cmd = new Help();
            	
            	if(!player.hasPermission(cmd.permission())) {
		    		MessageUtils.sendRawMessage(player, messages.invalid_permission);
		    		return true;
		        }
            	
            	
            	String key = cmd.name();

            	int timer = user.getTime(key);
            	
            	if(timer <= 0 || player.hasPermission(permissions.bypass)) {
            	
            		cmd.onCommand(player, args);
            	  
                	user.reset(key);
                
            	}else {
                	
                	TagFactory tagHelper = TagFactory.instance(cooldowns.message);
                
                	tagHelper.setCooldown(timer);
                	
                	MessageUtils.sendRawMessage(player, tagHelper.parse());
                }
            	
                return true;

            }

            SubCommand target = this.get(args[0]);

            if (target == null) {

                player.sendMessage(messages.invalid_syntax);

                return true;

            }
            
		    if(!player.hasPermission(target.permission())) {
		    		MessageUtils.sendRawMessage(player, messages.invalid_permission);
		    		return true;
		    }

            List<String> arrayList = new ArrayList<String>();

            arrayList.addAll(Arrays.asList(args));

            arrayList.remove(0);
            
            try{
            	
            	CooldownUser user = Candor.getInstance().cooldownManager.getUser(player.getUniqueId());
            	
            	String key = args[0].trim();

            	int timer = user.getTime(key);
            	
            	if(timer <= 0 || player.hasPermission(permissions.bypass)) {
            		
            	    target.onCommand(player, args);
            	    
            	    user.reset(key);
                
            	}else {
                	
                	TagFactory tagHelper = TagFactory.instance(cooldowns.message);
                
                	tagHelper.setCooldown(timer);
                	
                	MessageUtils.sendRawMessage(player, tagHelper.parse());
                }
            	
            
            
            }catch (Exception e){
                player.sendMessage(messages.error);
                e.printStackTrace();
            }

        }


    }catch(Throwable e) {
        player.sendMessage(messages.error);
        e.printStackTrace();
    }

        return true;
    
    }



    public SubCommand get(String name) {

        Iterator<SubCommand> subcommands = commands.iterator();

        while (subcommands.hasNext()) {

            SubCommand sc = (SubCommand) subcommands.next();

            if (sc.name().equalsIgnoreCase(name)) {
                return sc;
            }

            String[] aliases;

            int length = (aliases = sc.aliases()).length;

            for (int var5 = 0; var5 < length; ++var5) {

                String alias = aliases[var5];

                if (name.equalsIgnoreCase(alias)) {

                    return sc;

                }

            }

        }

        return null;

    }


	public Candor getPlugin() {
		return plugin;
	}


	public void setPlugin(Candor plugin) {
		this.plugin = plugin;
	}

}