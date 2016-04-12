package stellarium.stellars.view;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import stellarium.api.StellarSkyAPI;
import stellarium.config.INBTConfig;

public class PerDimensionSettings implements INBTConfig {

	private String dimensionName;
	
	public double latitude, longitude;
	public boolean patchProvider;
	public boolean hideObjectsUnderHorizon;
	protected boolean allowRefraction;
	public double sunlightMultiplier;
	public double skyDispersionMultiplier;
	public String skyRendererType;
	
	private Property propLatitude, propLongitude;
	private Property propPatchProvider;
	private Property propHideObjectsUnderHorizon;
	private Property propAllowRefraction;
	private Property propSunlightMultiplier;
	private Property propSkyDispersionMultiplier;
	private Property propRenderType;

	public PerDimensionSettings(String dimensionName) {
		this.dimensionName = dimensionName;
	}

	@Override
	public void setupConfig(Configuration config, String category) {
		config.setCategoryComment(category, "Configurations for this dimension.");
		config.setCategoryRequiresWorldRestart(category, true);
		
		List<String> propNameList = Lists.newArrayList();
		
		propPatchProvider = config.get(category, "Patch_Provider", true);
		propPatchProvider.comment = "Determine whether or not patch provider. Cannot adjust longitude and latitude when this is false.";
		propPatchProvider.setRequiresWorldRestart(true);
		propPatchProvider.setLanguageKey("config.property.dimension.patchprovider");
        propNameList.add(propPatchProvider.getName());
        
        String[] list = StellarSkyAPI.getRenderTypesForDimension(this.dimensionName);
        propRenderType = config.get(category, "Sky_Renderer_Type", list[0]);
        propRenderType.comment = "Sky renderer type for this dimension.\n"
        		+ "There are 'Overworld Sky' and 'End Sky' type by default.";
        propRenderType.setRequiresWorldRestart(true);
        propRenderType.setLanguageKey("config.property.dimension.skyrenderertype");
        propRenderType.setValidValues(list);
        propNameList.add(propRenderType.getName());
		
       	propLatitude = config.get(category, "Latitude", !dimensionName.equals("The End")? 37.5 : -52.5);
       	propLatitude.comment = "Latitude on this world, in Degrees.";
       	propLatitude.setRequiresWorldRestart(true);
       	propLatitude.setLanguageKey("config.property.dimension.latitude");
        propNameList.add(propLatitude.getName());

       	propLongitude = config.get(category, "Longitude", !dimensionName.equals("The End")? 0.0 : 180.0);
       	propLongitude.comment = "Longitude on this world, in Degrees. (East is +)";
       	propLongitude.setRequiresWorldRestart(true);
       	propLongitude.setLanguageKey("config.property.dimension.longitude");
        propNameList.add(propLongitude.getName());
        
        propHideObjectsUnderHorizon = config.get(category, "Hide_Objects_Under_Horizon", !dimensionName.equals("The End"));
        propHideObjectsUnderHorizon.comment = "Determine whether or not hide objects under horizon.";
        propHideObjectsUnderHorizon.setRequiresWorldRestart(true);
        propHideObjectsUnderHorizon.setLanguageKey("config.property.dimension.hidehorizonobj");
        propNameList.add(propHideObjectsUnderHorizon.getName());
        
        propAllowRefraction = config.get(category, "Allow_Atmospheric_Refraction", !dimensionName.equals("The End"));
        propAllowRefraction.comment = "Determine whether or not apply the atmospheric refraction.";
        propAllowRefraction.setRequiresWorldRestart(true);
        propAllowRefraction.setLanguageKey("config.property.dimension.allowrefraction");
        propNameList.add(propAllowRefraction.getName());
        
        propSunlightMultiplier = config.get(category, "SunLight_Multiplier", 1.0);
        propSunlightMultiplier.comment = "Relative amount of sunlight on the dimension.\n"
        		+ "Setting this to 0.0 will make the world very dark.";
        propSunlightMultiplier.setRequiresWorldRestart(true);
        propSunlightMultiplier.setLanguageKey("config.property.dimension.sunlightmultiplier");
        propNameList.add(propSunlightMultiplier.getName());
        
        propSkyDispersionMultiplier = config.get(category, "Sky_Dispersion_Miltiplier", 1.0);
        propSkyDispersionMultiplier.comment = "Relative strength of sky dispersion on the dimension.\n"
        		+ "The effect is similar with sunlight multiplier on client, but usually don't affect the server, e.g. do not spawn mobs.";
        propSkyDispersionMultiplier.setRequiresWorldRestart(true);
        propSkyDispersionMultiplier.setLanguageKey("config.property.dimension.skydispersionmultiplier");
        propNameList.add(propSkyDispersionMultiplier.getName());
        
        config.setCategoryPropertyOrder(category, propNameList);
	}

	@Override
	public void loadFromConfig(Configuration config, String category) {
       	this.patchProvider = propPatchProvider.getBoolean();
       	
       	if(this.patchProvider) {
       		this.latitude = propLatitude.getDouble();
       		this.longitude = propLongitude.getDouble();
       		this.allowRefraction = propAllowRefraction.getBoolean();
       		this.sunlightMultiplier = propSunlightMultiplier.getDouble();
       	} else {
       		this.latitude =  Double.parseDouble(propLatitude.getDefault());
       		this.longitude = Double.parseDouble(propLongitude.getDefault());
       		this.allowRefraction = Boolean.parseBoolean(propAllowRefraction.getDefault());
       		this.sunlightMultiplier = Double.parseDouble(propSunlightMultiplier.getDefault());
       	}
       	
   		this.skyDispersionMultiplier = propSkyDispersionMultiplier.getDouble();
   		this.hideObjectsUnderHorizon = propHideObjectsUnderHorizon.getBoolean();
   		this.skyRendererType = propRenderType.getString();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
       	this.latitude = compound.getDouble("lattitude");
       	this.longitude = compound.getDouble("longitude");
       	this.patchProvider = compound.getBoolean("patchProvider");
       	this.hideObjectsUnderHorizon = compound.getBoolean("hideObjectsUnderHorizon");
       	this.allowRefraction = compound.getBoolean("allowRefraction");
       	this.sunlightMultiplier = compound.getDouble("sunlightMultiplier");
       	this.skyDispersionMultiplier = compound.getDouble("skyDispersionMultiplier");
       	this.skyRendererType = compound.getString("skyRendererType");
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
       	compound.setDouble("lattitude", this.latitude);
       	compound.setDouble("longitude", this.longitude);
       	compound.setBoolean("patchProvider", this.patchProvider);
       	compound.setBoolean("hideObjectsUnderHorizon", this.hideObjectsUnderHorizon);
       	compound.setBoolean("allowRefraction", this.allowRefraction);
       	compound.setDouble("sunlightMultiplier", this.sunlightMultiplier);
       	compound.setDouble("skyDispersionMultiplier", this.skyDispersionMultiplier);
       	compound.setString("skyRendererType", this.skyRendererType);
	}

	@Override
	public INBTConfig copy() {
		PerDimensionSettings settings = new PerDimensionSettings(this.dimensionName);
		settings.latitude = this.latitude;
		settings.longitude = this.longitude;
		settings.patchProvider = this.patchProvider;
		settings.hideObjectsUnderHorizon = this.hideObjectsUnderHorizon;
		settings.allowRefraction = this.allowRefraction;
		settings.sunlightMultiplier = this.sunlightMultiplier;
		settings.skyRendererType = this.skyRendererType;
		settings.skyDispersionMultiplier = this.skyDispersionMultiplier;

		return settings;
	}

}