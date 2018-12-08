package com.kewpanel.moddownloader.proxy;

import com.kewpanel.moddownloader.Config;
import com.kewpanel.moddownloader.Main;
import com.kewpanel.moddownloader.Ref;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonProxy {

    public static Configuration config;
    public String path;

    public void preInit(FMLPreInitializationEvent e) {
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "moddownloader.cfg"));
        Config.readConfig();
        path = directory.getParent() + "\\mods";
        Ref.MC_VERSION = Minecraft.getMinecraft().getVersion();
        Config.modsList.removeIf(String::isEmpty);
        for (int i = 0; i < Config.modsList.size(); i++) {
            getMod(Config.modsList.get(i), Config.modsNameList.get(i), false);
        }
        Config.curseforgeModsList.removeIf(String::isEmpty);
        Config.curseforgeModsList.forEach(this::getModFromCurse);
    }

    public void init(FMLInitializationEvent e) {
    }

    public void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) {
            config.save();
        }
    }
    public void getMod(String url, String filename, boolean fromCurse) {
        try {
            downloadFileFromURL(url, filename, fromCurse);
        } catch (IOException e) {
            Main.logger.error(e.getMessage());
        }
    }


    public void getModFromCurse(String mod) {
        try {
            URL website = new URL("https://minecraft.curseforge.com/projects/" + mod + "/files");
            URLConnection connection = website.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            String content = scanner.next();
            Pattern pattern = Pattern.compile("project-file-list-item(?:.(?!project-file-list-item))*.(?s).*<span class=\"version-label\">" + Ref.MC_VERSION, Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(content);
            String fileName = "";
            String modURL = "";
            if (matcher.find()) {
                pattern = Pattern.compile("projects/" + mod + "/files/.*./download");
                matcher = pattern.matcher(matcher.group());
                if (matcher.find()) {
                    modURL = "https://minecraft.curseforge.com/" + matcher.group();
                    website = new URL(modURL.substring(0, modURL.length() - 8));
                    connection = website.openConnection();
                    connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                    scanner = new Scanner(connection.getInputStream());
                    scanner.useDelimiter("\\Z");
                    String filePage = scanner.next();
                    pattern = Pattern.compile(">.*.jar");
                    matcher = pattern.matcher(filePage);

                    if (matcher.find()) fileName = (matcher.group().substring(1));
                }
            }
            getMod(modURL, fileName, true);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void downloadFileFromURL(String FILE_URL, String filename, Boolean fromCurse) throws IOException {
        String output = path + "\\" + filename;
        Boolean exists = new File(output).exists();
        if (!exists) {
            String modType = fromCurse ? "CurseForge mod" : "mod";
            Main.logger.log(Level.INFO, "Started downloading " + modType + ": " + filename + " from " + FILE_URL);
            URL website = new URL(FILE_URL);
            URLConnection connection = website.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            InputStream is = connection.getInputStream();
            ReadableByteChannel rbc = Channels.newChannel(is);
            FileOutputStream fos = new FileOutputStream(output);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            Main.logger.log(Level.INFO, "Finished downloading " + modType + ": " + filename);
        } else {
            Main.logger.log(Level.INFO, "Mod " + filename + " has already on the folder so it won't be downloaded again");
        }
    }
}
