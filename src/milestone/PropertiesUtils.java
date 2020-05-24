package milestone;

import java.io.IOException;
import java.util.Properties;

import java.io.InputStream;

public class PropertiesUtils {
	
	public static String getProperty(String properties) throws IOException {
		 
		//System.out.println("[INFO] - getProperty method called.");
		Properties prop;
		
		try {
			
			prop = new Properties();
			InputStream fis = PropertiesUtils.class.getClassLoader().getResourceAsStream("conf.properties");
			prop.load(fis);
			
			//System.out.println("[INFO] - property " + prop.getProperty(properties) + " successfully loaded.");
			
			fis.close();
			return prop.getProperty(properties);
			
		
		}catch(IOException ioException) {
			
			System.out.println("[ERROR] - getProperty method failed: " + ioException);
			throw new IOException(ioException);
		}
		
	}

}
