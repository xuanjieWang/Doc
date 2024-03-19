### 1.代码如何实现mysql与es的实时同步？

##### MYSQL全量更新 

1.上传logstash7.3.0.zip  的安装包 到linux 下 

2.解压  

  unzip  logstash-7.3.0.zip 

3..在logstash-7.3.0 文件夹下创建totalJdbc.conf，totalPosition.sql两个文件　　  

totalJdbc.conf 内容如下 

```yml
input {
    stdin {
    }
    jdbc {
      # 驱动
      jdbc_driver_library => "/usr/logstash-7.3.0/mysql-connector-java-5.1.7-bin.jar"
      # 驱动类名
      jdbc_driver_class => "com.mysql.jdbc.Driver"
      # mysql 数据库链接,shop为数据库名
      jdbc_connection_string => "jdbc:mysql://192.168.211.136:3306/lagou_position"
      # 用户名和密码
      jdbc_user => "root"
      jdbc_password => "123456"
      jdbc_paging_enabled => "true"
      jdbc_page_size => "50000"
      # 执行的sql 文件路径+名称
      statement_filepath => "/usr/logstash-7.3.0/totalPosition.sql"
      # 设置监听间隔  各字段含义（由左至右）分、时、天、月、年，全部为*默认含义为每分钟都更新
      schedule => "* * * * *"
      # 索引类型
      type => "std"
    }
}
 
filter {
    json {
        source => "message"
        remove_field => ["message"]
    }
}
 
output {
    elasticsearch {
        hosts => ["192.168.211.136:9200"]
        index => "position"
        document_id => "%{id}"
    }
    stdout {
        codec => json_lines
    }
}
```



totalPosition.sql

```
select * from  position 
```

4.执行 全量更新 

```
./bin/logstash -f ./totalJdbc.conf
```



##### MYSQL增量更新 

1.在安装好的  logstash-7.3.0 文件夹下创建addJdbc.conf，totalPosition.sql两个文件

addJdbc.conf

```shell
input {
  jdbc {
    jdbc_driver_library => "/usr/logstash-7.3.0/mysql-connector-java-5.1.7-bin.jar"
    jdbc_driver_class => "com.mysql.jdbc.Driver"
    # 数据库相关配置
    jdbc_connection_string => "jdbc:mysql://192.168.211.136:3306/lagou_position"
    jdbc_user => "root"
    jdbc_password => "123456"
    # 数据库重连尝试次数
    connection_retry_attempts => "3"
    # 数据库连接可用校验超时时间，默认3600S
    jdbc_validation_timeout => "3600"
    # 开启分页查询（默认false不开启）；
    jdbc_paging_enabled => "true"
    # 单次分页查询条数（默认100000,若字段较多且更新频率较高，建议调低此值）；
    jdbc_page_size => "500"
    # 如果sql较复杂，建议配通过statement_filepath配置sql文件的存放路径；
    statement_filepath => "/usr/logstash-7.3.0/addPosition.sql"
    # 需要记录查询结果某字段的值时，此字段为true，否则默认tracking_column为timestamp的值；
    use_column_value => true
    # 是否将字段名转换为小写，默认true（如果有数据序列化、反序列化需求，建议改为false）；
    lowercase_column_names => false
    # 需要记录的字段，用于增量同步，需是数据库字段
    tracking_column => id
    # Value can be any of: numeric,timestamp，Default value is "numeric"
    tracking_column_type => numeric
    # record_last_run上次数据存放位置；
    record_last_run => true
    #上一个sql_last_value值的存放文件路径, 必须要在文件中指定字段的初始值
    last_run_metadata_path => "/usr/logstash-7.3.0/config/station_parameter.txt"
    # 是否清除last_run_metadata_path的记录，需要增量同步时此字段必须为false；
    clean_run => false
    # 同步频率(分 时 天 月 年)，默认每分钟同步一次；
    schedule => "* * * * *"
    # 索引类型
    type => "std"
  }
}
 
filter {
   json {
        source => "message"
        remove_field => ["message"]
    }
}
 
output {
  stdout {
    codec => rubydebug
  }
  elasticsearch {
    hosts => ["192.168.211.136:9200"]
    #将mysql数据加入myindex索引下，会自动创建
    index => "position"
    # 自增ID 需要关联的数据库中有有一个id字段，对应索引的id号
    document_id => "%{id}"
  }
}
```

addPosition.sql

```
select * from position where id > :sql_last_value
```

2.在config 目录下 建立 station_parameter.txt

station_parameter.txt 的内容是当前数据的id 最大值 

3.执行增量 

./bin/logstash -f  ./addJdbc.conf

4.在mysql 中增加数据进行测试 

```sql
insert  into `position`(`companyName`,`id`,`positionAdvantage`,`companyId`,`positionName`,`salary`,`salaryMin`,`salaryMax`,`salaryMonth`,`education`,`workYear`,`jobNature`,`chargeField`,`createTime`,`email`,`publishTime`,`isEnable`,`isIndex`,`city`,`orderby`,`isAdvice`,`showorder`,`publishUserId`,`workAddress`,`generateTime`,`bornTime`,`isReward`,`rewardMoney`,`isExpired`,`positionDetailPV`,`offlineTime`,`positionDetailPV_cnbeta`,`adviceTime`,`comeFrom`,`receivedResumeCount`,`refuseResumeCount`,`markCanInterviewCount`,`haveNoticeInterCount`,`isForbidden`,`reason`,`verifyTime`,`adWord`,`adRankAndTime`,`adTimes`,`adStartTime`,`adEndTime`,`adBeforeDetailPV`,`adAfterDetailPV`,`adBeforeReceivedCount`,`adAfterReceivedCount`,`adjustScore`,`weightStartTime`,`weightEndTime`,`isForward`,`forwardEmail`,`isSchoolJob`,`type`,`prolong_offline_time`) values (NULL,58,'优秀者，可解决北京市户口',11,'美术实习','2k-5k',2,5,0,'硕士','1年以下','实习',NULL,'2016-07-22 10:24:18','lixiao@a-onesoft.com','1',1,0,'北京',40,0,1373891110617,197,'北京','2015-08-18 12:05:24','2015-08-18 12:05:24',0,NULL,1,267,'2018-10-20 00:00:22',0,NULL,NULL,0,0,0,0,0,NULL,NULL,0,NULL,0,NULL,NULL,0,0,0,0,0,NULL,NULL,'\0','','\0',0,NULL);
```



