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

    public static class Senator {
        public String name;
        public String position;
        public String address;
        public String phone;
        public String email;
        public String url;

        public Senator(String name, String position, String address, String phone, String email, String url) {
            this.name = name;
            this.position = position;
            this.address = address;
            this.phone = phone;
            this.email = email;
            this.url = url;
        }
    }

    public static void main(String[] args) {
        try {
            WebDriverManager.chromedriver().setup();
            WebDriver driver = new ChromeDriver();

            driver.get("https://akleg.gov/senate.php");
            Thread.sleep(4000); // wait for the page to load fully

            // ✅ The senator boxes are inside <li> inside <ul class="people-list">
            List<WebElement> senatorCards = driver.findElements(By.cssSelector("ul.people-list > li"));

            List<Senator> senatorList = new ArrayList<>();

            for (WebElement card : senatorCards) {
                // ✅ Extract details using your HTML structure
                String name = safeGetText(card, By.cssSelector("strong.name"));
                String position = safeGetText(card, By.cssSelector("span.position"));
                String address = safeGetText(card, By.cssSelector("ul.list-info li:nth-child(1)"));
                String phone = safeGetText(card, By.cssSelector("ul.list-info li:nth-child(2)"));
                String email = safeGetText(card, By.cssSelector("ul.list-info li:nth-child(3) a"));
                String profileUrl = safeGetAttribute(card, By.cssSelector("a"), "href");

                senatorList.add(new Senator(name, position, address, phone, email, profileUrl));
            }

            driver.quit();

            // ✅ Write to JSON
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("senators.json"), senatorList);

            System.out.println("✅ Data scraped and saved to senators.json!");
            System.out.println("Total senators found: " + senatorList.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String safeGetText(WebElement parent, By by) {
        try {
            return parent.findElement(by).getText().trim();
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
