package com.example.demo.webscrape;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class WebScrape {

    @GetMapping("/web-scrape")
    public List<List<String>> webScrape() {
        final String url = "https://www.w3schools.com/html/html_tables.asp";

        List<List<String>> columnData = new ArrayList<>();
        try {
            final Document document = Jsoup.connect(url).get();

            Element table = document.select("table").first();
            Elements rows = table.select("tr");

            for (Element row : rows) {
                Elements cells = row.select("td");

                for (int columnIndex = 0; columnIndex < cells.size(); columnIndex++) {
                    Element cell = cells.get(columnIndex);
                    if (columnIndex >= columnData.size()) {
                        columnData.add(new ArrayList<>());
                    }
                    columnData.get(columnIndex).add(cell.text());
                }
            }

            return columnData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
