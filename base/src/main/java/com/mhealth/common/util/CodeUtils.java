package com.mhealth.common.util;

import java.util.Random;

public class CodeUtils {
	//生成随机数字符串
	public static String getCode(int num){
		StringBuffer sb=new StringBuffer();
		Random ran=new Random();
		for(int i=0;i<num;i++){
			int randomNum=ran.nextInt(10);
			sb.append(randomNum);
		}
		return sb.toString();
	}

}
