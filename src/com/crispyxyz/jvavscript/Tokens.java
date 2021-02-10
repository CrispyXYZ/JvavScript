package com.crispyxyz.jvavscript;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Tokens {
	
	private static String goBack;
	private static byte matchedClass;
	private static byte matchedField;
	private static byte matchedMethod;
	//private static String matchedParameters;
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
	
	public static String getReturn(){
		return goBack;
	}
	
	public static void match(String[] tokens, String param, boolean isNotMatchingParam) {
		goBack = tokens[0];
		if (tokens[0].contains("set")) {
			String expr = tokens[0].replaceAll(" ","");
			String[] values = expr.split(":");
			if(values.length < 2){
				jout("Invalid expressions.\n"); //failed
				return;
			}
			int index = Lvalue.indexOf(values[0]);
			if(index == -1) {
				//add new variable
				Lvalue.add(values[0]);
				Rvalue.add(param);
			}else {
				//reset variable
				Rvalue.set(index, param);
			}
			return; //success
		}
		switch(matchClass(tokens[0])) {
			case NONE:
				if(isNotMatchingParam)
					jout(ERR_MSG[0]+ERR_MSG[1]); //failed: Class not found
				return;
			case MATH:
				switch(matchField(tokens[1])) {
					case NONE:
						break;
					case E:
						goBack = Double.toString(Math.E);
						return;
					case PI:
						goBack = Double.toString(Math.PI);
						return;
				}
				switch(matchMethod(tokens[1])) {
					case NONE:
						break;
				}
				return;
			case SYSTEM:
				switch(matchField(tokens[1])) {
					case NONE:
						break; //field not found
					case OUT:
						switch(matchMethod(tokens[2])) {
							case ERR:
							case NO_PARAM:
								if(isNotMatchingParam)
									jout(ERR_MSG[0]+ERR_MSG[4]);//failed: Param not found
								return;
							case NONE:
								if(isNotMatchingParam)
									jout(ERR_MSG[0]+ERR_MSG[3]);//failed: Method not found
								return;
							case PRINTLN:
								int index = Lvalue.indexOf(param);
								if(index == -1)
									System.out.println(param);//success: param
								else
									System.out.println(Rvalue.get(index));//success: variable
								return;
							//.end case PRINTLN
						}
					//.end case OUT
				}
				switch(matchMethod(tokens[1])) {
					case ERR:
					case NO_PARAM:
						if(isNotMatchingParam)
							jout(ERR_MSG[0]+ERR_MSG[4]); //failed: Param not found
						return;
					case NONE:
						if(isNotMatchingParam)
							jout(ERR_MSG[0]+ERR_MSG[3]); //failed: Method not found
						return;
					case EXIT:
						Main.setFlag(false);
						return; //success
					//.end case EXIT
				}
			//.end case SYSTEM
		}
		jout(ERR_MSG[0] + ".\n"); //failed: unknown error
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
		switch(arg){
			case "println":
				return matchedMethod = PRINTLN;
			case "exit":
				return matchedMethod = EXIT;
			default:
				return matchedMethod = NONE;
			//.end default
		}
	}
	
	public static void jout(Object arg){
		System.out.print(arg);
	}
}
