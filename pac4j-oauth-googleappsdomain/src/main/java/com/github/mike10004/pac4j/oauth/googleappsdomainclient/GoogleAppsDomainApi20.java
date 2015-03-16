/*
 * (c) 2015 IBG, A Novetta Solutions Company.
 */
package com.github.mike10004.pac4j.oauth.googleappsdomainclient;

import org.scribe.builder.api.GoogleApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.utils.OAuthEncoder;

/**
 *
 * @author mchaberski
 */
public class GoogleAppsDomainApi20 extends GoogleApi20 {

    private static final String DOMAIN_RESTRICTED_AUTHORIZATION_URL = 
            "https://accounts.google.com/o/oauth2/auth?client_id=%s&redirect_uri=%s&scope=%s&response_type=code&hd=%s";
    
    @Override
    public String getAuthorizationUrl(final OAuthConfig config) {
        if (config instanceof GoogleAppsDomainOAuthConfig == false) {
            throw new InvalidOAuthConfigException(config);
        }
        GoogleAppsDomainOAuthConfig oauthConfig = (GoogleAppsDomainOAuthConfig) config;
        String domain = String.valueOf(oauthConfig.getDomain()); // null -> "null"
        String authorizationUrl = String.format(DOMAIN_RESTRICTED_AUTHORIZATION_URL, 
                config.getApiKey(), 
                OAuthEncoder.encode(config.getCallback()),
                OAuthEncoder.encode(config.getScope()),
                OAuthEncoder.encode(domain));
        return authorizationUrl;
    }
    
    static class InvalidOAuthConfigException extends IllegalArgumentException {
        
        public InvalidOAuthConfigException(OAuthConfig config) {
            super("invalid OAuthConfig class: " 
                    + (config == null ? "null" : config.getClass().getName())
            + "perhaps the client is not an instance of GoogleAppsDomainClient");
        }
    }
}
