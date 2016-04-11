package com.mhealth.common.util;



import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;



public class PropertyUtils {

	public static String getProperty(String pro) {

		Properties prop = new Properties();
		String value = null;
		InputStream in = PropertyUtils.class.getClassLoader()
				.getResourceAsStream("basic.properties");
		try {

			prop.load(in);
			value = (String) prop.get(pro);

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}

	
	public static void setProperty(String pro, String value) {

		Properties prop = new Properties();
		InputStream in=null;
		OutputStream out = null;
		try {
			 in = PropertyUtils.class.getClassLoader()
					.getResourceAsStream("basic.properties");
			prop.load(in);
			in.close();
			
			prop.setProperty(pro, value);
			
			 out = new FileOutputStream(PropertyUtils.class.getClassLoader()
			.getResource("basic.properties")
			.getFile());
			prop.store(out, pro);

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
		
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
		}

	}

	public static boolean isExist(String pro) {
		Properties prop = new Properties();
		boolean exist = false;
		InputStream in = PropertyUtils.class.getClassLoader()
				.getResourceAsStream("basic.properties");
		try {
			prop.load(in);
			if (prop.containsKey(pro)) {
				exist = true;
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);

		} finally {

			if (in != null) {

				try {

					in.close();

				} catch (IOException e) {

					e.printStackTrace();

				}
			}

		}
		return exist;
	}

}
