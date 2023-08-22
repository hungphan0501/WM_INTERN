package com.example.demo.webscrape;

import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WebScrapeController {

    @GetMapping("/web-scrape2")
    @ResponseBody
    public List<List<String>> webScrape() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--log-level=OFF");
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\hungp\\Downloads\\chromedriver-win64\\chromedriver.exe");

        List<List<String>> columnData = new ArrayList<>();
        try {
            WebDriver driver = new ChromeDriver();
            driver.get("https://vnexpress.net/kinh-doanh");

            List<WebElement> rows = driver.findElements(By.xpath("//table//tr"));

            for (WebElement row : rows) {
                List<String> rowData = new ArrayList<>();
                List<WebElement> cells = row.findElements(By.tagName("td"));

                if (!containsTableStructureKeywords(row)) {
                    for (WebElement cell : cells) {
                        rowData.add(cell.getText().trim());
                    }

                    if (!rowData.isEmpty()) {
                        columnData.add(rowData);
                    }
                }
            }

            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return columnData;
    }

    private boolean containsTableStructureKeywords(WebElement element) {
        String[] keywords = {"<table>", "<th>", "<tr>", "<td>", "<caption>", "<colgroup>", "<col>", "<thead>", "<tbody>", "<tfoot>"};
        for (String keyword : keywords) {
            if (element.getText().contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
