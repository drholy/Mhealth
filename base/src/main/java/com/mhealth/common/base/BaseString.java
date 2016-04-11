package com.mhealth.common.base;
public class BaseString {
	public final static String TOKEN_TABLE="he_usercontrol";
	public final static String USER_TABLE="hb_user";
	public final static String POREACTIVE_TABLE="hb_phoremailactive";
	public final static String EMAILBOX_TABLE="hb_emailbox";
	public final static String ATTENTION_TABLE="hb_attention";
	public final static String USERREQUEST_TABLE="hb_userrequest";
	public static final String PRIVILEGE_TABLE ="hb_privilege";
	public final static String ROLE_TABLE="hb_role";
	public final static String ROLE_PRIVILEGE_TABLE="hb_role_privilege";
	public final static String USER_ROLE_TALBE="hb_user_role";
	public static final String ALERT_TABLE = "hb_alert";
	public static final String DATADIC_TABLE = "hb_datadictionary";
	public static final String DEVICEUSER_TABLE="hb_deviceuser";
	public static final String RELATIONSHIP_TABLE = "hb_relationship";
	public static final String HANDLERECORD_TABLE="hb_handlerecord";
	public static final String HANDLEUSERMSG_TABLE="hb_handleusermsg";
	
	//邮箱的激活{内容}括号中的内容替换
		public final static String  ACTIVEEMIALCONTENT="尊敬的用户：<br/>" +
				" 请您点击以下链接连接激活你的账号,让您更方便的体验我的服务：<br/><br/>"+
				"<a href=' {baseUrl}/service/userRegister/activeEmail?" +
				"email={email}" +
				"&code={code} '" +
				">"
				+"{baseUrl}/service/userRegister/activeEmail?email={email}&code={code}"+"</a>";
	public static final String SUBJECT = "请您激活邮箱";
	public static final String RELACTIVE_EMAIL = "尊敬的亲情用户：<br/>" +
				" 请您点击以下链接连接激活你的账号,让您更方便的体验我的服务：<br/><br/>"+
				"<a href=' {baseUrl}/service/userRel/activeEmail?" +
				"email={email}" +
				"&relId={relId}"+
				"&code={code}'" +
				">"
				+"{baseUrl}/service/userRel/activeEmail?email={email}&code={code}"+"</a>";
}
