/*
 * (c) 2015 Mike Chaberski
 * 
 * Distributed under Apache License 2.0
 */
package com.github.mike10004.pac4j.oauth.googleappsdomainclient;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

/**
 *
 * @author mchaberski
 */
public class GoogleAppsDomainApi20Test {
    
    static final String PROFILE_SCOPE = "https://www.googleapis.com/auth/userinfo.profile";
    static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
    
    public GoogleAppsDomainApi20Test() {
    }

    @Test
    public void testGetAuthorizationUrl() throws Exception {
        System.out.println("test getAuthorizationUrl");
        
        String clientId = "someRandomChars.apps.googleusercontent.com";
        String clientSecret = "someMoreRandomness";
        String callbackUri = "https://app.example.com/oauth/callback";
        String scope = PROFILE_SCOPE + ' ' + EMAIL_SCOPE;
        OutputStream stream = null;
        String domain = "example.com";
        OAuthConfig config = new GoogleAppsDomainOAuthConfig(clientId, clientSecret, callbackUri, SignatureType.Header, scope, stream, domain);
        
        GoogleAppsDomainApi20 api = new GoogleAppsDomainApi20();
        String authorizationUrl = api.getAuthorizationUrl(config);
        
        URI uri = URI.create(authorizationUrl);
        System.out.println("authorization URL = " + authorizationUrl);
        List<NameValuePair> queryParts = URLEncodedUtils.parse(uri, Charsets.UTF_8.name());
        assertParameterValue(queryParts, "hd", domain);
        assertParameterValue(queryParts, "scope", scope);
        assertParameterValue(queryParts, "redirect_uri", callbackUri);
        assertParameterValue(queryParts, "client_id", clientId);
    }

    protected void assertParameterValue(Iterable<NameValuePair> params, String paramName, String expectedValue) {
        NameValuePair param = Iterables.find(params, byParameterName(paramName));
        assertNotNull("expected parameter to be present: " + paramName, param);
        String actualValue = param.getValue();
        System.out.format("%s = %s (expected %s)%n", paramName, actualValue, expectedValue);
        assertEquals("unexpected value of parameter " + paramName, expectedValue, actualValue);
    }
    
    protected Predicate<NameValuePair> byParameterName(final String parameterName) {
        return new Predicate<NameValuePair>() {

            @Override
            public boolean apply(NameValuePair nameValuePair) {
                return parameterName.equalsIgnoreCase(nameValuePair.getName());
            }
        };
    }
    
    @Test
    public void testOAuthConfigArgument() {
        System.out.println("testOAuthConfigArgument");
        
        OAuthConfig config = new OAuthConfig("someId", "someSecret");
        GoogleAppsDomainApi20 api = new GoogleAppsDomainApi20();
        try {
            api.getAuthorizationUrl(config);
        } catch (GoogleAppsDomainApi20.InvalidOAuthConfigException expected) {
            System.out.println("confirmed: use of invalid config throws exception");
        }
        
        // valid case is already tested in testGetAuthorizationUrl
    }
}
