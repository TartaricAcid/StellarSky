package stellarium.stellars.system;

import stellarapi.api.ICelestialCoordinate;
import stellarapi.api.ISkyEffect;
import stellarapi.api.lib.config.IConfigHandler;
import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.lib.math.Vector3;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IViewScope;
import stellarium.client.ClientSettings;
import stellarium.stellars.layer.IRenderCache;

public class SunRenderCache implements IRenderCache<Sun, IConfigHandler> {
	
	protected SpCoord appCoord = new SpCoord();
	protected double size;

	@Override
	public void initialize(ClientSettings settings, IConfigHandler config, Sun sun) { }

	@Override
	public void updateCache(ClientSettings settings, IConfigHandler config, Sun object,
			ICelestialCoordinate coordinate, ISkyEffect sky, IViewScope scope, IOpticalFilter filter) {
		Vector3 ref = new Vector3(object.earthPos);
		coordinate.getProjectionToGround().transform(ref);
		appCoord.setWithVec(ref);
		sky.applyAtmRefraction(this.appCoord);
		
		this.size = object.radius / object.earthPos.size()*99.0*20;
	}

	@Override
	public int getRenderId() {
		return LayerSolarSystem.sunRenderId;
	}

}
