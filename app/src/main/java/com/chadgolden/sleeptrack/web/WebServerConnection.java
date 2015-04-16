package com.chadgolden.sleeptrack.web;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by chad on 4/9/15.
 */
public class WebServerConnection {

    private static final String link = "http://192.168.1.101/sleeptrack/index.php/User_controller/login_get";
    private static final boolean connectionSuccessful = true;
    private static final boolean connectionUnsuccessful = false;

    private static WebLoginRequestThread loginRequestThread;

    public static boolean loginPost(final String email, final String password) {
        loginRequestThread = new WebLoginRequestThread(link, email, password);
        try {
            Thread thread =
            new Thread(loginRequestThread);
//            requestThread = new Thread(
//                    new Runnable() {
//                        private volatile boolean loginSuccessful;
//
//                        @Override
//                        public void run() {
//                            try {
//                                String linkWithGetVars = link + "?email=" + email + "&password=" +
//                                        password;
//
//                                URL url = new URL(linkWithGetVars);
//
//                                HttpClient client = new DefaultHttpClient();
//                                HttpGet request = new HttpGet();
//                                request.setURI(new URI(linkWithGetVars));
//                                HttpResponse response = client.execute(request);
//                                BufferedReader reader = new BufferedReader
//                                        (new InputStreamReader(response.getEntity().getContent()));
//
//
////                                String data  = URLEncoder.encode("email", "UTF-8")
////                                        + "=" + URLEncoder.encode(email, "UTF-8");
////                                data += "&" + URLEncoder.encode("password", "UTF-8")
////                                        + "=" + URLEncoder.encode(password, "UTF-8");
////                                URLConnection conn = url.openConnection();
////
////                                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
////                                wr.write( data );
////                                wr.flush();
////                                BufferedReader reader = new BufferedReader(
////                                        new InputStreamReader(conn.getInputStream())
////                                );
//                                StringBuilder sb = new StringBuilder();
//                                String line = null;
//                                while ((line = reader.readLine()) != null) {
//                                    sb.append(line);
//                                    break;
//                                }
//                                reader.close();
//                                System.err.println("StringBuilder: " + sb.toString());
//
//                            } catch (Exception ex) {
//                                System.err.println(ex.getMessage());
//                            }
//                        }
//                    }
//
//            );
                thread.start();
            thread.join();
//            requestThread.start();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return loginRequestThread.isLoginSuccessful();
    }

}
