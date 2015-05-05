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

    /**
     * Gets the apps domain that has been set, or null if none has been set.
     * @return the apps domain; may be null
     */
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
    
    public String getScopeName() {
        Google2Scope scope_ = super.getScope();
        if (scope_ == null) {
            return null;
        }
        return scope_.name();
    }
    
    /**
     * Sets the scope as specified by a given scope name. The scope name must
     * be a case-insensitive match for one of the {@code Google2Scope}
     * enum constants.
     * @param scopeName the scope name
     * @throws IllegalArgumentException if the scope name does not match a 
     * constant (exception would be thrown by 
     * {@link Enum#valueOf(java.lang.Class, java.lang.String) })
     */
    public void setScopeName(String scopeName) throws IllegalArgumentException {
        Google2Scope scope_;
        if (scopeName == null) {
            scope_ = null;
        } else {
            String normalizedScopeName = normalizeScopeName(scopeName);
            scope_ = Google2Scope.valueOf(normalizedScopeName);
        }
        setScope(scope_);
    }
    
    protected String normalizeScopeName(String scopeName) {
        if (scopeName == null) {
            return null;
        }
        return scopeName.toUpperCase();
    }
}
