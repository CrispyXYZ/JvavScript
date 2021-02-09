package com.crispyxyz.jvavscript;

import java.util.ArrayList;
import java.lang.Math;

@SuppressWarnings("unused")
public class Tokens {
	
	private static byte matchedClass;
	private static byte matchedField;
	private static byte matchedMethod;
	private static String matchedParameters;
	private static ArrayList<String> Lvalue = new ArrayList<String>();
	private static ArrayList<String> Rvalue = new ArrayList<String>();
	private static final String[] ERR_MSG = {"Failed to match ","class.\n","field.\n","method.\n","parameters.\n"};
	//Format: STTIIIII S=Status T=Tag I=Id
	public static final byte
	SYSTEM = (byte)0x20,  MATH = (byte)0x21,
	//001 00000             001 00001
	OUT = (byte)0x40,     PI = (byte)0x41,       E = (byte)0x42,
	//010 00000             010 00001              010 00010
	PRINTLN = (byte)0x60, EXIT = (byte)0x61,
	//011 00000             011 00001
	NONE = (byte)0xE0,    NO_PARAM = (byte)0xE1, ERR = (byte)0xE2;
	//111 00000             111 00001              111 00010
	
	public static String match(String[] args) {
		if (args[0].contains("=")) {
			String expr = args[0].replaceAll(" ","");
			String[] values = expr.split("="); //values={Lvalue, Rvalue}
			if(values.length < 2)
				return "Invalid expressions.\n"; //failed
			int index = Lvalue.indexOf(values[0]);
			if(index == -1) {
				//add new variable
				Lvalue.add(values[0]);
				Rvalue.add(values[1]);
			}else {
				//reset variable
				Rvalue.set(index, values[1]);
			}
			return ""; //success
		}
		switch(matchClass(args[0])) {
			case NONE:
				return ERR_MSG[0]+ERR_MSG[1]; //failed: Class not found
			case MATH:
				switch(matchField(args[1])) {
					case NONE:
						break;
					case PI:
						return Double.toString(Math.E);
					case E:
						return Double.toString(Math.PI);
				}
				switch(matchMethod(args[1])) {
					case NONE:
						break;
				}
				break;
			case SYSTEM:
				switch(matchField(args[1])) {
					case NONE:
						break; //field not found
					case OUT:
						switch(matchMethod(args[2])) {
							case ERR:
							case NO_PARAM:
								return ERR_MSG[0]+ERR_MSG[4];//failed: Param not found
							case NONE:
								return ERR_MSG[0]+ERR_MSG[3];//failed: Method not found
							case PRINTLN:
								int index = Lvalue.indexOf(matchedParameters);
								if(index == -1)
									return matchedParameters + "\n";//success: param
								else
									return Rvalue.get(index) + "\n";//success: variable
								//.end if-else
							//.end case PRINTLN
						}
					//.end case OUT
				}
				switch(matchMethod(args[1])) {
					case ERR:
					case NO_PARAM:
						return ERR_MSG[0]+ERR_MSG[4]; //failed: Param not found
					case NONE:
						return ERR_MSG[0]+ERR_MSG[3]; //failed: Method not found
					case EXIT:
						Main.setFlag(false);
						return ""; //success
					//.end case EXIT
				}
			//.end case SYSTEM
		}
		return ERR_MSG[0] + ".\n"; //failed: unknown error
	}
	
	private static byte matchClass(String arg) {
		switch(arg) {
			case "System":
				return matchedClass = SYSTEM;
			case "Math":
				return matchedClass = MATH;
			default:
				return matchedClass = NONE;
			//.end default
		}
	}
	
	private static byte matchField(String arg) {
		switch(arg) {
			case "out":
				return matchedField = OUT;
			case "PI":
				return matchedField = PI;
			case "E":
				return matchedField = E;
			default:
				return matchedField = NONE;
			//.end default
		}
	}
	
	private static byte matchMethod(String arg) {
		//NEED IMPROVEMENT
		try{
			//get param
			matchedParameters = arg.substring(arg.indexOf('(')+1,arg.indexOf(')'));
			if (matchedParameters != null && !matchedParameters.isEmpty()) {
				if (arg.contains("println")) {
					return matchedMethod = PRINTLN;
				}else {
					return matchedMethod = NONE;
				}
			}else if(matchedParameters != null) { //param is empty
				switch(arg) {
					case "exit()":
						return matchedMethod = EXIT;
					case "println()":
						return matchedMethod = PRINTLN;
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
