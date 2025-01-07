package com.reverb.app.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class AppProperties {

    private final JwtProperties jwt = new JwtProperties();

    public JwtProperties getJwt() {
        return jwt;
    }

    public static class JwtProperties {
        private String secret;
        private String issuer;
        private String audience;

        // Getters and Setters
        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getIssuer() {
            return issuer;
        }

        public void setIssuer(String issuer) {
            this.issuer = issuer;
        }

        public String getAudience() {
            return audience;
        }

        public void setAudience(String audience) {
            this.audience = audience;
        }
    }
}