package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class FirstLabTest {

    private WebDriver chromeDriver;

    private static final String nmuBaseUrl = "https://www.nmu.org.ua/ua/";

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        //Run driver
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        //set fullscreen
        chromeOptions.addArguments("--start-fullscreen");
        //setup wait for loading elements
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(15));
        this.chromeDriver = new ChromeDriver(chromeOptions);
    }

    @BeforeMethod
    public void preconditions() {
        //open main page
        chromeDriver.get(nmuBaseUrl);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {chromeDriver.quit();}

    @Test(dependsOnMethods = "testHeaderExists")
    //will be failed - fix it = DONE
    public void testHeaderExists() {
        //find element by id
        WebElement header = chromeDriver.findElement(By.id("heder"));
        //verification
        Assert.assertNotNull(header);
    }

    @Deprecated()
    @Test()
    public void testClickOnForStudent() {
        //find element by xpath
        WebElement forStudentButton = chromeDriver.findElement(By.xpath("/html/body/center/div[4]/div/div[1]/ul/li[3]/a"));
        //verification
        Assert.assertNotNull(forStudentButton);
        forStudentButton.click();
        //verification page changed
        Assert.assertNotNull(chromeDriver.getCurrentUrl(), nmuBaseUrl);
    }

    @Test(dependsOnMethods = "testClickOnForStudent")
    //will be failed - fix it
    public void testSearchFieldOnForStudentPage() {
        String studentPageUrl = "content/student_life/students/";
        chromeDriver.get(nmuBaseUrl + studentPageUrl);
        //find element by tagName
        WebElement searchField = chromeDriver.findElement(By.tagName("input"));
        //verification
        Assert.assertNotNull(searchField);
        //different params of searchField
        System.out.println( String.format("Name attribute: %s", searchField.getAttribute("name")) +
                String.format("\nID attribute: %s", searchField.getAttribute("id")) +
                String.format("\nType attribute: %s", searchField.getAttribute("type")) +
                String.format("\nValue attribute: %s", searchField.getAttribute("value")) +
                String.format("\nPosition: (%d;%d)", searchField.getLocation().x, searchField.getLocation().y) +
                String.format("\nSize: (%dx;%d)", searchField.getSize().height, searchField.getSize().width)
        );
        //input value
        String inputValue = "I need info";
        searchField.sendKeys(inputValue);
        //verification text
        Assert.assertEquals(searchField.getText(), inputValue);
        //click enter
        searchField.sendKeys(Keys.ENTER);
        //verification page changed
        Assert.assertEquals(chromeDriver.getCurrentUrl(), studentPageUrl);
    }

    @Test(dependsOnMethods = "testSlider")
    public void testSlider() {
        //find element by class name
        WebElement nextButton = chromeDriver.findElement(By.className("next"));
        //find element by css selector
        WebElement nextButtonByCss = chromeDriver.findElement(By.cssSelector("a.next"));
        //verification equality
        Assert.assertEquals(nextButton, nextButtonByCss);

        WebElement previousButton = chromeDriver.findElement(By.className("prev"));

        for(int i = 0; i < 20; i++ ) { //change count of iterations just for fun
            if(nextButton.getAttribute("class").contains("disabled")) {
                previousButton.click();
                Assert.assertTrue(previousButton.getAttribute("class").contains("disabled"));
                Assert.assertTrue(nextButton.getAttribute("class").contains("disabled"));
            }
            else {
                nextButton.click();
                Assert.assertTrue(nextButton.getAttribute("class").contains("disabled"));
                Assert.assertFalse(previousButton.getAttribute("class").contains("disabled"));
            }
        }
    }


    //1. Click on the element
    @Test
    public void testClickOnSearchButton() {
        WebElement forStudentButton = chromeDriver.findElement(By.xpath("/html/body/center/div[4]/div/div[1]/ul/li[1]/a"));

        Assert.assertNotNull(forStudentButton);
        forStudentButton.click();

        Assert.assertNotNull(chromeDriver.getCurrentUrl(), nmuBaseUrl);
    }

    //2. Entering data into a field and checking the field for data availability
    @Test
    public void testSearchFieldOnInputSearch() {
        String searchPageUrl = nmuBaseUrl +"search/";
        chromeDriver.get(searchPageUrl);

        WebElement searchField = chromeDriver.findElement(By.id("gsc-i-id1"));

        Assert.assertNotNull(searchField);

        System.out.println( String.format("Name attribute: %search", searchField.getAttribute("name")) +
                String.format("\nID attribute: %s", searchField.getAttribute("id")) +
                String.format("\nType attribute: %s", searchField.getAttribute("type")) +
                String.format("\nValue attribute: %s", searchField.getAttribute("value")) +
                String.format("\nPosition: (%d;%d)", searchField.getLocation().x, searchField.getLocation().y) +
                String.format("\nSize: (%dx;%d)", searchField.getSize().height, searchField.getSize().width)
        );

        searchField.sendKeys("I need info");
        searchField.sendKeys(Keys.ENTER);

        Assert.assertEquals(chromeDriver.getCurrentUrl(), searchPageUrl);
    }

    //3. Finding an element using indirect XPath (using functions or unique identifiers) - 4. Checking any condition
    @Test
    public void testSliderSearch() {
        String searchPageUrl = "search/";
        chromeDriver.get(nmuBaseUrl + searchPageUrl);

        WebElement nextButton = chromeDriver.findElement(By.className("gsc-search-button"));

        WebElement nextButtonByCss = chromeDriver.findElement(By.cssSelector("button.gsc-search-button"));

        Assert.assertEquals(nextButton.getText(), nextButtonByCss.getText());
        Assert.assertTrue(nextButtonByCss.getAttribute("className").contains("gsc-search-button"));
    }
}