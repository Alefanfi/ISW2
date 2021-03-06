package deliverable.metrics;

import java.io.IOException;
import java.text.ParseException;

import org.json.JSONException;

import util.PropertiesUtils;
import util.ReadPropertyFile;

public class Main {
	
	public static void main(String[] args) throws JSONException, IOException, ParseException {
		
		//Get token and project name from conf.properties
		
		String token = PropertiesUtils.getProperty(ReadPropertyFile.NEWTOKEN);
		
		String project = PropertiesUtils.getProperty(ReadPropertyFile.PROJECT);
		
		/* Start to get information about project that will be used for calculating metrics */
		
		Dataset.retrieveInfo(project, token);
		 	
	}

}