package com.bookit.step_definitions;

import com.bookit.pages.SelfPage;
import com.bookit.pages.SignInPage;
import com.bookit.utilities.ConfigurationReader;
import com.bookit.utilities.Driver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class MyInfoStepDefs {
    @Given("user logs in using {string} credentials")
    public void userLogsInUsingCredentials(String role) {

        Driver.get().get(ConfigurationReader.getProperty("base_url"));
        Driver.get().manage().window().maximize();
        SignInPage signInPage = new SignInPage();
        signInPage.login(role);

    }

    @When("user is on the my self page")
    public void user_is_on_the_my_self_page() {
        SelfPage selfPage = new SelfPage();
        selfPage.goToSelf();

    }




}
