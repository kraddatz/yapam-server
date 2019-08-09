package app.yapam;

import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Given;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(glue = "app.yapam.stepDefinitions", plugin = {"pretty"})
public class YapamTest {

}
