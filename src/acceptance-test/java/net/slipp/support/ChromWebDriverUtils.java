package net.slipp.support;

public class ChromWebDriverUtils {
    private static String OS = System.getProperty("os.name").toLowerCase();
    
    public static String getChromeWebDriverPath() {
        return ChromWebDriverUtils.class.getResource("/chromedriver/" + getChromeWebDriverFileNameForOS()).getPath();
    }
    
    private static String getChromeWebDriverFileNameForOS() {
        if (isWindows()) {
            return "chromedriver-win.exe";
        }
        if (isMac()) {
            return "chromedriver-mac";
        }

        throw new RuntimeException("지원하는 OS 가 없습니다.");
    }

    private static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    private static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }
}
