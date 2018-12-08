package com.kewpanel.moddownloader;

import com.kewpanel.moddownloader.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;


@Mod(modid = Ref.MODID, name = Ref.NAME, version = Ref.VERSION_MAJOR + "." + Ref.VERSION_MINOR + "." + Ref.VERSION_BUILD)
public class Main {

    @Mod.Instance(Ref.MODID)
    public static Main instance = new Main();

    @SidedProxy(clientSide = Ref.CLIENT_PROXY, serverSide = Ref.SERVER_PROXY)
    public static CommonProxy proxy = new CommonProxy();

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        proxy.preInit(e);
        logger = e.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }

}
