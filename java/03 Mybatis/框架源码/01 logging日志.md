org.apache.ibatis.logging
org.apache.ibatis.logging.commons
org.apache.ibatis.logging.jdbc
org.apache.ibatis.logging.jdk14
org.apache.ibatis.logging.log4j
org.apache.ibatis.logging.log4j2
org.apache.ibatis.logging.nologging
org.apache.ibatis.logging.slf4j    
org.apache.ibatis.logging.stdout  控制台输出
对象适配器设计模式
设计模式可参考http://www.cnblogs.com/liuling/archive/2013/04/12/adapter.html

1. 定义Log接口
2. LogFactory实现工厂日志，再static{}代码块中对所有的日志框架进行尝试，slf4j -commons Loggin log4j2 log4j1 JDK自带的 控制台（没用到） 不是使用日志
3. LogFactory.getLog(clazz);，并使用反射NEW INSTANCEH创建Log实例
4. 
