package stellarium.stellars.star;

import javax.vecmath.Vector3d;

import stellarapi.api.ICelestialCoordinate;
import stellarapi.api.ISkyEffect;
import stellarapi.api.lib.config.IConfigHandler;
import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.optics.IViewScope;
import stellarapi.api.optics.Wavelength;
import stellarium.client.ClientSettings;
import stellarium.render.IRenderCache;
import stellarium.stellars.Optics;
import stellarium.stellars.view.IStellarSkySet;

public class StarRenderCache implements IRenderCache<BgStar, IConfigHandler> {
	
	protected boolean shouldRender;
	protected SpCoord appCoord = new SpCoord();
	protected float appMag;
	protected float appB_V;
	protected float size;
	protected float multiplier;
	
	@Override
	public void initialize(ClientSettings settings, IConfigHandler config) { }

	@Override
	public void updateCache(ClientSettings settings, IConfigHandler config, BgStar object,
			ICelestialCoordinate coordinate, ISkyEffect sky, IViewScope scope) {
		Vector3d ref = new Vector3d(object.pos);
		coordinate.getProjectionToGround().transform(ref);
		appCoord.setWithVec(ref);
		double airmass = sky.calculateAirmass(this.appCoord);
		sky.applyAtmRefraction(this.appCoord);

		this.appMag = (float) (object.mag + airmass * Optics.ext_coeff_V);
		this.shouldRender = true;
		if(this.appMag > settings.mag_Limit)
		{
			this.shouldRender = false;
			return;
		}
		this.appB_V = (float) (object.B_V + airmass * Optics.ext_coeff_B_V);
		
		if(appCoord.y < 0.0
				&& sky instanceof IStellarSkySet
				&& ((IStellarSkySet) sky).hideObjectsUnderHorizon())
			this.shouldRender = false;
		
		this.size = (float) (scope.getResolution(Wavelength.visible) / (0.2 * scope.getMP()));
		this.multiplier = (float) scope.getLGP() / this.size / this.size;
		this.size *= 0.5f;
	}

}
