import appium.AppiumController;
import appium.AppiumUtilities;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
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

public class SecondTest extends AppiumController {

    @Factory(dataProvider = "deviceList")
    public SecondTest(String udid, String platformName,
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
    @Feature("Search")
    @Story("Check Quantity")
    @Description("Verifies that after a search, the quantity count is correct.")
    public void secondTest() throws Exception {
        try {
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

            //Find the Sign In button and click it
            AppiumUtilities.getScreenshot(driver, "1_before_login");
            WebElement signinButton = driver.findElementById("com.android.controls:id/loginButton");
            signinButton.click();

            //Find the Manufacturer dropdown
            AppiumUtilities.getScreenshot(driver, "2_search_page");
            WebElement spinner = driver.findElementById("com.android.controls:id/searchSpinner");
            spinner.click();

            //Find the Samsung item and select it
            WebElement samsung = driver.findElementByXPath(".//*[@text='Samsung']");
            samsung.click();
            AppiumUtilities.getScreenshot(driver, "3_search_dropdown");

            WebElement checkBox = driver.findElementById("com.android.controls:id/criteria3RadioButton3");
            if(checkBox.getAttribute("checked").equalsIgnoreCase("false")) {
                System.out.println("Not Checked!");
            }

            //Set Operating System and Inventory controls
            checkBox.click();

            if(checkBox.getAttribute("checked").equalsIgnoreCase("true")) {
                System.out.println("Checked!");
            }

            WebElement radio = driver.findElementById("com.android.controls:id/criteria4RadioButton1");
            radio.click();

            //Find and click the search button
            AppiumUtilities.getScreenshot(driver, "4_before_search");
            WebElement searchButton = driver.findElementById("com.android.controls:id/searchButton");
            searchButton.click();

            //Click on the first element in the list
            AppiumUtilities.getScreenshot(driver, "5_item_list");
            WebElement nestedItemElement = driver.findElementById("com.android.controls:id/text1");
            nestedItemElement.click();

            //Store the quantity of the "Droid Charge" device
            AppiumUtilities.getScreenshot(driver, "6_details_page");
            WebElement element = driver.findElementById("com.android.controls:id/productQtyOnHandText1");
            String qty = element.getText();

            //Assert (pass the test) if the quantity is 561
            Assert.assertEquals("561", qty);

            //Click the back button
            driver.navigate().back();
            AppiumUtilities.getScreenshot(driver, "7_back_item_list");

            //Click the more options button
            WebElement moreOptions = driver.findElementByXPath("//android.widget.ImageButton[@content-desc=\"More options\"]");
            moreOptions.click();
            AppiumUtilities.getScreenshot(driver, "8_more_options_click");

            //Find and click the logout button
            WebElement logout = driver.findElementByXPath("//*[@text='Log Out']");
            logout.click();
            AppiumUtilities.getScreenshot(driver, "9_after_logout");
        }
        catch (Exception e) {
            //output the error messages
            System.out.println("Error performing Appium test: " + e.getMessage());
            e.printStackTrace();

            //take screenshot of the error page
            AppiumUtilities.getScreenshot(driver, "FAIL_exception");

            //throw e to cause test to fail
            throw e;
        }
    }
}
