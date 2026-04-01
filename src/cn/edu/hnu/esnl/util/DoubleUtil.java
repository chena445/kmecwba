package cn.edu.hnu.esnl.util;

import java.math.BigDecimal;

/**
 * @author Guoqi Xie E-mail:xgqman@126.com
 * @version JDAS 5.0 Create time��Jan 20, 2016 9:42:47 PM
 */
public class DoubleUtil {
	
	public static void main(String[] args) {
		System.out.println(floor(4.222));
	}
	
	
public static  Double format1(Double value){
		
		value = Math.round(value * 10) / 10.0;
		return value;
	}
	public static  Double format(Double value){
		
		value = Math.round(value * 100) / 100.0;
		return value;
	}
	
	public static  Double format4(Double value){
		
		value = Math.round(value * 10000) / 10000.0;
		return value;
	}
	public static  Double format6(Double value){
		
		value = Math.round(value * 1000000) / 1000000.0;
		return value;
	}
	public static  Double floor(Double value){
		
		value = Math.floor(value * 100) / 100.0;
		return value;
	}
	public static  Double floor4(Double value){
		
		value = Math.floor(value * 10000) / 10000.0;
		return value;
	}
	
	public static  Double ceil4(Double value){
		
		value = Math.ceil(value * 10000) / 10000.0;
		return value;
	}
	
	public static  Double format8(Double value){
		
		value = Math.round(value * 100000000) / 100000000.0;
		return value;
	}
	
	
	
	
}
