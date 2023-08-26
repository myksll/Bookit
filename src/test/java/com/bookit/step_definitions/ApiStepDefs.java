package com.bookit.step_definitions;

import com.bookit.pages.SignInPage;
import com.bookit.utilities.BookitUtils;
import com.bookit.utilities.ConfigurationReader;
import com.bookit.utilities.DB_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.Map;

import static io.restassured.RestAssured.*;

public class ApiStepDefs {
    String token ;
    Response response;
    @Given("I logged Bookit api as a {string}")
    public void i_logged_bookit_api_as_a(String role) {

        token=BookitUtils.generateTokenByRole(role);
        System.out.println("token = " + token);

    }
    @When("I sent get request to {string} endpoint")
    public void i_sent_get_request_to_endpoint(String endpoint) {
        response = given().accept(ContentType.JSON)
                .header("Authorization", token)
                .when().get(ConfigurationReader.getProperty("base_url") + endpoint);
    }
    @Then("status code should be {int}")
    public void status_code_should_be(int expectedStatusCode) {
        System.out.println("response.statusCode() = " + response.statusCode());
        //verify status code
        Assert.assertEquals(expectedStatusCode,response.statusCode());
    }
    @Then("content type is {string}")
    public void content_type_is(String expectedContentType) {
        System.out.println("response.contentType() = " + response.contentType());
        //verify content type
        Assert.assertEquals(expectedContentType,response.contentType());
    }
    @Then("role is {string}")
    public void role_is(String expectedRole) {

        response.prettyPrint();
        String actualRole= response.path("role");
        System.out.println("actualRole = " + actualRole);

        Assert.assertEquals(expectedRole,actualRole);

    }

    @Then("the information about current user from api and database should match")
    public void the_information_about_current_user_from_api_and_database_should_match() {
        //Get data from api..actual
        JsonPath jsonPath = response.jsonPath();

        /**
         * {
         *     "id": 18608,
         *     "firstName": "Raymond",
         *     "lastName": "Reddington",
         *     "role": "student-team-member"
         * }
         */
        String actualFirstName = jsonPath.getString("firstName");
        String actualLastName = jsonPath.getString("lastName");
        String actualRole = jsonPath.getString("role");
        int actualId = jsonPath.getInt("id");

        System.out.println("actualLastName = " + actualLastName);
        System.out.println("actualFirstName = " + actualFirstName);
        System.out.println("actualRole = " + actualRole);
        System.out.println("actualId = " + actualId);

        //get data from database--expected
        //first we need to create Database connection which will handle by custom hooks
        String query="SELECT firstname,lastname,role,id from users\n" +
                "where email='raymond@cydeo.com'";
        //RUN YOUR QUERY
        DB_Util.runQuery(query);
        //get the result to map
        Map<String, String> dbMap = DB_Util.getRowMap(1);
        System.out.println("dbMap = " + dbMap);

        String expectedFirstName=dbMap.get("firstname");
        String expectedLastName=dbMap.get("lastname");
        String expectedRole=dbMap.get("role");
        int expectedId= Integer.parseInt(dbMap.get("id"));


        //COMPARE API AND  DATABASE
        Assert.assertEquals(expectedFirstName,actualFirstName);
        Assert.assertEquals(expectedLastName,actualLastName);
        Assert.assertEquals(expectedRole,actualRole);
        Assert.assertEquals(expectedId,actualId);

    }

}
