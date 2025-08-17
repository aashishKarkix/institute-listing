package com.institute.listing.core.config;

import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.writers.CompositeHeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SecurityHeadersConfig {

    private static final String CACHE_CONTROL = "Cache-Control";
    private static final String PRAGMA = "Pragma";
    private static final String EXPIRES = "Expires";
    private static final String X_FRAME_OPTIONS = "X-Frame-Options";
    private static final String X_XSS_PROTECTION = "X-XSS-Protection";
    private static final String CONTENT_SECURITY_POLICY = "Content-Security-Policy";
    private static final String X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";
    private static final String CONNECTION = "Connection";

    private static final String CACHE_CONTROL_VALUE = "public, max-age=31536000, immutable";
    private static final String PRAGMA_VALUE = "no-cache";
    private static final String EXPIRES_VALUE = "0";
    private static final String X_FRAME_OPTIONS_VALUE = "DENY";
    private static final String X_XSS_PROTECTION_VALUE = "1; mode=block";
    private static final String CONTENT_SECURITY_POLICY_VALUE =
            "default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self' data:; font-src 'self';";
    private static final String X_CONTENT_TYPE_OPTIONS_VALUE = "nosniff";
    private static final String CONNECTION_VALUE = "keep-alive";

    public HeaderWriter getDefaultHeaders() {
        return new CompositeHeaderWriter(Arrays.asList(
                new StaticHeadersWriter(CACHE_CONTROL, CACHE_CONTROL_VALUE),
                new StaticHeadersWriter(PRAGMA, PRAGMA_VALUE),
                new StaticHeadersWriter(EXPIRES, EXPIRES_VALUE),
                new StaticHeadersWriter(X_FRAME_OPTIONS, X_FRAME_OPTIONS_VALUE),
                new StaticHeadersWriter(X_XSS_PROTECTION, X_XSS_PROTECTION_VALUE),
                new StaticHeadersWriter(CONTENT_SECURITY_POLICY, CONTENT_SECURITY_POLICY_VALUE),
                new StaticHeadersWriter(X_CONTENT_TYPE_OPTIONS, X_CONTENT_TYPE_OPTIONS_VALUE),
                new StaticHeadersWriter(CONNECTION, CONNECTION_VALUE)
        ));
    }
}
