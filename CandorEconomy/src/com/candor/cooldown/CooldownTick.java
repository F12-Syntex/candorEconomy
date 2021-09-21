package com.candor.cooldown;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.scheduler.BukkitScheduler;

import com.candor.main.Candor;

public class CooldownTick {
	
	private BukkitScheduler scheduler;

	public CooldownTick() {
		this.scheduler = Candor.getInstance().getServer().getScheduler();
	}

	public void schedule() {

		new Thread(() -> {

	        scheduler.scheduleSyncRepeatingTask(Candor.getInstance(), new Runnable() {
	            @Override
	            public void run() {
	            	List<CooldownRunnable> remove = new ArrayList<CooldownRunnable>();
	            	
	            	
	            	for(CooldownRunnable i : Candor.getInstance().cooldownManager.getRunnables()) {
	            		
	            		i.setTimer((i.getTimer()-1));
	            		
	            		if(i.getTimer() == 0) {
	            			i.onComplete();
	            			remove.add(i);
	            		}else {
	            			i.onTick();
	            		}
	            		
	            	}
	            	
	            	for(CooldownRunnable i : remove) {
	            		Candor.getInstance().cooldownManager.getRunnables().remove(i);
	            	}
	            }  	
	            	
	        }, 0L, 20L);
	        
	        scheduler.runTaskTimerAsynchronously(Candor.getInstance(), new Runnable() {
	            @Override
	            public void run() {
	            	for(CooldownUser i : Candor.getInstance().cooldownManager.getUsers()) {
	            		i.tick();
	            	}
	            }  	
	            	
	        }, 0L, 20L);
			
		}, "Schedular").start();

	}
	
	public void stop() {
		this.scheduler.cancelTasks(Candor.getInstance());
	}
	
}
