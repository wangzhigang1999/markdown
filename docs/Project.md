## Graduation

[ClickHereToSeeDemo](https://www.bupt.site)

### For User

#### what's Graduation ?

Graduation is a WeChat Mini Program that helps graduates retain their last campus memories on this special day.

#### How to use ?

1. Download WeChat
2. Find the WeChat Mini Program By Searching "Graduation"
3. Start Use

### For Developers

#### Pull

https://github.com/wangzhigang1999/graduation.git

#### Init

You need to configure the following environment:

- MySql 5.x
- Redis
- Java 8 (At least)

Then , you need to change some applications :

```yml
spring:
  redis:
    host: RedisHost
    port: YourRedisPort
  datasource:
    username: YourDatasourceName
    password: YourDatasourcePassword
    url: YourDatasourceUrl
    driver-class-name: com.mysql.cj.jdbc.Driver
    
    #Not recommended to modify
    type: com.alibaba.druid.pool.DruidDataSource
    filters: stat, wall
  application:
    name: graduation
  servlet:
    multipart:
      #Limit the maximum size of a single file upload
      max-file-size: 5MB
      # Limit the maximum size of a single request
      max-request-size: 10MB
server:
  port: 8888
logging:
  file:
    name: graduation.log
```

After that , you need to change some code:



`com.bupt.graduation.config.DruidConfig`:

```java
init.put("loginUsername", "userName");
init.put("loginPassword", "passWord");
init.put("allow", "");
```

By setting this, you can log in to Druid's control panel.



`com.bupt.graduation.utils.ImageSegmentationFull`:

```java 
/**
 * Baidu's APP_ID
 */
private static final String APP_ID = "";
/**
 * Baidu's API_KEY
 */
private static final String API_KEY = "";
/**
 * Baidu' SECRET_KEy
 */
private static final String SECRET_KEY = "";
```

This project uses Baidu's portrait division technology, you must first go to get the relevant api authorization.

#### Run

If you use Idea, just click “Run” !

### Thanks

1. BaiduAi BodySegment    https://ai.baidu.com/tech/body/seg 
2. Apache Commons IO      http://commons.apache.org/proper/commons-io/
3. Lombok                           https://projectlombok.org/
4. Druid                               https://github.com/alibaba/druid
5. MyBatis-Plus                   https://mp.baomidou.com/
6. Gson                                https://github.com/google/gson

---

