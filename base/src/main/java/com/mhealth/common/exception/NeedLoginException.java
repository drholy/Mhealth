package com.mhealth.common.exception;

/**
 * 用户需要登录的异常
 * @author jiangwj
 *
 */
public class NeedLoginException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public NeedLoginException(){
		super();
	}
	
	public NeedLoginException(String msg){
		super(msg);
	}
	
	
}
