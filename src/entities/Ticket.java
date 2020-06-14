package entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Ticket {

	private String id;
	
	private List<Commit> commitsTicket;
	
	private LocalDate resolutionDate = null;
	
	private List<String> versions;
	
	private List<String> fixVersions;

	public Ticket(String id, List<String> versions, List<String> fixVersions) {

		this.id = id;
		this.commitsTicket = new ArrayList<>();
		this.versions = versions;
		this.fixVersions = fixVersions;
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void addCommit(Commit commit) {
		
		commitsTicket.add(commit);
		
	}
	
	public List<Commit> getCommitsTicket() {
		return commitsTicket;
	}

	public void setCommitsTicket(List<Commit> commitsTicket) {
		this.commitsTicket = commitsTicket;
	}
	
	//function to retrive the tickets with the latest date

	public LocalDate findDate() {
		
		if(resolutionDate == null) {

			for(int i=0;i<commitsTicket.size();i++) {
				
				if((resolutionDate == null) || (resolutionDate.compareTo(commitsTicket.get(i).getDate()) < 0)) {
					
					resolutionDate = commitsTicket.get(i).getDate();
				}
				
			}
		}

		return resolutionDate;
		
	}

	public List<String> getVersions() {
		return versions;
	}

	public void setVersions(List<String> versions) {
		this.versions = versions;
	}

	public List<String> getFixVersions() {
		return fixVersions;
	}

	public void setFixVersions(List<String> fixVersions) {
		this.fixVersions = fixVersions;
	}
	
}
