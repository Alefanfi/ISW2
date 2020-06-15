package deliverable.metrics;

import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Logger;

import org.json.JSONException;

import util.PropertiesUtils;
import util.ReadPropertyFile;

public class Main {
	
	static Integer numVersions;
	
	private static Logger logger = Logger.getLogger(Main.class.getName());
	
	public static void main(String[] args) throws JSONException, IOException, ParseException {
		
		//Get token and project name from conf.properties
		
		String token = PropertiesUtils.getProperty(ReadPropertyFile.TOKEN);
		
		String project = PropertiesUtils.getProperty(ReadPropertyFile.PROJECT);
		
		/* Start to get information about project that will be used for calculating metrics */
		
		GetMetrics.getReleaseInfo(project);
		GetMetrics.getTickets(project);
		GetMetrics.getCommits(project, token);
		GetMetrics.associatingCommitToTickets(GetMetrics.tickets, GetMetrics.commits);
		GetMetrics.getFile(GetMetrics.commits, project, token);
		
		Dataset.getSize(GetMetrics.commitFile);
		
		logger.info("Done");
		 	
	}

}
