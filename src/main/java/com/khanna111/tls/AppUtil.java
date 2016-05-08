package com.khanna111.tls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * 
 * @author gkhanna
 *
 */
@Component
public class AppUtil {

    @Autowired
    private AppConfiguration configuration;
    
    public AppConfiguration getAppConfig() {
	return configuration;
    }
    
}
