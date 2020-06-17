package deliverable.metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.json.JSONException;
import entities.Commit;
import entities.FileCommitted;
import entities.Release;
import entities.Ticket;

public class Dataset {
	
	private static final Logger LOGGER = Logger.getLogger(Dataset.class.getName());
	
	static Integer numBugs;

	static int p;
	
	static int numAnalyseBugs;
	
	//List of tickets from the project	
	static List<Ticket> tickets = null;	
			
	//List of all the commits of the project
	static List<Commit> commits = null;	
			
	//List of all the release of the project
	static List<Release> release = null;
			
	//List of all file committed
	static List<FileCommitted> commitedFile = null;
	
	static int numAffected;
	
	private Dataset() {}
	
    //Returns the number of LOC in a file
	
	public static int getLoc(FileCommitted file) {
		
		String[] lines;
				
		String content = file.getContent();
		
		lines = content.split("\n");
		
    	return lines.length;
		
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
	 
		 return count;
	 
	}	
	
	public static void countSize(List<FileCommitted> fileList) {
		
		for(int i=0; i < fileList.size(); i++) {
			
			int loc = getLoc(fileList.get(i));
			
			int comment = countComment(fileList.get(i));
			
			int size = loc - comment;
			
			fileList.get(i).setSize(size);			
			
		}
		
	}
	
	
	//remove all tickets that don't have fixed version or commit associating
	
	public static List<Ticket> checkTickets(List<Ticket> tickets) {
		
		for(int i = 0; i<tickets.size(); i++) {
			
			List<String> versions = tickets.get(i).getFixVersions();
			
			List<Commit> commitList = tickets.get(i).getCommitsTicket();
			
			if(versions.isEmpty() || commitList.isEmpty()) {
				
				tickets.remove(tickets.get(i));
				
			}
			
		}
		
		return tickets;
	}
	
	public static int getReleaseId(List<Release> release, String version) {
		
		int id = 0;
		
		for(int j=0; j< release.size(); j++) {
			
			if(version.compareTo(release.get(j).getVersion()) == 0) {
				
				id = j;	
				
				break;
				
			}
		}
		
		return id;
		
	}
	
	//comparing fix version of a tickets and take the fix versison that have the latest date
	
	public static void compareReleaseVersion(List<Ticket> tickets, List<Release> release) {
		
		int idFix1;
		int idFix2;
		String fix1 = null;
		String fix2 = null;
		
		for(int i=0; i<tickets.size(); i++) {
			
			List <String> fixVersion = tickets.get(i).getFixVersions();
			
			if(fixVersion.size()>1) {
				
				while(fixVersion.size() == 1) {
							
					fix1 = fixVersion.get(0);
					
					fix2 = fixVersion.get(1);
					
					idFix1 = getReleaseId(release, fix1);
					
					idFix2 = getReleaseId(release, fix2);
					
					if(idFix1 > idFix2) {
						
						fixVersion.remove(fix2);
					
					}else if(idFix1 < idFix2){
					
						fixVersion.remove(fix1);
					
					}
					
				}
			}

		}
			
	}
	
	//compare affected version to fix version
	
	public static void compareAffecteVersionToFixVersion(List<Ticket> tickets, List<Release> release) {
		
		String fix = null;
		String affected = null;
		
		int idFix;
		int idAffected;
		
		for(int i = 0; i < tickets.size(); i++) {
			
			List<String> affectedVersion = tickets.get(i).getAffectedVersions();
			
			List<String> fixVersion = tickets.get(i).getFixVersions();
			
			for(int j = 0; j < affectedVersion.size(); j++) {
				
				fix = fixVersion.get(0);
				
				affected = affectedVersion.get(j);
				
				idFix = getReleaseId(release, fix);
				
				idAffected = getReleaseId(release, affected);
				
				//if the affecte version is greater than the fix version, i have to use proportion, so i delete the affected versions' list 
				
				if(idAffected > idFix) {
					
					tickets.get(i).setAffectedVersions(new ArrayList<>());
				
				}
			}
			
		}
			
	}
	
	//function that compute the iv's value
	
	public static void findProportion(Ticket t) {
		
		String fixVersion = t.getFixVersions().get(0);
		
		String openingVersion = t.getOpeningVersion();
		
		double fv = getReleaseId(release, fixVersion) + 1;
		
		double ov = getReleaseId(release, openingVersion) + 1;
		
		int iv = (int) Math.round(fv-(fv-ov)*p);
		
		for(int i=iv-1; i<ov; i++) {
			
			t.addAffectedVersion(release.get(i).getVersion());
		}
		
	}
	
	//compute the proportional number (calculate the moving window)
	
	public static int proportionFunction(Ticket t) {
		
		numAffected += t.getAffectedVersions().size();
		
		numAnalyseBugs ++;
		
		if(numAnalyseBugs>=numBugs) {
			
			p = numAffected/numAnalyseBugs;
			
			numAnalyseBugs = 0;
			
		}
		
		return p;
			
	}
	
	public static void creatingResultList(List<Release> release, List<Ticket> tickets, List<Commit> commits, String projName) throws FileNotFoundException {
		
		List<Result> result = new ArrayList<>();
		
		Result r = null;
		
		Commit c = null;
	
		List<FileCommitted> fileList = null;
		
		FileCommitted f = null;
		
		List<HashMap<String,Result>> maps = new ArrayList<>();
		
		int numRelease = release.size()/2;
		
		LOGGER.info("Creating dataset...");
		
		for(int i = 0; i < numRelease; i++){
			
			maps.add(new HashMap<String,Result>());
			
			while(commits.size()>0){
				
				c = commits.get(0);
				
				//take the list of files associating to commit
				
				fileList = c.getCommitFile();
				
				if(!fileList.isEmpty()) {
				
					for(int j = 0; j < fileList.size(); j++) {
					
						f = fileList.get(j);
						
						r = maps.get(i).get(f.getFilename());
						
						if(r == null) {
							
							//add new result to the list
							
							r = new Result(i+1, f.getFilename());
							
							maps.get(i).put(f.getFilename(), r);
							
							result.add(r);
					
						}
						
						r.setSize(f.getSize());
						r.addLocTouched(f.getLineChange());
						r.addLocAdded(f.getLineAdded());
						r.addAuth(c.getAuth());
						r.addRevision();
						r.addChgSetSize(fileList.size());
					
					}
		
				}
				
				commits.remove(0);
			}
				
		}
		
		List<String> version = null;
		int id;
		
		p=0;
		numAffected = 0;
		numBugs = Math.round(tickets.size()/100);
		numAnalyseBugs = 0;
		
		//check tickets
		
		List<Ticket> newTicketList = checkTickets(tickets);
		compareReleaseVersion(newTicketList, release);
		compareAffecteVersionToFixVersion(newTicketList, release);
		
		//for the remaing tickets determine if they are buggy
		
		for(int k=0; k<newTicketList.size(); k ++) {

			checkTicket(newTicketList.get(k));
	
			c = newTicketList.get(k).getCommitFix();
			version = newTicketList.get(k).getAffectedVersions();
			
			for(int j=0; j<version.size(); j++) {
				
				id = getReleaseId(release, version.get(j));
				
				fileList = c.getCommitFile();
				
				//Checks release
				
				if(id !=-1 && id<numRelease && fileList != null) { 
					
					for(int w=0; w<fileList.size(); w++) {
						
						r = maps.get(id).get(fileList.get(w).getFilename());
						
						if(r != null) {
							
							r.setBuggy("Si");
						}
					}
				}
			}
			
			id = getReleaseId(release, newTicketList.get(k).getFixVersions().get(0));
			
			if(id != -1 && id < numRelease && !fileList.isEmpty()) {
				
				for(int i = 0; i<fileList.size(); i++) {
					
					r = maps.get(id).get(fileList.get(i).getFilename());
					
					if( r!= null) {
						
						r.addFix();
						r.setBuggy("Si");
					}
				}
			}
		}
					
		//Write the dataset in the file .csv
		writeDataset(projName, result);
		
	}
	
	
	//Check ticket and if proportion must be used
	
	private static void checkTicket(Ticket t) {
		
		List<String> affected = t.getAffectedVersions();
		
		//if there isn't affected version use proportion
		
		if(affected.isEmpty()) {
			
			findProportion(t);
		}
		
		proportionFunction(t);
		
		
	}
	
	//Write the dataset

	public static void writeDataset(String project, List<Result> resultList) throws FileNotFoundException{
		
		String outname = project + "DatasetInfo.csv";
		
		Result result = null;
		
		PrintStream printer = new PrintStream(new File(outname));
		  
		printer.println("Release, File, Size, LocTouched, LocAdded, MaxLocAdded, AvgLocAdded, Nauth, Nfix, Nr, ChgSetSize, Buggy");
		
		for(int i=0; i<resultList.size(); i++) {
			
			result = resultList.get(i);
			
			printer.println(result.getRelease() + "," + result.getFile() + "," + result.getSize() + "," + result.getLocTouched() 
					+ "," + result.getLocAdded()  + "," + result.getMaxLocAdded() + "," + result.getAvgLocAdded() + "," 
					+ result.getAuth() + "," + result.getnFix() + "," + result.getnR() + "," + result.getChgSetSize() 
					+ "," + result.getBuggy());
		}
		
		printer.flush();
		
		printer.close();
	
	}
	
	//function that permitte to start retrieve information about the project
	
	public static void retrieveInfo(String projName, String token) throws JSONException, IOException, ParseException {
		
		//retrieve info about releases
		release = GetMetrics.getReleaseInfo(projName);
		
		//retrieve info about tickets
		tickets = GetMetrics.getTickets(projName, release);
				
		//associating release to tickets
		GetMetrics.associatingReleaseToTickets(release, tickets);
		
		//retrieve info about commits
		commits = GetMetrics.getCommits(projName, token, release);
		
		//associating commit to tickets
		GetMetrics.associatingCommitToTickets(tickets, commits);
		
		//retrieve info about files
		commitedFile = GetMetrics.getFile(commits, projName, token);
		
		//evaluation of the files' size
		countSize(commitedFile);
			
		//Create list of result
		creatingResultList(release, tickets, commits, projName);
		
		LOGGER.info("Done!");
	
	}
}