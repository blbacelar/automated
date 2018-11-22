import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.android.connection.ConnectionStateBuilder;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.junit.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ErrorCollector;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.net.URL;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;

public class AbbvieTests extends ErrorCollector {

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    private String invalidEmail = "invalid@email.com";
    private String validEmail = "acc_test_2@curatio.me";
    private String unRegisteredEmail = "emailnotregistered@email.com";
    private String password = "A123456a";
    private String errorMessage = "Username or password is wrong! Please try again!";

    private String signupEmail = "acc_test_12@curatio.me";
    private String sigupInvalidName = "Nam";
    private String sigupValidName = "NameTest";

    private String[] textInputErrorMsgList = new String[100];
    private String invalidSpaceCharInput = "Please use latin characters while signing in (A-Z and numbers 1-10)";

    private String postTitleMoreThan80 = "This title is a test to verify if it is being validate the title size. This title is too long";
    private String postTitleValid = "Valid title";
    private String postDescription = "Description for the post";

    AndroidDriver driver;

    @Before
    public void setup() throws Exception{

        initializeMessageErrors();

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("reportDirectory", "/Users/curatio/Desktop/");
        capabilities.setCapability("reportFormat", "XML");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "8.1");
        capabilities.setCapability("deviceName", "Pixel 2");
        capabilities.setCapability("app", "/Users/curatio/Desktop/app-abbvie-debug.apk");
        capabilities.setCapability("automationName", "uiautomator2");

        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"),capabilities);
    }

    @After
    public void tearDown() throws Exception{
        driver.quit();
    }


    public void initializeMessageErrors(){

        // setup error messages
        textInputErrorMsgList[0] = "Your display name cannot be less than 4 characters long";
        textInputErrorMsgList[1] = "Email address’s format is not correct.";
        textInputErrorMsgList[2] = "Min. 8 characters, 1 uppercase letter, 1 lowercase letter, 1 number, A-Z and numbers 1-10.";
        textInputErrorMsgList[3] = "That email is already taken / in use.";
        textInputErrorMsgList[4] = "To be able to join ThaliMe, you need to accept our Terms of Use and Privacy Statement.";
        textInputErrorMsgList[5] = "Sorry, we could not recognize this email. Please use the email that you used to register for the AbbVie Care program.";
        textInputErrorMsgList[6] = "The user with email already exists. Please log in with your existing password.";
        textInputErrorMsgList[7] = "Sorry, we were not able to login at the moment. Please try again later.";
        textInputErrorMsgList[8] = "File too large. Please select a file 20MB or smaller.";
    }

    public void loginApp(Boolean validLogin){

        String email;

        if (validLogin) {
            email = validEmail;
        } else {
            email = invalidEmail;
        }


        // Wait until app finishes loading screen and click on Login button

        WebElement loginButton = (new WebDriverWait(driver,15))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("me.curatio.acc_a.debug:id/start_login_btn")));


        loginButton.click();


        // Input an invalid email

        WebElement emailTextField = (new WebDriverWait(driver,15))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("me.curatio.acc_a.debug:id/Login_email_editText")));
        emailTextField.sendKeys(email);

        WebElement passwordTextField = driver.findElement(By.id("me.curatio.acc_a.debug:id/Login_password_editText"));
        passwordTextField.sendKeys(password);

        WebElement login = driver.findElement(By.id("me.curatio.acc_a.debug:id/Login_login_button"));
        login.click();


        // if is an invalid login test if the app shows the error message

        if (!validLogin) {
            WebElement toastView = (new WebDriverWait(driver, 15))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.Toast[1]")));
//        String text = toastView.getAttribute("name");
//        System.out.print(text);

            assertEquals(toastView.getText(), errorMessage);
        }

    }

    public void signUpValidUser(){

        WebElement sigupButton = (new WebDriverWait(driver,30))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("me.curatio.acc_a.debug:id/start_sign_up_btn")));

        sigupButton.click();

        // get all the elements from the screen.
        WebElement titleToolBar = driver.findElement(By.id("me.curatio.acc_a.debug:id/detail_toolbar_title"));

        assertEquals(titleToolBar.getText(), "Sign Up");

        WebElement nickNameTextField = driver.findElement(By.id("me.curatio.acc_a.debug:id/SignUp_nick_name_editText"));
        WebElement emailTextField = driver.findElement(By.id("me.curatio.acc_a.debug:id/SignUp_email_editText"));
        WebElement passwordTextField = driver.findElement(By.id("me.curatio.acc_a.debug:id/SignUp_password_editText"));
        WebElement signupContinueButton = driver.findElement(By.id("me.curatio.acc_a.debug:id/SignUp_signup_button"));

        // Valid signup nickname, email and password
        nickNameTextField.clear();
        nickNameTextField.sendKeys(sigupValidName);

        emailTextField.clear();
        emailTextField.sendKeys(signupEmail);

        passwordTextField.clear();
        passwordTextField.sendKeys(password);

        signupContinueButton.click();
    }


    @Test
    @DisplayName("Login on the app with wrong e-mail")
    public void loginInvalidEmail(){
        loginApp(false);
    }

    @Test
    @DisplayName("Login on the app sucessfully")
    public void loginSucessfully(){
        loginApp(true);

        sleep(15000);

        WebElement homeButton = null;
        Boolean isPresent = driver.findElements(By.id("me.curatio.acc_a.debug:id/bb_bottom_bar_icon")).size() > 0;

        if (isPresent) {
            homeButton  = driver.findElement(By.id("me.curatio.acc_a.debug:id/bb_bottom_bar_icon"));
            homeButton.click();
        } else {
            WebElement subscribe  = driver.findElement(By.id("android:id/button1"));
            subscribe.click();
            homeButton.click();
        }
    }

    @Test
    @DisplayName("Create a post")
    public void createPost(){

        loginSucessfully();

        // Click on the fab button
        WebElement fabButton = (new WebDriverWait(driver,15))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("me.curatio.acc_a.debug:id/fab_expand_menu_button")));

        fabButton.click();

        // after click on the post button
        WebElement post = (new WebDriverWait(driver,15))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("me.curatio.acc_a.debug:id/Compose_story")));
        post.click();


        // Input title for your post
        WebElement titleTextField = (new WebDriverWait(driver,15))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("me.curatio.acc_a.debug:id/compose_title")));

        // title cannot has more than 80 chars.

        titleTextField.sendKeys(postTitleMoreThan80);

        WebElement textLenght = driver.findElement(By.id("me.curatio.acc_a.debug:id/text_length"));
        collector.checkThat(textLenght.getText(),equalTo("80/80"));
        titleTextField.clear();

        // Input a valid title
        titleTextField.sendKeys(postTitleValid);

        // Input description
        WebElement postDescriptionTextField = driver.findElement(By.id("me.curatio.acc_a.debug:id/compose_description"));
        postDescriptionTextField.sendKeys(postDescription);

        WebElement done = driver.findElement(By.id("me.curatio.acc_a.debug:id/add_asset_done"));
        done.click();


        // after posting, it should be shown on the news feed
        WebElement feedPostTitle = (new WebDriverWait(driver,15))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("me.curatio.acc_a.debug:id/asset_title")));

        collector.checkThat(feedPostTitle.getText(),equalTo(postTitleValid));
    }


    @Test
    @DisplayName("Create a post with rich media")
    public void createPostRichMedia(){
        loginSucessfully();

        // Click on the fab button
        WebElement fabButton = (new WebDriverWait(driver,15))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("me.curatio.acc_a.debug:id/fab_expand_menu_button")));

        fabButton.click();

        // after click on the post button
        WebElement post = (new WebDriverWait(driver,15))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("me.curatio.acc_a.debug:id/Compose_story")));
        post.click();


        // Input title for your post
        WebElement titleTextField = (new WebDriverWait(driver,15))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("me.curatio.acc_a.debug:id/compose_title")));

        titleTextField.sendKeys(postTitleValid);

        // Input description
        WebElement postDescriptionTextField = driver.findElement(By.id("me.curatio.acc_a.debug:id/compose_description"));
        postDescriptionTextField.sendKeys(postDescription);

        // Button to add an image
        WebElement addImage = driver.findElement(By.id("me.curatio.acc_a.debug:id/photo"));
        addImage.click();

        // select from Gallery
        WebElement gallery = driver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v7.widget.LinearLayoutCompat/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout[1]/android.widget.TextView"));
        gallery.click();

        // allow device have access to the gallery
        WebElement allowAccess = driver.findElement(By.id("com.android.packageinstaller:id/permission_allow_button"));
        allowAccess.click();

        // image from gallery

        Boolean isPresent = driver.findElements(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout/android.widget.LinearLayout/android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.RelativeLayout/android.widget.RelativeLayout")).size() > 0;

        if (isPresent) {

            WebElement selectedMedia = driver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout/android.widget.LinearLayout/android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.RelativeLayout/android.widget.RelativeLayout"));
            selectedMedia.click();

            // add video
            WebElement addVideo = driver.findElement(By.id("me.curatio.acc_a.debug:id/video"));
            addVideo.click();
            gallery.click();

            // validate media size
            selectedMedia = driver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout/android.widget.LinearLayout/android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.RelativeLayout/android.widget.FrameLayout/android.widget.ImageView[1]"));
            selectedMedia.click();

            WebElement toastView = (new WebDriverWait(driver, 15))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.Toast[1]")));

            collector.checkThat(toastView.getText(),equalTo(textInputErrorMsgList[8]));

            // select a valid media
            selectedMedia = driver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout/android.widget.LinearLayout/android.support.v7.widget.RecyclerView/android.widget.LinearLayout[2]/android.widget.RelativeLayout/android.widget.FrameLayout/android.widget.ImageView[1]"));
            selectedMedia.click();
        } else {

            driver.pressKeyCode(AndroidKeyCode.BACK);

        }




        // done
        WebElement done = driver.findElement(By.id("me.curatio.acc_a.debug:id/add_asset_done"));
        done.click();

        sleep(4000);

        // after posting, it should be shown on the news feed
        WebElement feedPostTitle = (new WebDriverWait(driver,15))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("me.curatio.acc_a.debug:id/asset_title")));

        collector.checkThat(feedPostTitle.getText(),equalTo(postTitleValid));


//
    }

    @Test
    @DisplayName("Sign up a new user")
    public void signingupUser() {

        WebElement messageError = null;

        WebElement sigupButton = (new WebDriverWait(driver,15))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("me.curatio.acc_a.debug:id/start_sign_up_btn")));


        sigupButton.click();

        // get all the elements from the screen.
        WebElement titleToolBar = (new WebDriverWait(driver,15))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("me.curatio.acc_a.debug:id/detail_toolbar_title")));

        collector.checkThat("Let's sign you up", equalTo(titleToolBar.getText()));

        WebElement nickNameTextField = driver.findElement(By.id("me.curatio.acc_a.debug:id/SignUp_nick_name_editText"));
        WebElement emailTextField = driver.findElement(By.id("me.curatio.acc_a.debug:id/SignUp_email_editText"));
        WebElement passwordTextField = driver.findElement(By.id("me.curatio.acc_a.debug:id/SignUp_password_editText"));
        WebElement signupContinueButton = driver.findElement(By.id("me.curatio.acc_a.debug:id/SignUp_signup_button"));
        WebElement checkBoxReceiveEmails = driver.findElement(By.id("me.curatio.acc_a.debug:id/receive_checkbox"));
        WebElement checkBoxTermsUse = driver.findElement(By.id("me.curatio.acc_a.debug:id/terms_of_use_checkbox"));
        WebElement checkBoxGuideline = driver.findElement(By.id("me.curatio.acc_a.debug:id/privacy_statement_checkbox"));


        // button should be disable
        signupContinueButton.isEnabled();


        collector.checkThat(signupContinueButton.isEnabled(), equalTo(false));


        // validate if nick name is correct

        nickNameTextField.sendKeys(sigupInvalidName);

        WebElement textInputError = (new WebDriverWait(driver,15))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("me.curatio.acc_a.debug:id/textinput_error")));

        collector.checkThat(textInputError.getText(),equalTo(textInputErrorMsgList[0]));


        // if the nickname contains a space char.

        nickNameTextField.clear();
        nickNameTextField.sendKeys(" ");

        WebElement toastView = (new WebDriverWait(driver, 15))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.Toast[1]")));

        collector.checkThat(toastView.getText(),equalTo(invalidSpaceCharInput));


        // Valid signup nickname.
        nickNameTextField.clear();
        nickNameTextField.sendKeys(sigupValidName);

        // inputing an invalid email
        emailTextField.clear();
        emailTextField.sendKeys("t@e");
        textInputError = (new WebDriverWait(driver,15))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("me.curatio.acc_a.debug:id/textinput_error")));

        collector.checkThat(textInputError.getText(),equalTo(textInputErrorMsgList[1]));

        emailTextField.clear();
        emailTextField.sendKeys(unRegisteredEmail);

        // input an invalid password
        passwordTextField.sendKeys("A1234");
        textInputError = (new WebDriverWait(driver,15))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("me.curatio.acc_a.debug:id/textinput_error")));

        collector.checkThat(textInputError.getText(),equalTo(textInputErrorMsgList[2]));

        // input a valid passoword
        passwordTextField.clear();
        passwordTextField.sendKeys(password);

        // button should be enable
        signupContinueButton.isEnabled();
        collector.checkThat(signupContinueButton.isEnabled(), equalTo(true));

        // check terms of use
        checkBoxTermsUse.click();
        signupContinueButton.click();

        messageError = (new WebDriverWait(driver,15))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("android:id/message")));
        collector.checkThat(messageError.getText(),equalTo(textInputErrorMsgList[4]));
        WebElement errorButtonMessage1 = driver.findElement(By.id("android:id/button1"));

        errorButtonMessage1.click();
        sleep(1000);


        // check Community Guideline
        checkBoxGuideline.click();
        signupContinueButton.click();

        // Email not registered in AbbVie Care Circle:
        toastView = (new WebDriverWait(driver, 15))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.Toast[1]")));
        collector.checkThat(toastView.getText(),equalTo(textInputErrorMsgList[5]));


        sleep(1000);

        // inputing an email already registered
        emailTextField.clear();
        emailTextField.sendKeys(validEmail);
        signupContinueButton.click();

        toastView = (new WebDriverWait(driver, 15))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.Toast[1]")));

        collector.checkThat(toastView.getText(),equalTo(textInputErrorMsgList[6]));

        sleep(2000);


        // Valid email, but unable to login:
        driver.setConnection(new ConnectionStateBuilder().withWiFiDisabled().build());
        emailTextField.clear();
        emailTextField.sendKeys(signupEmail);
        signupContinueButton.click();

        toastView = (new WebDriverWait(driver, 15))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.Toast[1]")));

        collector.checkThat(toastView.getText(),equalTo(textInputErrorMsgList[7]));

        // inputing a signup valid email
        driver.setConnection(new ConnectionStateBuilder().withWiFiEnabled().build());
        sleep(2000);
        emailTextField.clear();
        emailTextField.sendKeys(signupEmail);
        signupContinueButton.click();
    }


//    @Test
//    @DisplayName("Testing onBoarding")
    public void onBoardingTesting(){
        signUpValidUser();


        // Verify if the screen is for selecting a community
        WebElement titleToolBar = (new WebDriverWait(driver,15))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.LinearLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.RelativeLayout/android.widget.TextView[1]")));

        assertEquals(titleToolBar.getText(),"Select a Community");

        // select a community to join
        WebElement scrollView = (new WebDriverWait(driver, 15))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("me.curatio.acc_a.debug:id/badge_recycler_view")));

        scrollUpDown(true);
        scrollUpDown(false);


        WebElement joinCommunity = driver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.support.v7.widget.RecyclerView/android.widget.LinearLayout[2]/android.widget.RelativeLayout[2]/android.widget.TextView[2]"));
        joinCommunity.click();


        WebElement communityTitleToolBar = (new WebDriverWait(driver,20))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("me.curatio.acc_a.debug:id/spinner_target")));

        //assertEquals(communityTitleToolBar.getText(), "ThaliMe (English)");

        WebElement homeButton = driver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.RelativeLayout/android.widget.LinearLayout[2]/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout[1]/android.widget.TextView"));
        homeButton.click();

        WebElement profileButton = (new WebDriverWait(driver,15))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.RelativeLayout/android.widget.LinearLayout[2]/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout[4]/android.widget.TextView")));
        profileButton.click();

        WebElement letsGoButton = (new WebDriverWait(driver,15))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("me.curatio.acc_a.debug:id/onboarding_overlay_btn")));

        letsGoButton.click();

        // get myProfile Elements
        WebElement myRoleSpinner = driver.findElement(By.id("me.curatio.acc_a.debug:id/onboarding_profile_role_spinner"));
        WebElement myDiagnosisSpinner = driver.findElement(By.id("me.curatio.acc_a.debug:id/onboarding_profile_diagnosis"));
        WebElement myGenderMan = driver.findElement(By.id("me.curatio.acc_a.debug:id/account_info_gender_man"));
        WebElement myGenderWoman = driver.findElement(By.id("me.curatio.acc_a.debug:id/account_info_gender_woman"));
        WebElement myGenderOther = driver.findElement(By.id("me.curatio.acc_a.debug:id/account_info_gender_not_disclose"));

        // select a role
        myRoleSpinner.click();

        WebElement myRoleSelected = driver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.CheckedTextView[2]"));
        myRoleSelected.click();


        // select a diagnose
        myDiagnosisSpinner.click();

        WebElement diagnosisOkButton = driver.findElement(By.id("me.curatio.acc_a.debug:id/onboarding_diagnosis_ok_btn"));
        diagnosisOkButton.click();

        // verify if it has been selected a diagnoses
        WebElement toastView = (new WebDriverWait(driver, 15))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.Toast[1]")));

        assertEquals(toastView.getText(), "Select a Diagnose");


        WebElement myDiagnosesSelected = driver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.TextView"));
        myDiagnosesSelected.click(); // select diagnosis
        diagnosisOkButton.click();  // click ok button

        // verify if the Diagnoses shown on screen is the same as selected
        assertEquals(myDiagnosesSelected.getText(),myDiagnosisSpinner.getText());

        // select gender
        // if select the gender male, female and other should not be selected
        myGenderMan.click();
        assertEquals(true, myGenderMan.isSelected());
        assertEquals(false, myGenderWoman.isSelected());
        assertEquals(false, myGenderOther.isSelected());

        // scroll down
        scrollUpDown(true);

        WebElement myAgeSeekBar = driver.findElement(By.id("me.curatio.acc_a.debug:id/account_info_age_seek_bar"));
        WebElement myCountry = driver.findElement(By.id("me.curatio.acc_a.debug:id/location_country_spinner"));
        WebElement myRegion = driver.findElement(By.id("me.curatio.acc_a.debug:id/location_region_spinner"));
        WebElement myCity = driver.findElement(By.id("me.curatio.acc_a.debug:id/location_city_spinner"));
        WebElement myStoryTextField = driver.findElement(By.id("me.curatio.acc_a.debug:id/onboarding_profile_story_tex"));
        WebElement myInterests = driver.findElement(By.id("me.curatio.acc_a.debug:id/onboarding_profile_interest"));
        WebElement saveButton = driver.findElement(By.id("me.curatio.acc_a.debug:id/onboarding_save_btn"));
    }

    private void scrollUpDown(Boolean sDown) {
        //if pressX was zero it didn't work for me
        int pressX = driver.manage().window().getSize().width / 2;
        // 4/5 of the screen as the bottom finger-press point
        int bottomY = driver.manage().window().getSize().height * 4/5;
        // just non zero point, as it didn't scroll to zero normally
        int topY = driver.manage().window().getSize().height / 8;
        //scroll with TouchAction by itself
        if (sDown) {
            scroll(pressX, bottomY, pressX, topY);
        } else {
            scroll(pressX, topY, pressX, bottomY);
        }
    }
//  *
//  * don't forget that it's "natural scroll" where
//  * fromY is the point where you press the and toY where you release it
//  *
    private void scroll(int fromX, int fromY, int toX, int toY) {
        TouchAction action = new TouchAction(driver);
        action.press(PointOption.point(fromX, fromY)).waitAction(WaitOptions.waitOptions(ofSeconds(1)))
                .moveTo(PointOption.point(toX, toY)).release().perform();
    }


    private void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
