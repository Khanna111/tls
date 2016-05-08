package com.khanna111.tls;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * @author gkhanna
 *
 */
@ConfigurationProperties(prefix = "khanna111.tls", ignoreUnknownFields = false)
@Component
public class AppConfiguration {

    private String execPoolSize;

    private String topSitesPath;

    private String connectTimeOutMs;

    private String readTimeOutMs;

    public String getExecPoolSize() {
        return execPoolSize;
    }

    public void setExecPoolSize(String execPoolSize) {
        this.execPoolSize = execPoolSize;
    }

    public String getTopSitesPath() {
        return topSitesPath;
    }

    public void setTopSitesPath(String topSitesPath) {
        this.topSitesPath = topSitesPath;
    }

    public String getConnectTimeOutMs() {
        return connectTimeOutMs;
    }

    public void setConnectTimeOutMs(String connectTimeOutMs) {
        this.connectTimeOutMs = connectTimeOutMs;
    }

    public String getReadTimeOutMs() {
        return readTimeOutMs;
    }

    public void setReadTimeOutMs(String readTimeOutMs) {
        this.readTimeOutMs = readTimeOutMs;
    }

}
