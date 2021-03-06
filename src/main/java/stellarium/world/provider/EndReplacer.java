package stellarium.world.provider;

import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderEnd;
import stellarium.api.ICelestialHelper;
import stellarium.api.IWorldProviderReplacer;

public class EndReplacer implements IWorldProviderReplacer {

	@Override
	public boolean accept(World world, WorldProvider provider) {
		return provider instanceof WorldProviderEnd;
	}

	@Override
	public WorldProvider createWorldProvider(World world, WorldProvider originalProvider, ICelestialHelper helper) {
		return new StellarWorldProviderEnd(world, (WorldProviderEnd)originalProvider, helper);
	}

}
