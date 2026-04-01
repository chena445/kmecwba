package cn.edu.hnu.rtgg.bean;

public class DagComponent {

	private Double[][] communicationMatrix;
	private Double[][] computationMatrix;
	
	private Integer[][] compMartrix;
	private Integer[][] commMartrix;

	public Double[][] getCommunicationMatrix() {
		return communicationMatrix;
	}

	public void setCommunicationMatrix(Double[][] communicationMatrix) {
		this.communicationMatrix = communicationMatrix;
	}

	public Double[][] getComputationMatrix() {
		return computationMatrix;
	}

	public void setComputationMatrix(Double[][] computationMatrix) {
		this.computationMatrix = computationMatrix;
	}

	public Integer[][] getCompMartrix() {
		return compMartrix;
	}

	public void setCompMartrix(Integer[][] compMartrix) {
		this.compMartrix = compMartrix;
	}

	public Integer[][] getCommMartrix() {
		return commMartrix;
	}

	public void setCommMartrix(Integer[][] commMartrix) {
		this.commMartrix = commMartrix;
	}

	

}
