package deliverable.metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.json.JSONException;
import entities.Commit;
import entities.FileCommitted;
import entities.Release;
import entities.Result;
import entities.Ticket;

public class Dataset {
	
	private static final Logger LOGGER = Logger.getLogger(Dataset.class.getName());
	
	static int numBugs;

	static float p;
	
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
	
	/* remove all tickets that don't have fixed version or commit associating and
	 *
	 * comparing fix version of a tickets and take the fix versison that have the latest date*/
	
	public static List<Ticket> checkFixVersionTickets(List<Ticket> tickets, List<Release> release) {
		
		List<Commit> commitList = null;
		
		List<String> fixVersion = null;
		
		List <String> affectedVersion = null;
		
		int idFix1;
		int idFix2;
		String fix1 = null;
		String fix2 = null;
		
		for(int i = 0; i<tickets.size(); i++) {
			
			fixVersion = tickets.get(i).getFixVersions();
			
			commitList = tickets.get(i).getCommitsTicket();
			
			affectedVersion = tickets.get(i).getAffectedVersions();
			
			if(fixVersion.isEmpty()|| commitList.isEmpty()) {
				
				tickets.remove(tickets.get(i));
				
			} else if(!fixVersion.isEmpty()) {
				
				while(fixVersion.size() > 1) {
							
					fix1 = fixVersion.get(0);
					
					fix2 = fixVersion.get(1);
					
					idFix1 = getReleaseId(release, fix1);
					
					idFix2 = getReleaseId(release, fix2);
					
					// keep the most recent fix version
					
					if(idFix1 > idFix2) {
						
						fixVersion.remove(fix2);
						affectedVersion.add(fix2);
					
					}else if(idFix1 < idFix2){
					
						fixVersion.remove(fix1);
						affectedVersion.add(fix1);
					
					}
					
				}
				
			}
			
		}
		
		return tickets;
	}
	
	// return the number of a release
	
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
	
	
	//compare affected version to fix version
	
	public static List<Ticket> compareAffecteVersionToFixVersion(List<Ticket> tickets, List<Release> release) {
		
		String fix = null;
		String affected = null;
		
		int idFix;
		int idAffected;
		
		for(int i = 0; i < tickets.size(); i++) {
			
			List<String> affectedVersion = tickets.get(i).getAffectedVersions();
			
			List<String> fixVersion = tickets.get(i).getFixVersions();
			
			if(!fixVersion.isEmpty()) {
			
				for(int j = 0; j < affectedVersion.size(); j++) {
					
					fix = fixVersion.get(0);
					
					affected = affectedVersion.get(j);
					
					idFix = getReleaseId(release, fix);
					
					idAffected = getReleaseId(release, affected);
					
					/*if the affected version is greater than the fix version, i have to use proportion, 
					 * 
					 * so i delete the affected versions' list 
					 *
					 */
					
					if(idAffected > idFix) {
						
						tickets.get(i).setAffectedVersions(new ArrayList<>());
						
						break;
					
					}
				}
			}
			
		}
		
		return tickets;
			
	}
	
	//function that compute the iv's value
	
	public static void findProportion(Ticket t) {
		
		String fixVersion = t.getFixVersions().get(0);
		
		String openingVersion = t.getOpeningVersion();
		
		int fv = getReleaseId(release, fixVersion) + 1;
		
		int ov = getReleaseId(release, openingVersion) + 1;
		
		//check value of fv and ov
		
		if(fv<ov) {
    		
    		int var = ov;
    		ov = fv;
    		fv = var;
    		
    	}
		
		//compute injected version
		
		int iv = (int) Math.floor(fv-(fv-ov)*p);
		
		//Checks if the value is negative
    	if(iv < 0) {
    		
    		iv = 1;
    	
    	}
		
    	//Add new affected version to the ticket
    	
		for(int i=iv-1; i<fv-1; i++) {
			
			t.addAffectedVersion(release.get(i).getVersion());
		}
		
	}
	
	//compute the proportional number (calculate the moving window)
	
	public static float proportionFunction(Ticket t) {
		
		numAffected += t.getAffectedVersions().size();
		
		numAnalyseBugs ++;
		
		if(numAnalyseBugs>=numBugs) {
			
			p = numAffected/(float)numAnalyseBugs;
			
			numAnalyseBugs = 0;
			
			numAffected = 0;
			
		}
		
		return p;
			
	}
	
	//creating the value that will be print in the file csv
	
	public static void createDataset(List<Release> release, List<Commit> commits, String projName) throws FileNotFoundException {
		
		List<Result> result = new ArrayList<>();
		List<HashMap<String,Result>> maps = new ArrayList<>();
		List<FileCommitted> fileList = null;
		Commit c = null;
		
		LocalDate releaseDate = null;
			
		LOGGER.info("Creating dataset...");
		
		for(int i = 0; i < release.size()/2; i++) {
			
			releaseDate = release.get(i).getReleaseDate();
			
			//Create hashmap for the release
			
			maps.add(new HashMap<String, Result>());
			
			while(commits.size()>0 && commits.get(0).getDate().compareTo(releaseDate)<0) {

				//get the first commit
				c = commits.get(0);
				
				fileList = c.getCommitFile();
				
				if(!fileList.isEmpty()) {
					
					addResult(maps, c, fileList, i, result);
					
				}
				
				commits.remove(c);
				
			}
			
		}
		
		computeBugginess(maps, result);
			
		//Write the dataset in the file .csv
		
		writeDataset(projName, result);
		
	}
		
	//take file and analyse it or using proportion or using the affected version take from jira
	
	private static void computeBugginess(List<HashMap<String, Result>> maps,List<Result> result) {
		
		//Affected version
		
		List<String> version = null;
		int id;
		Commit c;		
		List<FileCommitted> fileList = null;
				
		p=0;
		numAffected = 0;
		numBugs = Math.round(tickets.size()/100);
		numAnalyseBugs = 0;
	
		List<Ticket> ticketList = checkFixVersionTickets(tickets, release);
					
		compareAffecteVersionToFixVersion(ticketList, release);
				
		for(int i = 0; i < ticketList.size(); i++) {
						
			c = ticketList.get(i).getCommitFix();
		
			version = tickets.get(i).getAffectedVersions();
					
			if(c == null || ticketList.get(i).getFixVersions().size() == 0) {
						
				continue;
				
			}else {
						
				fileList = c.getCommitFile();
						
				if(version.isEmpty()) {
					
					//if there isn't affected version use proportion		
					
					findProportion(ticketList.get(i));

					proportionFunction(ticketList.get(i));
											
				}else {
					
					proportionFunction(ticketList.get(i));

					for(int j=0; j<version.size(); j++) {
	
						id = getReleaseId(release, version.get(j));

						checkVersion(fileList, maps, false, id);	
						
					}			
					
				}						
				
			}					
			
			//fixed Version

			id = getReleaseId(release, ticketList.get(i).getFixVersions().get(0));

			if(id < release.size()/2) {

				checkVersion(fileList, maps, true, id);

			}

		}
		
	}
	
	//check if a file is buggy or not

	private static void checkVersion(List<FileCommitted> fileList, List<HashMap<String, Result>> maps, boolean updateFix, int id) {
		
		Result r = null;
				
		//Checks release
						
		if(id<release.size()/2) {
									
			for(int w=0; w<fileList.size(); w++) {
								
				r = maps.get(id).get(fileList.get(w).getFilename());
																				
				if(r != null) {
												
					r.setBuggy("Si");
											
					if(updateFix) {
						
						r.addFix();						
						
					}
				
				}
			
			}	
	
		}
		
	}
	
	//check the file and, if it's not new update info, else create the new result
		
	private static void addResult(List<HashMap<String, Result>> maps, Commit c, List<FileCommitted> fileList, int i, List<Result> result) {
		
		FileCommitted f = null;
		Result r = null;
		
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
	
	//Write the dataset

	public static void writeDataset(String project, List<Result> resultList) throws FileNotFoundException{
		
		String outname = project + "DatasetInfo.csv";
		
		Result result = null;
		
		PrintStream printer = new PrintStream(new File(outname));
		  
		printer.println("Release; File; Size; LocTouched; LocAdded; MaxLocAdded; AvgLocAdded; Nauth; Nfix; Nr; ChgSetSize; Buggy");
		
		//take result from the resaltList and print them in the file
		
		for(int i=0; i<resultList.size(); i++) {
			
			result = resultList.get(i);
			
			printer.println(result.getRelease() + ";" + result.getFile() + ";" + result.getSize() + ";" + result.getLocTouched() 
					+ ";" + result.getLocAdded()  + ";" + result.getMaxLocAdded() + ";" + String.format("%.2f", result.getAvgLocAdded()) + ";" 
					+ result.getAuth() + ";" + result.getnFix() + ";" + result.getnR() + ";" + result.getChgSetSize() 
					+ ";" + result.getBuggy());
		}
		
		printer.flush();
		
		printer.close();
	
	}
	
	/*function that permitte to start retrieve information about the project*/
	
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
		GetMetrics.countSize(commitedFile);
			
		//Create list of result
		createDataset(release, commits, projName);
		
		LOGGER.info("Dataset done!");
	
	}
}