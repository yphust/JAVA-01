## 1.（必做）整合你上次作业的 httpclient/okhttp

## 2.（必做）实现过滤器

## 3.（选做）实现路由

**作业说明：**

本周工程目录结构如下：

![image-20210131012037019](https://gitee.com/yphust/images/raw/master/image/image-20210131012037019.png)

1. 启动Main.java的main方法，会默认启动NettyHttpServer的main方法，监听地址与端口为：http://127.0.0.1:8808

2. 在HttpHandler类的handlerTest方法中，响应返回的字符串通过MyOKHttlpClient.getClientResponse（String url）实现，即整合了okhttp

3. 传入MyOKHttlpClient.getClientResponse（String url）中的url字符串是通过随机路由实现的。

   三个服务端的URL写死在Constant类的urlsString属性中

   通过下面的请求截图可以看出，每次响应是分发给不同的服务在处理

   ![image-20210131013024703](https://gitee.com/yphust/images/raw/master/image/image-20210131013024703.png)

4. 在请求后端前对请求FullHttpRequest进行了简单的过滤处理：在请求头中添加了一个字符串

   通过下面的截图可以看出，在请求头中确实添加了我们自己添加的字符串yp01:yp01

   <span style="color:red">**但是为什么请求一次会输出两次yp01呢？**请大明老师帮忙看下</span>

   ![image-20210131014829117](https://gitee.com/yphust/images/raw/master/image/image-20210131014829117.png)

5. 对响应FullHttpResponse同样做了简单的过滤处理，在返回头中添加了一个字符串

   通过下面的截图可以看到，在返回头中确实有我们自己添加的字符串yp02:yp02

   ![image-20210131014640006](https://gitee.com/yphust/images/raw/master/image/image-20210131014640006.png)