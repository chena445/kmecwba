package cn.edu.hnu.esnl.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.edu.hnu.esnl.bean.Application;
import cn.edu.hnu.esnl.bean.Task;
import cn.edu.hnu.esnl.bean.Processor;

/**
 * Level类表示工作流调度中的一个级别
 * 每个级别包含一组无直接依赖关系的任务
 */
public class Level {

    private int levelId;
    private List<Task> tasks;
    private double levelBudget;
    private double levelConsumed;
    private double levelCompletionTime;

    public Level(int levelId) {
        this.levelId = levelId;
        this.tasks = new ArrayList<>();
        this.levelBudget = 0d;
        this.levelConsumed = 0d;
        this.levelCompletionTime = 0d;
    }

    public int getLevelId() {
        return levelId;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public double getLevelBudget() {
        return levelBudget;
    }

    public void setLevelBudget(double levelBudget) {
        this.levelBudget = levelBudget;
    }

    public void consumeBudget(double cost) {
        this.levelConsumed += cost;
        if (this.levelConsumed > levelBudget) {
            this.levelConsumed = levelBudget;
        }
    }

    public double getRemainingBudget() {
        return levelBudget - levelConsumed;
    }

    public double getLevelCompletionTime() {
        return levelCompletionTime;
    }

    /**
     * 根据任务的 lower_bound_ft  计算当前层完成时间
     */
    public void calculateLevelCompletionTime(Application app, List<Task> scheduledTasks) {
        double maxFT = 0d;
        for (Task task : tasks) {
            if (task.getLower_bound_ft() != null && task.getLower_bound_ft() > maxFT) {
                maxFT = task.getLower_bound_ft();
            }
        }
        this.levelCompletionTime = maxFT;
    }

    /**
     * 静态方法：根据任务依赖构建层级
     * @param app Application对象
     * @return 层级列表
     */
    public static List<Level> buildTaskLevels(Application app) {
        List<Level> levels = new ArrayList<>();
        List<Task> allTasks = app.getScheduledSequence(); // 拓扑排序任务
        Set<Task> assignedTasks = new HashSet<>();
        int levelId = 0;

        while (assignedTasks.size() < allTasks.size()) {
            Level currentLevel = new Level(levelId);
            for (Task task : allTasks) {
                if (assignedTasks.contains(task)) continue;

                boolean ready = true;
                // 判断前驱任务是否都已分配
                for (Task pred : task.getPredTask$CommCostMap().keySet()) {
                    if (task.getPredTask$CommCostMap().get(pred) != 0 && !assignedTasks.contains(pred)) {
                        ready = false;
                        break;
                    }
                }

                if (ready) {
                    currentLevel.addTask(task);
                }
            }

            // 当前层任务标记为已分配
            assignedTasks.addAll(currentLevel.getTasks());
            levels.add(currentLevel);
            levelId++;
        }

        return levels;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Level ").append(levelId).append(": ");
        for (Task t : tasks) {
            sb.append(t.getName()).append(" ");
        }
        return sb.toString();
    }
}
