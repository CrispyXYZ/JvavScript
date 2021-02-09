package com.crispyxyz.jvavscript;

import java.util.*;

public class Main {
	
	private static Scanner sc = new Scanner(System.in);
	private static boolean flag = true;
	public static final int[] VERSION = {0, 2, 1};
	public static final int COMPLETED = 25;

	static void setFlag(boolean flag) {
		Main.flag = flag;
	}
	
	public static void main(String[] args) {
		switch(args.length) {
			case 0:
				interactive();
				break;
			case 1:
				script(); //not support
				break;
			default:
				joutf("The length of arguments must be %d or %d.%n", 0, 1);
				throw new ArgumentsException("Too many arguments.");
		}
	}
	
	private static void interactive() {
		//Print version
		joutf("JvavScript %d.%d-alpha%d (%d%% completed)%n", VERSION[0], VERSION[1], VERSION[2]+1, COMPLETED);
		while(flag){
			jout("> ");
			String input = sc.nextLine(); //get input
			
			ArrayList<String> result;
			try{
				result = splitAndMatch(input);
			}catch(ArrayIndexOutOfBoundsException e) {
				joutln("Input error. Method name required.");
				continue;
			}
			String[] resultArr = new String[result.size()];
			result.toArray(resultArr);
			for(String r: resultArr){
				joutln(r);
			}
		}
		sc.close();
		joutln("exit");
	}
	
	private static void script() {
		joutln("Sorry, Script mode is not currently supported.");
		joutln("Redirecting to interactive mode...");
		interactive();
	}
	
	public static ArrayList<String> splitAndMatch(String line){
		int index = line.indexOf('#');
		switch (index) {  //search '#'
			case -1:  //no comment
				break;
			default:  //found comment
				line = new StringBuilder()
					.append(line)
					.delete(index, line.length())
					.toString();
		}
		String[] cmds = line.split(";"); //split command

		ArrayList<String> ret = new ArrayList<String>();
		for (String eachCmd: cmds) {
			if(!eachCmd.isEmpty()){ //ignore empty command
				String[] tokens = eachCmd.split("\\."); //split token
				ret.add(Tokens.match(tokens));
			}
		}
		return ret;
	}
	//redirect
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
