package com.crispyxyz.jvavscript;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Tokens {
	
	private static byte matchedClass;
	private static byte matchedVariable;
	private static byte matchedMethod;
	private static String matchedParameters;
	private static ArrayList<String> Lvalue = new ArrayList<String>();
	private static ArrayList<String> Rvalue = new ArrayList<String>();
	private static final String[] ERR_MSG = {"Failed to match ","class.","variable.","method.","parameters."};
	//Format: STTIIIII S=Status T=Tag I=Id
	public static final byte
	SYSTEM = (byte)0x20, 
	//001 00000
	OUT = (byte)0x40,
	//010 00000
	PRINTLN = (byte)0x60, EXIT = (byte)0x61,
	//011 00000				011 00001
	NONE = (byte)0xE0, NO_PARAM = (byte)0xE1, ERR = (byte)0xE2;
	//111 00000				111 00001			111 00010
	
	public static String match(String[] args) {
		if (args[0].contains("=")) {
			String expr = args[0].replaceAll(" ","");
			String[] values = expr.split("=");
			if(values.length < 2)
				return "Invalid expressions.";
			int index = Lvalue.indexOf(values[0]);
			if(index == -1) {
				Lvalue.add(values[0]);
				Rvalue.add(values[1]);
			}else {
				Rvalue.set(index, values[1]);
			}
			return "";
		}
		switch(matchClass(args[0])) {
			case NONE:
				return ERR_MSG[0]+ERR_MSG[1];
			case SYSTEM:
				switch(matchVariable(args[1])) {
					case NONE:
						break;
					case OUT:
						switch(matchMethod(args[2])) {
							case ERR:
							case NO_PARAM:
								return ERR_MSG[0]+ERR_MSG[4];
							case NONE:
								return ERR_MSG[0]+ERR_MSG[3];
							case PRINTLN:
								int index = Lvalue.indexOf(matchedParameters);
								if(index == -1)
									return matchedParameters;
								else
									return Rvalue.get(index);
								//.end if-else
							//.end case PRINTLN
						}
					//.end case OUT
				}
				switch(matchMethod(args[1])) {
					case ERR:
					case NO_PARAM:
						return ERR_MSG[0]+ERR_MSG[4];
					case NONE:
						return ERR_MSG[0]+ERR_MSG[3];
					case EXIT:
						Main.flag = false;
						return "";
					//.end case EXIT
				}
			//.end case SYSTEM
		}
		return ERR_MSG[0] + ".";
	}
	
	private static byte matchClass(String arg) {
		switch(arg) {
			case "System":
				return matchedClass = SYSTEM;
			default:
				return matchedClass = NONE;
			//.end default
		}
	}
	
	private static byte matchVariable(String arg) {
		switch(arg) {
			case "out":
				return matchedVariable = OUT;
			default:
				return matchedVariable = NONE;
			//.end default
		}
	}
	
	private static byte matchMethod(String arg) {
		try{
			matchedParameters = arg.substring(arg.indexOf('(')+1,arg.indexOf(')'));
			if (matchedParameters != null && !matchedParameters.isEmpty()) {
				if (arg.contains("println")) {
					return matchedMethod = PRINTLN;
				}else {
					return matchedMethod = NONE;
				}
			}else if(matchedParameters != null) {
				switch(arg) {
					case "exit()":
						return matchedMethod = EXIT;
					default:
						return matchedMethod = NONE;
					//.end default
				}
			}else {
				return NO_PARAM;
			}
		}catch (StringIndexOutOfBoundsException e) {
			return matchedMethod = ERR;
		}
	}
}
