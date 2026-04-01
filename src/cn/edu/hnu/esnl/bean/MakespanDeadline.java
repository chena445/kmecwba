package cn.edu.hnu.esnl.bean;
/**
 * @author Guoqi Xie E-mail:xgqman@126.com
 * @version JDAS 5.0 Create time：May 20, 2016 1:14:46 AM
 */
public class MakespanDeadline {
	
	private Double makespan;
	
	private Double deadline;
	
	private Double resource;
	
	private Double reliability;
	
	private Integer nr;
	
	private Double deadlineslack;
	
	private Boolean remain;
	
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((makespan == null) ? 0 : makespan.hashCode());
		result = prime * result + ((reliability == null) ? 0 : reliability.hashCode());
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MakespanDeadline other = (MakespanDeadline) obj;
		if (makespan == null) {
			if (other.makespan != null)
				return false;
		} else if (!makespan.equals(other.makespan))
			return false;
		if (reliability == null) {
			if (other.reliability != null)
				return false;
		} else if (!reliability.equals(other.reliability))
			return false;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		return true;
	}

	public Double getDeadlineslack() {
		return deadlineslack;
	}

	public void setDeadlineslack(Double deadlineslack) {
		this.deadlineslack = deadlineslack;
	}

	public Double getReliability() {
		return reliability;
	}

	public void setReliability(Double reliability) {
		this.reliability = reliability;
	}

	public Double getResource() {
		return resource;
	}

	public void setResource(Double resource) {
		this.resource = resource;
	}

	public Double getMakespan() {
		return makespan;
	}

	public void setMakespan(Double makespan) {
		this.makespan = makespan;
	}

	public Double getDeadline() {
		return deadline;
	}

	public void setDeadline(Double deadline) {
		this.deadline = deadline;
	}

	
	
	public Boolean getRemain() {
		return remain;
	}

	public void setRemain(Boolean remain) {
		this.remain = remain;
	}

	
	
	public Integer getNr() {
		return nr;
	}

	public void setNr(Integer nr) {
		this.nr = nr;
	}

	public MakespanDeadline( Double reliability,Double makespan,Double resource,Double deadline,Integer nr, Double deadlineslack, Boolean remain) {
		
		this.reliability = reliability;
		this.makespan = makespan;
		
		this.resource = resource;
		this.deadline = deadline;
		this.nr = nr;
		this.deadlineslack = deadlineslack;
		this.remain = remain;
	}
	
}
