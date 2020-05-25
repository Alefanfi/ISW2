package milestone;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStream;

public class PropertiesUtils {
	
	public static String getProperty(String properties) throws IOException {
		
		final Logger LOGGER= Logger.getLogger(PropertiesUtils.class.getName());
		 
		//LOGGER.info("[INFO] - getProperty method called.");
		Properties prop;
		
		try {
			
			prop = new Properties();
			InputStream fis = PropertiesUtils.class.getClassLoader().getResourceAsStream("conf.properties");
			prop.load(fis);
			
			//LOGGER.info("[INFO] - property " + prop.getProperty(properties) + " successfully loaded.");
			
			fis.close();
			return prop.getProperty(properties);
			
		
		}catch(IOException ioException) {
			
			LOGGER.log(Level.SEVERE,"[ERROR] - getProperty method failed: " + ioException);
			throw new IOException(ioException);
		}
		
	}

}
