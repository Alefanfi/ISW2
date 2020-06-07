package milestone;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Commit {
	
	private String message;
	
	private Date date;
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	
	private String auth;
	
	public Commit(String message, String date, String auth) throws ParseException{
		
		this.message = message;
		
		this.date = formatter.parse(date);
		
		this.auth = auth;
			
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
