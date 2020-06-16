package deliverable.metrics;

import java.util.List;

public class Result {
	
	private Integer release;
	
	private String file;
	
	private Integer size;
	
	private Integer locTouched;
	
	private Integer locAdded;
	
	private Integer maxLocAdded;
	
	private Integer avgLocAdded;
	
	private List<String> auth;

	private Integer nFix;
	
	private Integer nR;
	
	private Integer chgSetSize;
	
	private String buggy;

	public Result(Integer release, String file) {
		
		this.release = release;
		this.file = file;
	}
		
	public void setRelease(Integer release) {
	
		this.release = release;
		
	}
	
	public Integer getRelease() {
		return release;
	}

	public String getFile() {
		return file;
	}

	public Integer getSize() {
		return size;
	}

	public Integer getLocTouched() {
		return locTouched;
	}

	public Integer getLocAdded() {
		return locAdded;
	}

	public Integer getMaxLocAdded() {
		return maxLocAdded;
	}

	public Integer getAvgLocAdded() {
		return avgLocAdded;
	}

	public Integer getnFix() {
		return nFix;
	}

	public Integer getnR() {
		return nR;
	}

	public Integer getChgSetSize() {
		return chgSetSize;
	}

	public String getBuggy() {
		return buggy;
	}

	public void setFile(String file) {
	
		this.file = file;
		
	}
	
	public void setSize(Integer size) {
	
		this.size = size;
		
	}

	public void setLocTouched(Integer locTouched) {
	
		this.locTouched = locTouched;
		
	}

	public void setLocAdded(Integer locAdded) {
	
		this.locAdded = locAdded;
		
	}

	public void setMaxLocAdded(Integer maxLocAdded) {
	
		this.maxLocAdded = maxLocAdded;
		
	}

	public void setAvgLocAdded(Integer avgLocAdded) {
	
		this.avgLocAdded = avgLocAdded;
		
	}

	public void setnFix(Integer nFix) {
	
		this.nFix = nFix;
		
	}

	public void setnR(Integer nR) {
	
		this.nR = nR;
		
	}
	
	public void setChgSetSize(Integer chgSetSize) {
	
		this.chgSetSize = chgSetSize;
		
	}
	
	public void setBuggy(String buggy) {
	
		this.buggy = buggy;
		
	}
	
	//Adds loc to the number of loc touched
		
	public void addLocTouched(int loc) {
	
		this.locTouched += loc;
		
	}

	//Adds loc to the number of loc added
	
	public void addLocAdded(int loc) {
	
		this.locAdded += loc;
		
		//Updates the value of the max loc added
		
		if(this.maxLocAdded < loc) {
		
			this.maxLocAdded = loc;
			
		}
		
	}
	
	//Adds to the change set size the new number of files committed together
		
	public void addChgSetSize(int num) {
	
		this.chgSetSize += num;
		
	}
	
	//Adds a revision
		
	public void addRevision() {
	
		this.nR++;
		
	}
	
	//Adds an author to the list
		
	public void addAuth(String auth) {

		if(!this.auth.contains(auth)) {
		
			this.auth.add(auth);
			
		}
			
	}
	
	public List<String> getAuth() {
		return auth;
	}

	public void setAuth(List<String> auth) {
		this.auth = auth;
	}
	
}
