package com.candor.libraries;

import java.io.File;

import org.bukkit.plugin.Plugin;

import com.candor.main.Candor;
import com.candor.utils.JarUtils;
import com.candor.utils.MessageUtils;

public class LibrariesLoader {

	private Candor plugin;
	public LibrariesLoader(Candor base) {
		this.plugin = base;
	}
		
	public void load() {

		File libFolder = new File(plugin.getDataFolder(), "libs");
		
		boolean restart = false;
		
        final File[] libs = new File[] {
        		//new File(libFolder, "Help.txt"),
        };
        
        if(libs.length == 0) return;
		
        try {

            MessageUtils.sendConsoleMessage("[" + plugin.getName() + "] " + "&6Loading libraries.");
        	
            for (final File lib : libs) {
                if (!lib.exists()) {
                    MessageUtils.sendConsoleMessage("[" + plugin.getName() + "] " + "&3Installing " + lib.getName());
                    JarUtils.extractFromJar(lib.getName(), lib.getAbsolutePath());
                    restart = true;
                }
            }
            
        } catch (final Exception e) {
            e.printStackTrace();
        }
        
        MessageUtils.sendConsoleMessage("[" + plugin.getName() + "] " + "&5Libraries installed to " + libFolder.getPath());
    
        if(restart) {
            MessageUtils.sendConsoleMessage("[" + plugin.getName() + "] " + "&c&lPLEASE RESTART IN ORDER FOR THE PLUGIN TO FUNCTION PROPERLY");
        }
        
	}
	
	public Plugin getPlugin() {
		return plugin;
	}

	public void setPlugin(Candor plugin) {
		this.plugin = plugin;
	}
	
}