package com.kewpanel.moddownloader;

import com.kewpanel.moddownloader.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Config {

    private static final String CATEGORY_CURSEFORGE = "curseforge";

    public static String[] curseforgeMods = {""};
    public static List<String> curseforgeModsList = new ArrayList<>();

    public static void readConfig() {
        Configuration cfg = CommonProxy.config;
        cfg.load();
        initGeneralConfig(cfg);
    }

    public static void initGeneralConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_CURSEFORGE, "Curseforge Mods");
        curseforgeMods = cfg.getStringList("mods", CATEGORY_CURSEFORGE, curseforgeMods, "Define the mods name in the curseforge url. Example: https://minecraft.curseforge.com/projects/mod-downloader/files becomes mod-downloader.");
        Collections.addAll(curseforgeModsList, curseforgeMods);
    }
}
