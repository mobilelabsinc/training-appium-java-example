package appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.qameta.allure.Attachment;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class AppiumUtilities {

    public final static String DEVICECONNECT_PROPERTIES_FILE = "deviceconnect.properties";
    public final static String DEVICECONNECT_URL = "deviceconnect.url";
    public final static String DEVICECONNECT_USERNAME = "deviceconnect.username";
    public final static String DEVICECONNECT_API_KEY = "deviceconnect.api.key";

    public final static String IOS_IDS = "ios.ids";
    public final static String IOS_BUNDLE_ID = "ios.bundle.id";
    public final static String IOS_PLATFORM_NAME = "IOS";
    public final static String IOS_AUTOMATION_NAME = "XCUITest";

    public final static String ANDROID_IDS = "android.ids";
    public final static String ANDROID_BUNDLE_ID = "android.bundle.id";
    public final static String ANDROID_PLATFORM_NAME = "ANDROID";
    public final static String ANDROID_AUTOMATION_NAME = "Appium";


    @Attachment(value = "{attachmentName}", type = "image/png")
    public static byte[] getScreenshot(AppiumDriver driver, String attachmentName) throws Exception {
        // make screenshot and get is as base64
        return driver.getScreenshotAs(OutputType.BYTES);
    }

    public static void clearScreenshots() {
        File targetFile = new File("screenshots");
        String[]entries = targetFile.list();
        if (entries != null) {
            for(String s: entries){
                File currentFile = new File(targetFile.getPath(),s);
                currentFile.delete();
            }
            targetFile.delete();
        }
    }

    public static void buildDeviceList(List<Object[]> list, String deviceList, String bundleId,
                                        String platformName, String automationName) {
        if (deviceList != null && !deviceList.trim().isEmpty()) {
            for (String device : deviceList.split(",")) {
                list.add(new Object[]{
                        device.trim(), platformName, bundleId, automationName
                });
            }
        }
    }


    public static String getPlatform(AppiumDriver driver){
        return driver.getCapabilities().getCapability("platform").toString().toUpperCase();
    }

    public static void hideKeyboard(AppiumDriver driver) {
        try{
            if (getPlatform(driver).equals("IOS")) {
                MobileElement keyboardDone = (MobileElement) driver.findElement(By.xpath("//*[@name='Done']"));
                keyboardDone.click();
            }
            else if (getPlatform(driver).toUpperCase().equals("ANDROID")) {
                driver.hideKeyboard();
            }
        }
        catch (Exception ex){
            //do nothing
        }
    }

}
