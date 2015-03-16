/*
 * (c) 2015 Mike Chaberski
 * 
 * Distributed under Apache License 2.0
 */
package com.github.mike10004.pac4j.oauth.googleappsdomainclient;

import org.pac4j.core.util.CommonHelper;
import org.pac4j.oauth.client.Google2Client;
import org.scribe.model.SignatureType;
import org.scribe.oauth.ProxyOAuth20ServiceImpl;

/**
 * 
 * @author Mike Chaberski
 * @author Jerome Leleu
 */
public class GoogleAppsDomainClient extends Google2Client {

    private String domain;
    
    public GoogleAppsDomainClient() {
    }

    public GoogleAppsDomainClient(String key, String secret) {
        super(key, secret);
    }
    
    @Override
    protected void internalInit() {
        super.internalInit();
        CommonHelper.assertNotNull("scope", this.scope);
        if (this.scope == Google2Scope.EMAIL) {
            this.scopeValue = this.EMAIL_SCOPE;
        } else if (this.scope == Google2Scope.PROFILE) {
            this.scopeValue = this.PROFILE_SCOPE;
        } else {
            this.scopeValue = this.PROFILE_SCOPE + " " + this.EMAIL_SCOPE;
        }
        GoogleAppsDomainApi20 api = createApi();
        String domain_ = getDomain();
        GoogleAppsDomainOAuthConfig config = new GoogleAppsDomainOAuthConfig(
                this.key, this.secret, this.callbackUrl,
                SignatureType.Header, this.scopeValue, null);
        config.setDomain(domain_);
        this.service = new ProxyOAuth20ServiceImpl(api, config,
                this.connectTimeout, this.readTimeout, this.proxyHost,
                this.proxyPort, false, true);
    }

    protected GoogleAppsDomainApi20 createApi() {
        return new GoogleAppsDomainApi20();
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
    
}
