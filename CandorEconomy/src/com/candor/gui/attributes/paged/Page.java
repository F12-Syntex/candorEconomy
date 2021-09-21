package com.candor.gui.attributes.paged;

public class Page {
	
	private int size;
	private int page;

	public Page(int size, int page) {
		super();
		this.size = size;
		this.page = page;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
	public void nextPage() {
		this.page++;
	}
	
	public void prevPage() {
		this.page--;
	}

}
