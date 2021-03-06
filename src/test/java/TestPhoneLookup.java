import appium.AppiumController;
import appium.AppiumUtilities;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.*;
import screens.LoginScreen;
import screens.SearchScreen;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;


public class TestPhoneLookup extends AppiumController {

    protected LoginScreen loginScreen;
    protected SearchScreen searchScreen;

    @Factory (dataProvider = "deviceList")
    public TestPhoneLookup(String udid, String platformName,
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

        loginScreen = new LoginScreen(driver);
        searchScreen = new SearchScreen(driver);
    }

    @Test
    @Feature("Login")
    @Story("Valid Login")
    @Description("Verifies that the Search Button appears on the Search Screen after entering the username and password and then clicking the Sign In button on the login screen")
    public void loginTest() throws Exception {
        try {
            AppiumUtilities.getScreenshot(driver, "Launch Screen");
            loginScreen.login("mobilelabs", "demo");
            Assert.assertTrue(searchScreen.isSearchButtonPresent());
            AppiumUtilities.getScreenshot(driver, "Search Screen");
        } catch (Exception ex) {

            //Get screenshot if test fails
            AppiumUtilities.getScreenshot(driver, "Failed - Exception");
            throw ex;
        }
    }

    @Test
    @Feature("Search")
    @Story("Valid Search")
    @Description("Verifies that the list of items is returned after filling out the search form")
    public void searchTest() throws Exception {
        try {
            AppiumUtilities.getScreenshot(driver, "Login Screen");
            searchScreen.fillSearchForm("Droid Charge", "Samsung", true, true, false, false, "In Stock");
            AppiumUtilities.getScreenshot(driver, "Search Results");
        } catch (Exception ex) {

            //Get screenshot if test fails
            AppiumUtilities.getScreenshot(driver, "Failed - Exception");
            throw ex;
        }
    }

    @AfterClass
    public void tearDown() throws Exception {
        stopAppium();
    }

}