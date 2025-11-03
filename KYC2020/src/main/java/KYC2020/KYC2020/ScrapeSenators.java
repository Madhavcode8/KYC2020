package KYC2020.KYC2020;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.*;

public class ScrapeSenators {

    // A simple class to hold each senator's data
    public static class Senator {
        public String name;
        public String title;
        public String position;
        public String party;
        public String address;
        public String phone;
        public String email;
        public String url;

        public Senator(String name, String title, String position, String party,
                       String address, String phone, String email, String url) {
            this.name = name;
            this.title = title;
            this.position = position;
            this.party = party;
            this.address = address;
            this.phone = phone;
            this.email = email;
            this.url = url;
        }
    }

    public static void main(String[] args) {
        try {
            // Step 1: Setup Chrome browser automatically
            WebDriverManager.chromedriver().clearDriverCache().clearResolutionCache().setup();
            WebDriver driver = new ChromeDriver();

            // Step 2: Open the website
            driver.get("https://akleg.gov/senate.php");
            Thread.sleep(3000); // wait a bit for the page to load

            // Step 3: Find all senators (each inside a div with class 'member')
            List<WebElement> members = driver.findElements(By.cssSelector(".member"));

            // Step 4: Create a list to store all senator objects
            List<Senator> senatorList = new ArrayList<>();

            // Step 5: Loop through each member and extract details
            for (WebElement member : members) {
                String name = safeGetText(member, By.cssSelector("class=\"people-list\""));
                String url = safeGetAttribute(member, By.cssSelector("h3 a"), "href");
                String title = safeGetText(member, By.xpath(".//p[contains(text(),'Title')]"));
                String position = safeGetText(member, By.xpath(".//p[contains(text(),'Position')]"));
                String party = safeGetText(member, By.xpath(".//p[contains(text(),'Party')]"));
                String address = safeGetText(member, By.xpath(".//p[contains(text(),'Address')]"));
                String phone = safeGetText(member, By.xpath(".//p[contains(text(),'Phone')]"));
                String email = safeGetText(member, By.xpath(".//p[contains(text(),'Email')]"));

                senatorList.add(new Senator(name, title, position, party, address, phone, email, url));
            }

            // Step 6: Close the browser
            driver.quit();

            // Step 7: Write data to JSON file
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("senators.json"), senatorList);

            System.out.println("✅ Data scraped and saved to senators.json!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper function to avoid errors if an element is missing
    private static String safeGetText(WebElement parent, By by) {
        try {
            return parent.findElement(by).getText();
        } catch (Exception e) {
            return "";
        }
    }

    private static String safeGetAttribute(WebElement parent, By by, String attr) {
        try {
            return parent.findElement(by).getAttribute(attr);
        } catch (Exception e) {
            return "";
        }
    }
}
