package com.crispyxyz.jvavscript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
	
	private static Scanner sc = new Scanner(System.in);
	private static boolean flag = true;
	private static boolean shouldPrintErr = false;
	private static String using = "";
	public static final int[] VERSION = {1, 1, 0};
	public static final String SUFFIX = "";
	public static final int COMPLETED = 70;
	public static boolean debug// = true
	;

	static void setFlag(boolean flag) {
		Main.flag = flag;
	}
	
	public static void setUsing(String us){
		using = us;
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
				} else if(args[0].equals("--help")||args[0].equals("-h")) {
					joutln("usage: java -jar JvavScript.jar [--debug|-d] [scriptFile]");
					joutln("   or  dalvikvm -cp JvavScript.dex com.crispyxyz.jvavscript.Main [--debug|-d] [scriptFile]");
					joutln();
					joutln("Options: ");
					joutln("    -d, --debug:   Enable debug mode.");
				} else {
					script(args[0]);
				}
				break;
			case 2:
				if(args[0].equals("--debug")||args[0].equals("-d")) {
					debug = true;
					script(args[1]);
				}
				break;
			default:
				joutf("The length of arguments must be %d to %d.%n", 0, 2);
				throw new ArgumentsException("Too many arguments.");
		}
	}
	
	private static void interactive() {
		d("Debug is enabled.");
		joutf("JvavScript %d.%d.%d%s (%d%% completed  Java)%n", VERSION[0], VERSION[1], VERSION[2], SUFFIX, COMPLETED);
		while(flag){
			jout("jvavscript> ");
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
	
	private static void script(String fileName) {
		File file = new File(fileName);
		try {
			ArrayList<String> lines = readFile(file);
			d("Lines="+lines.toString());
			for(String line: lines){
				splitAndMatch(line);
			}
		} catch (IOException e) {
			joutln("Error: " + e.getMessage());
		} catch (ArrayIndexOutOfBoundsException e) {
			joutln("Inout error. Method name required.");
		}
	}
	
	private static ArrayList<String> readFile(File fin) throws IOException {
		FileInputStream fis = new FileInputStream(fin);
		ArrayList<String> lines = new ArrayList<String>();
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		String line = null;
		while ((line = br.readLine()) != null) {
			lines.add(line);
		}
		br.close();
        fis.close();
		return lines;
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
				if(eachCmd.contains("=="))
					eachCmd = eachCmd.replaceAll(" ","").replace("==",":equals(").concat(")");
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
	
	private static void splitParam(String in){ //in = eachCmd
		Tokens.setSuccess(false);
		String in_backup = in;
		if(!using.isEmpty()){
			in = using + "." + in; //add using prefix
		}
		d("Using="+using);
		shouldPrintErr = false;
		for(int i =0;i<=1;i++){
			if(!Tokens.isSuccess()){
				String matchedParameters = null;
				try{
					matchedParameters = in.substring(in.indexOf('(')+1,in.lastIndexOf(')')); //match parameters
				}catch (StringIndexOutOfBoundsException e) {
					d("Warning: Param not found!");
				}
				d("Origin param="+matchedParameters);
				String fullMethodName = null;
				String param = Tokens.getReturn();
				if (matchedParameters != null && !matchedParameters.isEmpty()) {
					if(matchedParameters.contains("(")){
						splitParam(matchedParameters); //recursive
						matchedParameters = Tokens.getReturn();
					}
					if( isNumeric(matchedParameters) || in.contains("using(")){
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
				Tokens.match(tokens, param, shouldPrintErr);
				d(Tokens.getMatchInfo());
				d("Return="+Tokens.getReturn());
				in = in_backup;
				shouldPrintErr = true;
			}
		}
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
