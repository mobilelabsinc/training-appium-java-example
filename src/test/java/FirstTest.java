import appium.AppiumController;
import appium.AppiumUtilities;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class FirstTest extends AppiumController {

    @Factory(dataProvider = "deviceList")
    public FirstTest(String udid, String platformName,
                           String bundleID, String automationName) throws Exception {
        this.udid = udid;
        this.bundleID = bundleID;
        this.automationName = automationName;
        this.platform = OperatingSystem.valueOf(platformName);

        //Load deviceConnect properties from file used for every test connection
        Properties props = new Properties();
        props.load(new FileReader(new File(AppiumUtilities.DEVICECONNECT_PROPERTIES_FILE)));

        //Load the server connection properties
        server = props.getProperty(AppiumUtilities.DEVICECONNECT_URL);
        username = props.getProperty(AppiumUtilities.DEVICECONNECT_USERNAME);
        apiToken = props.getProperty(AppiumUtilities.DEVICECONNECT_API_KEY);
    }

    @DataProvider(name = "deviceList", parallel=true)
    private static Iterator<Object[]> buildDeviceList() throws IOException {
        List<Object[]> devices = new ArrayList<>();

        //Pull device properties from file
        //Used to run multiple devices in parallel
        Properties props = new Properties();
        props.load(new FileReader(new File(AppiumUtilities.DEVICECONNECT_PROPERTIES_FILE)));

        //Load iOS devices from properties file
        AppiumUtilities.buildDeviceList(devices, props.getProperty(AppiumUtilities.IOS_IDS), props.getProperty(AppiumUtilities.IOS_BUNDLE_ID),
                AppiumUtilities.IOS_PLATFORM_NAME, AppiumUtilities.IOS_AUTOMATION_NAME);

        //Load Android devices from properties file
        AppiumUtilities.buildDeviceList(devices, props.getProperty(AppiumUtilities.ANDROID_IDS), props.getProperty(AppiumUtilities.ANDROID_BUNDLE_ID),
                AppiumUtilities.ANDROID_PLATFORM_NAME, AppiumUtilities.ANDROID_AUTOMATION_NAME);

        return devices.iterator();
    }

    @BeforeClass
    public void setUp() throws Exception {
        //start the appium connection here
        startAppium();

        //Set an implicit wait to handle animation and loading times
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @AfterClass
    public void tearDown() throws Exception {
        stopAppium();
    }

    @Test
    public void firstTest() throws Exception {
        //Find UserName field and enter in UserName
        WebElement userField = driver.findElementById("com.android.controls:id/usernameEditText");
        userField.clear();
        userField.sendKeys("mobilelabs");

        //hide the keyboard if it is still displayed
        AppiumUtilities.hideKeyboard(driver);

        //Find the Password field and enter in password
        WebElement passField = driver.findElementById("com.android.controls:id/passwordEditText");
        passField.clear();
        passField.sendKeys("demo");

        //hide the keyboard if it is still displayed
        AppiumUtilities.hideKeyboard(driver);

        WebElement rememberButton = driver.findElementById("com.android.controls:id/rememberMe");
        rememberButton.click();

        //Find the Sign In button and click it
        WebElement signinButton = driver.findElementById("com.android.controls:id/loginButton");
        signinButton.click();

        //Make sure the next page is displayed
        WebElement itemLabel = driver.findElementById("com.android.controls:id/criteria1Text");
        Assert.assertEquals(true, itemLabel.isDisplayed());
    }

}
