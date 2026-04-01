package cn.edu.hnu.rtgg.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;



import cn.edu.hnu.rtgg.bean.DagComponent;
import cn.edu.hnu.rtgg.util.GenericUtil;


public class DAGGenerator {
	
	//private static Logger logger=Logger.getLogger(DAGGenerator.class);
	
	private static FileWriter fout;
	static String FILE_PATH = GenericUtil.class.getResource("").getFile().toString();
	public static void main(String args[]){
		
		System.out.println("FILE_PATH:"+FILE_PATH);
		DagComponent comp = DAGGenerator.randomDAG(10, 3, 10, 1, 0.5, 1);

	//	System.out.println(comp.getCommunicationMatrix());


   }
	
	public static DagComponent randomDAG(int taskNumber, int processorNumber, int avgComputation, double ccr, double heteroFactor, double shape) {
		
		Integer[][] compMartrix2 = new Integer[taskNumber+1][processorNumber+1];
		for (int i = 0; i < taskNumber+1; i++) {
			for (int j = 0; j < processorNumber+1; j++) {

				compMartrix2[i][j] = 0;

			}
		}
		Integer[][] commMartrix2 = new Integer[taskNumber+1][taskNumber+1];
		for (int i = 1; i < taskNumber+1; i++) {
			for (int j = 1; j < taskNumber+1; j++) {

				commMartrix2[i][j] = 0;
			}
		}
		
		
		
		DagComponent comp = random(taskNumber,processorNumber,avgComputation,ccr,heteroFactor,shape);
		
		for (int i = 1; i < taskNumber+1; i++) {
			for (int j = 1; j < processorNumber+1; j++) {

				compMartrix2[i][j] = comp.getComputationMatrix()[i-1][j-1].intValue();

			}
		}
		for (int i = 1; i < taskNumber+1; i++) {
			for (int j = 1; j < taskNumber+1; j++) {

				commMartrix2[i][j] =  comp.getCommunicationMatrix()[i-1][j-1].intValue();
			}
		}
		
		comp.setCompMartrix(compMartrix2);
		comp.setCommMartrix(commMartrix2);
		return comp;
	
	}
			
	public static DagComponent random(int taskNumbers, int processorNumbers, int avgComputation, double ccr, double heteroFactor, double shape) {
		
		DagComponent component=new DagComponent();
		
		
		String communicationsFilePath=FILE_PATH+"communications.txt";
		String computationsFilePath=FILE_PATH+"computations.txt";				
		
		try {
			
			fout = new FileWriter("args.txt");
			
			fout.write((new StringBuilder()).append(taskNumbers).append("\n").toString());
			fout.write((new StringBuilder()).append(avgComputation).append("\n").toString());
			fout.write((new StringBuilder()).append(ccr).append("\n").toString());
			fout.write((new StringBuilder()).append(processorNumbers).append("\n").toString());
			fout.write((new StringBuilder()).append(heteroFactor).append("\n").toString());
			fout.write((new StringBuilder()).append(shape).append("\n").toString());
			fout.write((new StringBuilder()).append(communicationsFilePath).append("\n").toString());
			fout.write((new StringBuilder()).append(computationsFilePath).append("\n").toString());
			fout.flush();
			fout.close();
			
			Runtime rt=Runtime.getRuntime();
			String cmd=FILE_PATH+"rtgg.exe";
			Process p=rt.exec(cmd);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inputLine;
            while((inputLine = in.readLine()) != null) {
              // System.out.println(inputLine);
            }
            in.close();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}			
		
		component.setCommunicationMatrix(generateCommunicationMatrixRandom(taskNumbers));
		//GenericUtil.printCommunicationMatrix(component.getCommunicationMatrix());
		GenericUtil.writeCommunicationMatrix(component.getCommunicationMatrix());
		
		
		component.setComputationMatrix(generateComputationMatrix(taskNumbers, processorNumbers, avgComputation, heteroFactor));
		//GenericUtil.printComputationMatrix(component.getComputationMatrix());
		GenericUtil.writeComputationMatrix(component.getComputationMatrix());
		return component;
		
	}
	
	
	
	private static Double[][] generateCommunicationMatrixRandom(int taskNumbers) {
		
		String communicationsFilePath=FILE_PATH+"communications.txt";
		
		Double[][] communicationMatrix=new Double[taskNumbers][taskNumbers];
		File communications=new File(communicationsFilePath);
		
		for(int i=0;i<taskNumbers;i++) { // Initiate the matrix with 0.0
			for(int j=0;j<taskNumbers;j++) {
				
				communicationMatrix[i][j]=0.0;
												
			}
		}
		
		if(communications.exists()&&communications.isFile()) {			
			
			int lineNumber=0;
			String line=null;
			
			try {
				
				InputStreamReader reader=new InputStreamReader(new FileInputStream(communications));
				BufferedReader bufferedReader=new BufferedReader(reader);
				
				while((line=bufferedReader.readLine())!=null) {
					
					lineNumber++;
					
					if(lineNumber>3) {

						//System.out.println("i="+lineNumber+": "+line);
						//logger.info("i="+lineNumber+": "+line);

						String[] pairs=line.split("\t");												
						
						int t1=Integer.parseInt(pairs[0]);
						int t2=Integer.parseInt(pairs[1]);
						int cm=Integer.parseInt(pairs[2]);
						//if(cm==0) cm=1+(new Random()).nextInt((int) (avgComputation*ccr));
						
						Double communication=Double.valueOf(cm);						
						
						
						for(int i=0;i<taskNumbers;i++) {
							for(int j=0;j<taskNumbers;j++) {
								
								if((i+1)==t1&&(j+1)==t2) { // task's suffix start from 1, so i and j should add 1 at the first, respectively, then comparing  
									communicationMatrix[i][j]=communication;
								}								
																
							}
						}
							
						
					}
				}
				bufferedReader.close();
				reader.close();
				
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {				
				e.printStackTrace();
			} finally {				
			}
			
		}
		
		//GenericUtil.printCommunicationMatrix(communicationMatrix);
		
		return communicationMatrix;
	}
	
	private static Double[][] generateComputationMatrixRandom(int taskNumbers, int processorNumbers) {
		
		String computationsFilePath=FILE_PATH+"computations.txt";
		
		Double[][] computationMatrix=new Double[taskNumbers][processorNumbers];
		File computations=new File(computationsFilePath);				
		
		if(computations.exists()&&computations.isFile()) {			
			
			int lineNumber=0;
			String line=null;
			
			try {
				
				InputStreamReader reader=new InputStreamReader(new FileInputStream(computations));
				BufferedReader bufferedReader=new BufferedReader(reader);
				
				while((line=bufferedReader.readLine())!=null) {
					
					lineNumber++;
					
					if(lineNumber>3) {
						
						int i=lineNumber-4; // the real computation value starts from line 4 in computations.txt

						//System.out.println("lineNumber="+lineNumber+": "+line);												

						String[] pairs=line.split("\t");												
																			
						for(int j=0; j<processorNumbers; j++) {
							
							Double computation=Double.valueOf(pairs[j]);
								
							computationMatrix[i][j]=computation;
							
						}																																																			
						
					}
				}
				
				reader.close();
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {				
				e.printStackTrace();
			} finally {				
			}
			
		}
		
		//GenericUtil.printComputationMatrix(computationMatrix);
		
		return computationMatrix;
	}
	
	
	
	public static Double[][] generateComputationMatrix(int taskNumbers, int processorNumbers, int avgComputation, double heteroFactor) {
		
		Double[][] computationMatrix=new Double[taskNumbers][processorNumbers];		
		
		int offset=(int) (avgComputation*heteroFactor);
		
		int upper=avgComputation+offset;
		int lower=avgComputation-offset+1;
		
		for(int i=0;i<taskNumbers;i++) {
			for(int j=0;j<processorNumbers;j++) {
				Integer v=lower+(new Random()).nextInt(upper-lower);
				Double cost=new Double(v.toString());
				computationMatrix[i][j]=cost;				
			}
		}
		
		return computationMatrix;
	}		

}
