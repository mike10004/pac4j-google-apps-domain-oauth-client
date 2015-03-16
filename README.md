# pac4j-google-apps-domain-oauth-client
Pac4J OAuth client implementation that restricts authentication to a Google 
Apps domain.

## How to use with Shiro

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

In your Maven user settings (`$HOME/.m2/settings.xml`), define the following
properties:

* my-app.shiro.google.domain (e.g. example.com)
* my-app.shiro.google.client.id
* my-app.shiro.google.client.secret
* my-app.shiro.google.remoteRedirectUri
* shiro.main.ssl.enabled

Development builds will use http://localhost:8080/my-app/callback as the callback 
URL, and builds where the `remote-deployable` profile is activated will use
the value of `${my-app.shiro.google.remoteRedirectUri}` defined in your user
settings.
