package com.crispyxyz.jvavscript;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.reflect.Method;

public class Tokens {
	
	private static boolean success;
	private static String goBack;
	private static byte matchedClass;
	private static byte matchedField;
	private static byte matchedMethod;
	//private static String matchedParameters;
	private static ArrayList<String> Lvalue = new ArrayList<String>();
	private static ArrayList<String> Rvalue = new ArrayList<String>();
	private static final String[] ERR_MSG = {"Failed to match ","class.\n","field.\n","method.\n","parameters.\n"};
	private static final String RETURN_VOID = "";
	//Format: STTIIIII S=Status T=Tag I=Id
	public static final byte
	SYSTEM = (byte)0x20,  MATH = (byte)0x21,
	//001 00000             001 00001
	OUT = (byte)0x40,     PI = (byte)0x41,       E = (byte)0x42,
	//010 00000             010 00001              010 00010
	PRINTLN = (byte)0x60, EXIT = (byte)0x61,     SIN = (byte)0x62, COS = (byte)0x63, TAN = (byte)0x64, COT = (byte)0x65, SEC = (byte)0x66, CSC = (byte)0x67,
	//011 00000             011 00001              011 00010         011 00011         011 00100         011 00101         011 00110         011 00111
	USING = (byte)0x68,   INVOKE = (byte)0x69,
	//011 01000             011 01001
	NONE = (byte)0xE0,    NO_PARAM = (byte)0xE1, ERR = (byte)0xE2;
	//111 00000             111 00001              111 00010
	
	public static String getReturn(){
		return goBack;
	}
	
	public static boolean isSuccess(){
		return success;
	}
	
	public static void setSuccess(boolean suc){
		success = suc;
	}
	
	public static String getMatchInfo(){
		return "Matched class=0x"+Integer.toHexString(matchedClass)+
		";Matched field=0x"+Integer.toHexString(matchedField)+
		";Matched method=0x"+Integer.toHexString(matchedMethod);
	}
	
	public static void match(String[] tokens, String param, boolean isNotMatchingParam) {
		goBack = tokens[0];
		success = false;
		int index1 = Lvalue.indexOf(tokens[0]); //e.g. 〈System〉.out 
		if( index1 != -1){ //is a variale name
			goBack = Rvalue.get(index1);
			success = true;
			return;
		}
		if (tokens[0].contains(":")) {
			String expr = tokens[0].replaceAll(" ","");
			String[] values = expr.split(":");
			if(values.length < 2){
				jout("Invalid expressions.\n"); //failed
				return;
			}
			int index2 = Lvalue.indexOf(values[0]); //index2 is the index of the name of Lvariable in Lvalue. e.g. 〈a〉: set(0)
			switch(values[1]) {
				case "eq":
				case "equals":
					if(index2 == -1) {
						goBack = Boolean.toString(values[0].equals(param));
					}else {
						goBack = Boolean.toString(Rvalue.get(index2).equals(param));
					}
					success = true;
					return;
				case "set":
					if(index2 == -1) {
						//add new variable
						Lvalue.add(values[0]);
						Rvalue.add(param);
					}else {
						//reset variable
						Rvalue.set(index2, param);
					}
					goBack = RETURN_VOID;
					success = true;
					return; //success
				case "add":
				case "plus":
					if(index2 == -1) {
						try{
							goBack = new BigDecimal(Double.toString(Double.parseDouble(values[0]) + Double.parseDouble(param))).toPlainString();
							success = true;
						}catch (NumberFormatException e) {
							jout("Error: variable not found or number format error: "+e.getMessage()+"\n");
						}
					}else {
						try{
							goBack = new BigDecimal(Double.toString(Double.parseDouble(Rvalue.get(index2))+Double.parseDouble(param))).toPlainString();
							success = true;
						}catch (NumberFormatException e) {
							jout("Error: number format error: "+e.getMessage()+"\n");
						}
					}
					return;
				case "minus":
					if(index2 == -1) {
						try{
							goBack = new BigDecimal(Double.toString(Double.parseDouble(values[0])-Double.parseDouble(param))).toPlainString();
							success = true;
						}catch (NumberFormatException e) {
							jout("Error: variable not found or number format error: "+e.getMessage()+"\n");
						}
					}else {
						try{
							goBack = new BigDecimal(Double.toString(Double.parseDouble(Rvalue.get(index2))-Double.parseDouble(param))).toPlainString();
							success = true;
						}catch (NumberFormatException e) {
							jout("Error: number format error: "+e.getMessage()+"\n");
						}
					}
					return;
				case "time":
				case "times":
				case "multiply":
					if(index2 == -1) {
						try{
							goBack = new BigDecimal(Double.toString(Double.parseDouble(values[0])*Double.parseDouble(param))).toPlainString();
							success = true;
						}catch (NumberFormatException e) {
							jout("Error: variable not found or number format error: "+e.getMessage()+"\n");
						}
					}else {
						try{
							goBack = new BigDecimal(Double.toString(Double.parseDouble(Rvalue.get(index2))*Double.parseDouble(param))).toPlainString();
							success = true;
						}catch (NumberFormatException e) {
							jout("Error: number format error: "+e.getMessage()+"\n");
						}
					}
					return;
				case "divide":
				case "divided":
					if(index2 == -1) {
						try{
							goBack = new BigDecimal(Double.toString(Double.parseDouble(values[0])/Double.parseDouble(param))).toPlainString();
							success = true;
						}catch (NumberFormatException e) {
							jout("Error: variable not found or number format error: "+e.getMessage()+"\n");
						}catch (ArithmeticException e) {
							jout("Error: "+e.getMessage());
						}
					}else {
						try{
							goBack = new BigDecimal(Double.toString(Double.parseDouble(Rvalue.get(index2))/Double.parseDouble(param))).toPlainString();
							success = true;
						}catch (NumberFormatException e) {
							jout("Error: number format error: "+e.getMessage()+"\n");
						}catch (ArithmeticException e) {
							jout("Error: "+e.getMessage());
						}
					}
					return;
			}
		}
		switch(matchMethod(tokens[0])) {
			case USING:
				Main.setUsing(param);
				goBack = RETURN_VOID;
				success = true;
				return;
			case INVOKE:
				try {
					Object invRet = invoke();
					goBack = (invRet == null) ? RETURN_VOID : invRet.toString() ;
				} catch (Exception e) {
					e.printStackTrace();
				}
				success = true;
				return;
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
						success = true;
						return;
					case PI:
						goBack = Double.toString(Math.PI);
						success = true;
						return;
				}
				switch(matchMethod(tokens[1])) {
					case NONE:
						break;
					case SIN:
						try{
							goBack = new BigDecimal(Double.toString(Math.sin(Double.parseDouble(param)))).toPlainString();
							success = true;
						}catch (NumberFormatException e) {
							jout("Calculation error:"+e.getMessage()+"\n");
						}
						return;
					case COS:
						try{
							goBack = new BigDecimal(Double.toString(Math.cos(Double.parseDouble(param)))).toPlainString();
							success = true;
						}catch (NumberFormatException e) {
							jout("Calculation error:"+e.getMessage()+"\n");
						}
						return;
					case TAN:
						try{
							goBack = new BigDecimal(Double.toString(Math.tan(Double.parseDouble(param)))).toPlainString();
							success = true;
						}catch (NumberFormatException e) {
							jout("Calculation error:"+e.getMessage()+"\n");
						}
						return;
					case COT:
						try{
							goBack = new BigDecimal(Double.toString(1 / Math.tan(Double.parseDouble(param)) )).toPlainString();
							success = true;
						}catch (NumberFormatException e) {
							jout("Calculation error:"+e.getMessage()+"\n");
						}
						return;
					case SEC:
						try{
							goBack = new BigDecimal(Double.toString(1 / Math.cos(Double.parseDouble(param)))).toPlainString();
							success = true;
						}catch (NumberFormatException e) {
							jout("Calculation error:"+e.getMessage()+"\n");
						}
						return;
					case CSC:
						try{
							goBack = new BigDecimal(Double.toString(1/ Math.sin(Double.parseDouble(param)))).toPlainString();
							success = true;
						}catch (NumberFormatException e) {
							jout("Calculation error:"+e.getMessage()+"\n");
						}
						return;
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
								goBack = RETURN_VOID;
								success = true;
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
						goBack = RETURN_VOID;
						success = true;
						return; //success
					//.end case EXIT
				}
			//.end case SYSTEM
		}
		goBack = RETURN_VOID;
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
			case "using":
				return matchedMethod = USING;
			case "println":
				return matchedMethod = PRINTLN;
			case "exit":
				return matchedMethod = EXIT;
			case "sin":
				return matchedMethod = SIN;
			case "cos":
				return matchedMethod = COS;
			case "tan":
				return matchedMethod = TAN;
			case "cot":
				return matchedMethod = COT;
			case "sec":
				return matchedMethod = SEC;
			case "csc":
				return matchedMethod = CSC;
			case "invoke":
				return matchedMethod = INVOKE;
			default:
				return matchedMethod = NONE;
			//.end default
		}
	}
	
	private static Object invoke() throws Exception {
		Scanner sc = new Scanner(System.in);
		jout("Class>>> ");
		Class clazz = Class.forName(sc.nextLine());
		jout("Method>>> ");
		String mtdNam = sc.nextLine();
		jout("Method Param num>>>");
		int parNum = Integer.parseInt(sc.nextLine());
		int i = 0;
		Class[] paramTypes = new Class[parNum];
		Object[] params = new Object[parNum];
		while(i < parNum){
			jout("Param"+i+" Data Type>>>");
			String parTyp = sc.nextLine();
			paramTypes[i] = getClazz(parTyp);
			jout("Param"+i+">>>");
			params[i] = cast(sc.nextLine(), parTyp);
			i++;
		}
		Method method = clazz.getDeclaredMethod(mtdNam, paramTypes);
		return method.invoke(null, params);
	}
	
	private static Object cast(Object obj, String target) throws ClassNotFoundException{
		switch(target){
			case "byte":
				return Byte.parseByte(obj.toString());
			case "short":
				return Short.parseShort(obj.toString());
			case "char":
				return obj.toString().charAt(0);
			case "int":
				return Integer.parseInt(obj.toString());
			case "long":
				return Long.parseLong(obj.toString());
			case "float":
				return Float.parseFloat(obj.toString());
			case "double":
				return Double.parseDouble(obj.toString());
			case "void":
				return null;
			case "boolean":
				return Boolean.parseBoolean(obj.toString());
			default:
				return Class.forName(target).cast(obj);
		}
	}
	
	private static Class getClazz(String className) throws ClassNotFoundException{
		switch(className){
			case "byte":
				return byte.class;
			case "short":
				return short.class;
			case "char":
				return char.class;
			case "int":
				return int.class;
			case "long":
				return long.class;
			case "float":
				return float.class;
			case "double":
				return double.class;
			case "void":
				return void.class;
			case "boolean":
				return boolean.class;
			default:
				return Class.forName(className);
		}
	}
	
	public static void jout(Object arg){
		System.out.print(arg);
	}
	
}
