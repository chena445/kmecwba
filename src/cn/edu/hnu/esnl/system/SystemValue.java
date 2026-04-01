package cn.edu.hnu.esnl.system;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hnu.esnl.bean.Task;

/**
 * @author Guoqi Xie E-mail:xgqman@126.com
 * @version JDAS 5.0 Create time��Nov 19, 2015 10:23:29 AM
 */
public class SystemValue {
	
	

    public static Integer SPAN=40;
	 
	 public static Integer SPAN2=30;
	 
	 public static Integer SPAN3=20;
	 
	 public static Integer SPAN4=10;
	 
	 public static Integer SPAN5=5;
	
	 public static Integer system_criticality = 0;
	 
	 public static boolean isPrint= false;
	 

	 public static double POWER_COMM = 0.2; //通信能耗
	
		
	 public static double COMM_AST= 0d;
	 
	 public static double COMM_AFT =0d;
	 
	 public static Double COMM_AVAIL =0d;
	 
	 
	 
	 
	 public static Integer CL_BASE = 2;
	 
	 
	 public static Integer RESOURC_USAGE_RATIO= 1;//1000;
	 
	 public static double FREQUENCY_PRECISION = 0.01;
	 
	 
	 public static void initCC(){
		 COMM_AST= 0d;
		 COMM_AFT =0d;
	 }
	 
	 
	 public static double WCET_REDUCTION_LEVEL=0.2;
	
}
