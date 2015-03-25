# pac4j-google-apps-domain-oauth-client
Pac4J OAuth client implementation that restricts authentication to a Google 
Apps domain.

## How to use with Shiro

### shiro.ini

Contents of `src/main/resources/shiro.ini`:

    [main] 
    subjectFactory = io.buji.pac4j.ClientSubjectFactory
    securityManager.subjectFactory = $subjectFactory
    standardCacheManager = org.apache.shiro.cache.MemoryConstrainedCacheManager
    securityManager.cacheManager = $standardCacheManager

    googleClient = com.github.mike10004.pac4j.oauth.googleappsdomainclient.GoogleAppsDomainClient
    googleClient.key = ${google.oauth.client.id}
    googleClient.secret = ${google.oauth.client.secret}
    googleClient.domain = ${google.oauth.domain}

    clients = org.pac4j.core.client.Clients
    clients.callbackUrl = ${google.oauth.redirectUri}
    clients.clientsList = $googleClient

    clientsRealm = io.buji.pac4j.ClientRealm
    clientsRealm.defaultRoles = ROLE_USER
    clientsRealm.clients = $clients

    clientsFilter = io.buji.pac4j.ClientFilter
    clientsFilter.clients = $clients
    clientsFilter.failureUrl = /error500.jsp

    googleRoles = io.buji.pac4j.filter.ClientRolesAuthorizationFilter
    googleRoles.client = $googleClient

    ssl.enabled = ${shiro.main.ssl.enabled}

    [urls] 
    /callback = ssl, clientsFilter
    /logout = ssl, logout
    /** = ssl, googleRoles[ROLE_USER]

### Project pom.xml    
    
In your project `pom.xml`, include the following:

    <properties>
        ...
        <google.oauth.domain>${my-app.shiro.google.domain}</google.oauth.domain>
        <google.oauth.client.id>${my-app.shiro.google.client.id}</google.oauth.client.id>
        <google.oauth.client.secret>${my-app.shiro.google.client.secret}</google.oauth.client.secret>
        <google.oauth.redirectUri>http://localhost:8080/my-app/callback</google.oauth.redirectUri>
        <shiro.main.ssl.enabled>false</shiro.main.ssl.enabled>
        ...
    </properties>

    <profiles>
        ...
        <profile>
            <id>remote-deployable</id>
            <properties>
                <google.oauth.redirectUri>${my-app.shiro.google.remoteRedirectUri}</google.oauth.redirectUri>
                <shiro.main.ssl.enabled>true</shiro.main.ssl.enabled>
            </properties>
        </profile>
        ...
    </profiles>
    
    <resources>
        ...
        <resource>
            <directory>src/main/resources</directory>
            <filtering>true</filtering>
            <includes>
                <include>shiro.ini</include>
            </includes>
        </resource>
        ...
    </resources>

### Maven user settings
    
In your Maven user settings (`$HOME/.m2/settings.xml`), define the following
properties:

* my-app.shiro.google.domain (e.g. example.com)
* my-app.shiro.google.client.id
* my-app.shiro.google.client.secret
* my-app.shiro.google.remoteRedirectUri

Visit https://console.developers.google.com to get your client ID and client
secret and to set the permitted redirect URI. 

These properties must be set in a profile that is activated by default. A good
way to activate a profile default is described in [an answer to this SO
question](http://stackoverflow.com/questions/5309379/how-to-keep-maven-profiles-which-are-activebydefault-active-even-if-another-prof). 
Following those instructions, your settings file would look something like this:

    <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
    ...
    <profiles>
        ...
        <profile>
            <id>default_profile</id>
            <activation>
                <property>
                    <name>!not_default_profile</name>
                </property>
            </activation>
            <properties>
                <my-app.shiro.google.client.id>a78_CLIENT_ID_HERE_yoT.apps.googleusercontent.com</my-app.shiro.google.client.id>
                <my-app.shiro.google.client.secret>V7H_CLIENT_SECRET_HERE_t4P</my-app.shiro.google.client.secret>
                <my-app.shiro.google.redirectUri.remote>https://my-app.appspot.com/callback</my-app.shiro.google.redirectUri.remote>
            </properties>
        </profile>
        ...
    </profiles>
    ...
    </settings>


Development builds will use 
http://localhost:8080/my-app/callback as the callback 
URL, and builds where the `remote-deployable` profile is activated will use
the value of `${my-app.shiro.google.remoteRedirectUri}` defined in your user
settings.
