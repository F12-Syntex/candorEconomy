package com.candor.gui.attributes.paged;

import com.candor.gui.ConfigurableGui;

@FunctionalInterface
public interface Condition {
	public abstract boolean valid(ConfigurableGui instance);
}
