package com.candor.utils;

import java.util.Optional;

import org.apache.commons.lang.WordUtils;

public class StringMinipulation extends WordUtils{
	
	public static String removeLastCharOptional(String s, int length) {
	    return Optional.ofNullable(s)
	      .filter(str -> str.length() != 0)
	      .map(str -> str.substring(0, str.length() - length))
	      .orElse(s);
	    }

}
