package com.crispyxyz.jvavscript;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
	
	private static Scanner sc = new Scanner(System.in);
	private static boolean flag = true;
	public static final int[] VERSION = {0, 2, 0};
	public static final int COMPLETED = 33;
	public static boolean debug// = true
	;

	static void setFlag(boolean flag) {
		Main.flag = flag;
	}
	
	public static void main(String[] args) {
		switch(args.length) {
			case 0:
				interactive();
				break;
			case 1:
				if(args[0].equals("--debug")||args[0].equals("-d"))
					debug = true;
				oneArgs();
				break;
			default:
				joutf("The length of arguments must be %d or %d.%n", 0, 1);
				throw new ArgumentsException("Too many arguments.");
		}
	}
	
	private static void interactive() {
		d("Debug is enabled.");
		joutf("JvavScript %d.%d%s%d (%d%% completed)%n", VERSION[0], VERSION[1], ".", VERSION[2], COMPLETED);
		while(flag){
			jout("> ");
			String input = sc.nextLine(); //get input
			try{
				splitAndMatch(input);
			}catch(ArrayIndexOutOfBoundsException e) {
				joutln("Input error. Method name required.");
				continue;
			}
		}
		sc.close();
		joutln("exit");
	}
	
	private static void oneArgs() {
		interactive();
		/*
		joutln("Sorry, Script mode is not currently supported.");
		joutln("Redirecting to interactive mode...");
		interactive();
		*/
	}
	
	public static void splitAndMatch(String line){
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

		for (String eachCmd: cmds) {
			if(!eachCmd.isEmpty()){ //ignore empty command (comment)
				String param = null;
				//split param
				String matchedParameters = null;
				try{
					matchedParameters = eachCmd.substring(eachCmd.indexOf('(')+1,eachCmd.lastIndexOf(')'));
				}catch (StringIndexOutOfBoundsException e) {
					d("Warning: Param not found!");
				}
				d("Origin param="+matchedParameters);
				String fullMethodName = null;
				if (matchedParameters != null && !matchedParameters.isEmpty()) {
					String[] parameterTokens = matchedParameters.split("\\.");
					Tokens.match(parameterTokens, null, false);
					param = Tokens.getReturn();
					fullMethodName = new StringBuilder()
						.append(eachCmd)
						.delete(eachCmd.indexOf('('), eachCmd.indexOf(')')+1)
						.toString();
				}else if(matchedParameters != null) { //param is empty
					param = matchedParameters;
					fullMethodName = new StringBuilder()
						.append(eachCmd)
						.delete(eachCmd.indexOf('('), eachCmd.indexOf(')')+1)
						.toString();
				}else {
					param = matchedParameters;
					fullMethodName = eachCmd;
				}
				d("Converted param="+param);
				String[] tokens = fullMethodName.split("\\."); //split token
				d("Tokens="+Arrays.toString(tokens));
				Tokens.match(tokens, param, true);
			}
		}
		return;
	}
	
	private static void d(String msg){
		if(debug)
			System.out.println("[DEBUG: "+msg+"]");
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
