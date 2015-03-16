/*
 * (c) 2015 Mike Chaberski
 * 
 * Distributed under Apache License 2.0
 */
package com.github.mike10004.pac4j.oauth.googleappsdomainclient;

import java.io.OutputStream;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

/**
 *
 * @author mchaberski
 */
public class GoogleAppsDomainOAuthConfig extends OAuthConfig {

    private String domain;
    
    public GoogleAppsDomainOAuthConfig(String key, String secret) {
        super(key, secret);
    }

    public GoogleAppsDomainOAuthConfig(String key, String secret, String callback, SignatureType type, String scope, OutputStream stream) {
        super(key, secret, callback, type, scope, stream);
    }

    public GoogleAppsDomainOAuthConfig(String key, String secret, String callback, SignatureType type, String scope, OutputStream stream, String domain) {
        super(key, secret, callback, type, scope, stream);
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        return "GoogleAppsDomainOAuthConfig{" + "domain=" + domain + '}';
    }
    
}
