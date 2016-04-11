package com.mhealth.common.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.apache.commons.lang.StringUtils.isBlank;

public class StringUtils {
	
	public static boolean isEmpty(String obj){
		return isBlank(obj);
	}
	 public static boolean isPhone(String mobiles){     
	        Pattern p = Pattern.compile("1\\d{10}");     
	        Matcher m = p.matcher(mobiles);     
	        return m.matches();     
	    } 
	   
	    public static boolean isEmail(String email){     
	     String str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
	        Pattern p = Pattern.compile(str);     
	        Matcher m = p.matcher(email);     
	        return m.matches();     
	    } 
	    public static boolean isLengthRight(String str,int len){
	    	return (str.length()>len)?false:true;
	    }
	    
	    public static boolean isEmpty(String... args){
	    	boolean empty=false;
	    	for(String temp:args){
	    		if(isBlank(temp)){
	    			empty=true;
	    			break;
	    		}
	    	}
	    	return empty;
	    }
	    public static boolean isTimeStr(String time){
	        try{
	        	Long.valueOf(time);
	        }catch(Exception e){
	        	return false;
	        }
	        return true;
	    }
	    
		public static boolean notEquals(String[] strings) {
			return false;
		}

}
