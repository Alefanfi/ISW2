package entities;

import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Commit {
	
	private String message;
	
	private Date date;
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	
	private String auth;
	
	private String sha;
	
	private List<File> commitFile;

	public Commit(String message, String date, String auth, String sha) throws ParseException{
		
		this.message = message;
		
		this.date = formatter.parse(date);
		
		this.auth = auth;
		
		this.sha = sha;
			
	}
	
	public List<File> getCommitFile() {
		return commitFile;
	}

	public void setCommitFile(List<File> commitFile) {
		this.commitFile = commitFile;
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
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(String date) throws ParseException {
		
		this.date = formatter.parse(date);
				
	}

}
