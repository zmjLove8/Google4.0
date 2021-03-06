package widgets.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import util.webElementUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by paddy.zhong on 7/18/2017.
 */
public class LoginPage {

    WebDriver driver ;
    public Logger logger = org.apache.logging.log4j.LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());




    public LoginPage(){
        System.out.println("Init driver");
        this.driver = initDriver();
    }

    @BeforeTest
    public void openChrome() throws InterruptedException {
        loginStep();

    }

    @AfterTest
    public void closeChrome(){
        driver.close();
    }

    public void loginStep() throws InterruptedException {
        driver.get("chrome-extension://pgjpmeckehbghpkamdammcgmmmbojbdi/background.html"); //open Google
        Thread.sleep(3000);
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("toggleEnv()");
        Thread.sleep(3000);
        WebElement element = webElementUtils.findElement(widgetBasePage.SERVER_BOX);
        element.clear();
        element.sendKeys("https://api-up.lab.rcch.ringcentral.com");
        webElementUtils.findElement(widgetBasePage.ENABLE_BUTTON).click();
        webElementUtils.findElement(widgetBasePage.SAVE_BUTTON).click(); // change environment
        Thread.sleep(3000);
        webElementUtils.findElement(widgetBasePage.SINGIN_BUTTON).click();
    }

    private WebDriver initDriver() {
        String path = System.getProperty("user.dir");
        String osName = System.getProperty("os.name");
        logger.info("OS name is " + osName);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("load-extension=" + path + "//extension//rc");
        if (osName.equals("Mac OS X")){
            logger.info("chromedriver for Mac OS is running");
            System.setProperty("webdriver.chrome.driver", path+"//drivers//chromedriver");
        }else if (osName.equals("Linux")){
            logger.info("chromedriverLinux for Linux is running");
            System.setProperty("webdriver.chrome.driver", path + "//drivers//chromedriverLinux");
        }else {
            logger.info("chromedriver.exe for Windows is running");
            System.setProperty("webdriver.chrome.driver", path + "//drivers//chromedriver.exe");
        }
//        final DesiredCapabilities capabilities = new DesiredCapabilities();
//        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        driver = new ChromeDriver(options);
        webElementUtils.driver = driver;
        return driver;
    }



    @Test
    public void login(String username, String pwd) throws InterruptedException {
        openChrome();
        String handle = driver.getWindowHandle();
        for (String handles : driver.getWindowHandles()){
            if(handles.equals(handle)){
                continue;
            }
            driver.switchTo().window(handles);
        }
        new WebDriverWait(driver,60).until(ExpectedConditions.numberOfElementsToBe(By.xpath("//*[@id=\"credential\"]"),1));
        WebElement element2 = webElementUtils.findElement(widgetBasePage.FIRST_USERNAME_BOX);
        element2.clear();
        element2.sendKeys(username);
        WebElement webElement =webElementUtils.findElement(widgetBasePage.NEXT_BUTTON);
        webElement.click();
        Thread.sleep(6000);
        WebElement element1 = webElementUtils.findElement(widgetBasePage.SECOND_USERNAME_BOX);
        element1.clear();
        element1.sendKeys(username);
        WebElement password = webElementUtils.findElement(widgetBasePage.PASSWORD_BOX);
        password.clear();
        password.sendKeys(pwd);
        webElementUtils.findElement(widgetBasePage.SING_BUTTON).click();
        driver.switchTo().window(handle);
        webElementUtils.findElement(widgetBasePage.CALLHISTORY).click();
        Thread.sleep(3000);
    }


    public static void success() throws InterruptedException {
        webElementUtils.findElement(widgetBasePage.COMPOSE_TEXT).click();
        Thread.sleep(3000);
    }


}
