package com.summer.lijiahao.script.studio;

import com.summer.lijiahao.base.NccEnvSettingService;

import java.io.*;

public class StudioUtil {
    private static final String UAPHOME_FOLDERNAME = "UAP";
    private static final String LANG_FILE = ".lang";
    private static final NccEnvSettingService envSettingService = NccEnvSettingService.getInstance();
    private static String studioHome;

    public static final String getStudioHome() {
        if (studioHome == null) {
            File file = new File(System.getProperty("user.dir"));
            studioHome = file.getParent();
        }
        return studioHome;
    }

    public static final String getNCHome() {
        return envSettingService.getNcHomePath();
    }

    public static String getDefaultNCHome() {
        return getStudioHome() + File.separator + "UAP";
    }

    public static String getStudioLang() {
        return "zh_CN";
    }

    public static String getStudioPersistenceLang() {
        String file = getStudioHome() + File.separator + ".lang";
        File langFile = new File(file);
        if (!langFile.exists())
            return null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(langFile));
            return reader.readLine().trim();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException iOException) {
                }
        }
        return null;
    }

    public static boolean setStudioLang(String lang) {
        if (lang == null)
            lang = "en";
        String file = getStudioHome() + File.separator + ".lang";
        File langFile = new File(file);
        FileWriter writer = null;
        try {
            writer = new FileWriter(langFile);
            writer.write(lang);
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException e) {
                }
        }
        return true;
    }
}
