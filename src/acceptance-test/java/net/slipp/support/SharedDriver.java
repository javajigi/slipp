package net.slipp.support;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class SharedDriver extends EventFiringWebDriver {
    private static final WebDriver REAL_DRIVER = WebDriverFactory.createWebDriver();
    private static final Thread CLOSE_THREAD = new Thread() {
        @Override
        public void run() {
            REAL_DRIVER.quit();
        }
    };
 
    static {
        Runtime.getRuntime().addShutdownHook(CLOSE_THREAD);
    }
 
    public SharedDriver() {
        super(REAL_DRIVER);
    }
 
    @Override
    public void close() {
        if(Thread.currentThread() != CLOSE_THREAD) {
            throw new UnsupportedOperationException("You shouldn't close this WebDriver. It's shared and will close when the JVM exits.");
        }
        super.quit();
    }
 
    public void deleteAllCookies() {
        manage().deleteAllCookies();
    }
}
