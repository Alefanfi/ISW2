package deliverable.metrics;

public class Result {
	
	private Integer release;
	private String file;
	private Integer size;
	private Integer locTouched;
	private Integer locAdded;
	private Integer maxLocAdded;
	private Integer avgLocAdded;
	private Integer nauth;
	private Integer nfix;
	private Integer nr;
	private Integer chgSetSize;
	
	public Result(Integer release, String file, Integer size, Integer locTouched, Integer locAdded, Integer maxLocAdded, 
			Integer avgLocAdded, Integer nauth, Integer nfix, Integer nr, Integer chgSetSize) {

		this.release = release;
		this.file = file;
		this.size = size;
		this.locTouched = locTouched;
		this.locAdded = locAdded;
		this.maxLocAdded = maxLocAdded;
		this.avgLocAdded = avgLocAdded;
		this.nauth = nauth;
		this.nfix = nfix;
		this.nr = nr;
		this.chgSetSize = chgSetSize;
		
	}

	public Integer getRelease() {
		return release;
	}

	public void setRelease(Integer release) {
		this.release = release;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getLocTouched() {
		return locTouched;
	}

	public void setLocTouched(Integer locTouched) {
		this.locTouched = locTouched;
	}

	public Integer getLocAdded() {
		return locAdded;
	}

	public void setLocAdded(Integer locAdded) {
		this.locAdded = locAdded;
	}

	public Integer getNauth() {
		return nauth;
	}

	public void setNauth(Integer nauth) {
		this.nauth = nauth;
	}

	public Integer getMaxLocAdded() {
		return maxLocAdded;
	}

	public void setMaxLocAdded(Integer maxLocAdded) {
		this.maxLocAdded = maxLocAdded;
	}

	public Integer getAvgLocAdded() {
		return avgLocAdded;
	}

	public void setAvgLocAdded(Integer avgLocAdded) {
		this.avgLocAdded = avgLocAdded;
	}

	public Integer getNfix() {
		return nfix;
	}

	public void setNfix(Integer nfix) {
		this.nfix = nfix;
	}

	public Integer getNr() {
		return nr;
	}

	public void setNr(Integer nr) {
		this.nr = nr;
	}

	public Integer getChgSetSize() {
		return chgSetSize;
	}

	public void setChgSetSize(Integer chgSetSize) {
		this.chgSetSize = chgSetSize;
	}

}
