# example-appium-testng-native-app
Example Appium project using TestNG to allow for parallel cross platform testing

## Prerequisites 

Upload both the Android and iOS PhoneLookup applications to your deviceConnect cart.

## Execution of the sample

To execute these tests, you'll need to rename deviceconnect.properties.default to deviceconnect.properties. Once renamed, you'll need to fill in the following information:

```
# The deviceConnect server URL
# i.e. http://192.0.2.0/Appium
deviceconnect.url=

# Your full username
# i.e. first.last@sample.company
deviceconnect.username=

# Your full api token
# i.e. 00000000-0000-0000-0000-000000000000
deviceconnect.api.key=

# A comma separated list of one or more iOS device ids
# i.e. 0123456789012345678901234567890123456789 or
# 0123456789012345678901234567890123456789,0123456789012345678901234567890123456789
ios.ids=

# The bundleId for your iOS application
# i.e. sample.company.App
ios.bundle.id=

# A comma separated list of one or more Android device ids
# i.e. 0123456789012 or 0123456789012,0123456789012
android.ids=

# The bundleId, and Activity, for your Android application
# i.e. sample.company.App/SampleActivity
android.bundle.id=
```

Run all tests with your IDEs test runner or:

`mvn clean test allure:serve`

To run a specific test:

`mvn clean -Dtest=[TEST_NAME_HERE] test allure:serve`



