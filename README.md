# hgmall
使用springboot集成shiro和redis，搭建了电商网站，实现基本的购物功能，留言功能，会员功能，店铺功能，帮助功能，物流功能。
## 一：导入数据
导入sql文件
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190622212633533.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNjkwNDU2OA==,size_16,color_FFFFFF,t_70)
## 二：创建项目
1. 点击Create New Project新建项目
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190622212955557.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNjkwNDU2OA==,size_16,color_FFFFFF,t_70)
2.  左边选中Spring Initializr，直接下一步
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190622213038813.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNjkwNDU2OA==,size_16,color_FFFFFF,t_70)
3. GroupId: 填写项目组的名字， ArtifactId: 填写项目的名字
4. 左边选中 Web，右边选中 Web
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190622213252969.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNjkwNDU2OA==,size_16,color_FFFFFF,t_70)
5. 项目路径选择，完成

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190622213426639.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNjkwNDU2OA==,size_16,color_FFFFFF,t_70)

## 二：部署环境
### 1. 配置pom.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.lh</groupId>
    <artifactId>hgmall</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>war</packaging>

    <name>hgmall</name>
    <description>Demo project for Spring Boot</description>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.9.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.session</groupId>
            <artifactId>spring-session-data-redis</artifactId>
        </dependency>
        <!-- https://mvnrepository.com/artifact/com.esotericsoftware/kryo -->
        <!--<dependency>-->
            <!--<groupId>com.esotericsoftware</groupId>-->
            <!--<artifactId>kryo</artifactId>-->
            <!--<version>4.0.0</version>-->
        <!--</dependency>-->

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.47</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>
        <!-- springboot tomcat 支持 -->

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
            <!--<version>1.3.0.RELEASE</version>-->
            <scope>provided</scope>
            <!--<scope>compile</scope>-->
        </dependency>
        <!-- thymeleaf -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <!-- jpa-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <!-- redis -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <!-- thymeleaf legacyhtml5 模式支持 -->
        <dependency>
            <groupId>net.sourceforge.nekohtml</groupId>
            <artifactId>nekohtml</artifactId>
            <version>1.9.22</version>
        </dependency>
        <!-- 测试支持 -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
        <!-- 分页 -->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper</artifactId>
            <version>4.1.6</version>
        </dependency>
        <!-- mybatis -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.1.1</version>
        </dependency>
        <!-- tomcat的支持.-->
        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-jasper</artifactId>
            <!--<version>7.0.82</version>-->
            <version>8.5.23</version>
            <scope>provided</scope>
        </dependency>
        <!-- 热部署 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
        </dependency>
        <!-- mysql-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.21</version>
        </dependency>
        <!-- commons-lang -->
        <dependency>
            <groupId>commons-lang</groupId>
            <artifactId>commons-lang</artifactId>
            <version>2.6</version>
        </dependency>
        <!-- shiro -->
        <dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-spring</artifactId>
            <version>1.3.2</version>
        </dependency>
        <!-- hsqldb -->
        <dependency>
            <groupId>org.hsqldb</groupId>
            <artifactId>hsqldb</artifactId>
        </dependency>
        <!-- elastic search -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
        </dependency>
        <!-- 用了 elasticsearch 就要加这么一个，不然要com.sun.jna.Native 错误 -->
        <dependency>
            <groupId>com.sun.jna</groupId>
            <artifactId>jna</artifactId>
            <version>3.0.9</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

```
###  2. 配置基本的项目结构
#### （1）在项目包下创建bean包以及相关的实体类
根据自定义的数据库，创建每个表对应的实体类以及相关的属性、getXX()和setXX()方法
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190622213928464.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNjkwNDU2OA==,size_16,color_FFFFFF,t_70)
#### （2）创建DAO包以及相应的dao接口
DAO 类集成了 JpaRepository，就提供了CRUD和分页 的各种常见功能。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190622214107981.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNjkwNDU2OA==,size_16,color_FFFFFF,t_70)
#### （3）创建service包以及相应的service类
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190622214338538.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNjkwNDU2OA==,size_16,color_FFFFFF,t_70)
#### （4）创建controller包以及网站每个功能对应的controller类
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190622214433901.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNjkwNDU2OA==,size_16,color_FFFFFF,t_70)
#### （5）编写启动类
![在这里插入图片描述](https://img-blog.csdnimg.cn/2019062221461021.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNjkwNDU2OA==,size_16,color_FFFFFF,t_70)
#### （7）配置异常处理、拦截器、过滤器
##### 创建exception包并编写异常处理类
主要是在处理删除父类信息的时候，因为外键约束的存在，而导致违反约束
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190622214706939.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNjkwNDU2OA==,size_16,color_FFFFFF,t_70)
##### 创建filter包、realm包，配置shiro
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190622223913793.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNjkwNDU2OA==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190622223929599.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNjkwNDU2OA==,size_16,color_FFFFFF,t_70)
##### 创建interceptor包并编写拦截器类
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190622223959586.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNjkwNDU2OA==,size_16,color_FFFFFF,t_70)
### 3. 配置属性文件
#### （1）配置application.properties
![在这里插入图片描述](https://img-blog.csdnimg.cn/2019062221483123.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNjkwNDU2OA==,size_16,color_FFFFFF,t_70)
### 4. 导入静态资源
自行创建 webapp目录。一般说来，在约定里，springboot 的静态资源会在 static 目录下，但是我们是放在 webapp 目录下， 因为我们还要做上传图片的功能，如果是放在 static 下，上传后的图片就无法被访问，放在 webapp 下，上传后，能够立即被访问。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190622220314716.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNjkwNDU2OA==,size_16,color_FFFFFF,t_70)
## 三：运行
### 1. 启动redis
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190622220541351.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNjkwNDU2OA==,size_16,color_FFFFFF,t_70)
### 2. 启动项目
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190622224115440.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNjkwNDU2OA==,size_16,color_FFFFFF,t_70)

