package com.ll.commars.global.app;

import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
public class AppConfig {

	@Getter
	private static String siteFrontUrl;
	@Getter
	private static String siteBackUrl;
	@Getter
	private static String siteCookieDomain;
    /*
    @Value("${custom.site.frontUrl}")
    public void setSiteFrontUrl(String siteFrontUrl) {
        this.siteFrontUrl = siteFrontUrl;
    }

    @Value("${custom.site.backUrl}")
    public void setSiteBackUrl(String siteBackUrl) {
        this.siteBackUrl = siteBackUrl;
    }

    @Value("${custom.site.cookieDomain}")
    public void setSiteCookieDomain(String siteCookieDomain) {
        this.siteCookieDomain = siteCookieDomain;
    }*/
}
