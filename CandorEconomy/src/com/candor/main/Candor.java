package com.candor.main;
import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.candor.config.ConfigManager;
import com.candor.cooldown.CooldownManager;
import com.candor.cooldown.CooldownTick;
import com.candor.events.EventHandler;
import com.candor.gui.GuiStorage;
import com.candor.libraries.LibrariesLoader;
import com.candor.utils.MessageUtils;


public class Candor extends JavaPlugin implements Listener{


    private static Candor instance;
    public com.candor.commands.CommandManager CommandManager;
    public ConfigManager configManager;
    public EventHandler eventHandler;
    public CooldownManager cooldownManager;
    public CooldownTick cooldownTick;
	public File ParentFolder;
	public ServerVersion version;
	
	public LibrariesLoader filesLoader;
	public GuiStorage guiStorage;
	
	@Override
	public void onEnable(){
	
		this.filesLoader = new LibrariesLoader(this);
		this.filesLoader.load();
		
		this.guiStorage = new GuiStorage();
		
		this.version = new ServerVersion();
		
		ParentFolder = getDataFolder();
	    instance = this;
	    
	    this.reload();
	    
	    eventHandler = new EventHandler();
	    eventHandler.setup();
	    
	    this.cooldownManager = new CooldownManager();

	    this.cooldownTick = new CooldownTick();
	    this.cooldownTick.schedule();
	    
	    this.CommandManager = new com.candor.commands.CommandManager();
	    this.CommandManager.setup(this);
	    
	    Candor.info("Server has started.");
	    
	}
	
	
	@Override
	public void onDisable(){
		this.eventHandler = null;
		HandlerList.getRegisteredListeners(instance);
		this.configManager.sqlLoader.disconnect();
	}
	
	public static void info(String msg){
		
		final String caller = Thread.currentThread().getStackTrace()[2].getClassName();
		final String callerMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
		final int callerLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
		
		Thread infoThread = new Thread(() -> {
			Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAlternateColorCodes(msg));
		});
		
		infoThread.setName(caller + "." + callerMethod + "." + callerLine);
		infoThread.start();
	}
	
	public static void inform(String msg){
		
		Thread infoThread = new Thread(() -> {
			Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAlternateColorCodes(msg));
		});
		
		infoThread.setName(Candor.getInstance().getName());
		infoThread.start();
	}
	

	public void reload() {
		
		this.configManager = new ConfigManager();
		this.configManager.setup(this);
	}
		

	public static Candor getInstance() {
		return instance;
	}
	
}
