package cn.edu.hnu.rtgg.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;











import cn.edu.hnu.rtgg.bean.VRF;
import cn.edu.hnu.rtgg.bean.ecs.RTGGApplication;
import cn.edu.hnu.rtgg.bean.ecs.AssignedProcessor;
import cn.edu.hnu.rtgg.bean.ecs.DVSProcessor;
import cn.edu.hnu.rtgg.bean.ecs.Group;
import cn.edu.hnu.rtgg.bean.ecs.RTGGTask;
import cn.edu.hnu.rtgg.dag.system.Configuration;


public class GenericUtil {
	
	//private static Logger logger = Logger.getLogger(GenericUtil.class);

	public static void printTaskList(List<RTGGTask> tasks) {
		for (int t = 0; t < tasks.size(); t++) {
			RTGGTask task = tasks.get(t);
			String message=task.toString() + ",it's ranku="
					+ format(task.getRanku()) + ", rankd=" + format(task.getRankd())
					+ ", sum=" + format(task.getRankSum()) + ", it's "
					+ (task.getIsCritical() ? "" : "not") + " a critical path task.";
			
			//System.out.println(message);
			
			//logger.info(message);

		}				
	}
	
	public static void printSchedulingList(LinkedHashMap<RTGGTask, AssignedProcessor> schedulingList) {
		for(RTGGTask task:schedulingList.keySet()) {
			Double est=schedulingList.get(task).getRunningTime().getStartTime();
			Double eft=schedulingList.get(task).getRunningTime().getEndTime();
			//Double est=task.getMakespan_st();
			//Double eft=task.getMakespan_ft();
			Double span=eft-est;
			//String message="Task "+task+" is assigned to "+schedulingList.get(task).getProcessor()+" with voltage "+schedulingList.get(task).getVrs().getVoltage()+", the corresponding est="+format(est)+", eft="+format(eft)+", makespan="+format(span);			
			String message="Task "+task+" is assigned to "+schedulingList.get(task).getProcessor()+" with voltage "+schedulingList.get(task).getVrf().getVoltage()+" "+GenericUtil.printVoltageLevel(schedulingList.get(task).getVrf(), schedulingList.get(task).getProcessor())+", the corresponding est="+format(est)+", eft="+format(eft)+", makespan="+format(span);
			//logger.info(message);
			//System.out.println(message);
		}
		//logger.info("");
	}
	
	public static void printProcessors(List<DVSProcessor> processors) {

		for(DVSProcessor processor:processors) {
			//System.out.println("Processor "+processor);
		}
		
	}
	
	public static String printVoltageLevel(VRF vrf, DVSProcessor processor) {
		int level=0;
		
		int totalLevels=processor.getVrfs().size();
		
		for(int i=0; i<processor.getVrfs().size(); i++) {
			VRF v=processor.getVrfs().get(i);
			if(vrf.equals(v)) level=i+1; 
		}
		
		return "[".concat(String.valueOf(level)).concat("/").concat(String.valueOf(totalLevels)).concat("]");
	}
	
	public static void printHeteroDVSProcessor(List<DVSProcessor> processors) {

		for(DVSProcessor processor:processors) {
			//System.out.println("Processor "+processor+", vrfs=[");
			//logger.info("Processor "+processor+", vrfs=[");
			List<VRF> vrfs0=processor.getVrfs();
			for(VRF vrf:vrfs0) {
				//System.out.println(vrf.getVoltage()+","+vrf.getFrequency());
				//logger.info(vrf.getVoltage()+","+vrf.getFrequency());
			}
			//System.out.println("]");
		//	logger.info("]");
		}
		
	}

	public static void printSuccessors(List<RTGGTask> tasks) {
		for (int t = 0; t < tasks.size(); t++) {
			RTGGTask task = tasks.get(t);
			//System.out.println(task.toString() + "'s successors:");
			for (RTGGTask succTask : task.getSuccTask$CommCostMap().keySet()) {
				//System.out.println(succTask.toString() + "  ");
			}
		}
	}

	public static void printPredecessors(List<RTGGTask> tasks) {
		for (int t = 0; t < tasks.size(); t++) {
			RTGGTask task = tasks.get(t);
			//System.out.println(task.toString() + "'s predecessors:");
			for (RTGGTask predTask : task.getPredTask$CommCostMap().keySet()) {
				//System.out.println(predTask.toString() + "  ");
			}
		}
	}
	
	public static void printTaskGroup(RTGGApplication a) {
		for(Integer groupID:a.getTaskGroups().keySet()) {
			System.out.print("groupID="+groupID);
			System.out.print(", members=[ ");
			Group group=a.getTaskGroups().get(groupID);
			/*
			Iterator<Task> members=group.getTasksSet().iterator();
			while(members.hasNext()) {
				System.out.print(members.next()+" ");
			}
			*/
			for(RTGGTask task:group.getTasksList()) {
				System.out.print(task+" ");
			}
			System.out.print("]");
			System.out.println("");
		}
	}
	
	public static void printGroup(Group group) {	
		System.out.print("groupID="+group.getGroupdID());
		System.out.print(", members=[ ");	
		/*
		Iterator<Task> members=group.getTasksSet().iterator();
		while(members.hasNext()) {
			System.out.print(members.next()+" ");
		}
		*/
		for(RTGGTask task:group.getTasksList()) {
			System.out.print(task+" ");
		}
		System.out.print("]");
		System.out.println("");		
	}

	private static double[] reset(double[] values) {
		double[] v = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		return v;
	}

	private static double getMaxValue(double[] values) {
		double v = 0.0;

		for (int i = 0; i < values.length; i++) {
			if (values[i] > v)
				v = values[i];
		}

		return v;
	}
	
	public static String printMatrix(Double[][] matrix) {
		
		StringBuffer sb=new StringBuffer();		
		
		int rows=matrix.length;
		int cols=matrix[0].length;
		
		sb=sb.append("{");				
				
		for(int i=0;i<rows;i++) {				
			sb=sb.append("{");			
			for(int j=0;j<cols;j++) {						
				if(j==cols-1)	{
					sb=sb.append(matrix[i][j].toString());					
				} else {
					sb=sb.append(matrix[i][j]+",");					
				}
			}			
			if(i==rows-1) {
				sb=sb.append("}");				
			} else {
				sb=sb.append("},");				
			}
			sb=sb.append("");
		}		
		sb=sb.append("}");
		
		return sb.toString();
	}
	
	public static void printResidentTasks(List<DVSProcessor> processors) {
		int i=0;
		for(DVSProcessor p:processors) {
			i=0;
			System.out.println("processor="+p+", the resident task list = [");
			for(RTGGTask task:p.getResidentTasks()) {
				i++;
				System.out.println("	"+i+", task="+task+", task.ast="+task.getMakespan_st()+", task.aft="+task.getMakespan_ft());
			}
			System.out.println("]");
		}		
	}
	
	public static void printCommunicationMatrix(Double[][] communicationMatrix) {
		String message="";
		
		boolean flag=true;
		
		int taskNumber=communicationMatrix.length;
		
		message="communicationMatrix = {";		
		System.out.println("communicationMatrix = {");
		
		for(int i=0;i<taskNumber;i++) {	
			boolean f=false;
			message=message.concat("{");			
			System.out.print("{");
			
			for(int j=0;j<taskNumber;j++) {
				
				if(j==taskNumber-1)	{
					message=message.concat(communicationMatrix[i][j].toString());					
					System.out.print(communicationMatrix[i][j]);
				} else {
					message=message.concat(communicationMatrix[i][j]+",");					
					System.out.print(communicationMatrix[i][j]+",");
				}
				
				f=f||!communicationMatrix[i][j].equals(0.0);
				
			}
			if(i==taskNumber-1) {
				message=message.concat("}");				
				System.out.print("}");
			} else {
				message=message.concat("},");				
				System.out.print("},");
			}
			message=message.concat("");			
			System.out.println("");
			
			if(i<taskNumber-1) flag=flag&&f;
		}		
		message=message.concat("}");		
		System.out.println("}");
		
		System.out.println(flag);
		
	//	logger.info(message);
	}
	
	
	
	public static void writeCommunicationMatrix(Double[][] communicationMatrix)  {
		// String filePath = GenericUtil.class.getResource("").getFile().toString();
		String communicationsFilePath="communications.txt";
		File file = new java.io.File(communicationsFilePath);
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		
		FileWriter fout;
		try {
			fout = new FileWriter(file);
			
			for(int i =0;i<communicationMatrix.length;i++){
				List<Double> line = Arrays.asList(communicationMatrix[i]);
				fout.write((new StringBuilder()).append(line.toString()).append("\n").toString());
			}
			
			fout.flush();
			fout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
	
	public static void printComputationMatrix(Double[][] computationMatrix) {
		String message="";
		
		int taskNumber=computationMatrix.length;
		int processorNumber=computationMatrix[0].length;
		
		message="TaskNumber = "+taskNumber;
		message=message.concat(", ProcessorNumber = "+processorNumber);
	//	logger.info(message);
		System.out.println(message);
		
		message="computationMatrix = {";
		
		System.out.println("computationMatrix = {");
		
		for(int i=0;i<taskNumber;i++) {	
			message=message.concat("{");			
			System.out.print("{");
			
			for(int j=0;j<processorNumber;j++) {
				if(j==processorNumber-1)	{
					message=message.concat(computationMatrix[i][j].toString());					
					System.out.print(computationMatrix[i][j].toString());
				} else {
					message=message.concat(computationMatrix[i][j]+",");					
					System.out.print(computationMatrix[i][j]+",");
				}
			}
			if(i==taskNumber-1) {
				message=message.concat("}");				
				System.out.print("}");
			} else {
				message=message.concat("},");				
				System.out.print("},");
			}
			message=message.concat("");			
			System.out.println("");
		}		
		message=message.concat("}");		
		System.out.println("}");
		
		//logger.info(message);
	}
	
	
public static void writeComputationMatrix(Double[][] computationMatrix)  {
		
	// String filePath = GenericUtil.class.getResource("").getFile().toString();
		String computationFilePath="computation.txt";
		File file = new java.io.File(computationFilePath);
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		
		FileWriter fout;
		try {
			fout = new FileWriter(file);
			
			for(int i =0;i<computationMatrix.length;i++){
				List<Double> line = Arrays.asList(computationMatrix[i]);
				fout.write((new StringBuilder()).append(line.toString()).append("\n").toString());
			}
			
			fout.flush();
			fout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
	
	public static Double calculateMakespan(RTGGApplication a, RTGGTask task, DVSProcessor processor, VRF vrf) {
		VRF maxVRF=processor.getVrfs().get(0);
		return (task.getProcessor$CompCostMap().get(processor)*maxVRF.getFrequency())/vrf.getFrequency();
	}
	
	public static Double format2(int point, Double value) {
		return (new BigDecimal(value).setScale(point, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	public static Double format(Double value) {		
		return (new BigDecimal(value).setScale(Configuration.PRECISION, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	public static Double average(Double[] array) {
		Double avg=0.00;
		Double sum=0.0;
		for(int i=0;i<array.length;i++) {
			sum += array[i];
		}
		avg=sum/array.length;
		return avg;
	}		
	
	public static Double consumeEnergyInVRF(List<DVSProcessor> processors, LinkedHashMap<RTGGTask, AssignedProcessor> schedulingList, Double makespan) {
		
		Double runningE=0.00;
		Double idleE=0.00;
		Double allE=0.00;		
		
		for(RTGGTask task:schedulingList.keySet()) {
			DVSProcessor processor=schedulingList.get(task).getProcessor();
			VRF vrf=schedulingList.get(task).getVrf();
			runningE += runningEnergyInVRF(task, processor, vrf);
		}
		
		idleE = idleEnergyInVRF(processors, schedulingList);
		
		allE = runningE + idleE;
		//allE = runningE;  // just consider running energy consumption.
		
		return allE;
	}	
	
	public static Double pureRunningEnergyInVRF(RTGGApplication a, LinkedHashMap<RTGGTask, AssignedProcessor> schedulingList) {
		
		Double runningE=0.00;		
		Double allE=0.00;		
		
		for(RTGGTask task:schedulingList.keySet()) {
			DVSProcessor processor=schedulingList.get(task).getProcessor();
			VRF vrf=schedulingList.get(task).getVrf();
			runningE += runningEnergyInVRF(task, processor, vrf);
		}				
				
		allE = runningE;  // just consider running energy consumption.
		
		//return allE;
		return format(allE);
	}	
	
	public static Double runningEnergyInVRF(RTGGTask task, DVSProcessor processor, VRF vrf) {
		VRF maxVRF=processor.getVrfs().get(0);
		Double e=0.00;
		
		if(task.getIsVirtual()) {
			e=0.00;
		} else {
			e=Math.pow(vrf.getVoltage(), 2)*vrf.getFrequency()*((task.getProcessor$CompCostMap().get(processor)*maxVRF.getFrequency()/vrf.getFrequency()));
		}
		
		return e;
	}		
	
	public static Double idleEnergyInVRF(List<DVSProcessor> processors, LinkedHashMap<RTGGTask, AssignedProcessor> schedulingList) {
						
		Double makespan=0.00;
		Double energy=0.00;
		Double eachE=0.00;
		
		Double sumMS=0.00;
						
		for(DVSProcessor p:processors) {
			
			makespan=0.00;
			eachE=0.00;
			sumMS=0.00;	
			
			List<VRF> vrfs=p.getVrfs();			
			Double lowestV=vrfs.get(vrfs.size()-1).getVoltage();
			Double lowestF=vrfs.get(vrfs.size()-1).getFrequency();
			
			List<RTGGTask> residentTasks=p.getResidentTasks();
			if(residentTasks.size()>0) makespan=residentTasks.get(residentTasks.size()-1).getMakespan_ft();							
			
			for(RTGGTask task:schedulingList.keySet()) {				
				DVSProcessor assignedProcessor=schedulingList.get(task).getProcessor();
				if(p.equals(assignedProcessor)) {
					Double st=schedulingList.get(task).getRunningTime().getStartTime();
					Double ft=schedulingList.get(task).getRunningTime().getEndTime();
					Double ms=ft-st;
					sumMS += ms;
				}
			}						
			
			Double idleTime=makespan-sumMS;
			
			eachE=Math.pow(lowestV, 2)*lowestF*idleTime;
			
			energy += eachE;
		}
		
		return energy;
	}
	
	public static Double baseEnergy(RTGGApplication a, List<DVSProcessor> processors) {
		Double baseE=0.00;
		Double allcost=0.00;
		Double tempcost=Double.MAX_VALUE;	
		DVSProcessor criticalProcessor=null;
		
		
		for(RTGGTask task:a.getCriticalTasks()) {
			System.out.print(task+" ");
		}
		
		for(DVSProcessor processor:processors) {
									
			allcost = 0.00;

			for(RTGGTask task:a.getCriticalTasks()) {				
				Double compcost=task.getProcessor$CompCostMap().get(processor);
				allcost += compcost;
			}
			
			if(tempcost>allcost) {
				tempcost=allcost;
				criticalProcessor=processor;
			}
		}
		
		System.out.println("The critical processor is "+criticalProcessor);
		
		for(RTGGTask task:a.getCriticalTasks()) {
			VRF maxVRF=criticalProcessor.getVrfs().get(0);
			baseE += task.getProcessor$CompCostMap().get(criticalProcessor)*Math.pow(maxVRF.getVoltage(), 2)*maxVRF.getFrequency();
		}
		
		return baseE;
		
	}
	
	public static Double systemResourceUtilization(RTGGApplication a) {
		Double utilization=0.00;
		List<DVSProcessor> processors=a.getOriginalProcessors();
		Double allTime=processors.size()*a.getDeadline();
		Double runningTime=0.00;
		LinkedHashMap<RTGGTask, AssignedProcessor> schedulingList=a.getNewSchedulingList();
		
		for(RTGGTask task:schedulingList.keySet()) {
			runningTime += task.getMakespan();
		}
		utilization=runningTime/allTime;
		return utilization;
	}
	
	public static void synchronizeWithScheduling(RTGGApplication a, List<DVSProcessor> processors, LinkedHashMap<RTGGTask, AssignedProcessor> schedulingList) {
		
		for(DVSProcessor processor:processors) {		
			processor.getTask$AvailTimeMap().clear();
			processor.getTask$RunningTimeMap().clear();
			processor.getResidentTasks().clear();
		}		

		for(RTGGTask task:schedulingList.keySet()) {
			
			Double ast=schedulingList.get(task).getRunningTime().getStartTime();
			Double aft=schedulingList.get(task).getRunningTime().getEndTime();
			task.setMakespan_st(ast);
			task.setMakespan_ft(aft);
			task.setMakespan(aft-ast);
			task.setProcessor(schedulingList.get(task).getProcessor());							
						
			task.getProcessor().getTask$AvailTimeMap().put(task, aft);
			task.getProcessor().getTask$RunningTimeMap().put(task, schedulingList.get(task).getRunningTime());
			task.getProcessor().getResidentTasks().add(task);
																		
		}		
		
	}
	
	public static void unProcess(List<RTGGTask> taskList) {
		for(int t=0;t<taskList.size();t++) {
			taskList.get(t).setIsProcessed(false);
		}
	}

	public static void clear(List<DVSProcessor> processors) {
		for(int i=0;i<processors.size();i++) {
			DVSProcessor p=processors.get(i);
			//p.getTempTask$RunningTimeMap().clear();
			p.getTask$AvailTimeMap().clear();
			p.getTask$RunningTimeMap().clear();			
		}
	}
	
	public static boolean hasUnoptimized(List<RTGGTask> taskList) {
		boolean flag=false;
		
		for(RTGGTask task:taskList) {
			flag=flag||!task.getIsOptimized();
		}
		
		return flag;
	}		
	
	public static RTGGApplication copy(RTGGApplication source, RTGGApplication destination) {
		destination.setEntryTask(source.getEntryTask());
		destination.setExitTask(source.getExitTask());		
		destination.setComputationMatrix(source.getComputationMatrix());
		destination.setCommunicationMatrix(source.getCommunicationMatrix());
		destination.setTaskList(source.getTaskList());
		destination.setTaskPriorityList(source.getTaskPriorityList());
		destination.setProcessors(source.getProcessors());		
		destination.setOriginalSchedulingList(source.getOriginalSchedulingList());
		destination.setNewSchedulingList(source.getNewSchedulingList());
		destination.setLowerbound(source.getLowerbound());
		destination.setMakespan(source.getMakespan());
		destination.setArrivalTime(source.getArrivalTime());
		destination.setOriginalE(source.getOriginalE());
		destination.setDeadline(source.getDeadline());
		destination.setNewTaskList(source.getNewTaskList());
		destination.setLftTaskPriorityList(source.getLftTaskPriorityList());
		destination.setMyOptimizedSchedulingList(source.getMyOptimizedSchedulingList());
		destination.setOriginalDeadline(source.getOriginalDeadline());
		
		return destination;
	}
	
	public static void reset(RTGGApplication a, List<DVSProcessor> processors) {			
		
		a.getOriginalSchedulingList().clear();
		a.getNewSchedulingList().clear();
		a.getMiniEnergySchedulingList().clear();
		
		for(DVSProcessor processor:processors) {
			processor.getResidentTasks().clear();
			processor.getTask$AvailTimeMap().clear();
			processor.getTask$RunningTimeMap().clear();
		}
		
		for(RTGGTask task:a.getTaskList()) {
			
			task.setProcessor(null);			
			
			task.setMakespan(0.00);
			task.setMakespan_st(0.00);
			task.setMakespan_ft(0.00);
			
			task.setNewMakespan(0.00);
			task.setNewMakespan_st(0.00);
			task.setNewMakespan_ft(0.00);
			
			task.setEst(0.00);
			task.setLft(0.00);
			task.setSlack(0.00);
			
			task.getTask$AssignedProcessor().clear();
		}
	}

}
