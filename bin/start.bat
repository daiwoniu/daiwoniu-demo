@echo off

set CP1="webapp\WEB-INF\classes"
set CP2=";lib\spring-aop-4.1.2.RELEASE.jar;lib\aopalliance-1.0.jar;lib\spring-beans-4.1.2.RELEASE.jar;lib\spring-core-4.1.2.RELEASE.jar;lib\commons-logging-1.1.3.jar;lib\spring-context-4.1.2.RELEASE.jar;lib\spring-expression-4.1.2.RELEASE.jar;lib\spring-context-support-4.1.2.RELEASE.jar;lib\spring-web-4.1.2.RELEASE.jar;lib\spring-webmvc-4.1.2.RELEASE.jar;lib\spring-jdbc-4.1.2.RELEASE.jar;lib\spring-tx-4.1.2.RELEASE.jar;lib\displaytag-1.2.jar;lib\commons-collections-3.1.jar;lib\commons-beanutils-1.7.0.jar;lib\jsoup-1.8.1.jar;lib\guava-18.0.jar;lib\hibernate-jpa-2.0-api-1.0.1.Final.jar;lib\javax.inject-1.jar;lib\commons-dbcp2-2.0.1.jar;lib\commons-pool2-2.2.jar;lib\commons-cli-1.2.jar;lib\commons-io-2.4.jar;lib\commons-lang-2.3.jar;lib\mysql-connector-java-5.1.23.jar;lib\validation-api-1.1.0.Final.jar;lib\hibernate-validator-5.1.3.Final.jar;lib\jboss-logging-3.1.3.GA.jar;lib\classmate-1.0.0.jar;lib\slf4j-api-1.7.7.jar;lib\logback-classic-1.1.2.jar;lib\logback-core-1.1.2.jar;lib\jcl-over-slf4j-1.7.7.jar;lib\log4j-over-slf4j-1.7.7.jar;lib\jackson-databind-2.4.3.jar;lib\jackson-annotations-2.4.0.jar;lib\jackson-core-2.4.3.jar;lib\jackson-datatype-joda-2.4.3.jar;lib\joda-time-2.5.jar;lib\jetty-server-9.2.5.v20141112.jar;lib\javax.servlet-api-3.1.0.jar;lib\jetty-http-9.2.5.v20141112.jar;lib\jetty-util-9.2.5.v20141112.jar;lib\jetty-io-9.2.5.v20141112.jar;lib\jetty-webapp-9.2.5.v20141112.jar;lib\jetty-xml-9.2.5.v20141112.jar;lib\jetty-servlet-9.2.5.v20141112.jar;lib\jetty-security-9.2.5.v20141112.jar;lib\jetty-jsp-9.2.5.v20141112.jar;lib\jetty-schemas-3.1.M0.jar;lib\javax.servlet.jsp-api-2.3.1.jar;lib\javax.servlet.jsp-2.3.2.jar;lib\javax.servlet.jsp.jstl-1.2.0.v201105211821.jar;lib\javax.servlet.jsp.jstl-1.2.2.jar;lib\javax.el-3.0.0.jar;lib\org.eclipse.jdt.core-3.8.2.v20130121.jar;lib\blade-patchca-1.0.3.jar;lib\blade-core-1.6.0M1.jar;lib\blade-kit-1.2.9-beta.jar"
set CP=%CP1%
set CPTMP=-Djava.ext.dirs="lib"

"D:\Program Files\Java\jdk1.7.0_02\jre\bin\java" -Dfile.encoding=UTF-8 -classpath %CP% %CPTMP% com.woniu.base.web.Main -w "webapp" -p 8080