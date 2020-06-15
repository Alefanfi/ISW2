package deliverable.metrics;

public class Result {
	
	private final Integer release;
	
	private final String file;
	
	private final Integer size;
	
	private final Integer locTouched;
	
	private final Integer locAdded;
	
	private final Integer maxLocAdded;
	
	private final Integer avgLocAdded;
	
	private final Integer nauth;
	
	private final Integer nfix;
	
	private final Integer nr;
	
	private final Integer chgSetSize;
	
	//use a builder patter instead of many costructor parameters

	public Result(Builder builder) {
		
		this.release = builder.release;
		
		this.file = builder.file;
		
		this.size = builder.size;
		
		this.locTouched = builder.locTouched;
		
		this.locAdded = builder.locAdded;
		
		this.maxLocAdded = builder.maxLocAdded;
		
		this.avgLocAdded = builder.avgLocAdded;
		
		this.nauth = builder.nauth;
		
		this.nfix = builder.nfix;
		
		this.nr = builder.nr;
		
		this.chgSetSize = builder.chgSetSize;
		
	}

	public Integer getRelease() {
		return this.release;
	}

	public String getFile() {
		return this.file;
	}
	
	public Integer getSize() {
		return this.size;
	}

	public Integer getLocTouched() {
		return this.locTouched;
	}

	public Integer getLocAdded() {
		return this.locAdded;
	}

	public Integer getNauth() {
		return this.nauth;
	}

	public Integer getMaxLocAdded() {
		return this.maxLocAdded;
	}

	public Integer getAvgLocAdded() {
		return this.avgLocAdded;
	}
	
	public Integer getNfix() {
		return this.nfix;
	}

	public Integer getNr() {
		return this.nr;
	}

	public Integer getChgSetSize() {
		return this.chgSetSize;
	}

	public static class Builder{
		
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
		
		public Result build() {
            return new Result(this);
        }
		
		public Builder withRelease(Integer release) {
			this.release = release;
			return this;
		}
		
		public Builder withFile(String file) {
			this.file = file;
			return this;
		}
		
		public Builder withSize(Integer size) {
			this.size = size;
			return this;
		}
		
		public Builder withLocTouched(Integer locTouched) {
			this.locTouched = locTouched;
			return this;
		}
		
		public Builder withLocAdded(Integer locAdded) {
			this.locAdded = locAdded;
			return this;
		}
		
		public Builder withMaxLocAdded(Integer maxLocAdded) {
			this.maxLocAdded = maxLocAdded;
			return this;
		}
		
		public Builder withAvgLocAdded(Integer avgLocAdded) {
			this.avgLocAdded = avgLocAdded;
			return this;
		}
		
		public Builder withNauth(Integer nauth) {
			this.nauth = nauth;
			return this;
		}
		
		public Builder withNfix(Integer nfix) {
			this.nfix = nfix;
			return this;
		}
		
		public Builder withNr(Integer nr) {
			this.nr = nr;
			return this;
		}
		
		public Builder withChgSetSize(Integer chgSetSize) {
			this.chgSetSize = chgSetSize;
			return this;
		}
		
	}

}
