package deliverable.metrics;

import java.util.Date;

public class Release {
	
	private Date releaseDate; 
	
	private String id;
	
	private String version;
	
	public Release(String id, Date releaseDate, String version) {
		
		this.id = id;
		
		this.releaseDate = releaseDate;
		
		this.version = version;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
	
	

}
