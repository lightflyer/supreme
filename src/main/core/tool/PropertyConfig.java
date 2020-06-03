package tool;

import java.io.*;
import java.util.Properties;


public class PropertyConfig {

	private static String classPath;

	private static String propertyFileName;

	private static String pcPath;

	private static Properties properties;

	public PropertyConfig() {

	}

	static {
		classPath = new FileOperation().getClassPath();
		propertyFileName = "config.txt";
		while(true)
		{
			pcPath = new FileOperation().queryFilePath(classPath, propertyFileName);
			if(pcPath!=null)
				break;
			int end =classPath.lastIndexOf("/");
			if(0>end)
		    end =classPath.lastIndexOf("\\");
			if(0>end)
				break;
			classPath=classPath.substring(0, end);
		}
		properties = new Properties();
		InputStream is = null;
		InputStreamReader isr=null;
		try {
			is = new FileInputStream(new File(pcPath));
			isr = new InputStreamReader(is, "utf-8");
			properties.load(isr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();;
		} catch (IOException e) {
			e.printStackTrace();;
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();;
				}
		}
	}

	/***
	 * 获取相应的键值对
	 * 
	 * @param key
	 */
	public static String getPropertyV(String key) {
		Object value = properties.get(key);
		return value == null ? null : value.toString();
	}

	public static void main(String[] args) {

	}
	
	
	public static String getPropertyV(String key,String content)
	{
		return RegexParse.baseParse(content, key+"=([\\s\\S]*?)[,|}]", 1);
	}
	
}
