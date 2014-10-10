package net.slipp.domain.summary;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;

public enum SiteDefaultLogo {
	dzone("http://java.dzone.com", "http://java.dzone.com/sites/all/themes/dzone2012/images/mh_dzone_logo.png"), 
	theserverside("http://www.theserverside.com", "http://media.techtarget.com/rms/ux/images/theserverside/headerLogoBandBackground.png"),
	okjsp("http://www.okjsp.net", "http://www.okjsp.net/images/okjsp_160x50.gif");
	

	private String domain;
	private String logoPath;

	SiteDefaultLogo(String domain, String logoPath) {
		this.domain = domain;
		this.logoPath = logoPath;
	}

	public String getDomain() {
		return domain;
	}

	public String getLogoPath() {
		return logoPath;
	}
	
	public static String findDefaultLogo(String path){
		String baseUrl = SiteDefaultLogo.getBaseUrl(path);
		
		if( StringUtils.isBlank(baseUrl) ){
			return null;
		}
		SiteDefaultLogo[] siteDefaultLogos = SiteDefaultLogo.values();
		for (SiteDefaultLogo siteDefaultLogo : siteDefaultLogos) {
			if( StringUtils.contains(siteDefaultLogo.getDomain(), baseUrl)){
				return siteDefaultLogo.getLogoPath();
			}
		}
		return null;
	}
	
	public static String getBaseUrl(String path) {
		try {
			URL url = new URL(path);
			return url.getHost();
		} catch (MalformedURLException e) {}
		return null;
	}
}
