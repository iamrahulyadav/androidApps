package com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ABC on 4/16/2018.
 */

class DownloadUrl {
    public String readUrl(String myUrl) throws IOException {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection  = null;

        try {
            URL url = new URL(myUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            String urlRes = urlConnection.getResponseMessage();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer sb = new StringBuffer();

                String line = "";

                while ((line = br.readLine()) != null)
                {
                    sb.append(line);
                }
                data = sb.toString();
                br.close();
            }

        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        finally {
            if (inputStream != null) {
                inputStream.close();
            }
            urlConnection.disconnect();
        }

        return data;
    }

}
