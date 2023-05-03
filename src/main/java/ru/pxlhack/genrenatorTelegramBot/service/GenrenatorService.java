package ru.pxlhack.genrenatorTelegramBot.service;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
public class GenrenatorService {
    public static String getGenre() {
        StringBuilder response = new StringBuilder();
        try {

            URL url = new URL("https://binaryjazz.us/wp-json/genrenator/v1/genre/");

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

        } catch (MalformedURLException e) {
            log.error("Malformed URL: " + e.getMessage());
        } catch (IOException e) {
            log.error("I/O Error: " + e.getMessage());
        }
        String responseString = response.substring(1, response.length() - 1);
        return responseString;
    }
}
