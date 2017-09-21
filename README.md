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
1. Install Java 8.

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
2. Install maven.

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


# Requirements
The oxd-spring requires the oxD Server. Please use the following link to install it

* [oxd Server Installation Guide](https://oxd.gluu.org/docs/install/)


## Install oxd-spring
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

## Sample Code

Usage of Oxd-spring is very simple. First of all we need to create parameter object related to command we are going to perform and pass to related method.
Check Sample code below we are creating commandParams object  related to commands and calling related method with created params.

### 1) register_site

---

```java

// create registerSiteParams
try{
        final RegisterSiteParams commandParams = new RegisterSiteParams();
        commandParams.setOpHost(opHost);
        commandParams.setAuthorizationRedirectUri(redirectUrl);
        commandParams.setPostLogoutRedirectUri(postLogoutRedirectUrl);
        commandParams.setClientLogoutUri(Lists.newArrayList(logoutUrl));
        commandParams.setRedirectUris(Arrays.asList(redirectUrl));
        commandParams.setAcrValues(new ArrayList<>());
        commandParams.setScope(Lists.newArrayList("openid", "profile"));
        commandParams.setGrantType(Lists.newArrayList("authorization_code"));
        commandParams.setResponseTypes(Lists.newArrayList("code"));
        final Command command = new Command(CommandType.REGISTER_SITE).setParamsObject(commandParams);
        return client.send(command);                
 }
catch (Exception e) 
{
    e.printStackTrace();
 }
 
//oxd_ host - oxd-server host eg.localhost or 127.0.0.1 port - oxd-server listing port (default port is 8099)

```



### 2) update_site_registration
   
---

```java

//create UpdateSiteParams
 try {
        final UpdateSiteParams commandParams = new UpdateSiteParams();
        commandParams.setOxdId(oxdId);
        commandParams.setAuthorizationRedirectUri(redirectUrl);
        final Command command = new Command(CommandType.UPDATE_SITE).setParamsObject(commandParams);
        return client.send(command);
}
catch (Exception e) {
  e.printStackTrace();
 }
```


### 3) get_authorization_url

---

```java

try
{
        final GetAuthorizationUrlParams commandParams = new GetAuthorizationUrlParams();
        commandParams.setOxdId(oxdId);
        final Command command = new Command(CommandType.GET_AUTHORIZATION_URL).setParamsObject(commandParams);
        return client.send(command);
}
catch (Exception e) {
  e.printStackTrace();
 }        
```



### 4) get_tokens_by_code

---
```java
try
{
        final GetTokensByCodeParams commandParams = new GetTokensByCodeParams();
        commandParams.setOxdId(oxdId);
        commandParams.setCode(code);
        commandParams.setState(state);
        final Command command = new Command(CommandType.GET_TOKENS_BY_CODE).setParamsObject(commandParams);
        return client.send(command);
}
 catch (Exception e) {
   e.printStackTrace();
  }
```


### 5) get_user_info

---
```java
 
 try
 {
        GetUserInfoParams params = new GetUserInfoParams();
        params.setOxdId(oxdId);
        params.setAccessToken(accessToken);
        final Command command = new Command(CommandType.GET_USER_INFO).setParamsObject(params);
        return client.send(command);
 }
catch (Exception e) {
  e.printStackTrace();
 }            
            
```

### 6) get_logout_uri

---
  
```java
//create GetLogoutUrlParams
try{
        GetLogoutUrlParams params = new GetLogoutUrlParams();
        params.setOxdId(oxdId);
        params.setIdTokenHint(idToken);
        final Command command = new Command(CommandType.GET_LOGOUT_URI).setParamsObject(params);
        return client.send(command);       
}
catch (Exception e) {
  e.printStackTrace();
 }        

```
 
----

