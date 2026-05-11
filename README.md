# Budget-Constrained Workflow Scheduling Approaches for Reducing Energy Consumption in Cloud Environments

## 项目描述

本项目提出并实现了一种预算约束的工作流调度方法，旨在减少云计算环境中的能源消耗。通过多目标优化算法，实现工作流任务的高效调度，同时控制预算和能源使用。

项目实现了两种主要算法：

- **MECWBA**：多目标节能工作流调度算法
- **K-MECWBA**：基于K-means聚类的多目标节能工作流调度算法

## 主要功能

- 实现MECWBA和K-MECWBA算法
- 提供多种对比算法：BDCE, BDAS, DERG, NDERG, REWS
- 支持敏感性分析（sum_w），使用$w_i$作为预算分配依据
- 集成K-means聚类用于任务分组
- 支持多种工作流生成器：FFT, LA, GE等

## 对比算法

项目包含以下对比算法的实现：

- BDCE.java
- MECWS_BDAS.java
- MECWS_DERG.java
- MECWS_NDERG.java
- MECWS_REWS.java

## 文件结构

```
KMECEBA代码/
├── README.md                    # 项目说明
├── args.txt                     # 参数文件
├── communications.txt           # 通信数据
├── computation.txt              # 计算数据
├── graph.txt                    # 图数据
├── 算法/                        # 主要算法实现
│   ├── KMECWBA.java
│   └── MECWBA.java
├── 对比算法和论文/              # 对比算法和相关论文
│   ├── 对比算法/
│   │   ├── BDCE.java
│   │   ├── MECWS_BDAS.java
│   │   ├── MECWS_DERG.java
│   │   ├── MECWS_NDERG.java
│   │   └── MECWS_REWS.java
├── src/                         # 源代码
│   ├── cn/edu/hnu/esnl/
│   │   ├── app/yuekunchen/      # 应用层
│   │   │   └── cost/
│   │   │       └── access/
│   │   │           ├── alg/     # 算法存放位置
│   │   │           │   ├── KMECWBA.java
│   │   │           │   ├── MECWBA.java
│   │   │           │   ├── BDCE.java
│   │   │           │   ├── MECWS_BDAS.java
│   │   │           │   ├── MECWS_DERG.java
│   │   │           │   ├── MECWS_NDERG.java
│   │   │           │   ├── MECWS_REWS.java
│   │   │           │   ├── MECABP.java
│   │   │           │   ├── MECBL.java
│   │   │           │   ├── EHBCS.java
│   │   │           │   ├── RECFPAB2.java
│   │   │           │   ├── DYNAMIC12.java
│   │   │           │   ├── MECWS_AET_II_k.java
│   │   │           │   ├── alg2.java
│   │   │           │   ├── sum_w.java
│   │   │           │   └── bin/
│   │   │           ├── example/
│   │   │           └── exp/
│   │   ├── bean/                # 数据模型
│   │   │   ├── Application.java
│   │   │   ├── Task.java
│   │   │   ├── Processor.java
│   │   │   └── ...
│   │   ├── realgraph/           # 工作流生成器
│   │   │   ├── FFTGenrattor.java
│   │   │   ├── LAGenrattor.java
│   │   │   └── GEGenrattor.java
│   │   ├── schedule/            # 调度器
│   │   │   ├── scheduler/       # 各种调度算法
│   │   │   └── assitant/
│   │   ├── service/             # 服务层
│   │   └── system/              # 系统层
│   └── k_means/                 # K-means聚类实现
│       ├── KMeansRun.java
│       ├── Cluster.java
│       └── Point.java
├── test/                        # 测试代码
│   └── Test1.java
└── WebRoot/                     # Web应用和示例
    ├── 固定处理器大例子.java
    ├── 固定处理器小例子.java
    ├── MEMC_EXample.java
    └── MMEC_EXample.java
```

### K-MECWBA聚类配置

在K-MECWBA算法中，可以修改聚类数量：

```java
int task_number = g0.getScheduledSequence().size();
int cluster_number = (task_number + 12 - 1) / 12;  // 向上取整，12个任务为一个cluster
KMeansRun kRun = new KMeansRun(cluster_number, taskList);
Set<Cluster> clusterSet = kRun.run();
```

## 实验设置与编译运行

### 实验设置

- **工作流生成器**：FFT, LA, GE
- **实验数据位置**：`src\cn\edu\hnu\esnl\app\yuekunchen\cost\access\exp\bin`
- **实验文件**：EX2_GE.java      EX3_FFT.java     EX4_LA.java
- **测试文件**：example / MotivationExample（10个任务流）

### 运行步骤

1. 确保Java环境已安装（JDK 8+）。
2. 编译项目代码：

   ```bash
   javac -cp . src/**/*.java
   ```
3. 运行实验文件，例如：

   ```bash
   java -cp . cn.edu.hnu.esnl.app.yuekunchen.cost.access.EX2_GE
   ```

   （根据实际包路径调整）

### 实验结果

以下是基于Gaussian elimination应用（902个任务）的实验输出示例，展示了各算法的成本（cost）、能源消耗（energy）、调度长度（schedule length）、运行时间和内存开销。

#### 终端输出

```
rho=42 The task number of the Gaussian elimination application is 902
4791.0
appMinCost:1328.9320000000023  appMaxCost:117866.08976650565 appCostBudget:1594.7184000000027
 ratio:1.2processor_number=20
=====================================================================================================================================================================================================

MECWS_REWS 's  cost:1526.0023
MECWS_REWS 's  energy:7204.823
MECWS_REWS 's  schedule length:6518.88

MECWS_REWS's程序运行时间：342 ms
taskTime=58378.999999999985
MECWS_DERG's  cost:1594.7123
MECWS_DERG's  energy:5565.0293
MECWS_DERG's  schedule length:6370.56

MECWS_DERG's程序运行时间：410 ms
 MECWS_NDERGI's  cost:1594.7172
 MECWS_NDERGs  energy:6903.7683
MECWS_NDERG's  schedule length:6478.1

MECWS_NDERG's程序运行时间：318 ms
MECWS_BDAS 's  cost:2541.8397
MECWS_BDAS 's  energy:5818.0578
MECWS_BDAS 's  schedule length:6890.51

MECWS_BDAS's程序运行时间：358 ms
BDCE's  cost:1385.9657
BDCE's  energy:7444.1247
BDCE's  schedule length:6501.72

BDCE's程序运行时间：309 ms
以上是对比算法的实验结果--------------------------------------------------------------
 MECWBA's  cost:1594.7102
 MECWBA's  energy:5494.1681
 MECWBA's  schedule length:6424.07
MECWBA's程序运行时间：378 ms
MECWBA's程序内存开销：50635 KB
 KMECWBA's  cost:1593.8449
 KMECWBA's  energy:5290.3316
 KMECWBA's  schedule length:6427.62

KMECWBA's程序运行时间：420 ms
KMECWBA's程序内存开销：67116 KB
```

#### 对比算法结果

- **MECWS_REWS**：成本 1526.0023，能源 7204.823，调度长度 6518.88，运行时间 342 ms
- **MECWS_DERG**：成本 1594.7123，能源 5565.0293，调度长度 6370.56，运行时间 410 ms
- **MECWS_NDERG**：成本 1594.7172，能源 6903.7683，调度长度 6478.1，运行时间 318 ms
- **MECWS_BDAS**：成本 2541.8397，能源 5818.0578，调度长度 6890.51，运行时间 358 ms
- **BDCE**：成本 1385.9657，能源 7444.1247，调度长度 6501.72，运行时间 309 ms

#### 主要算法结果

- **MECWBA**：成本 1594.7102，能源 5494.1681，调度长度 6424.07，运行时间 378 ms，内存开销 50635 KB
- **KMECWBA**：成本 1593.8449，能源 5290.3316，调度长度 6427.62，运行时间 420 ms，内存开销 67116 KB

实验参数：

- rho=42
- appMinCost: 1328.932，appMaxCost: 117866.09，appCostBudget: 1594.718
- ratio: 1.2，processor_number=20
- taskTime=58379.0
