package cn.edu.hnu.esnl.bean.view;

import cn.edu.hnu.esnl.bean.Processor;

/**
 * @author Guoqi Xie E-mail:xgqman@126.com
 * @version JDAS 5.0 Create time：May 31, 2016 4:47:02 PM
 */
public class ProcessorFT {
	
	private Processor processor;
	
	private Double ft;
	
	
	

	public ProcessorFT(Processor processor, Double ft) {
		
		this.processor = processor;
		this.ft = ft;
	}

	public Processor getProcessor() {
		return processor;
	}

	public void setProcessor(Processor processor) {
		this.processor = processor;
	}

	public Double getFt() {
		return ft;
	}

	public void setFt(Double ft) {
		this.ft = ft;
	}
	
	
	
}
