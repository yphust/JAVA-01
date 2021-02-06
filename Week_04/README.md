## 第四周

### 思考有多少种方式，在main函数启动一个新线程或线程池， 异步运行一个方法，拿到这个方法的返回值后，退出主线程.

1. [通过主线程睡眠等待子线程执行完毕的方式获取](https://github.com/yphust/JAVA-01/blob/main/Week_04/src/main/java/Demo1.java)

2. [使用Join等待的方法进行获取](https://github.com/yphust/JAVA-01/blob/main/Week_04/src/main/java/Demo2.java)
3. [使用死循环的方式进行获取](https://github.com/yphust/JAVA-01/blob/main/Week_04/src/main/java/Demo3.java)
4. [使用synchronized加wait的方式获取](https://github.com/yphust/JAVA-01/blob/main/Week_04/src/main/java/Demo4.java)
5. [使用countDownLatch获取](https://github.com/yphust/JAVA-01/blob/main/Week_04/src/main/java/Demo5.java)
6. [使用FutureTask进行获取](https://github.com/yphust/JAVA-01/blob/main/Week_04/src/main/java/Demo6.java)

肯定远不止这些方法，而且里面有些方法（比如死循环的方式、sleep的方式）有明显的缺陷，sleep的睡眠时间设置就是一个问题。但是暂时能在脑中想到的只有这些方式，说明其他的类跟方法都还只停留在笔记中，得真正花时间去理解。

### 把多线程和并发相关知识梳理一遍，画一个脑图，截图上传到 GitHub 上。 可选工具:xmind，百度脑图，wps，MindManage，或其他。

[脑图](https://github.com/yphust/JAVA-01/blob/main/Week_04/多线程并发编程.svg)