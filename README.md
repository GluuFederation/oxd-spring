# oxd-spring
This is a sample project that demonstrates how to authenticate using Gluu as an authentication provider in spring project.

# Dependencies
Before you can run or build this project, you must install and configure the following dependencies on your machine:

1. [oxd-server-2.4.3.Final](https://ox.gluu.org/maven/org/xdi/oxd-server/2.4.3.Final/). Configure `${install_dir}/oxd-server/conf/oxd-conf.json`->
 change `op_host` to your openid provider domain e.g `"op_host": "https://ce-dev.gluu.org"`

2. [gluu-server-2.4.3](https://www.gluu.org/docs/deployment/)


# Building&Running
Clone this repo to your computer, and cd into the project directory:
```
git clone https://github.com/GluuFederation/oxd-spring.git
cd oxd-spring 
```
Run maven task

```
mvn clean package
```
if dependencies are not installed yet, you can skip the tests
```
mvn clean package -Dmaven.test.skip=true
```
Run application
```
java -jar target/oxd-spring-0.0.1-SNAPSHOT.jar
```
And point browser to `https://127.0.0.1:8443/`.

***Note:*** oxd-server must run on *localhost* and be bound to port: *8099*, otherwise you'll need to configure `oxd-spring/src/main/resources/application.properties` file.




