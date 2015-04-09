package com.chadgolden.sleeptrack.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by chad on 4/9/15.
 */
public class WebServerConnection {

    private static final String link = "localhost/sleeptrack/login";

    public void loginPost(String username, String password) {
        try {
            URL url = new URL(link);
            String data  = URLEncoder.encode("username", "UTF-8")
                    + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8")
                    + "=" + URLEncoder.encode(password, "UTF-8");
            URLConnection conn = url.openConnection();

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
