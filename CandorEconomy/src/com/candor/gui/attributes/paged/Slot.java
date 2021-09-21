package com.candor.gui.attributes.paged;

import java.util.ArrayList;
import java.util.List;

import com.candor.utils.Numbers;

public class Slot {
	
	private List<Integer> slot;
	private int[] page;
	private boolean isPaged;
	
	
	public boolean allPages = false;

	public Slot(List<Integer> slot, int[] page) {
		this.slot = slot;
		this.page = page;
		this.setPaged(true);
	}

	public Slot(List<Integer> slot) {
		this.slot = slot;
		this.setPaged(false);
	}

	public List<Integer> getSlot() {
		return slot;
	}
	public void setSlot(List<Integer> slot) {
		this.slot = slot;
	}
	public int[] getPage() {
		return page;
	}
	public String getPageAsString() {
		if(allPages) {
			return "ALL";
		}
		return Numbers.buildStringFromInt(getPage());
	}
	public void setPage(int[] page) {
		this.page = page;
	}
	public boolean pageIncluded(int p) {
		if(this.allPages) return true;
		for(int i : page) {
			if(i == p) return true;
		}
		return false;
	}

	public static Slot slot(int... slot) {
		
		List<Integer> slots = new ArrayList<Integer>();
		slots.addAll(slots);
		
		return new Slot(slots);
	}

	public boolean isPaged() {
		return isPaged;
	}

	public void setPaged(boolean isPaged) {
		this.isPaged = isPaged;
	}
	
	public static Slot slot(List<Integer> slot, int page) {
		return new Slot(slot, new int[] {page});
	}
	
	public static Slot pagedSlot(int slot, int page) {
		return new Slot(Numbers.of(slot), new int[] {page});
	}
	
	public static Slot defaultSlot(List<Integer> slot) {
		Slot s = Slot.slot(slot, 1);
		s.allPages = true;
		return s;
	}

}
