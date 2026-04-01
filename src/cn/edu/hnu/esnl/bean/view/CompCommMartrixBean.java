package cn.edu.hnu.esnl.bean.view;

import java.util.Arrays;

/**
 * @author Guoqi Xie E-mail:xgqman@126.com
 * @version JDAS 5.0 Create time：Sep 24, 2016 8:44:13 PM
 */
public class CompCommMartrixBean {
	Integer[][] compMartrix ;
	
	Integer[][] commMartrix;
	
	

	public CompCommMartrixBean(Integer[][] compMartrix, Integer[][] commMartrix) {
		super();
		this.compMartrix = compMartrix;
		this.commMartrix = commMartrix;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(commMartrix);
		result = prime * result + Arrays.hashCode(compMartrix);
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
		CompCommMartrixBean other = (CompCommMartrixBean) obj;
		if (!Arrays.deepEquals(commMartrix, other.commMartrix))
			return false;
		if (!Arrays.deepEquals(compMartrix, other.compMartrix))
			return false;
		return true;
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
