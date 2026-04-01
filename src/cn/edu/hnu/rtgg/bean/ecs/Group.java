package cn.edu.hnu.rtgg.bean.ecs;

import java.util.LinkedHashSet;
import java.util.LinkedList;

public class Group {

	private Integer groupdID;
	private LinkedHashSet<RTGGTask> tasksSet = new LinkedHashSet<RTGGTask>();
	private LinkedList<RTGGTask> tasksList=new LinkedList<RTGGTask>();

	public Integer getGroupdID() {
		return groupdID;
	}

	public void setGroupdID(Integer groupdID) {
		this.groupdID = groupdID;
	}

	public LinkedHashSet<RTGGTask> getTasksSet() {
		return tasksSet;
	}

	public void setTasksSet(LinkedHashSet<RTGGTask> tasksSet) {
		this.tasksSet = tasksSet;
	}

	public LinkedList<RTGGTask> getTasksList() {
		return tasksList;
	}

	public void setTasksList(LinkedList<RTGGTask> tasksList) {
		this.tasksList = tasksList;
	}

	public Group(Integer groupdID) {
		this.groupdID = groupdID;
	}

}
