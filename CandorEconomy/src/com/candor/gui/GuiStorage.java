package com.candor.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import com.candor.main.Candor;

public class GuiStorage {
	
	private List<ConfigurableGui> guis;
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public GuiStorage() {
		this.guis = new ArrayList<ConfigurableGui>();
		
		//this.guis.add(new ExampeGui());
		//this.guis.add(new ExamplePagedGui());
		
		Class<?>[] abstractClazz = {
				MutablePagedGUI.class,
				MutableGUI.class
		};
		
    	try {
    		
    		for(Class<?> clazz : abstractClazz) {
    	        Reflections reflections = new Reflections(this.getClass().getPackage().getName());    
    	        Set<?> classes = reflections.getSubTypesOf(clazz);
    	        
    	        for(Object command : classes) {
    				ConfigurableGui gui = ((Class<? extends ConfigurableGui>) command).newInstance();
    				Candor.info("Logging: " + gui.config() + " (" + command + ")");
    				this.guis.add(gui);
    	        }
    			
    		}
	        
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		
	}

	public List<ConfigurableGui> getGuis() {
		return guis;
	}

	public void setGuis(List<ConfigurableGui> guis) {
		this.guis = guis;
	}


}
