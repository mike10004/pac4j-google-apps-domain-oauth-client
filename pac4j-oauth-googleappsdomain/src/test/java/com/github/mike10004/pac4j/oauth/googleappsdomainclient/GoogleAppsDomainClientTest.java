/*
 * (c) 2015 IBG, A Novetta Solutions Company.
 */
package com.github.mike10004.pac4j.oauth.googleappsdomainclient;

import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;
import org.pac4j.oauth.client.Google2Client.Google2Scope;

/**
 *
 * @author mchaberski
 */
public class GoogleAppsDomainClientTest {
    
    public GoogleAppsDomainClientTest() {
    }

    @Test
    public void testGetScopeName() {
        System.out.println("testGetScopeName");
        GoogleAppsDomainClient client = new GoogleAppsDomainClient();
        client.setScope(null);
        assertNull(client.getScopeName());
        client.setScope(Google2Scope.EMAIL);
        assertEquals("EMAIL", client.getScopeName());
        client.setScope(Google2Scope.PROFILE);
        assertEquals("PROFILE", client.getScopeName());
        client.setScope(Google2Scope.EMAIL_AND_PROFILE);
        assertEquals("EMAIL_AND_PROFILE", client.getScopeName());
        System.out.println("passed getScopeName tests");
    }

    @Test
    public void testSetScopeName() {
        System.out.println("testSetScopeName");
        GoogleAppsDomainClient client = new GoogleAppsDomainClient();
        client.setScopeName(null);
        assertNull("expect set scope name null ==> scope is null", client.getScope());
        Map<Google2Scope, List<String>> allowedInputMap = ImmutableMap.of(
                Google2Scope.EMAIL, Arrays.asList("EMAIL", "email", "Email"),
                Google2Scope.PROFILE, Arrays.asList("PROFILE", "profile", "Profile"),
                Google2Scope.EMAIL_AND_PROFILE, Arrays.asList("EMAIL_AND_PROFILE", "email_and_profile", "Email_and_profile", "Email_and_Profile", "Email_And_Profile")
        );
        for (Google2Scope expectedScope : allowedInputMap.keySet()) {
            List<String> allowedInputs = allowedInputMap.get(expectedScope);
            for (String allowedInput : allowedInputs) {
                client.setScopeName(allowedInput);
                Google2Scope actualScope = client.getScope();
                System.out.format("'%s' -> %s%n", allowedInput, actualScope);
                assertEquals("expect scope " + expectedScope + " for input " + allowedInput, expectedScope, actualScope);
            }
        }
        String illegalScopeName = "turtle";
        try {
            client.setScopeName(illegalScopeName);
            fail("expect setScopeName " + illegalScopeName + " to throw exception");
        } catch (IllegalArgumentException e) {
        }
    }
    
}
