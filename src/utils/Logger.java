package utils;

import java.util.Date;

public final class Logger {
	public static void info(String message){
		System.out.println("INFO " + new Date(System.currentTimeMillis()) + " : " + message);
	}
	
	public static void error(String message){
		System.err.println("ERROR " + new Date(System.currentTimeMillis()) + " : " + message);
	}
		
	public static void warning(String message){
		System.out.println("WARNING " + new Date(System.currentTimeMillis()) + " : " + message);
	}
}
