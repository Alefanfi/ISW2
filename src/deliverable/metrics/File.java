package deliverable.metrics;

public class File {
	
	private String filename;
	
	private Integer lineChange;
	
	private Integer lineDelete;
	
	private Integer lineAdded;
	

	public File(String filename, Integer lineChange, Integer lineDelete, Integer lineAdded) {
		
		this.filename = filename;
		
		this.lineChange = lineChange;
		
		this.lineDelete = lineDelete;
		
		this.lineAdded = lineAdded;
		
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public Integer getLineChange() {
		return lineChange;
	}

	public void setLineChange(Integer lineChange) {
		this.lineChange = lineChange;
	}

	public Integer getLineDelete() {
		return lineDelete;
	}

	public void setLineDelete(Integer lineDelete) {
		this.lineDelete = lineDelete;
	}

	public Integer getLineAdded() {
		return lineAdded;
	}

	public void setLineAdded(Integer lineAdded) {
		this.lineAdded = lineAdded;
	}
	
	

}
