package stellarium;

import java.io.IOException;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public interface IProxy {
	
	public void preInit(FMLPreInitializationEvent event) throws IOException;
	
    public void load(FMLInitializationEvent event);

    public void postInit(FMLPostInitializationEvent event);

}
