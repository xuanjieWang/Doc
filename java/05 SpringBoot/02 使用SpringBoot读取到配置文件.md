## 使用SpringBoot读取到配置文件

### 使用@value注解
```java
## yml配置文件
lesson: SpringBoot
server: port: 82
enterprise:
  name: itcast
  age: 16
  tel: 4006184000
  subject:
    - Java
    - 前端
    - 大数据

@Value("${lesson}")
private String lessonName;
@Value("${server.port}")
private int port;
@Value("${enterprise.subject[1]}")
private String[] subject_01;
```

## 使用@configurationProperties(prefix = 'enterprise')
```java
enterprise:
  name: itcast
  age: 16
  tel: 4006184000
  subject: - Java - 前端
  - 大数据

@Component
@ConfigurationProperties(prefix = "enterprise")
public class Enterprise {
  private String name;
  private Integer age;
  private String[] subject;
}
```
