package entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Ticket {

	private String id;
	
	private List<Commit> commitsTicket;
	
	private LocalDate resolutionDate = null;
	
	private List<String> affectedVersions;
	
	private List<String> fixVersions;
	
	private String openingVersion;
	
	private String injectedVersion;
	
	private LocalDate createdDate;
	
	private Commit commitFix;

	public Ticket(String id, List<String> affectedVersions, List<String> fixVersions, LocalDate createdDate) {

		this.id = id;
		
		this.commitsTicket = new ArrayList<>();
		
		this.affectedVersions = affectedVersions;
		
		this.fixVersions = fixVersions;
		
		this.createdDate = createdDate;
		
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
	
	public void setResolutionDate(LocalDate resolutionDate) {
		this.resolutionDate = resolutionDate;		
	}

	
	public LocalDate getResolutionDate() {
		return this.resolutionDate;
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

	public List<String> getAffectedVersions() {
		return affectedVersions;
	}

	public void setAffectedVersions(List<String> versions) {
		this.affectedVersions = versions;
	}

	public List<String> getFixVersions() {
		return fixVersions;
	}

	public void setFixVersions(List<String> fixVersions) {
		this.fixVersions = fixVersions;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public String getInjectedVersion() {
		return injectedVersion;
	}

	public void setInjectedVersion(String injectedVersion) {
		this.injectedVersion = injectedVersion;
	}

	public String getOpeningVersion() {
		return openingVersion;
	}

	public void setOpeningVersion(String openingVersion) {
		this.openingVersion = openingVersion;
	}

	public Commit getCommitFix() {
		return commitFix;
	}

	//find fix commit
	public void setCommitFix(List<Commit> commitsTicket, Commit commitFix) {
		
		LocalDate resolutionDate = commitFix.getDate();
		
		for(int i = 0; i<commitsTicket.size(); i++) {
					
			if(this.resolutionDate == null || this.resolutionDate.compareTo(resolutionDate)>0) {
				
				setResolutionDate(resolutionDate);
				this.commitFix = commitFix;
								
			}
			
		}

	}

}
