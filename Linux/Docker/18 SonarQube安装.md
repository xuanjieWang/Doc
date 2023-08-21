## SonarQube安装：是一个开源代码扫描平台,持续扫描，分析和评测代码质量和安全

1. docker拉起postgre sql 和sonarqube：9.9-community镜像
2. 创建文件夹编写docker-cpmpose文件
3. 


```php
services:
  postgres:
    image: postgres
    container_name: pgdb
    restart: always
    ports:
      - 5432:5432
    environment:
      POSTGRES_USERNAME: sonar
      POSTGRES_PASSWORD: sonar

  sonarqube:
    image: sonarqube:9.9-community
    container_name: sonarqb
    restart: always
    depends_on:
      - postgres
    ports:
      - 9001:9000
    environment:
      SONAR_JDBC_URL: jdbc:postgresql://pgdb:5432/sonar
      SONAR_JDBC_USERNAME: sonar
      SONAR_JDBC_PASSWORD: sonar
```
