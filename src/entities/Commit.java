package entities;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Commit {
	
	private String message;
	
	private LocalDate date;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	private String auth;
	
	private String sha;
	
	private List<FileCommitted> commitFile = new ArrayList<>();

	public Commit(String message, String date, String auth, String sha){
		
		this.message = message;
		
		this.date = LocalDate.parse(date, formatter);
		
		this.auth = auth;
		
		this.sha = sha;
			
	}
	
	public List<FileCommitted> getCommitFile() {
		return commitFile;
	}
	
	public void setCommitFile(List<FileCommitted> commitFile) {
		this.commitFile = commitFile;
	}

	public void addCommitFile(FileCommitted f) {
		
		commitFile.add(f);
	}
	
	public String getSha() {
		return sha;
	}
	
	public void setSha(String sha) {
		this.sha = sha;
	}
	
	public String getAuth() {
		return auth;
	}
	
	public void setAuth(String auth) {
		this.auth = auth;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(String date) {
		
		this.date = LocalDate.parse(date, formatter);
				
	}

}
