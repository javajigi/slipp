### 설치 및 빌드
* JDK 8.0을 설치해야 한다.
* MAVEN을 설치하고 시스템 환경 변수의 PATH를 설정한다.
* MySQL 데이터베이스를 설치하고 slipp 프로젝트에서 사용할 데이터베이스를 생성한다.(인코딩 utf-8)
* USER_HOME/.m2 디렉토리에 settings.xml 파일을 생성한 후 다음 설정을 추가한다. 데이터베이스명, 사용자 아이디, 비밀번호를 자신의 환경에 맞도록 수정한다.

```
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <profiles>
    <profile>
      <id>development</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <properties>
        <environment>development</environment>
        <database.driverClassName>com.mysql.jdbc.Driver</database.driverClassName>
        <database.url>jdbc:mysql://127.0.0.1/slipp?uniCode=true&amp;characterEncoding=utf8</database.url>
        <database.username>userid</database.username>
        <database.password>password</database.password>
      </properties>
    </profile>
  </profiles>
</settings>
```

* 위와 같이 데이터베이스 설정을 완료했다면 "mvn flyway:migrate"를 실행해 데이터베이스 스키마를 생성한다.
* 이클립스를 사용하는 경우 명령 창에서 "mvn eclipse:clean eclipse:eclipse"를 실행해 이클립스 프로젝트로 변환한 후 가져오기 한다.
* src/main/resources-development 디렉토리의 temp-application-properties.xml 파일을 application-properties.xml으로 이름을 바꾼다. 인증을 하려면 페이스북, 트위트, 구글에 앱을 등록한 후 application-properties.xml 설정 파일에 관련 내용을 변경해야 한다.

### 서버 실행
* 서버를 실행하는 가장 간단한 방법은 src/main/resources-development 디렉토리의 temp-application-properties.xml 파일의 DB 설정을 한 후 ./startup.sh 파일을 실행해 서버를 시작한다.
* slipp 서비스는 embedded tomcat을 사용한다. embedded tomcat 설정 방법은 http://www.slipp.net/wiki/pages/viewpage.action?pageId=16711743 문서를 참고한다.
* embedded tomcat은 src/test/java 디렉토리의 net.slipp.support.web.WebServerLauncher 클래스의 main method를 활용해 시작한다.
