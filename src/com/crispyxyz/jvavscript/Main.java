package com.crispyxyz.jvavscript;

import java.util.*;

public class Main {
	
	private static Scanner sc = new Scanner(System.in);
	static boolean flag = true;
	public static final int[] VERSION = {0, 1, 0};
	public static final int COMPLETED = 20;
	
	public static void main(String[] args) {
		switch(args.length) {
			case 0:
				Interactive();
				break;
			case 1:
				Script();
				break;
			default:
				joutf("The length of arguments must be %d or %d.%n", 0, 1);
				throw new ArgumentsException("Too many arguments.");
		}
	}
	
	private static void Interactive() {
		joutf("JvavScript %d.%d.%d (%d%% completed)%n", VERSION[0], VERSION[1], VERSION[2], COMPLETED);
		while(flag){
			jout("> ");
			String line = sc.nextLine();
			String[] cmds = line.split(";");
			
			
			try {
				for (String eachCmd: cmds) {
					String[] tokens = eachCmd.split("\\.");
					joutln(Tokens.match(tokens));
				}
			}catch(ArrayIndexOutOfBoundsException e) {
				joutln("Input error. Method name required.");
			}
		}
		sc.close();
		joutln("exit");
	}
	
	private static void Script() {
		joutln("Sorry, Script mode is not currently supported.");
		joutln("Redirecting to interactive mode...");
		Interactive();
	}
	
	public static void joutln(Object x) {
		System.out.println(x);
	}
	public static void joutln() {
		System.out.println();
	}
	public static void jout(Object obj) {
		System.out.print(obj);
	}
	public static void joutf(String format, Object... args) {
		System.out.printf(format, args);
	}
}
