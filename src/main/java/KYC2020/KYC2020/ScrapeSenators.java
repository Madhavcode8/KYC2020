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
        public String tollfree;
        public String url;

        public Senator(String name, String position, String address, String phone, String tollfree, String url) {
            this.name = name;
            this.position = position;
            this.address = address;
            this.phone = phone;
            this.tollfree = tollfree;
            this.url = url;
        }
    }

    public static void main(String[] args) {
        try {
            WebDriverManager.chromedriver().setup();
            WebDriver driver = new ChromeDriver();

            driver.get("https://akleg.gov/senate.php");
            Thread.sleep(4000); // wait for page load

            List<Senator> senatorList = new ArrayList<>();
            Set<String> seenUrls = new HashSet<>();

            List<WebElement> senatorCards = driver.findElements(By.cssSelector("ul.people-list > li"));
            for (WebElement card : senatorCards) {
                String name = safeGetText(card, By.cssSelector("strong.name"));
                String position = safeGetText(card, By.cssSelector("span.position"));
                String address = safeGetText(card, By.cssSelector("ul.list-info li:nth-child(1)"));
                String phone = safeGetText(card, By.cssSelector("ul.list-info li:nth-child(2)"));
                String tollfree = safeGetText(card, By.cssSelector("ul.list-info li:nth-child(3) a"));
                String profileUrl = safeGetAttribute(card, By.cssSelector("a"), "href");

                if (!name.isEmpty() && !profileUrl.isEmpty() && seenUrls.add(profileUrl)) {
                    senatorList.add(new Senator(name, position, address, phone, tollfree, profileUrl));
                }
            }

            List<WebElement> peopleItems = driver.findElements(By.cssSelector("ul.item > li"));
            for (WebElement person : peopleItems) {
                String url = safeGetAttribute(person, By.cssSelector("a"), "href");
                String name = safeGetText(person, By.cssSelector("a"));
                String city = safeGetText(person, By.xpath(".//dt[text()='City:']/following-sibling::dd[1]"));
                String party = safeGetText(person, By.xpath(".//dt[text()='Party:']/following-sibling::dd[1]"));
                String district = safeGetText(person, By.xpath(".//dt[text()='District:']/following-sibling::dd[1]"));
                String phone = safeGetText(person, By.xpath(".//dt[text()='Phone:']/following-sibling::dd[1]"));
                String tollFree = safeGetText(person, By.xpath(".//dt[text()='Toll-Free:']/following-sibling::dd[1]"));

                String position = party.isEmpty() ? "" : party;
                String address = (city + " | " + district).trim();
                String tollfree = tollFree;

                if (!name.isEmpty() && !url.isEmpty() && seenUrls.add(url)) {
                    senatorList.add(new Senator(name, position, address, phone, tollfree, url));
                }
            }

            driver.quit();

            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("senators_clean.json"), senatorList);

            System.out.println("Clean data saved to senators_clean.json!");
            System.out.println("Total unique senators found: " + senatorList.size());

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
            return parent.findElement(by).getAttribute(attr).trim();
        } catch (Exception e) {
            return "";
        }
    }
}
// final json //
