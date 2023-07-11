package net.mindoth.shadowizardlib.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class ThanksList {
    private static volatile String thanksMap = "";

    private static boolean startedLoading = false;
    public static void firstStart() {
        if (!startedLoading) {
            Thread thread = new Thread(ThanksList::parsed);
            thread.setName("Thread for supporters of Mindoth's mods");
            thread.setDaemon(true);
            thread.start();

            startedLoading = true;
        }
    }
    public static String getThanksMap() {
        return thanksMap;
    }
    private static String getUrl() {
        return "https://raw.githubusercontent.com/Mindoth/shadowizardlib/main/thanks.json";
    }
    private static String fetch(String urlString) throws IOException {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) buffer.append(chars, 0, read);
            return buffer.toString();
        }
        finally {
            if (reader != null) reader.close();
        }
    }
    private static void parsed() {
        try {
            thanksMap = fetch(getUrl()).replace("\n", "");
        }
        catch (Exception e) {
            System.out.println("Something went wrong with getting the thanks list. Either you're offline or GitHub is down. If neither is true report to the mod creator");
        }
    }
}
