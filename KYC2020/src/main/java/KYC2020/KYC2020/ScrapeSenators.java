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

    // Class matching your JSON structure
    public static class Senator {
        public String name;
        public String title;
        public String party;
        public String profile;
        public String dob;
        public String type;
        public String country;
        public String url;
        public String otherinfo;

        public Senator(String name, String title, String party, String profile,
                       String dob, String type, String country, String url, String otherinfo) {
            this.name = name;
            this.title = title;
            this.party = party;
            this.profile = profile;
            this.dob = dob;
            this.type = type;
            this.country = country;
            this.url = url;
            this.otherinfo = otherinfo;
        }
    }

    public static void main(String[] args) {
        try {
            // Setup Chrome browser
            WebDriverManager.chromedriver().setup();
            WebDriver driver = new ChromeDriver();

            driver.get("https://akleg.gov/senate.php");
            Thread.sleep(3000);

            // Find senator cards
            List<WebElement> senators = driver.findElements(By.cssSelector(".people-list .person"));

            List<Senator> senatorList = new ArrayList<>();

            for (WebElement s : senators) {
                String name = safeGetText(s, By.cssSelector("h3"));
                String party = safeGetText(s, By.xpath(".//p[contains(text(),'Party')]"));
                String profileUrl = safeGetAttribute(s, By.cssSelector("a"), "href");

                // Construct a short profile text (can be updated)
                String profile = "Senator profile page for " + name;

                // Create object with blanks for missing fields
                Senator senator = new Senator(
                        name,
                        "Senator",       // title
                        party,
                        profile,
                        "",              // dob
                        "Senate",        // type
                        "USA",           // country
                        profileUrl,
                        ""               // otherinfo
                );

                senatorList.add(senator);
            }

            driver.quit();

            // Save to JSON
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("senators.json"), senatorList);

            System.out.println("✅ Data scraped and saved to senators.json!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
