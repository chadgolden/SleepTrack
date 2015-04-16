package com.chadgolden.sleeptrack.web;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

/**
 * Created by Chad on 4/9/2015.
 */
public class WebLoginRequestThread implements Runnable {

    private String link;
    private String email;
    private String password;

    private volatile boolean loginSuccessful;

    public WebLoginRequestThread(String link, String email, String password) {
        this.link = link;
        this.email = email;
        this.password = password;
    }

    @Override
    public void run() {
        try {
            String linkWithGetVars = link + "?email=" + email + "&password=" +
                    password;

            URL url = new URL(linkWithGetVars);

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(linkWithGetVars));
            HttpResponse response = client.execute(request);
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            reader.close();
            System.err.println("StringBuilder: " + sb.toString());
            loginSuccessful = sb.toString().equals("\"Login successful\"");
            int foo = 1;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            loginSuccessful = false;
        }
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
}
