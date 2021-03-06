package stellarium.display.eqgrid;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stellarium.display.DisplayRenderInfo;
import stellarium.display.IDisplayRenderer;

@SideOnly(Side.CLIENT)
public class EqGridRenderer implements IDisplayRenderer<EqGridCache> {

	@Override
	public void render(DisplayRenderInfo info, EqGridCache cache) {		
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		
		if(!cache.enabled || info.isPostCelesitals)
			return;
		
		GlStateManager.disableTexture2D();
		GlStateManager.pushMatrix();
		GlStateManager.scale(info.deepDepth, info.deepDepth, info.deepDepth);

		if(cache.gridEnabled) {
			GlStateManager.glLineWidth(2.0f);
			GlStateManager.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);

			info.worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

			for(int longc=0; longc<cache.longn; longc++){
				for(int latc=0; latc<cache.latn; latc++){
					int longcd=(longc+1)%cache.longn;

					info.worldRenderer.pos(cache.displayvec[longc][latc].getX(), cache.displayvec[longc][latc].getY(), cache.displayvec[longc][latc].getZ());
					info.worldRenderer.color((float)cache.colorvec[longc][latc].getX(),
							(float)cache.colorvec[longc][latc].getY(),
							(float)cache.colorvec[longc][latc].getZ(), cache.brightness);
					info.worldRenderer.endVertex();
					
					info.worldRenderer.pos(cache.displayvec[longcd][latc].getX(), cache.displayvec[longcd][latc].getY(), cache.displayvec[longcd][latc].getZ());
					info.worldRenderer.color((float)cache.colorvec[longcd][latc].getX(),
							(float)cache.colorvec[longcd][latc].getY(),
							(float)cache.colorvec[longcd][latc].getZ(), cache.brightness);
					info.worldRenderer.endVertex();
					
					info.worldRenderer.pos(cache.displayvec[longcd][latc+1].getX(), cache.displayvec[longcd][latc+1].getY(), cache.displayvec[longcd][latc+1].getZ());
					info.worldRenderer.color((float)cache.colorvec[longcd][latc+1].getX(),
							(float)cache.colorvec[longcd][latc+1].getY(),
							(float)cache.colorvec[longcd][latc+1].getZ(), cache.brightness);
					info.worldRenderer.endVertex();
					
					info.worldRenderer.pos(cache.displayvec[longc][latc+1].getX(), cache.displayvec[longc][latc+1].getY(), cache.displayvec[longc][latc+1].getZ());
					info.worldRenderer.color((float)cache.colorvec[longc][latc+1].getX(),
							(float)cache.colorvec[longc][latc+1].getY(),
							(float)cache.colorvec[longc][latc+1].getZ(), cache.brightness);
					info.worldRenderer.endVertex();
				}
			}

			info.tessellator.draw();

			GlStateManager.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			GlStateManager.glLineWidth(1.0f);
		}

		if(cache.equatorEnabled) {
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
			
			GlStateManager.glLineWidth(5.0f);

			GlStateManager.color(1.0f, 0.3f, 0.3f, 3.0f * cache.brightness);
			
			info.worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

			for(int longc=0; longc<cache.longn; longc++){
				int longcd=(longc+1)%cache.longn;
				info.worldRenderer.pos(cache.equator[longc].getX(), cache.equator[longc].getY(), cache.equator[longc].getZ()).endVertex();
				info.worldRenderer.pos(cache.equator[longcd].getX(), cache.equator[longcd].getY(), cache.equator[longcd].getZ()).endVertex();
			}

			info.tessellator.draw();
			
			GlStateManager.glLineWidth(1.0f);

			GlStateManager.shadeModel(GL11.GL_FLAT);
		}
		
		GlStateManager.enableTexture2D();
		GlStateManager.popMatrix();
	}

}
