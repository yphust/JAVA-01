[TOC]

### 周三作业

### 1.使用GCLogAnalysis.java自己演练一遍串行/并行/CMS/G1的案例。

```java
package Week_02;


import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
/*
演示GC日志生成与解读
*/
public class GCLogAnalysis {
    private static Random random = new Random();
    public static void main(String[] args) {
        // 当前毫秒时间戳
        long startMillis = System.currentTimeMillis();
        // 持续运行毫秒数; 可根据需要进行修改
        long timeoutMillis = TimeUnit.SECONDS.toMillis(1);
        // 结束时间戳
        long endMillis = startMillis + timeoutMillis;
        LongAdder counter = new LongAdder();
        System.out.println("正在执行...");
        // 缓存一部分对象; 进入老年代
        int cacheSize = 2000;
        Object[] cachedGarbage = new Object[cacheSize];
        // 在此时间范围内,持续循环
        while (System.currentTimeMillis() < endMillis) {
            // 生成垃圾对象
            Object garbage = generateGarbage(100*1024);
            counter.increment();
            int randomIndex = random.nextInt(2 * cacheSize);
            if (randomIndex < cacheSize) {
                cachedGarbage[randomIndex] = garbage;
            }
        }
        System.out.println("执行结束!共生成对象次数:" + counter.longValue());
    }

    // 生成对象
    private static Object generateGarbage(int max) {
        int randomSize = random.nextInt(max);
        int type = randomSize % 4;
        Object result = null;
        switch (type) {
            case 0:
                result = new int[randomSize];
                break;
            case 1:
                result = new byte[randomSize];
                break;
            case 2:
                result = new double[randomSize];
                break;
            default:
                StringBuilder builder = new StringBuilder();
                String randomString = "randomString-Anything";
                while (builder.length() < randomSize) {
                    builder.append(randomString);
                    builder.append(max);
                    builder.append(randomSize);
                }
                result = builder.toString();
                break;
        }
        return result;
    }
}
```

#### 使用串行GC

##### 128m

> java -Xmx128m -Xms128m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_sefial_128m.log  -XX:+UseSerialGC Week_02/GCLogAnalysis

结果：

Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at Week_02.GCLogAnalysis.generateGarbage(GCLogAnalysis.java:44)
	at Week_02.GCLogAnalysis.main(GCLogAnalysis.java:27)

##### 256m

> java -Xmx256m -Xms256m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_sefial_256m.log  -XX:+UseSerialGC Week_02/GCLogAnalysis

结果：执行结束!共生成对象次数:4371

##### 512m

> java -Xmx512m -Xms512m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_sefial_512m.log  -XX:+UseSerialGC Week_02/GCLogAnalysis

结果：执行结束!共生成对象次数:7787

##### 1g

> java -Xmx1g -Xms1g -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_sefial_1g.log  -XX:+UseSerialGC Week_02/GCLogAnalysis

结果：执行结束!共生成对象次数:7628

##### 2g

> java -Xmx2g -Xms2g -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_sefial_2g.log  -XX:+UseSerialGC Week_02/GCLogAnalysis

结果:执行结束!共生成对象次数:8335

##### 4g

> java -Xmx4g -Xms4g -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_sefial_4g.log  -XX:+UseSerialGC Week_02/GCLogAnalysis

结果：执行结束!共生成对象次数:4196



#### 使用并行GC

##### 128m

> java -Xmx128m -Xms128m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_parallel_128m.log  -XX:+UseParallelGC Week_02/GCLogAnalysis

结果：

Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at java.util.Arrays.copyOf(Arrays.java:3332)
	at java.lang.AbstractStringBuilder.ensureCapacityInternal(AbstractStringBuilder.java:124)
	at java.lang.AbstractStringBuilder.append(AbstractStringBuilder.java:674)
	at java.lang.StringBuilder.append(StringBuilder.java:208)
	at Week_02.GCLogAnalysis.generateGarbage(GCLogAnalysis.java:58)
	at Week_02.GCLogAnalysis.main(GCLogAnalysis.java:27)

##### 256m

> java -Xmx256m -Xms256m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_parallel_256m.log  -XX:+UseParallelGC Week_02/GCLogAnalysis

结果：

Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at Week_02.GCLogAnalysis.generateGarbage(GCLogAnalysis.java:50)
	at Week_02.GCLogAnalysis.main(GCLogAnalysis.java:27)

##### 512m

> java -Xmx512m -Xms512m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_parallel_512m.log  -XX:+UseParallelGC Week_02/GCLogAnalysis

结果：执行结束!共生成对象次数:6876

##### 1g

> java -Xmx1g -Xms1g -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_parallel_1g.log  -XX:+UseParallelGC Week_02/GCLogAnalysis

结果：执行结束!共生成对象次数:8859

##### 2g

> java -Xmx2g -Xms2g -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_parallel_2g.log  -XX:+UseParallelGC Week_02/GCLogAnalysis

结果：执行结束!共生成对象次数:9843

##### 4g

> java -Xmx4g -Xms4g -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_parallel_4g.log  -XX:+UseParallelGC Week_02/GCLogAnalysis

结果：执行结束!共生成对象次数:7934

#### 使用 CMS  GC

##### 128m

> java -Xmx128m -Xms128m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_cms_128m.log  -XX:+UseConcMarkSweepGC Week_02/GCLogAnalysis

结果：

Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at Week_02.GCLogAnalysis.generateGarbage(GCLogAnalysis.java:50)
	at Week_02.GCLogAnalysis.main(GCLogAnalysis.java:27)

##### 256m

> java -Xmx256m -Xms256m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_cms_256m.log  -XX:+UseConcMarkSweepGC Week_02/GCLogAnalysis

结果：执行结束!共生成对象次数:4177

##### 512m

> java -Xmx512m -Xms512m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_cms_512m.log  -XX:+UseConcMarkSweepGC Week_02/GCLogAnalysis

结果：执行结束!共生成对象次数:8892

##### 1g

> java -Xmx1g -Xms1g -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_cms_1g.log  -XX:+UseConcMarkSweepGC Week_02/GCLogAnalysis

结果:执行结束!共生成对象次数:9199

##### 2g

> java -Xmx2g -Xms2g -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_cms_2g.log  -XX:+UseConcMarkSweepGC Week_02/GCLogAnalysis

结果：执行结束!共生成对象次数:9184

##### 4g

> java -Xmx4g -Xms4g -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_cms_4g.log  -XX:+UseConcMarkSweepGC Week_02/GCLogAnalysis

结果：执行结束!共生成对象次数:8307

#### 使用G1 GC

##### 128m

> java -Xmx128m -Xms128m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_g1_128m.log  -XX:+UseG1GC Week_02/GCLogAnalysis

结果：

Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at java.util.Arrays.copyOf(Arrays.java:3332)
	at java.lang.AbstractStringBuilder.ensureCapacityInternal(AbstractStringBuilder.java:124)
	at java.lang.AbstractStringBuilder.append(AbstractStringBuilder.java:674)
	at java.lang.StringBuilder.append(StringBuilder.java:208)
	at Week_02.GCLogAnalysis.generateGarbage(GCLogAnalysis.java:58)
	at Week_02.GCLogAnalysis.main(GCLogAnalysis.java:27)

##### 256m

> java -Xmx256m -Xms256m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_g1_256m.log  -XX:+UseG1GC Week_02/GCLogAnalysis

结果：

Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at Week_02.GCLogAnalysis.generateGarbage(GCLogAnalysis.java:50)
	at Week_02.GCLogAnalysis.main(GCLogAnalysis.java:27)

##### 512m

> java -Xmx512m -Xms512m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_g1_512m.log  -XX:+UseG1GC Week_02/GCLogAnalysis

结果：执行结束!共生成对象次数:8515

##### 1g

> java -Xmx1g -Xms1g -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_g1_1g.log  -XX:+UseG1GC Week_02/GCLogAnalysis

结果:执行结束!共生成对象次数:8922

##### 2g

> java -Xmx2g -Xms2g -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_g1_2g.log  -XX:+UseG1GC Week_02/GCLogAnalysis

结果：执行结束!共生成对象次数:7555

##### 4g

> java -Xmx4g -Xms4g -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:log/gc_g1_4g.log  -XX:+UseG1GC Week_02/GCLogAnalysis

结果：执行结束!共生成对象次数:9645



### Item2快捷键

新建标签：command + t
关闭标签：command + w
切换标签：command + 数字 command + 左右方向键
切换全屏：command + enter
查找：command + f
垂直分屏：command + d
水平分屏：command + shift + d
切换屏幕：command + option + 方向键 command + [ 或 command + ]
查看历史命令：command + ;
查看剪贴板历史：command + shift + h
清除当前行：ctrl + u
到行首：ctrl + a
到行尾：ctrl + e
前进后退：ctrl + f/b (相当于左右方向键)
上一条命令：ctrl + p
搜索命令历史：ctrl + r
删除当前光标的字符：ctrl + d
删除光标之前的字符：ctrl + h
删除光标之前的单词：ctrl + w
删除到文本末尾：ctrl + k
交换光标处文本：ctrl + t
清屏1：command + r
清屏2：ctrl + l

⌘ + 数字在各 tab 标签直接来回切换

### Mac下端口被占用的解决方式

查看端口号的进城情况:   sudo lsof -i tcp:port

   port : 你所被占用的端口号  例如8080

kill掉被占用的端口的进程: kill PID

   每一个进程都会有一个PID

### 2.使用压测工具（wrk或superbenchmark）演练gateway-server-0.0.1-SNAPSHOT.jar示例

#### 使用串行GC

> java -Xmx1g -Xms1g -XX:-UseAdaptiveSizePolicy -XX:+UseSerialGC -jar gateway-server-0.0.1-SNAPSHOT.jar

压测结果：

 yphust@yphustdeMacBook-Pro  ~  wrk  -c10 -t5  -d60s http://localhost:8088/api/hello
Running 1m test @ http://localhost:8088/api/hello
  5 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     6.89ms   26.24ms 484.71ms   94.17%
    Req/Sec     2.46k     1.17k    5.40k    64.30%
  728008 requests in 1.00m, 86.92MB read
Requests/sec:  12118.94
Transfer/sec:      1.45MB

#### 使用并行GC

> java -Xmx1g -Xms1g -XX:-UseAdaptiveSizePolicy -XX:+UseParallelGC -jar gateway-server-0.0.1-SNAPSHOT.jar

压测结果：

yphust@yphustdeMacBook-Pro  ~  wrk  -c10 -t5  -d60s http://localhost:8088/api/hello
Running 1m test @ http://localhost:8088/api/hello
  5 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    10.97ms   38.10ms 639.72ms   93.27%
    Req/Sec     1.89k     0.92k    4.81k    67.87%
  558750 requests in 1.00m, 66.71MB read
Requests/sec:   9297.35
Transfer/sec:      1.11MB

#### 使用CMS GC

> java -Xmx1g -Xms1g -XX:-UseAdaptiveSizePolicy -XX:+UseConcMarkSweepGC -jar gateway-server-0.0.1-SNAPSHOT.jar

压测结果：

 yphust@yphustdeMacBook-Pro  ~  wrk  -c10 -t5  -d60s http://localhost:8088/api/hello
Running 1m test @ http://localhost:8088/api/hello
  5 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     7.97ms   24.82ms 394.12ms   92.30%
    Req/Sec     2.35k     1.10k    5.15k    67.32%
  696061 requests in 1.00m, 83.10MB read
Requests/sec:  11588.36
Transfer/sec:      1.38MB

#### 使用G1 GC

> java -Xmx1g -Xms1g -XX:-UseAdaptiveSizePolicy -XX:+UseG1GC -jar gateway-server-0.0.1-SNAPSHOT.jar

 yphust@yphustdeMacBook-Pro  ~  wrk  -c10 -t5  -d60s http://localhost:8088/api/hello
Running 1m test @ http://localhost:8088/api/hello
  5 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     7.19ms   19.69ms 292.38ms   91.28%
    Req/Sec     2.17k     1.06k    5.13k    66.39%
  644982 requests in 1.00m, 77.00MB read
Requests/sec:  10736.64
Transfer/sec:      1.28MB

### 3.（必做）根据上述自己对于1和2的演示，写一段对于不同GC的总结，提交到GitHub

#### 7种JVM垃圾收集器比较

#### 一、常见垃圾收集器

**现在常见的垃圾收集器有如下几种：**

**新生代收集器：**

- Serial
- ParNew
- Parallel Scavenge

**老年代收集器：**

- Serial Old
- CMS
- Parallel Old

**堆内存垃圾收集器：**G1

每种垃圾收集器之间有连线，表示他们可以搭配使用。

[![sIljfA.png](https://s3.ax1x.com/2021/01/22/sIljfA.png)](https://imgchr.com/i/sIljfA)

#### 二、新生代垃圾收集器

**（1）Serial 收集器**

Serial 是一款用于新生代的单线程收集器，采用复制算法进行垃圾收集。Serial 进行垃圾收集时，不仅只用一条线程执行垃圾收集工作，它在收集的同时，所有的用户线程必须暂停（Stop The World）。

如下是 Serial 收集器和 Serial Old 收集器结合进行垃圾收集的示意图，当用户线程都执行到安全点时，所有线程暂停执行，Serial 收集器以单线程，采用复制算法进行垃圾收集工作，收集完之后，用户线程继续开始执行。

[![sIr780.png](https://s3.ax1x.com/2021/01/22/sIr780.png)](https://imgchr.com/i/sIr780)

**适用场景**：Client 模式（桌面应用）；单核服务器。

可以用 -XX:+UserSerialGC 来选择 Serial 作为新生代收集器。

**（2）ParNew 收集器**

ParNew 就是一个 Serial 的多线程版本，其它与Serial并无区别。ParNew 在单核 CPU 环境并不会比 Serial  收集器达到更好的效果，它默认开启的收集线程数和 CPU 数量一致，可以通过 -XX:ParallelGCThreads 来设置垃圾收集的线程数。

如下是 ParNew 收集器和 Serial Old 收集器结合进行垃圾收集的示意图，当用户线程都执行到安全点时，所有线程暂停执行，ParNew 收集器以多线程，采用复制算法进行垃圾收集工作，收集完之后，用户线程继续开始执行。

[![sIs0MT.png](https://s3.ax1x.com/2021/01/22/sIs0MT.png)](https://imgchr.com/i/sIs0MT)

**适用场景：**多核服务器；与 CMS 收集器搭配使用。当使用  -XX:+UserConcMarkSweepGC 来选择 CMS 作为老年代收集器时，新生代收集器默认就是 ParNew，也可以用  -XX:+UseParNewGC 来指定使用 ParNew 作为新生代收集器。

**（3）Parallel Scavenge 收集器**

Parallel Scavenge 也是一款用于新生代的多线程收集器，与 ParNew 的不同之处是ParNew 的目标是尽可能缩短垃圾收集时用户线程的停顿时间，Parallel Scavenge 的目标是达到一个可控制的吞吐量。

吞吐量就是 CPU 执行用户线程的的时间与 CPU 执行总时间的比值【吞吐量 =  运行用户代代码时间/（运行用户代码时间+垃圾收集时间）】，比如虚拟机一共运行了 100 分钟，其中垃圾收集花费了 1 分钟，那吞吐量就是 99%  。比如下面两个场景，垃圾收集器每 100 秒收集一次，每次停顿 10 秒，和垃圾收集器每 50 秒收集一次，每次停顿时间 7  秒，虽然后者每次停顿时间变短了，但是总体吞吐量变低了，CPU 总体利用率变低了。

| 收集频率       | 每次停顿时间 | 吞吐量 |
| -------------- | ------------ | ------ |
| 每100s收集一次 | 10s          | 91%    |
| 每50s收集一次  | 7s           | 88%    |

可以通过 -XX:MaxGCPauseMillis 来设置收集器尽可能在多长时间内完成内存回收，可以通过 -XX:GCTimeRatio 来精确控制吞吐量。

如下是 Parallel 收集器和 Parallel Old  收集器结合进行垃圾收集的示意图，在新生代，当用户线程都执行到安全点时，所有线程暂停执行，ParNew  收集器以多线程，采用复制算法进行垃圾收集工作，收集完之后，用户线程继续开始执行；在老年代，当用户线程都执行到安全点时，所有线程暂停执行，Parallel  Old 收集器以多线程，采用标记整理算法进行垃圾收集工作。

[![sIsWz6.png](https://s3.ax1x.com/2021/01/22/sIsWz6.png)](https://imgchr.com/i/sIsWz6)

**适用场景：**注重吞吐量，高效利用 CPU，需要高效运算且不需要太多交互。

可以使用 -XX:+UseParallelGC 来选择 Parallel Scavenge 作为新生代收集器，jdk7、jdk8 默认使用 Parallel Scavenge 作为新生代收集器。

#### 三、老年代垃圾收集器

**（1）Serial Old 收集器**

Serial Old 收集器是 Serial 的老年代版本，同样是一个单线程收集器，采用标记-整理算法。

如下图是 Serial 收集器和 Serial Old 收集器结合进行垃圾收集的示意图：

[![sIsxOS.png](https://s3.ax1x.com/2021/01/22/sIsxOS.png)](https://imgchr.com/i/sIsxOS)

**适用场景：**Client 模式（桌面应用）；单核服务器；与 Parallel Scavenge 收集器搭配；作为 CMS 收集器的后备预案。

**（2）CMS(Concurrent Mark Sweep) 收集器**

CMS 收集器是一种以最短回收停顿时间为目标的收集器，以 “ 最短用户线程停顿时间 ” 著称。整个垃圾收集过程分为 4 个步骤：

**① 初始标记：**标记一下 GC Roots 能直接关联到的对象，速度较快。

**② 并发标记：**进行 GC Roots Tracing，标记出全部的垃圾对象，耗时较长。

**③ 重新标记：**修正并发标记阶段引用户程序继续运行而导致变化的对象的标记记录，耗时较短。

**④ 并发清除：**用标记-清除算法清除垃圾对象，耗时较长。

整个过程耗时最长的并发标记和并发清除都是和用户线程一起工作，所以从总体上来说，CMS 收集器垃圾收集可以看做是和用户线程并发执行的。

[![sIy3Sx.png](https://s3.ax1x.com/2021/01/22/sIy3Sx.png)](https://imgchr.com/i/sIy3Sx)

**CMS 收集器也存在一些缺点：**

对 CPU 资源敏感：默认分配的垃圾收集线程数为（CPU 数+3）/4，随着 CPU 数量下降，占用 CPU 资源越多，吞吐量越小

无法处理浮动垃圾：在并发清理阶段，由于用户线程还在运行，还会不断产生新的垃圾，CMS  收集器无法在当次收集中清除这部分垃圾。同时由于在垃圾收集阶段用户线程也在并发执行，CMS  收集器不能像其他收集器那样等老年代被填满时再进行收集，需要预留一部分空间提供用户线程运行使用。当 CMS  运行时，预留的内存空间无法满足用户线程的需要，就会出现 “ Concurrent Mode Failure  ”的错误，这时将会启动后备预案，临时用 Serial Old 来重新进行老年代的垃圾收集。

因为 CMS 是基于标记-清除算法，所以垃圾回收后会产生空间碎片，可以通过  -XX:UserCMSCompactAtFullCollection 开启碎片整理（默认开启），在 CMS 进行 Full GC  之前，会进行内存碎片的整理。还可以用 -XX:CMSFullGCsBeforeCompaction 设置执行多少次不压缩（不进行碎片整理）的  Full GC 之后，跟着来一次带压缩（碎片整理）的 Full GC。

**适用场景：**重视服务器响应速度，要求系统停顿时间最短。可以使用 -XX:+UserConMarkSweepGC 来选择 CMS 作为老年代收集器。

**（3）Parallel Old 收集器**

Parallel Old 收集器是 Parallel Scavenge 的老年代版本，是一个多线程收集器，采用标记-整理算法。可以与 Parallel Scavenge 收集器搭配，可以充分利用多核 CPU 的计算能力。

[![sIyfts.png](https://s3.ax1x.com/2021/01/22/sIyfts.png)](https://imgchr.com/i/sIyfts)

**适用场景：**与Parallel Scavenge 收集器搭配使用；注重吞吐量。jdk7、jdk8 默认使用该收集器作为老年代收集器，使用 -XX:+UseParallelOldGC 来指定使用 Paralle Old 收集器。

#### 四、新生代和老年代垃圾收集器

**G1 收集器**

G1 收集器是 jdk1.7 才正式引用的商用收集器，现在已经成为 jdk9  默认的收集器。前面几款收集器收集的范围都是新生代或者老年代，G1 进行垃圾收集的范围是整个堆内存，它采用 “ 化整为零 ”  的思路，把整个堆内存划分为多个大小相等的独立区域（Region），在 G1 收集器中还保留着新生代和老年代的概念，它们分别都是一部分  Region，如下图：

[![sIyTXT.png](https://s3.ax1x.com/2021/01/22/sIyTXT.png)](https://imgchr.com/i/sIyTXT)

每一个方块就是一个区域，每个区域可能是 Eden、Survivor、老年代，每种区域的数量也不一定。JVM  启动时会自动设置每个区域的大小（1M ~ 32M，必须是 2 的次幂），最多可以设置 2048 个区域（即支持的最大堆内存为 32M*2048 =  64G），假如设置 -Xmx8g -Xms8g，则每个区域大小为 8g/2048=4M。

为了在 GC Roots Tracing 的时候避免扫描全堆，在每个 Region 中，都有一个 Remembered Set  来实时记录该区域内的引用类型数据与其他区域数据的引用关系（在前面的几款分代收集中，新生代、老年代中也有一个 Remembered Set  来实时记录与其他区域的引用关系），在标记时直接参考这些引用关系就可以知道这些对象是否应该被清除，而不用扫描全堆的数据。

G1 收集器可以 “ 建立可预测的停顿时间模型 ”，它维护了一个列表用于记录每个 Region 回收的价值大小（回收后获得的空间大小以及回收所需时间的经验值），这样可以保证 G1 收集器在有限的时间内可以获得最大的回收效率。

**如下图所示，G1 收集器收集器收集过程有初始标记、并发标记、最终标记、筛选回收，和 CMS 收集器前几步的收集过程很相似：**

[![sIyXN9.png](https://s3.ax1x.com/2021/01/22/sIyXN9.png)](https://imgchr.com/i/sIyXN9)

**① 初始标记：**标记出 GC Roots 直接关联的对象，这个阶段速度较快，需要停止用户线程，单线程执行。

**② 并发标记：**从 GC Root 开始对堆中的对象进行可达新分析，找出存活对象，这个阶段耗时较长，但可以和用户线程并发执行。

**③ 最终标记：**修正在并发标记阶段引用户程序执行而产生变动的标记记录。

**④ 筛选回收：**筛选回收阶段会对各个 Region 的回收价值和成本进行排序，根据用户所期望的 GC  停顿时间来指定回收计划（用最少的时间来回收包含垃圾最多的区域，这就是 Garbage First  的由来——第一时间清理垃圾最多的区块），这里为了提高回收效率，并没有采用和用户线程并发执行的方式，而是停顿用户线程。

**适用场景：**要求尽可能可控 GC 停顿时间；内存占用较大的应用。可以用 -XX:+UseG1GC 使用 G1 收集器，jdk9 默认使用 G1 收集器。

#### 五、JVM垃圾收集器总结

衡量垃圾收集器的三项最重要的指标是：内存占用（Footprint）、吞吐量（Throughput）和延迟（Latency），三者共同构成了一个“不可能三角[1]”。三者总体的表现会随技术进步而越来越好，但是要在这三个方面同时具有卓越表现的“完美”收集器是极其困难甚至是不可能的，一款优秀的收集器通常最多可以同时达成其中的两项。

在内存占用、吞吐量和延迟这三项指标里，延迟的重要性日益凸显，越发备受关注。其原因是随着计算机硬件的发展、性能的提升，我们越来越能容忍收集器多占用一点点内存；硬件性能增长，对软件系统的处理能力是有直接助益的，硬件的规格和性能越高，也有助于降低收集器运行时对应用程序的影响，换句话说，吞吐量会更高。但对延迟则不是这样，硬件规格提升，准确地说是内存的扩大，对延迟反而会带来负面的效果，这点也是很符合直观思维的：虚拟机要回收完整的1TB的堆内存，毫无疑问要比回收1GB的堆内存耗费更多时间。由此，我们就不难理解为何延迟会成为垃圾收集器最被重视的性能指标了。

在CMS和G1之前的全部收集器，其工作的所有步骤都会产生“Stop The World”式的停顿；CMS和G1分别使用增量更新和原始快照技术，实现了标记阶段的并发，不会因管理的堆内存变大，要标记的对象变多而导致停顿时间随之增长。但是对于标记阶段之后的处理，仍未得到妥善解决。CMS使用标记-清除算法，虽然避免了整理阶段收集器带来的停顿，但是清除算法不论如何优化改进，在设计原理上避免不了空间碎片的产生，随着空间碎片不断淤积最终依然逃不过“Stop TheWorld”的命运。G1虽然可以按更小的粒度进行回收，从而抑制整理阶段出现时间过长的停顿，但毕竟也还是要暂停的。

本文主要介绍了JVM中的垃圾回收器，主要包括串行回收器、并行回收器以及CMS回收器、G1回收器。他们各自都有优缺点，通常来说你需要根据你的业务，进行基于垃圾回收器的性能测试，然后再做选择。下面给出配置回收器时，经常使用的参数：

> -XX:+UseSerialGC：在新生代和老年代使用串行收集器
>
> -XX:+UseParNewGC：在新生代使用并行收集器
>
> -XX:+UseParallelGC ：新生代使用并行回收收集器，更加关注吞吐量
>
> -XX:+UseParallelOldGC：老年代使用并行回收收集器
>
> -XX:ParallelGCThreads：设置用于垃圾回收的线程数
>
> -XX:+UseConcMarkSweepGC：新生代使用并行收集器，老年代使用CMS+串行收集器
>
> -XX:ParallelCMSThreads：设定CMS的线程数量
>
> -XX:+UseG1GC：启用G1垃圾回收器

### 周日作业

### 1.（选做）运行课上的例子，以及Netty的例子，分析相关现象。

#### 单线程的socket程序

wrk  -c10 -t5  -d60s http://127.0.0.1:8801

 ✘ yphust@yphustdeMacBook-Pro  ~/local  wrk  -c10 -t5  -d60s http://localhost:8801
Running 1m test @ http://localhost:8801
  5 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     4.73ms   21.12ms 870.11ms   99.64%
    Req/Sec    37.51     34.54   343.00     90.21%
  **10403** requests in 1.00m, 1.91MB read
  Socket errors: connect 0, read 78007, write 13, timeout 0
Requests/sec:    173.14
Transfer/sec:     32.56KB

#### 每个请求一个线程

wrk  -c10 -t5  -d60s http://127.0.0.1:8802

yphust@yphustdeMacBook-Pro  ~/local  wrk  -c10 -t5  -d60s http://localhost:8802
Running 1m test @ http://localhost:8802
  5 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.47ms    1.09ms  79.71ms   97.02%
    Req/Sec   168.96     51.13   570.00     73.24%
  **50394** requests in 1.00m, 12.08MB read
  Socket errors: connect 0, read 371867, write 298, timeout 0
Requests/sec:    838.49
Transfer/sec:    205.85KB

#### 创建一个固定大小的线程池处理请求

wrk  -c10 -t5  -d60s http://127.0.0.1:8803

 ✘ yphust@yphustdeMacBook-Pro  ~/local  wrk  -c10 -t5  -d60s http://localhost:8803
Running 1m test @ http://localhost:8803
  5 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.83ms    9.57ms 450.51ms   99.31%
    Req/Sec    44.71     58.53   525.00     91.75%
  **11741** requests in 1.00m, 2.42MB read
  Socket errors: connect 0, read 97948, write 13, timeout 0
Requests/sec:    195.37
Transfer/sec:     41.32KB

#### Netty例子

wrk  -c10 -t5  -d60s --latency http://127.0.0.1:8808/test

 yphust@yphustdeMacBook-Pro  ~/local  wrk  -c10 -t5  -d60s --latency http://127.0.0.1:8808/test
Running 1m test @ http://127.0.0.1:8808/test
  5 threads and 10 connections 5线程10连接
  Thread Stats 线程状态   Avg平均值      Stdev 标准差    Max最大值   +/- Stdev 正负一个标准差占比
    Latency响应时间     9.82ms   24.54ms 250.66ms   89.98%
    Req/Sec每线程每秒完成请求数     6.08k     4.46k   19.50k    67.92%
  Latency Distribution  延迟统计
     50%  129.00us  有50%的请求执行时间是在129微秒内完成
     75%    1.14ms 有75%的请求执行时间是在1.14毫秒内完成
     90%   14.12ms 有90%的请求执行时间是在14.12毫秒内完成
     99%   78.43ms 有99%的请求执行时间是在78.43毫秒内完成
  2473242 requests in 1.00m, 120.29MB read
Requests/sec:  41193.02  每秒请求数（也就是QPS）
Transfer/sec:      2.00MB 每秒钟读取2兆数据量



#### wrk参数释义

-t：需要模拟的线程数

-c：需要模拟的连接数

-d：测试的持续时间

----timeout 或 -T：超时的时间 

--latency：显示延迟统计

-s 或 --script:    lua脚本,使用方法往下看

-H, --header:    添加http header, 比如. "User-Agent: wrk"



### 2.（必做）写一段代码，使用HttpClient或OKHttp访问http://localhost:8801,代码提交到GitHub。

