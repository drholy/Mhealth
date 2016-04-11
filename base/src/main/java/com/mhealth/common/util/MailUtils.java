package com.mhealth.common.util;


import com.mhealth.common.entity.MailSenderInfo;
import com.mhealth.common.entity.SimpleMailSender;

public class MailUtils {
	public static  boolean sendEamil(String toAdress,String subject,String content, MailSenderInfo mailInfo){
		  //==============================================
		  mailInfo.setToAddress(toAdress); 
		  mailInfo.setSubject(subject); 
		  mailInfo.setContent(content); 
		  SimpleMailSender sms = new SimpleMailSender();
	     return  sms.sendHtmlMail(mailInfo);//发送html格式
	}
	
}