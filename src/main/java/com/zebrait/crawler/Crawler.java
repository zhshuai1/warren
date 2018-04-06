package com.zebrait.crawler;

import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

@Log4j2
public class Crawler {
    public String getContent(String url) {
        return getContent(url, null);
    }

    public String getContent(String urlStr, String pattern) {
        URL url = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            url = new URL(urlStr);
            InputStream is = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String l;

            while ((l = reader.readLine()) != null) {
                if (null == pattern) {
                    stringBuilder.append(l).append("\n");
                } else {
                    if (l.matches(pattern)) {
                        stringBuilder.append(l).append("\n");
                    }
                }
            }
        } catch (IOException e) {
            log.error("Failed to retrieve the content from {}", urlStr, e);
        }
        return stringBuilder.toString();
    }
}
