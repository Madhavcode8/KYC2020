KYC2020 PROJECT FOR WEB SCRAPING
Project: Web Scraper for Alaska Senators Legislature	from {https://akleg.gov/senate.php}
Goal: Automate data extraction from the Alaska State Legislature website and store details in a structured JSON format.
Languages & Tools: Java, Selenium, Jackson, WebDriverManager

STEPS I HAVE PERFORMED IN THE PROJECT FOR SCRAPING DATA USING SELENIUM
1) PROJECT SETUP - Initialize the project and add dependencies in the pom file, dependencies added are:
a)Selenium Webdriver - for website opening and interaction with elements to find HTML elem 
b)WebDriverManager - for matching browser driver to work on the browser
c)Jackson Databind - to convert java obj to JSON

2)Created the class and constructors and defined all required fields according to data present on alaska HTML 

3)Added drivers for opening website and sleep method for crawling time

4)I have configured two sections on alaska Html and inspected html in chrome and extracted data using css selectors
  and as same configured and using scraping tool find 2nd section of data

5)As some fields are empty i have removed duplicates anb removes empty data set

6)Using Jacksons object mapper converted data to JSON

7)Json Printed 

Mentioned all data steps performed and time taken by me in this is 2hours in that time initialization,testing,scraping data, and web crawling html performed

PROJECT DEMO LINK : https://drive.google.com/drive/folders/1CAzXAjYau7N7GFm7M1hsP_uup3tNWdVc?usp=drive_link
