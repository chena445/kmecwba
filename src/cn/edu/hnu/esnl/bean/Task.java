package cn.edu.hnu.esnl.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import cn.edu.hnu.esnl.bean.view.MSLRUF;
import cn.edu.hnu.esnl.bean.view.Scheme;

/**
 * @author xgq 
 * E-mail:xgqman@126.com
 * @version ����ʱ�䣺Nov 24, 2013 11:49:30 PM
 * ��˵��:
 *
 */
public class Task implements Serializable,Comparable<Task> {
	
	private String name ;
	
	private Application application ;
	
	private Boolean isEntry =false;
	
	private Boolean isExit = false;
	

	private LinkedHashMap<Processor, Integer> processor$CompCostMap = new LinkedHashMap<Processor, Integer>();
	
	
	LinkedHashMap<Task, Integer> succTask$CommCostMap  = new  LinkedHashMap<Task, Integer>();
	
	LinkedHashMap<Task, Integer> predTask$CommCostMap  = new  LinkedHashMap<Task, Integer>();
	
	
	private Integer outd = 0 ; // ����
	
	private Double AvgW =0.0 ; //
	
	private Double ranku ; //
	
	private LinkedHashMap<String, Double> octMap = new  LinkedHashMap<String, Double>(); //
	
	private Double oct;
	
	private Double lower_bound_st ; //ʵ�����ʵ��
	
	private Double lower_bound_ft ; //ʵ�����ʵ��
	
	private Double lower_bound_nerror ; //ʵ�����ʵ��
	
	
	
	
	
	private Processor assignedprocessor1; //�����Ĵ�����
	
	
	private Double makespan_st ; //���ϵ���ʱ�����ʵ��
	
	private Double makespan_ft ; //���ϵ���ʱ�����ʵ��
	
	private Processor assignedprocessor2; //���������Ĵ�����
	
	
	
	private Double rankr; //
	
	
	private Double f;
	
	private Double energy; //������������Ĵ�����
	
	
	
	//������ʱ��Լ������С������
	private Double deadline;
	
	//������EES�㷨
	private Double lft;
	
	private Double eft;
	
	private Task downNeighbor;
	
	private Task upwardNeighbor;
	
	//����Լ��
	private Double energyMin;
	
	private Double energyMax;
	
	private Double energyPre = 0d;
	
	private Double AE=0d;
	
	
	private Double AP; //ʵ�ʷ���Ĵ�����

	
	private Double AR;  //实际可靠性
	
	
	private Double mest;
	
	private Double mlft;
	
	private Double oldAR;  //之前的分配的实际可靠性
	
	private Double unMaxAR;  //未分配的最大的可靠性
	
	private Processor unMaxProcessor;  //未分配的最大的可靠性
	
	private Double RMin;
	
	private Double RMax;
	
	private Processor ProcessorMin;
	
	private Processor ProcessorMax;
	
	
	
	private Double resourceCost = 0d;
	
	
	private Double lower_bound_st_s0  =0d;
	
	private Double lower_bound_st_s1  =0d;
	
	private Double lower_bound_st_s2  =0d;
	
	private Double lower_bound_st_s3  =0d;
	
	
	private Double lower_bound_ft_s0  =0d;
	
	private Double lower_bound_ft_s1  =0d;
	
	private Double lower_bound_ft_s2  =0d;
	
	private Double lower_bound_ft_s3  =0d;
	
	
	

	private List<Processor> assignedprocessors1 = new ArrayList<Processor>();
	
    private List<Double> lower_bound_sts = new ArrayList<Double>() ; 
	
	private List<Double> lower_bound_fts = new ArrayList<Double>()  ; 
	
	private List<Double> lower_bound_nerrors = new ArrayList<Double>() ; 
	
	private List<String> lower_bound_asils = new ArrayList<String>() ; 
	
	private List<Double> lower_bound_ernerys = new ArrayList<Double>() ; 
	
	private List<MSLRUF> lower_bound_mslfs = new ArrayList<MSLRUF>();
	
	private List<Double> lower_bound_frequencies = new ArrayList<Double>();
	
	private List<String> assignedprocessors1_old = new ArrayList<String>();
	
	  private List<Double> lower_bound_sts_old = new ArrayList<Double>() ; 
		
		private List<Double> lower_bound_fts_old = new ArrayList<Double>()  ; 
		
		private List<Double> lower_bound_nerrors_old = new ArrayList<Double>() ; 
		
		private List<String> lower_bound_asils_old = new ArrayList<String>() ; 
		
		private List<Double> lower_bound_ernerys_old = new ArrayList<Double>() ; 
		
		private List<MSLRUF> lower_bound_mslfs_old = new ArrayList<MSLRUF>();
		
		private List<Double> lower_bound_frequencies_old = new ArrayList<Double>();
	
	private List<MSLRUF> mslfs = new ArrayList<MSLRUF>();
	
	List<MSLRUF> executedMSLFs = new ArrayList<MSLRUF>();
	
	List<Processor> executedProcessors= new ArrayList<Processor>();
	
	List<Processor> fixedUnmoveProcessors= new ArrayList<Processor>();
	
	private Double developmentCost;
	
	private Scheme scheme;
	
	
	//for fse 2019
	LinkedHashMap<String, Double> ASIL$DevelopmentCostMap  = new  LinkedHashMap<String, Double>();
	
	

	LinkedHashMap<Integer, Double> scheme$ReliabilityMap  = new  LinkedHashMap<Integer, Double>();

	
	
	private Double spanE;
	


	private Double RBP;
	
	
	

	


	public Double getRBP() {
		return RBP;
	}


	public void setRBP(Double rBP) {
		RBP = rBP;
	}


	public Double getEnergyPre() {
		return energyPre;
	}


	public void setEnergyPre(Double energyPre) {
		this.energyPre = energyPre;
	}


	public Double getSpanE() {
		return spanE;
	}


	public void setSpanE(Double spanE) {
		this.spanE = spanE;
	}


	public LinkedHashMap<Integer, Double> getScheme$ReliabilityMap() {
		return scheme$ReliabilityMap;
	}


	public void setScheme$ReliabilityMap(LinkedHashMap<Integer, Double> scheme$ReliabilityMap) {
		this.scheme$ReliabilityMap = scheme$ReliabilityMap;
	}


	public LinkedHashMap<String, Double> getASIL$DevelopmentCostMap() {
		return ASIL$DevelopmentCostMap;
	}


	public void setASIL$DevelopmentCostMap(LinkedHashMap<String, Double> aSIL$DevelopmentCostMap) {
		ASIL$DevelopmentCostMap = aSIL$DevelopmentCostMap;
	}


	


	public List<Processor> getFixedUnmoveProcessors() {
		return fixedUnmoveProcessors;
	}


	public void setFixedUnmoveProcessors(List<Processor> fixedUnmoveProcessors) {
		this.fixedUnmoveProcessors = fixedUnmoveProcessors;
	}


	public List<Processor> getExecutedProcessors() {
		return executedProcessors;
	}


	public void setExecutedProcessors(List<Processor> executedProcessors) {
		this.executedProcessors = executedProcessors;
	}

	//开发成本
	List<Scheme> schemes = new ArrayList<Scheme>();
	
	
	
	
	public List<MSLRUF> getExecutedMSLFs() {
		return executedMSLFs;
	}


	public void setExecutedMSLFs(List<MSLRUF> executedMSLFs) {
		this.executedMSLFs = executedMSLFs;
	}

	private Double DCMin;
	
	private Double DCMax;
	
	private Double   RCMaxReach;
	
	
	private LinkedHashMap<Integer, Double> scheme$DCMap = new  LinkedHashMap<Integer, Double>(); // scheme$dc
	
	private Integer schemeId;
	
	
	private Double pass;
	

	private Double rgoal;
	
	
	private Integer wcet;
	
	private Integer maxWi;
	
	Set<Task> predTaskAlls  = new HashSet<Task>();
	
	
	Set<Task> succTaskAlls = new HashSet<Task>();
	
	Set<Task> unDepenTaskAlls = new HashSet<Task>();
	
	
	
	
	LinkedHashMap<Integer, Double> num$r  = new  LinkedHashMap<Integer, Double>();
	LinkedHashMap<Integer, Double> num$cost  = new  LinkedHashMap<Integer, Double>();
	
	LinkedHashMap<Integer, Double> num$increR  = new  LinkedHashMap<Integer, Double>();
	
	
	private Integer numPC= 0;
	
	
	
	private Double preGoal;
	
	private Double optimalRedundancyR; //optimial redundancy r
	
	
	private Double optimalRedundancyCost; //optimial redundancy r
	
	
	

  //tcps2
	private Boolean reassignment1 = false;
	
	
	


	public Boolean getReassignment1() {
		return reassignment1;
	}


	public void setReassignment1(Boolean reassignment1) {
		this.reassignment1 = reassignment1;
	}


	public Double getOptimalRedundancyR() {
		return optimalRedundancyR;
	}


	public void setOptimalRedundancyR(Double optimalRedundancyR) {
		this.optimalRedundancyR = optimalRedundancyR;
	}


	public Double getOptimalRedundancyCost() {
		return optimalRedundancyCost;
	}


	public void setOptimalRedundancyCost(Double optimalRedundancyCost) {
		this.optimalRedundancyCost = optimalRedundancyCost;
	}


	public Double getPreGoal() {
		return preGoal;
	}


	public void setPreGoal(Double preGoal) {
		this.preGoal = preGoal;
	}

	private Double numCost= 0d;
	
	


	public Double getNumCost() {
		return numCost;
	}


	public void setNumCost(Double numCost) {
		this.numCost = numCost;
	}


	


	public LinkedHashMap<Integer, Double> getNum$cost() {
		return num$cost;
	}


	public void setNum$cost(LinkedHashMap<Integer, Double> num$cost) {
		this.num$cost = num$cost;
	}


	public Integer getNumPC() {
		return numPC;
	}


	public void setNumPC(Integer numPC) {
		this.numPC = numPC;
	}


	public LinkedHashMap<Integer, Double> getNum$increR() {
		return num$increR;
	}


	public void setNum$increR(LinkedHashMap<Integer, Double> num$increR) {
		this.num$increR = num$increR;
	}


	public LinkedHashMap<Integer, Double> getNum$r() {
		return num$r;
	}


	public void setNum$r(LinkedHashMap<Integer, Double> num$r) {
		num$r = num$r;
	}


	public Double getMest() {
		return mest;
	}


	public void setMest(Double mest) {
		this.mest = mest;
	}


	public Double getMlft() {
		return mlft;
	}


	public void setMlft(Double mlft) {
		this.mlft = mlft;
	}


	public Double getLower_bound_nerror() {
		return lower_bound_nerror;
	}


	public void setLower_bound_nerror(Double lower_bound_nerror) {
		this.lower_bound_nerror = lower_bound_nerror;
	}


	public Processor getUnMaxProcessor() {
		return unMaxProcessor;
	}


	public void setUnMaxProcessor(Processor unMaxProcessor) {
		this.unMaxProcessor = unMaxProcessor;
	}


	public Double getUnMaxAR() {
		return unMaxAR;
	}


	public void setUnMaxAR(Double unMaxAR) {
		this.unMaxAR = unMaxAR;
	}


	public Double getOldAR() {
		return oldAR;
	}


	public void setOldAR(Double oldAR) {
		this.oldAR = oldAR;
	}




	public Set<Task> getUnDepenTaskAlls() {
		return unDepenTaskAlls;
	}


	public void setUnDepenTaskAlls(Set<Task> unDepenTaskAlls) {
		this.unDepenTaskAlls = unDepenTaskAlls;
	}


	public Set<Task> getPredTaskAlls() {
		return predTaskAlls;
	}


	public void setPredTaskAlls(Set<Task> predTaskAlls) {
		this.predTaskAlls = predTaskAlls;
	}


	public Set<Task> getSuccTaskAlls() {
		return succTaskAlls;
	}


	public void setSuccTaskAlls(Set<Task> succTaskAlls) {
		this.succTaskAlls = succTaskAlls;
	}


	public Integer getMaxWi() {
		return maxWi;
	}


	public void setMaxWi(Integer maxWi) {
		this.maxWi = maxWi;
	}


	public Integer getWcet() {
		return wcet;
	}


	public void setWcet(Integer wcet) {
		this.wcet = wcet;
	}


	public List<String> getAssignedprocessors1_old() {
		return assignedprocessors1_old;
	}


	public void setAssignedprocessors1_old(List<String> assignedprocessors1_old) {
		this.assignedprocessors1_old = assignedprocessors1_old;
	}


	public List<Double> getLower_bound_sts_old() {
		return lower_bound_sts_old;
	}


	public void setLower_bound_sts_old(List<Double> lower_bound_sts_old) {
		this.lower_bound_sts_old = lower_bound_sts_old;
	}


	public List<Double> getLower_bound_fts_old() {
		return lower_bound_fts_old;
	}


	public void setLower_bound_fts_old(List<Double> lower_bound_fts_old) {
		this.lower_bound_fts_old = lower_bound_fts_old;
	}


	public List<Double> getLower_bound_nerrors_old() {
		return lower_bound_nerrors_old;
	}


	public void setLower_bound_nerrors_old(List<Double> lower_bound_nerrors_old) {
		this.lower_bound_nerrors_old = lower_bound_nerrors_old;
	}


	public List<String> getLower_bound_asils_old() {
		return lower_bound_asils_old;
	}


	public void setLower_bound_asils_old(List<String> lower_bound_asils_old) {
		this.lower_bound_asils_old = lower_bound_asils_old;
	}


	public List<Double> getLower_bound_ernerys_old() {
		return lower_bound_ernerys_old;
	}


	public void setLower_bound_ernerys_old(List<Double> lower_bound_ernerys_old) {
		this.lower_bound_ernerys_old = lower_bound_ernerys_old;
	}


	public List<MSLRUF> getLower_bound_mslfs_old() {
		return lower_bound_mslfs_old;
	}


	public void setLower_bound_mslfs_old(List<MSLRUF> lower_bound_mslfs_old) {
		this.lower_bound_mslfs_old = lower_bound_mslfs_old;
	}


	public List<Double> getLower_bound_frequencies_old() {
		return lower_bound_frequencies_old;
	}


	public void setLower_bound_frequencies_old(List<Double> lower_bound_frequencies_old) {
		this.lower_bound_frequencies_old = lower_bound_frequencies_old;
	}


	public List<Double> getLower_bound_frequencies() {
		return lower_bound_frequencies;
	}


	public void setLower_bound_frequencies(List<Double> lower_bound_frequencies) {
		this.lower_bound_frequencies = lower_bound_frequencies;
	}


	public Double getRgoal() {
		return rgoal;
	}


	public void setRgoal(Double rgoal) {
		this.rgoal = rgoal;
	}


	public Double getPass() {
		return pass;
	}


	public void setPass(Double pass) {
		this.pass = pass;
	}


	public List<String> getLower_bound_asils() {
		return lower_bound_asils;
	}


	public void setLower_bound_asils(List<String> lower_bound_asils) {
		this.lower_bound_asils = lower_bound_asils;
	}


	public Integer getSchemeId() {
		return schemeId;
	}


	public void setSchemeId(Integer schemeId) {
		this.schemeId = schemeId;
	}


	public LinkedHashMap<Integer, Double> getScheme$DCMap() {
		return scheme$DCMap;
	}


	public void setScheme$DCMap(LinkedHashMap<Integer, Double> scheme$dcMap) {
		scheme$DCMap = scheme$dcMap;
	}


	public Double getRCMaxReach() {
		return RCMaxReach;
	}


	public void setRCMaxReach(Double maxReach) {
		RCMaxReach = maxReach;
	}


	public void clear(){
		
		
		
		
	}
	
	
	public Task getUpwardNeighbor() {
		return upwardNeighbor;
	}

	public void setUpwardNeighbor(Task upwardNeighbor) {
		this.upwardNeighbor = upwardNeighbor;
	}

	public Double getDCMin() {
		return DCMin;
	}

	public void setDCMin(Double dCMin) {
		DCMin = dCMin;
	}

	public Double getDCMax() {
		return DCMax;
	}

	public void setDCMax(Double dCMax) {
		DCMax = dCMax;
	}

	public Scheme getScheme() {
		return scheme;
	}

	public void setScheme(Scheme scheme) {
		this.scheme = scheme;
	}

	public List<Scheme> getSchemes() {
		return schemes;
	}

	public void setSchemes(List<Scheme> schemes) {
		this.schemes = schemes;
	}

	public Double getDevelopmentCost() {
		return developmentCost;
	}

	public void setDevelopmentCost(Double developmentCost) {
		this.developmentCost = developmentCost;
	}

	public Double getOct() {
		return oct;
	}

	public void setOct(Double oct) {
		this.oct = oct;
	}

	private Integer nr=0 ;
	
	
	
	
	private Integer level=1;
	
	
	private Integer seq; //调度顺序
	
	
	

	private Boolean isCritical=false;
	
	private Task predCriticalTask=null;
	
	private Double maximumOptSpan; //越大越好

	private Double maximumOptEnergy; //越小越好
	

	private Double givenEnergy ;// tsusc special
	
	
	

	

	

	public LinkedHashMap<String, Double> getOctMap() {
		return octMap;
	}

	public void setOctMap(LinkedHashMap<String, Double> octMap) {
		this.octMap = octMap;
	}

	public List<MSLRUF> getLower_bound_mslfs() {
		return lower_bound_mslfs;
	}

	public void setLower_bound_mslfs(List<MSLRUF> lower_bound_mslfs) {
		this.lower_bound_mslfs = lower_bound_mslfs;
	}

	public List<Double> getLower_bound_ernerys() {
		return lower_bound_ernerys;
	}

	public void setLower_bound_ernerys(List<Double> lower_bound_ernerys) {
		this.lower_bound_ernerys = lower_bound_ernerys;
	}

	public Double getGivenEnergy() {
		return givenEnergy;
	}

	public void setGivenEnergy(Double givenEnergy) {
		this.givenEnergy = givenEnergy;
	}

	public Double getMaximumOptSpan() {
		return maximumOptSpan;
	}

	public void setMaximumOptSpan(Double maximumOptSpan) {
		this.maximumOptSpan = maximumOptSpan;
	}

	public Double getMaximumOptEnergy() {
		return maximumOptEnergy;
	}

	public void setMaximumOptEnergy(Double maximumOptEnergy) {
		this.maximumOptEnergy = maximumOptEnergy;
	}

	public Boolean getIsCritical() {
		return isCritical;
	}

	public void setIsCritical(Boolean isCritical) {
		this.isCritical = isCritical;
	}

	

	public Task getPredCriticalTask() {
		return predCriticalTask;
	}

	public void setPredCriticalTask(Task predCriticalTask) {
		this.predCriticalTask = predCriticalTask;
	}

	public Integer getNr() {
		return nr;
	}

	public void setNr(Integer nr) {
		this.nr = nr;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Double getEft() {
		return eft;
	}

	public void setEft(Double eft) {
		this.eft = eft;
	}

	public List<Double> getLower_bound_nerrors() {
		return lower_bound_nerrors;
	}

	public void setLower_bound_nerrors(List<Double> lower_bound_nerrors) {
		this.lower_bound_nerrors = lower_bound_nerrors;
	}

	public List<Processor> getAssignedprocessors1() {
		return assignedprocessors1;
	}

	public void setAssignedprocessors1(List<Processor> assignedprocessors1) {
		this.assignedprocessors1 = assignedprocessors1;
	}

	
	public List<Double> getLower_bound_sts() {
		return lower_bound_sts;
	}

	public void setLower_bound_sts(List<Double> lower_bound_sts) {
		this.lower_bound_sts = lower_bound_sts;
	}

	public List<Double> getLower_bound_fts() {
		return lower_bound_fts;
	}

	public void setLower_bound_fts(List<Double> lower_bound_fts) {
		this.lower_bound_fts = lower_bound_fts;
	}

	public List<MSLRUF> getMslfs() {
		return mslfs;
	}

	public void setMslfs(List<MSLRUF> mslfs) {
		this.mslfs = mslfs;
	}

	public Double getLower_bound_st_s0() {
		return lower_bound_st_s0;
	}

	public void setLower_bound_st_s0(Double lower_bound_st_s0) {
		this.lower_bound_st_s0 = lower_bound_st_s0;
	}

	public Double getLower_bound_st_s1() {
		return lower_bound_st_s1;
	}

	public void setLower_bound_st_s1(Double lower_bound_st_s1) {
		this.lower_bound_st_s1 = lower_bound_st_s1;
	}

	public Double getLower_bound_st_s2() {
		return lower_bound_st_s2;
	}

	public void setLower_bound_st_s2(Double lower_bound_st_s2) {
		this.lower_bound_st_s2 = lower_bound_st_s2;
	}

	public Double getLower_bound_st_s3() {
		return lower_bound_st_s3;
	}

	public void setLower_bound_st_s3(Double lower_bound_st_s3) {
		this.lower_bound_st_s3 = lower_bound_st_s3;
	}

	public Double getLower_bound_ft_s0() {
		return lower_bound_ft_s0;
	}

	public void setLower_bound_ft_s0(Double lower_bound_ft_s0) {
		this.lower_bound_ft_s0 = lower_bound_ft_s0;
	}

	public Double getLower_bound_ft_s1() {
		return lower_bound_ft_s1;
	}

	public void setLower_bound_ft_s1(Double lower_bound_ft_s1) {
		this.lower_bound_ft_s1 = lower_bound_ft_s1;
	}

	public Double getLower_bound_ft_s2() {
		return lower_bound_ft_s2;
	}

	public void setLower_bound_ft_s2(Double lower_bound_ft_s2) {
		this.lower_bound_ft_s2 = lower_bound_ft_s2;
	}

	public Double getLower_bound_ft_s3() {
		return lower_bound_ft_s3;
	}

	public void setLower_bound_ft_s3(Double lower_bound_ft_s3) {
		this.lower_bound_ft_s3 = lower_bound_ft_s3;
	}

	public Double getResourceCost() {
		return resourceCost;
	}

	public void setResourceCost(Double resourceCost) {
		this.resourceCost = resourceCost;
	}

	public Double getRMin() {
		return RMin;
	}

	public void setRMin(Double rMin) {
		RMin = rMin;
	}

	public Double getRMax() {
		return RMax;
	}

	public void setRMax(Double rMax) {
		RMax = rMax;
	}

	@Override
	public String toString() {
		
		return this.name+",rank_u="+this.getRanku()+",oct="+this.getOct()+",lowerbound="+this.getLower_bound_ft()+",processor="+this.getAssignedprocessor1();
	}

	public Task() {
		
	}


	public Task(String name) {
		super();
		this.name = name;
	}
	
	
	
	
	

	public Double getAP() {
		return AP;
	}

	public void setAP(Double aP) {
		AP = aP;
	}

	

	public Double getAE() {
		return AE;
	}

	public void setAE(Double aE) {
		AE = aE;
	}

	public Double getAR() {
		return AR;
	}

	public void setAR(Double aR) {
		AR = aR;
	}

	
	

	public Double getEnergyMin() {
		return energyMin;
	}

	public void setEnergyMin(Double energyMin) {
		this.energyMin = energyMin;
	}

	public Double getEnergyMax() {
		return energyMax;
	}

	public void setEnergyMax(Double energyMax) {
		this.energyMax = energyMax;
	}

	

	public Task getDownNeighbor() {
		return downNeighbor;
	}

	public void setDownNeighbor(Task downNeighbor) {
		this.downNeighbor = downNeighbor;
	}

	public Double getF() {
		return f;
	}

	public void setF(Double f) {
		this.f = f;
	}

	public Double getEnergy() {
		return energy;
	}

	public void setEnergy(Double energy) {
		this.energy = energy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public Boolean getIsEntry() {
		return isEntry;
	}

	public void setIsEntry(Boolean isEntry) {
		this.isEntry = isEntry;
	}

	public Boolean getIsExit() {
		return isExit;
	}

	public void setIsExit(Boolean isExit) {
		this.isExit = isExit;
	}

	public LinkedHashMap<Processor, Integer> getProcessor$CompCostMap() {
		return processor$CompCostMap;
	}

	public void setProcessor$CompCostMap(LinkedHashMap<Processor, Integer> processor$CompCostMap) {
		this.processor$CompCostMap = processor$CompCostMap;
	}

	public LinkedHashMap<Task, Integer> getSuccTask$CommCostMap() {
		return succTask$CommCostMap;
	}

	public void setSuccTask$CommCostMap(LinkedHashMap<Task, Integer> succTask$CommCostMap) {
		this.succTask$CommCostMap = succTask$CommCostMap;
	}

	public LinkedHashMap<Task, Integer> getPredTask$CommCostMap() {
		return predTask$CommCostMap;
	}

	public void setPredTask$CommCostMap(LinkedHashMap<Task, Integer> predTask$CommCostMap) {
		this.predTask$CommCostMap = predTask$CommCostMap;
	}

	public Integer getOutd() {
		return outd;
	}

	public void setOutd(Integer outd) {
		this.outd = outd;
	}

	public Double getAvgW() {
		return AvgW;
	}

	public void setAvgW(Double avgW) {
		AvgW = avgW;
	}

	public Double getRanku() {
		return ranku;
	}

	public void setRanku(Double ranku) {
		this.ranku = ranku;
	}

	public Double getLower_bound_st() {
		return lower_bound_st;
	}

	public void setLower_bound_st(Double lower_bound_st) {
		this.lower_bound_st = lower_bound_st;
	}

	public Double getLower_bound_ft() {
		return lower_bound_ft;
	}

	public void setLower_bound_ft(Double lower_bound_ft) {
		this.lower_bound_ft = lower_bound_ft;
	}

	public Processor getAssignedprocessor1() {
		return assignedprocessor1;
	}

	public void setAssignedprocessor1(Processor assignedprocessor1) {
		this.assignedprocessor1 = assignedprocessor1;
	}

	public Double getMakespan_st() {
		return makespan_st;
	}

	public void setMakespan_st(Double makespan_st) {
		this.makespan_st = makespan_st;
	}

	public Double getMakespan_ft() {
		return makespan_ft;
	}

	public void setMakespan_ft(Double makespan_ft) {
		this.makespan_ft = makespan_ft;
	}

	public Processor getAssignedprocessor2() {
		return assignedprocessor2;
	}

	public void setAssignedprocessor2(Processor assignedprocessor2) {
		this.assignedprocessor2 = assignedprocessor2;
	}

	public Double getRankr() {
		return rankr;
	}

	public void setRankr(Double rankr) {
		this.rankr = rankr;
	}

	
	

	public Double getDeadline() {
		return deadline;
	}

	public void setDeadline(Double deadline) {
		this.deadline = deadline;
	}

	public Double getLft() {
		return lft;
	}

	public void setLft(Double lft) {
		this.lft = lft;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		final Task other = (Task) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public int compareTo(Task o) {
		int result =0;
		if(result==0)
			 result = -this.getRanku().compareTo(o.getRanku());
		return result;
	}

	public Processor getProcessorMin() {
		return ProcessorMin;
	}

	public void setProcessorMin(Processor processorMin) {
		ProcessorMin = processorMin;
	}

	public Processor getProcessorMax() {
		return ProcessorMax;
	}

	public void setProcessorMax(Processor processorMax) {
		ProcessorMax = processorMax;
	}

	//增加getId方法 name从1开始 id 从0开始
	public int getId() {
    if (name == null) return -1;
    // name格式为 F_1.n_3
    int idx = name.lastIndexOf('.');
    if (idx != -1 && idx + 1 < name.length()) {
        String numPart = name.substring(idx + 1);
        if (numPart.startsWith("n_")) numPart = numPart.substring(2);
        try {
            return Integer.parseInt(numPart);
        } catch (NumberFormatException e) {
            // 解析失败返回-1
        }
    }
    return -1;
}
	

	
	

	
	
	
	
	
	
	
	
	
	
	
}
