package cn.edu.hnu.rtgg.bean.ecs;

import cn.edu.hnu.rtgg.bean.VRF;
import cn.edu.hnu.rtgg.bean.VRS;
import cn.edu.hnu.rtgg.bean.view.RunningTime;

public class AssignedProcessor {

	private DVSProcessor processor;
	private VRS vrs;
	private VRF vrf;
	private RunningTime runningTime;

	public DVSProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(DVSProcessor processor) {
		this.processor = processor;
	}

	public VRS getVrs() {
		return vrs;
	}

	public void setVrs(VRS vrs) {
		this.vrs = vrs;
	}	
	
	public VRF getVrf() {
		return vrf;
	}

	public void setVrf(VRF vrf) {
		this.vrf = vrf;
	}

	public RunningTime getRunningTime() {
		return runningTime;
	}

	public void setRunningTime(RunningTime runningTime) {
		this.runningTime = runningTime;
	}

	public AssignedProcessor(DVSProcessor processor, VRS vrs) {
		this.processor = processor;
		this.vrs = vrs;		
	}
	
	public AssignedProcessor(DVSProcessor processor, VRF vrf) {
		this.processor = processor;
		this.vrf = vrf;		
	}

	public AssignedProcessor(DVSProcessor processor, VRS vrs, RunningTime runningTime) {
		this.processor = processor;
		this.vrs = vrs;
		this.runningTime = runningTime;
	}
	
	public AssignedProcessor(DVSProcessor processor, VRF vrf, RunningTime runningTime) {
		this.processor = processor;
		this.vrf = vrf;
		this.runningTime = runningTime;
	}

	@Override
	public String toString() {
		return this.getProcessor().getName() + " run at voltage "
				+ this.getVrs().getVoltage();
	}

}
