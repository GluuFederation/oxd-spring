# oxd-spring <!-- intro -->
### Table of Contents 
1. [About](https://github.com/GluuFederation/oxd-spring#about)
2. [How it works](https://github.com/GluuFederation/oxd-spring#how-it-works)
3. [Run demo application](https://github.com/GluuFederation/oxd-spring#run-demo-application)
    1. [Prerequisites](https://github.com/GluuFederation/oxd-spring#prerequisites)
    2. [Deploy](https://github.com/GluuFederation/oxd-spring#deploy)
4. [Customize](https://github.com/GluuFederation/oxd-spring#customize-oxd-spring)

# About
The following documentation demonstrates how to use Gluu's commercial OAuth 2.0 client software, 
[oxd](http://oxd.gluu.org), to send users from a Spring application to an OpenID Connect Provider 
(OP) for login. You can send users to any standard OP for login, including Google. 
In these docs we use the [free open source Gluu Server](http://gluu.org/gluu-server) as the OP.

!!! Note:
    You can also refer to the [oxd java library](https://gluu.org/docs/oxd/latest/libraries/java/) for more details on java classes.
  
# How it works
1. What happens when application is started.

    The first time you run the application, it tries to register site using the parameters specified in [application.properties](https://github.com/GluuFederation/oxd-spring/blob/master/src/main/resources/application.properties). If registration was successful, then [oxd.server.op-host](https://github.com/GluuFederation/oxd-spring/blob/master/src/main/resources/application.properties#L19) and received from oxd server `oxdId` are stored in the H2 database (which is embedded in oxd-spring-0.0.1-SNAPSHOT.jar). Next time you run application with the same [oxd.server.op-host](https://github.com/GluuFederation/oxd-spring/blob/master/src/main/resources/application.properties#L19), it will obtain `oxdId` from database. [Here](https://github.com/GluuFederation/oxd-spring/blob/master/src/main/java/org/xdi/oxd/spring/Settings.java#L41) is the source code that does this.
    
2. H2 databse

    This example is using H2 database to store `oxdId`. You can access the database console by pointing browser to `https://127.0.0.1:8443/h2-console`. By default [expiration time](https://github.com/GluuFederation/community-edition-setup/blob/master/templates/oxauth-config.json#L185) for newly registered clients/sites is set to 24 hours, so if client is already expired you need to remove the record from database, then re-run the application.
    
3. Pages
    - /home this is welcome page from which you can log in
    - /user this is protected page which becomes available after successful login.
    
# Run demo application

## Prerequisites
1. Install oxd server.

    ```
    echo "deb https://repo.gluu.org/ubuntu/ trusty main" > /etc/apt/sources.list.d/gluu-repo.list
    url https://repo.gluu.org/ubuntu/gluu-apt.key | apt-key add -
    pt-get update
    apt-get install gluu-oxd-server
    ```
    For more info see the [official docs](https://gluu.org/docs/oxd/install/).
2. Install Java 8.

    [oxd-spring](https://github.com/GluuFederation/oxd-spring) uses java 8. If java 8 already installed, skip this step.

    First of all, you need to add webupd8team Java PPA repository in your system. After that install, Oracle Java 8 using following a set of commands.
    ```
    sudo add-apt-repository ppa:webupd8team/java
    sudo apt-get update
    sudo apt-get install oracle-java8-installer
    ```

    After successfully installing Oracle Java using above step verify installed version using the following command.
    ```
    root@gluu:~# java -version
    java version "1.8.0_144"
    Java(TM) SE Runtime Environment (build 1.8.0_144-b01)
    Java HotSpot(TM) 64-Bit Server VM (build 25.144-b01, mixed mode)
    ```

    Make sure that Java configuration package is installed.
    ```
    sudo apt-get install oracle-java8-set-default
    ```

    Now add the JAVA_HOME and JRE_HOME environment variable in /etc/environment configuration file using following command.
    ```
    cat >> /etc/environment <<EOL
    JAVA_HOME=/usr/lib/jvm/java-8-oracle
    JRE_HOME=/usr/lib/jvm/java-8-oracle/jre
    EOL
    ```
3. Install maven.

    To install the latest Apache Maven run the following command: 
    ```
    sudo apt-get install maven
    ```
    Run command mvn -version to verify your installation.
    ```
    Apache Maven 3.0.5
    Maven home: /usr/share/maven
    Java version: 1.8.0_144, vendor: Oracle Corporation
    Java home: /usr/lib/jvm/java-8-oracle/jre
    Default locale: en_US, platform encoding: UTF-8
    OS name: "linux", version: "4.4.0-31-generic", arch: "amd64", family: "unix"
    ```
## Deploy
Clone oxd-spring from Github repo and run maven command to install it:
```
git clone https://github.com/GluuFederation/oxd-spring.git
cd oxd-spring 
mvn clean package -Dmaven.test.skip=true
```
Make sure that oxd server is running. Now you can run the executable jar:
```
java -jar target/oxd-spring-0.0.1-SNAPSHOT.jar
```

Point the browser to `https://127.0.0.1:8443/`. 

## Customize oxd-spring
By default [oxd-spring](https://github.com/GluuFederation/oxd-spring) uses [ce-dev.gluu.org](https://ce-dev.gluu.org) as openid provider, however you can easelly point it at your own server. There are two ways you can do it:
1. Modify [oxd.server.op-host](https://github.com/GluuFederation/oxd-spring/blob/master/src/main/resources/application.properties#L19) property, e.g:
    ```
    oxd.server.op-host=https://your.gluu.server
    ```
    Build and run the application by running the following commands:
    ```
    mvn clean package -Dmaven.test.skip=true
    java -jar target/oxd-spring-0.0.1-SNAPSHOT.jar
    ```
2. Or you can just launch the application with additional parameter, e.g:
    ```
    java -jar target/oxd-spring-0.0.1-SNAPSHOT.jar --oxd.server.op-host=https://your.gluu.server
    ```
[oxd-spring](https://github.com/GluuFederation/oxd-spring) also allows configuring other parameters:
- [oxd.server.scopes](https://github.com/GluuFederation/oxd-spring/blob/master/src/main/resources/application.properties#L23) a list of comma separated scopes, this parameter used during [client registration](https://github.com/GluuFederation/oxd-spring/blob/master/src/main/java/org/xdi/oxd/spring/service/OxdServiceImpl.java#L55) and [obtaining login url](https://github.com/GluuFederation/oxd-spring/blob/master/src/main/java/org/xdi/oxd/spring/service/OxdServiceImpl.java#L83)

- [oxd.server.acr-values](https://github.com/GluuFederation/oxd-spring/blob/master/src/main/resources/application.properties#L22) a list of comma separated acr values, this parameter used during [client registration](https://github.com/GluuFederation/oxd-spring/blob/master/src/main/java/org/xdi/oxd/spring/service/OxdServiceImpl.java#L55) and [obtaining login url](https://github.com/GluuFederation/oxd-spring/blob/master/src/main/java/org/xdi/oxd/spring/service/OxdServiceImpl.java#L83)

- [oxd.server.port](https://github.com/GluuFederation/oxd-spring/blob/master/src/main/resources/application.properties#L21) used to [create connection](https://github.com/GluuFederation/oxd-spring/blob/master/src/main/java/org/xdi/oxd/spring/service/OxdServiceImpl.java#L43) with oxd server

- [oxd.server.host](https://github.com/GluuFederation/oxd-spring/blob/master/src/main/resources/application.properties#L20) used to [create connection](https://github.com/GluuFederation/oxd-spring/blob/master/src/main/java/org/xdi/oxd/spring/service/OxdServiceImpl.java#L43) with oxd server

These parameters could be modified in the same way as [oxd.server.op-host](https://github.com/GluuFederation/oxd-spring/blob/master/src/main/resources/application.properties#L19), e.g:
```
java -jar target/oxd-spring-0.0.1-SNAPSHOT.jar --oxd.server.host=https://your.gluu.server --oxd.server.port=your.port --oxd.server.acr-values=basic,u2f --oxd.server.scopes=openid,profile,email,phone
```
