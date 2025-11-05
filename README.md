#  KYC2020 Project

**Source URL:** [https://akleg.gov/senate.php](https://akleg.gov/senate.php)

**Goal:** Automate data extraction from the Alaska State Legislature website and store details in a structured **JSON** format.

**Languages & Tools:** **Java**, **Selenium**, **Jackson**, **WebDriverManager**

---

## 🛠️ Steps Performed in the Project for Scraping Data Using Selenium

1.  **PROJECT SETUP**: Initialize the project and add dependencies in the `pom.xml` file.
    * **Dependencies added are:**
        * **Selenium Webdriver** - For website opening and interaction with elements to find HTML elements.
        * **WebDriverManager** - For matching the browser driver to the browser.
        * **Jackson Databind** - To convert Java objects to JSON.
2.  Created the class and constructors and defined all required fields according to data present on the Alaska HTML.
3.  Added drivers for opening the website and a `sleep` method for crawling time.
4.  Configured two sections on the Alaska HTML, inspected HTML in Chrome, and extracted data using **CSS selectors**. The second section of data was configured and found using the scraping tool.
5.  Removed duplicates and empty data sets, as some fields were empty.
6.  Converted data to **JSON** using Jackson's `ObjectMapper`.
7.  Printed the resulting JSON.

---

## ⏱️ Project Summary

All data steps performed, including initialization, testing, data scraping, and web crawling HTML, took a total time of **2 hours**.

---

## 🔗 Project Demo Link

[Google Drive Folder](https://drive.google.com/drive/u/0/folders/1CAzXAjYau7N7GFm7M1hsP_uup3tNWdVc)
