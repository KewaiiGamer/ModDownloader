package com.kewpanel.moddownloader.proxy;

import com.kewpanel.moddownloader.Config;
import com.kewpanel.moddownloader.Main;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class CommonProxy {

    public static Configuration config;
    public String path;

    public static String getFileNameFromURL(String url) {
        if (url == null) {
            return "";
        }
        try {
            URL resource = new URL(url);
            String host = resource.getHost();
            if (host.length() > 0 && url.endsWith(host)) {
                // handle ...example.com
                return "";
            }
        } catch (MalformedURLException e) {
            return "";
        }

        int startIndex = url.lastIndexOf('/') + 1;
        int length = url.length();

        // find end index for ?
        int lastQMPos = url.lastIndexOf('?');
        if (lastQMPos == -1) {
            lastQMPos = length;
        }

        // find end index for #
        int lastHashPos = url.lastIndexOf('#');
        if (lastHashPos == -1) {
            lastHashPos = length;
        }

        // calculate the end index
        int endIndex = Math.min(lastQMPos, lastHashPos);
        return url.substring(startIndex, endIndex);
    }

    public void preInit(FMLPreInitializationEvent e) {
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "moddownloader.cfg"));
        Config.readConfig();
        path = directory.getParent() + "\\mods";
    }

    public void init(FMLInitializationEvent e) {
        Config.modsList.removeIf(String::isEmpty);
        Config.modsList.forEach(this::getMod);
    }

    public void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) {
            config.save();
        }
    }

    public void getMod(String url) {
        try {
            downloadFilesFromURL(url);
        } catch (IOException e) {
            Main.logger.error(e.getMessage());
        }
    }

    public void downloadFilesFromURL(String FILE_URL) throws IOException {
        String filename = getFileNameFromURL(FILE_URL);
        if (!filename.isEmpty()) {
            String output = path + "\\" + filename;
            Boolean exists = new File(output).exists();
            if (!exists) {
                Main.logger.log(Level.INFO, "Started downloading mod: " + filename);
                URL website = new URL(FILE_URL);
                URLConnection connection = website.openConnection();
                connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                InputStream is = connection.getInputStream();
                ReadableByteChannel rbc = Channels.newChannel(is);
                FileOutputStream fos = new FileOutputStream(output);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                Main.logger.log(Level.INFO, "Finished downloading mod: " + filename);
            } else {
                Main.logger.log(Level.INFO, "Mod " + filename + " has already on the folder so it won't be downloaded again");
            }
        }
    }
}
