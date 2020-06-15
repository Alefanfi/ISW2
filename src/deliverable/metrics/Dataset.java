package deliverable.metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.List;
import org.json.JSONException;
import entities.Commit;
import entities.FileCommitted;
import entities.Release;
import entities.Ticket;

public class Dataset {
	
	//private static final Logger LOGGER = Logger.getLogger(Dataset.class.getName());
	
    //Returns the number of LOC in a file
	
	public static int getLoc(FileCommitted file) {
		
		String[] lines;
				
		String content = file.getContent();
		
		lines = content.split("\n");
		 
		int loc = lines.length;

		System.out.println("LOC " + loc);
		
    	return loc;
		
	}
	
	//Returns the number of comments in a file
	
	public static int countComment(FileCommitted file) {

		 int count = 0;
		 
		 String content = file.getContent();
		 
		 String[] lines = content.split("\n");
		 
		 for(int j=0; j < lines.length; j++) {
			 
			 if(lines[j].startsWith("//") || (lines[j].startsWith("/*") && lines[j].endsWith("*/"))) {
				 
				 count ++;
				 	 
			 } else if(lines[j].startsWith("/*") && !lines[j].endsWith("*/")) {
				 
				 int k = j;
				 
				 do {
					 
					 count ++;
					 
					 k++;
				 
				 }while (!lines[k].endsWith("*/") && k<lines.length-1); 
		
			 }
			 
		 }
		 
		 System.out.println("count = " + count);
	 
		 return count;
	 
	}	
	
	public static void countSize(List<FileCommitted> fileList) {
		
		for(int i=0; i < fileList.size(); i++) {
			
			System.out.println("File " + i);
			
			int loc = getLoc(fileList.get(i));
			
			int comment = countComment(fileList.get(i));
			
			int size = loc - comment;
			
			//fileList.get(i).setSize(size);			
			
			System.out.println(size);
		}
		
	}
	
	
	//Write dataset
	
	public void writeDataset(String project, List<Result> resultList) throws FileNotFoundException{
		
		String outname = project + "DatasetInfo.csv";
		
		Result result = null;
		
		PrintStream printer = new PrintStream(new File(outname));
		  
		printer.println("Release, File, Size, LocTouched, LocAdded, MaxLocAdded, AvgLocAdded, Nauth, Nfix, Nr, ChgSetSize");
		
		for(int i=0; i<resultList.size(); i++) {
			
			result = resultList.get(i);
			
			printer.println(result.getRelease() + "," + result.getFile() + "," + result.getSize() + "," + result.getLocTouched() 
					+ "," + result.getLocAdded()  + "," + result.getMaxLocAdded() + "," + result.getAvgLocAdded() + "," 
					+ result.getAvgLocAdded() + "," + result.getNauth() + "," + result.getNfix() + "," + result.getLocAdded());
		}
		
		printer.flush();
		
		printer.close();
	
	}
	
	public static void retrieveInfo(String projName, String token) throws JSONException, IOException, ParseException {
		
		//List of tickets from the project	
		List<Ticket> tickets = null;	
		
		//List of all the commits of the project
		List<Commit> commits = null;	
		
		//List of all the release of the project
		List<Release> release = null;
		
		//List of alla file committed
		List<FileCommitted> commitedFile = null;
		
		//retrieve info about releases
		release = GetMetrics.getReleaseInfo(projName);
		
		//retrieve info about tickets
		tickets = GetMetrics.getTickets(projName, release);
		
		//retrieve info about commits
		commits = GetMetrics.getCommits(projName, token, release);
		
		//associating commit to tickets
		GetMetrics.associatingCommitToTickets(tickets, commits);
		
		//retrieve info about files
		commitedFile = GetMetrics.getFile(commits, projName, token);
		
		countSize(commitedFile);
	
	}
}