1. 使用docker安装oracl
2. 创建oracl表以及序列
3. jdbc驱动安装
4. 配置序列和分页插件和在实体类中指定序列的名称

``` sql
-创建表，表名以及字段名都要大写
CREATE TABLE "TB_USER" (
  "ID" NUMBER(20) VISIBLE NOT NULL ,
  "USER_NAME" VARCHAR2(255 BYTE) VISIBLE ,
  "PASSWORD" VARCHAR2(255 BYTE) VISIBLE ,
  "NAME" VARCHAR2(255 BYTE) VISIBLE ,
  "AGE" NUMBER(10) VISIBLE ,
  "EMAIL" VARCHAR2(255 BYTE) VISIBLE
)
--创建序列
CREATE SEQUENCE SEQ_USER START WITH 1 INCREMENT BY 1

``驱动，安装本地jar包，或者是存放在本地仓库
mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc8 -Dversion=12.1.0.1 Dpackaging=jar -Dfile=ojdbc8.jar
```

#### 配置文件
``` yml
数据库连接配置
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver spring.datasource.url=jdbc:oracle:thin:@192.168.31.81:1521:xe spring.datasource.username=system spring.datasource.password=oracle
#id生成策略
mybatis-plus.global-config.db-config.id-type=input

```

#### 配置序列
```java
package cn.itcast.mp;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration @MapperScan("cn.itcast.mp.mapper") //设置mapper接口的扫描包 public class MybatisPlusConfig {
/** * 分页插件 */
@Bean public PaginationInterceptor paginationInterceptor() {
  return new PaginationInterceptor();
}
/** * 序列生成器 */
@Bean public OracleKeyGenerator oracleKeyGenerator(){
  return new OracleKeyGenerator();
}

@KeySequence(value = "SEQ_USER", clazz = Long.class)
public class User{
    ......
}
```
