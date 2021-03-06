package entities;

import java.time.LocalDate;

public class Release {
	
	private LocalDate releaseDate; 
	
	private String id;
	
	private String version;

	public Release(String id, LocalDate releaseDate, String version) {
		
		this.id = id;
		
		this.releaseDate = releaseDate;
		
		this.version = version;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
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
