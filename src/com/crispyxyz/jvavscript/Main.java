package com.crispyxyz.jvavscript;

import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
	
	private static Scanner sc = new Scanner(System.in);
	private static boolean flag = true;
	public static final int[] VERSION = {0, 3, 2};
	public static final int COMPLETED = 45;
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
				if(args[0].equals("--debug")||args[0].equals("-d")) {
					debug = true;
					interactive();
				}
				script();
				break;
			default:
				joutf("The length of arguments must be %d or %d.%n", 0, 1);
				throw new ArgumentsException("Too many arguments.");
		}
	}
	
	private static void interactive() {
		d("Debug is enabled.");
		joutf("JvavScript %d.%d%s%d (%d%% completed)%n", VERSION[0], VERSION[1], "-alpha", VERSION[2], COMPLETED);
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
	
	private static void script() {
		joutln("Sorry, Script mode is not currently supported.");
		joutln("Redirecting to interactive mode...");
		interactive();
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
				if(eachCmd.contains("="))
					eachCmd = eachCmd.replaceAll(" ","").replace("=",":set(").concat(")");
				if(eachCmd.contains("+"))
					eachCmd = eachCmd.replaceAll(" ","").replace("+",":plus(").concat(")");
				if(eachCmd.contains("-"))
					eachCmd = eachCmd.replaceAll(" ","").replace("-",":minus(").concat(")");
				if(eachCmd.contains("*"))
					eachCmd = eachCmd.replaceAll(" ","").replace("*",":multiply(").concat(")");
				if(eachCmd.contains("/"))
					eachCmd = eachCmd.replaceAll(" ","").replace("/",":divided(").concat(")");
				splitParam(eachCmd);
			}
		}
		return;
	}
	
	private static void d(String msg){
		if(debug)
			System.out.println("[DEBUG: "+msg+"]");
	}
	
	private static void splitParam(String in){
		String matchedParameters = null;
		try{
			matchedParameters = in.substring(in.indexOf('(')+1,in.lastIndexOf(')'));
		}catch (StringIndexOutOfBoundsException e) {
			d("Warning: Param not found!");
		}
		d("Origin param="+matchedParameters);
		String fullMethodName = null;
		String param = Tokens.getReturn();
		if (matchedParameters != null && !matchedParameters.isEmpty()) {
			if(matchedParameters.contains("(")){
				splitParam(matchedParameters);
				matchedParameters = Tokens.getReturn();
			}
			if( isNumeric(matchedParameters)){
				param = matchedParameters;
			}else {
				String[] parameterTokens = matchedParameters.split("\\.");
				d("Parameter tokens="+Arrays.toString(parameterTokens));
				Tokens.match(parameterTokens, param, false);
				d(Tokens.getMatchInfo());
				param = Tokens.getReturn();
			}
			fullMethodName = new StringBuilder()
				.append(in)
				.delete(in.indexOf('('), in.lastIndexOf(')')+1)
				.toString();
		}else if(matchedParameters != null) { //param is empty
			param = matchedParameters;
			fullMethodName = new StringBuilder()
				.append(in)
				.delete(in.indexOf('('), in.lastIndexOf(')')+1)
				.toString();
		}else {
			param = matchedParameters;
			fullMethodName = in;
		}
		d("Converted param="+param);
		String[] tokens = fullMethodName.split("\\."); //split token
		d("Tokens="+Arrays.toString(tokens));
		Tokens.match(tokens, param, true);
		d(Tokens.getMatchInfo());
		d("Return="+Tokens.getReturn());
	}
	
	public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^-?[0-9]*$");
        if(str.indexOf(".")>0){
            if(str.indexOf(".")==str.lastIndexOf(".") && str.split("\\.").length==2){
                return pattern.matcher(str.replace(".","")).matches();
            }else {
                return false;
            }
        }else {
            return pattern.matcher(str).matches();
        }
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
