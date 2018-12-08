package com.kewpanel.moddownloader;

import com.kewpanel.moddownloader.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Config {

    private static final String CATEGORY_GENERAL = "general";

    public static String[] mods = {""};
    public static List<String> modsList = new ArrayList<>();

    public static void readConfig() {
        Configuration cfg = CommonProxy.config;
        cfg.load();
        initGeneralConfig(cfg);
    }

    public static void initGeneralConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
        mods = cfg.getStringList("mods", CATEGORY_GENERAL, mods, "Define the urls for the mods you want to download.");
        Collections.addAll(modsList, mods);
    }
}
