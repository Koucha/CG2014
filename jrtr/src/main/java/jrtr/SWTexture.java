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
		BILINEAR,
		BICUBIC
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
			break;
		case BICUBIC:
			break;
		}
		
		return color;
	}
}
