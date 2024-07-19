package xyz.syodo.utils;

import java.sql.Timestamp;

public class Logger {
	
	final static String DARK_GREEN = "\u001b[32m";
	final static String DARK_RED = "\u001b[33m";
	final static String DARK_YELLOW = "\u001b[33m";
	
	final static String GREEN = "\u001b[32;1m";
	final static String RED = "\u001b[31m";
	final static String YELLOW = "\u001b[33;1m";
	final static String WHITE = "\u001b[37m";
	
	final static String RESET = "\u001b[0m";
	
	
	public static void success(String message) {
		
		Timestamp time = new Timestamp(System.currentTimeMillis());
		System.out.println(DARK_YELLOW + time.toLocaleString() + DARK_GREEN + "    [SUCCESS] " + GREEN + message + RESET + RESET);
		
	}

	public static void info(String message) {
		
		Timestamp time = new Timestamp(System.currentTimeMillis());
		System.out.println(DARK_YELLOW + time.toLocaleString() + DARK_GREEN + "    [INFO] " + GREEN + message + RESET);
		
	}
	
	public static void warn(String message) {
		
		Timestamp time = new Timestamp(System.currentTimeMillis());
		System.out.println(DARK_YELLOW + time.toLocaleString() + DARK_YELLOW + "    [WARN] " + YELLOW + message + RESET);
		
	}
	
	public static void fatal(String message) {
		
		Timestamp time = new Timestamp(System.currentTimeMillis());
		System.out.println(DARK_YELLOW + time.toLocaleString() + DARK_RED + "    [FATAL] " + message + RESET);
		
	}
	
	public static void critical(String message) {
		
		Timestamp time = new Timestamp(System.currentTimeMillis());
		System.out.println(DARK_YELLOW + time.toLocaleString() + DARK_RED + "    [CRITICAL] " + message + RESET);
		
	}
	
	public static void error(String message) {
		
		Timestamp time = new Timestamp(System.currentTimeMillis());
		System.out.println(DARK_YELLOW + time.toLocaleString() + DARK_RED + "    [ERROR] " + RED + message + RESET);
		
	}	
	
	public static void debug(String message) {
		
		Timestamp time = new Timestamp(System.currentTimeMillis());
		System.out.println(DARK_YELLOW + time.toLocaleString() + YELLOW + "    [DEBUG] " + message + RESET);
		
	}
	
}
