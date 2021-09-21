package com.candor.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;

import com.candor.main.Candor;

public class EventHandler {

    public List<SubEvent> events = new ArrayList<SubEvent>();	
    private Plugin plugin = Candor.getInstance();
    
    public EventHandler() {
		this.events.add(new InputHandler());
    }
    
	@SuppressWarnings("deprecation")
	public void setup() {
		try {
	    	//Dynamically register all events.
	        Reflections reflections = new Reflections(this.getClass().getPackage().getName());    
	        Set<Class<? extends SubEvent>> classes = reflections.getSubTypesOf(SubEvent.class);
	        
	        for(Class<? extends SubEvent> command : classes) {
				SubEvent event = command.newInstance();
					this.events.add(event);
	        }       
		} catch (Throwable e) {
			e.printStackTrace();
		}
		this.events.forEach(i -> plugin.getServer().getPluginManager().registerEvents(i, plugin));
	}
	
}
