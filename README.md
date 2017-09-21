# oxd-spring <!-- intro -->
### Table of Contents 
1. [About](https://github.com/GluuFederation/oxd-spring#about)
2. [How it works](https://github.com/GluuFederation/oxd-spring#how-it-works)
3. [Run demo application](https://github.com/GluuFederation/oxd-spring#run-demo-application)
    1. [Prerequisites](https://github.com/GluuFederation/oxd-spring#prerequisites)
    2. [Deploy](https://github.com/GluuFederation/oxd-spring#deploy)

# About
The following documentation demonstrates how to use Gluu's commercial OAuth 2.0 client software, 
[oxd](http://oxd.gluu.org), to send users from a Spring application to an OpenID Connect Provider 
(OP) for login. You can send users to any standard OP for login, including Google. 
In these docs we use the [free open source Gluu Server](http://gluu.org/gluu-server) as the OP.

!!! Note:
    You can also refer to the [oxd java library](https://gluu.org/docs/oxd/latest/libraries/java/) for more details on java classes.
  
# How it works

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

Now you can run the executable jar:
```
java -jar target/oxd-spring-0.0.1-SNAPSHOT.jar
```

Point browser to `https://127.0.0.1:8443/`. 

***Note:*** oxd-server must run on *localhost* and be bound to port: *8099*, otherwise you'll need to configure `oxd-spring/src/main/resources/application.properties` file.

## Customize oxd-spring
To use your own server as openid provider you need to modify `oxd.server.op-host` property from `oxd-spring/src/main/resources/application.properties`, e.g:

```
oxd.server.op-host=https://gluu.localhost.info
```

Make sure the server already installed on your machine, or you can follow 
[this](https://gluu.org/docs/ce/latest/installation-guide/install/) guide to install it.



