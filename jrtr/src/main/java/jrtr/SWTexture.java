package jrtr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.vecmath.Vector3f;

/**
 * Manages textures for the software renderer. Not implemented here.
 */
public class SWTexture implements Texture {
	
	public enum Interpolation{
		NEAREST_NEIGHBOR,
		BILINEAR
	};
	
	BufferedImage texture;
	
	public void load(String fileName) throws IOException
	{
		File f = new File(fileName);
		texture = ImageIO.read(f);
	}
	
	public Vector3f interpol(float u, float v, Interpolation typ)
	{
		Vector3f color = new Vector3f();
		
		switch(typ)
		{
		case NEAREST_NEIGHBOR:
			int co = texture.getRGB((int)(u*(texture.getWidth() - 0.5)), (int)((1 - v)*(texture.getHeight() - 0.5)));
			color.x = (co&0x00ff0000)/((float)0x00ff0000);
			color.y = (co&0x0000ff00)/((float)0x0000ff00);
			color.z = (co&0x000000ff)/((float)0x000000ff);
			break;
		case BILINEAR:
			float up = u*texture.getWidth() - 0.5f;
			float vp = (1-v)*texture.getHeight() - 0.5f;
			
			int utmin = (up > 0.5)?((int)up):(0);
			int utmax = (utmin >= texture.getWidth() - 1)?(utmin):((int)up + 1);
			int vtmin = (vp > 0.5)?((int)vp):(0);
			int vtmax = (vtmin >= texture.getHeight() - 1)?(vtmin):((int)vp + 1);
			
			float a = (utmin != utmax)?(up - utmin):(0);
			float b = (vtmin != vtmax)?(vp - vtmin):(0);
			
			Vector3f color_tl = getColVector(utmin, vtmin);
			Vector3f color_tr = getColVector(utmax, vtmin);
			Vector3f color_bl = getColVector(utmin, vtmax);
			Vector3f color_br = getColVector(utmax, vtmax);
			
			color_tl.scale((1 - a)*(1 - b));
			color_tr.scale(a*(1 - b));
			color_bl.scale((1 - a)*b);
			color_br.scale(a*b);
			
			color.add(color_tl);
			color.add(color_tr);
			color.add(color_bl);
			color.add(color_br);
			color.x = Math.min(1, Math.max(0, color.x));
			color.y = Math.min(1, Math.max(0, color.y));
			color.z = Math.min(1, Math.max(0, color.z));
			break;
		}
		
		return color;
	}
	
	private Vector3f getColVector(int x, int y)
	{
		Vector3f color = new Vector3f();
		
		int co = texture.getRGB(x, y);
		color.x = (co&0x00ff0000)/((float)0x00ff0000);
		color.y = (co&0x0000ff00)/((float)0x0000ff00);
		color.z = (co&0x000000ff)/((float)0x000000ff);
		
		return color;
	}
}
