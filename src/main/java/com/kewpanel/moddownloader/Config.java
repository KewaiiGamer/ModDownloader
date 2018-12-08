package com.kewpanel.moddownloader;

import com.kewpanel.moddownloader.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Config {

    private static final String CATEGORY_GENERAL = "general";
    private static final String CATEGORY_CURSEFORGE = "curseforge";

    public static String[] mods = {""};
    public static String[] modsName = {""};
    public static String[] curseforgeMods = {""};
    public static List<String> modsList = new ArrayList<>();
    public static List<String> modsNameList = new ArrayList<>();
    public static List<String> curseforgeModsList = new ArrayList<>();

    public static void readConfig() {
        Configuration cfg = CommonProxy.config;
        cfg.load();
        initGeneralConfig(cfg);
    }

    public static void initGeneralConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General");
        cfg.addCustomCategoryComment(CATEGORY_CURSEFORGE, "Curseforge Mods");
        mods = cfg.getStringList("mods", CATEGORY_GENERAL, mods, "Define the urls for the mods you want to download.");
        modsName = cfg.getStringList("mods filename", CATEGORY_GENERAL, modsName, "Define the file names for the mods you want to download.");
        curseforgeMods = cfg.getStringList("mods", CATEGORY_CURSEFORGE, curseforgeMods, "Define the mods name in the curseforge url. Example: https://minecraft.curseforge.com/projects/mod-downloader/files becomes mod-downloader.");
        Collections.addAll(modsList, mods);
        Collections.addAll(modsNameList, modsName);
        Collections.addAll(curseforgeModsList, curseforgeMods);
    }
}
