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
            Thread.sleep(4000); // wait for page to load fully

            List<Senator> senatorList = new ArrayList<>();

            // ✅ --- SECTION 1: scrape from <ul class="people-list"> ---
            List<WebElement> senatorCards = driver.findElements(By.cssSelector("ul.people-list > li"));
            for (WebElement card : senatorCards) {
                String name = safeGetText(card, By.cssSelector("strong.name"));
                String position = safeGetText(card, By.cssSelector("span.position"));
                String address = safeGetText(card, By.cssSelector("ul.list-info li:nth-child(1)"));
                String phone = safeGetText(card, By.cssSelector("ul.list-info li:nth-child(2)"));
                String email = safeGetText(card, By.cssSelector("ul.list-info li:nth-child(3) a"));
                String profileUrl = safeGetAttribute(card, By.cssSelector("a"), "href");

                senatorList.add(new Senator(name, position, address, phone, email, profileUrl));
            }

            // ✅ --- SECTION 2: scrape from <ul class="item"> (your new section) ---
            List<WebElement> peopleItems = driver.findElements(By.cssSelector("ul.item > li"));
            for (WebElement person : peopleItems) {
                String url = safeGetAttribute(person, By.cssSelector("a"), "href");
                String city = safeGetText(person, By.xpath(".//dt[text()='City:']/following-sibling::dd[1]"));
                String party = safeGetText(person, By.xpath(".//dt[text()='Party:']/following-sibling::dd[1]"));
                String district = safeGetText(person, By.xpath(".//dt[text()='District:']/following-sibling::dd[1]"));
                String phone = safeGetText(person, By.xpath(".//dt[text()='Phone:']/following-sibling::dd[1]"));
                String tollFree = safeGetText(person, By.xpath(".//dt[text()='Toll-Free:']/following-sibling::dd[1]"));

                // Name might be in the link text if not present in other tags
                String name = safeGetText(person, By.cssSelector("a"));
                String position = party; // optional - reuse or leave blank

                senatorList.add(new Senator(name, position, city + " | " + district, phone, tollFree, url));
            }

            driver.quit();

            // ✅ --- Write to JSON ---
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("senators.json"), senatorList);

            System.out.println("✅ Data scraped and saved to senators.json!");
            System.out.println("Total entries found: " + senatorList.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper methods
    private static String safeGetText(WebElement parent, By by) {
        try {
            return parent.findElement(by).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    private static String safeGetAttribute(WebElement parent, By by, String attr) {
        try {
            return parent.findElement(by).getAttribute(attr).trim();
        } catch (Exception e) {
            return "";
        }
    }
}
//updated now