package com.candor.config;

public enum Folder {

	DEFAULT(""), EXAMPLE("example"), GUI("menus"), DATABASE("databases");
	
	private String path;
	
	private Folder(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return this.path;
	}
	
	
}
