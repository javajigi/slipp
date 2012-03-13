package supports;

import org.apache.commons.lang.StringUtils;

import play.Play;

public class Slipp {
	public static boolean isDevMode() {
		if (StringUtils.isBlank(Play.id)) {
			return true;
		}
		
		if ("dev".equals(Play.id)){
			return true;
		}
		
		return false;
	}

	public static boolean isTestMode() {
		if (isDevMode()) {
			return false;
		}
		
		if ("test".equals(Play.id)){
			return true;
		}
		
		return false;
	}

	
}
