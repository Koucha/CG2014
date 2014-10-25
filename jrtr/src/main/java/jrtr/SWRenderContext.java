package jrtr;

import jrtr.RenderContext;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.util.Iterator;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.SingularMatrixException;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;


/**
 * A skeleton for a software renderer. It works in combination with
 * {@link SWRenderPanel}, which displays the output image. In project 3 
 * you will implement your own rasterizer in this class.
 * <p>
 * To use the software renderer, you will simply replace {@link GLRenderPanel} 
 * with {@link SWRenderPanel} in the user application.
 */
public class SWRenderContext implements RenderContext {

	private SceneManagerInterface sceneManager;
	private BufferedImage colorBuffer;
	private float[][] zBuffer;
	
	final int delta = 6;
		
	public void setSceneManager(SceneManagerInterface sceneManager)
	{
		this.sceneManager = sceneManager;
	}
	
	/**
	 * This is called by the SWRenderPanel to render the scene to the 
	 * software frame buffer.
	 */
	public void display()
	{
		if(sceneManager == null) return;
		
		beginFrame();
	
		SceneManagerIterator iterator = sceneManager.iterator();	
		while(iterator.hasNext())
		{
			draw(iterator.next());
		}		
		
		endFrame();
	}

	/**
	 * This is called by the {@link SWJPanel} to obtain the color buffer that
	 * will be displayed.
	 */
	public BufferedImage getColorBuffer()
	{
		return colorBuffer;
	}
	
	/**
	 * Set a new viewport size. The render context will also need to store
	 * a viewport matrix, which you need to reset here. 
	 */
	public void setViewportSize(int width, int height)
	{
		colorBuffer = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		zBuffer = new float[width][height];
	}
		
	/**
	 * Clear the framebuffer here.
	 */
	private void beginFrame()
	{
		Graphics2D graphics = (Graphics2D) colorBuffer.getGraphics();
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, colorBuffer.getWidth(), colorBuffer.getHeight());
		zBuffer = new float[colorBuffer.getWidth()][colorBuffer.getHeight()];
	}
	
	private void endFrame()
	{		
	}
	
	/**
	 * The main rendering method. You will need to implement this to draw
	 * 3D objects.
	 */
	private void draw(RenderItem renderItem)
	{
		Shape shape = renderItem.getShape();
		VertexData vd = shape.getVertexData();
		
		Matrix4f toPixelspace = new Matrix4f();
		toPixelspace.m00 = colorBuffer.getWidth()/2.0f;
		toPixelspace.m03 = colorBuffer.getWidth()/2.0f;
		toPixelspace.m11 = -colorBuffer.getHeight()/2.0f;
		toPixelspace.m13 = colorBuffer.getHeight()/2.0f;
		toPixelspace.m22 = 0.5f;
		toPixelspace.m23 = 0.5f;
		toPixelspace.m33 = 1;
		
		toPixelspace.mul(sceneManager.getFrustum().getProjectionMatrix());
		toPixelspace.mul(sceneManager.getCamera().getCameraMatrix());
		toPixelspace.mul(renderItem.getT());
		toPixelspace.mul(shape.getTransformation());
		
		
		int[] indices = vd.getIndices();
		Vector4f vertex1 = null, vertex2 = null, vertex3 = null;
		Vector4f normal1 = null, normal2 = null, normal3 = null;
		Vector2f teco1 = null, teco2 = null, teco3 = null;
		Vector3f col1 = null, col2 = null, col3 = null;
		Matrix3f invvercor = null;
		
		boolean skip = false;
		for(int i = 0; i < indices.length; i = i + 3)
		{
			skip = false;
			
			Iterator<VertexData.VertexElement> vdIt = vd.getElements().descendingIterator();
			VertexData.VertexElement ve;
			while(!skip && vdIt.hasNext())
			{
				ve = vdIt.next();
				float[] data = ve.getData();
				switch(ve.getSemantic())
				{
				case POSITION:
					vertex1 = new Vector4f(data[3*indices[i]], data[3*indices[i] + 1], data[3*indices[i] + 2], 1);
					vertex2 = new Vector4f(data[3*indices[i + 1]], data[3*indices[i + 1] + 1], data[3*indices[i + 1] + 2], 1);
					vertex3 = new Vector4f(data[3*indices[i + 2]], data[3*indices[i + 2] + 1], data[3*indices[i + 2] + 2], 1);
					
					toPixelspace.transform(vertex1);
					toPixelspace.transform(vertex2);
					toPixelspace.transform(vertex3);
					
					if(vertex1.w < 0 && vertex2.w < 0 && vertex3.w < 0)	// triangle behind eye
					{
						skip = true;
						break;
					}
					
					Matrix3f vercor = new Matrix3f();
					vercor.m00 = vertex1.x; vercor.m01 = vertex1.y; vercor.m02 = vertex1.w;
					vercor.m10 = vertex2.x; vercor.m11 = vertex2.y; vercor.m12 = vertex2.w;
					vercor.m20 = vertex3.x; vercor.m21 = vertex3.y; vercor.m22 = vertex3.w;
					
					if(vercor.determinant() < 0)	// backface culling
					{
						skip = true;
						break;
					}
					
					invvercor = new Matrix3f(vercor);
					try{
						invvercor.invert();
					}catch(SingularMatrixException e){	// singular
						skip = true;
						break;
					}
					
					break;
				case NORMAL:
					normal1 = new Vector4f(data[3*indices[i]], data[3*indices[i] + 1], data[3*indices[i] + 2], 0);
					normal2 = new Vector4f(data[3*indices[i + 1]], data[3*indices[i + 1] + 1], data[3*indices[i + 1] + 2], 0);
					normal3 = new Vector4f(data[3*indices[i + 2]], data[3*indices[i + 2] + 1], data[3*indices[i + 2] + 2], 0);
					//TODO
					break;
				case TEXCOORD:
					teco1 = new Vector2f(data[2*indices[i]], data[2*indices[i] + 1]);
					teco2 = new Vector2f(data[2*indices[i + 1]], data[2*indices[i + 1] + 1]);
					teco3 = new Vector2f(data[2*indices[i + 2]], data[2*indices[i + 2] + 1]);
					//TODO
					break;
				case COLOR:
					col1 = new Vector3f(data[3*indices[i]], data[3*indices[i] + 1], data[3*indices[i] + 2]);
					col2 = new Vector3f(data[3*indices[i + 1]], data[3*indices[i + 1] + 1], data[3*indices[i + 1] + 2]);
					col3 = new Vector3f(data[3*indices[i + 2]], data[3*indices[i + 2] + 1], data[3*indices[i + 2] + 2]);
					break;
				}
			}
			
			if(skip)
			{
				continue;
			}
			
			// Color mapping:
			int xmin = 0, xmax = colorBuffer.getWidth() - 1, ymin = 0, ymax = colorBuffer.getHeight() - 1;
			
			if(vertex1.w > 0 && vertex2.w > 0 && vertex3.w > 0)	// calculate bounding square if possible
			{
				float x1 = vertex1.x/vertex1.w, y1 = vertex1.y/vertex1.w, 
					  x2 = vertex2.x/vertex2.w, y2 = vertex2.y/vertex2.w,
					  x3 = vertex3.x/vertex3.w, y3 = vertex3.y/vertex3.w;
				xmin = (int) Math.max(Math.min(Math.min(Math.min(x1, x2), x3), colorBuffer.getWidth() - 1), 0);
				xmax = (int) Math.min(Math.max(Math.max(Math.max(x1 + 1, x2 + 1), x3 + 1), 0), colorBuffer.getWidth() - 1);;
				ymin = (int) Math.max(Math.min(Math.min(Math.min(y1, y2), y3), colorBuffer.getHeight() - 1), 0);
				ymax = (int) Math.min(Math.max(Math.max(Math.max(y1 + 1, y2 + 1), y3 + 1), 0), colorBuffer.getHeight() - 1);
				
				if(xmax - xmin <= 0 || ymax - ymin <= 0)	// triangle not inside the picture
				{
					continue;
				}
			}
			
			// iterate over bounding box
			Vector3f eins = new Vector3f(1, 1, 1);
			invvercor.transform(eins);
			
			//*
			for(int y = ymin; y < ymax; y++)
			{
				for(int x = xmin; x < xmax; x++)
				{
					if( inside(x, y, invvercor) )	// if inside triangle
					{
						calcPixel(invvercor, eins, col1, col2, col3, y, x);
					}
				}
			}
			/*/
			for(int yb = ymin; yb < ymax + 2*delta; yb += 2*delta)
			{
				for(int xb = xmin; xb < xmax + 2*delta; xb += 2*delta)
				{
					level1(xb, yb, xmax, ymax, invvercor, eins, col1, col2, col3);
				}
			}
			//*/
		}
	}
	
	private void level1(int xb, int yb, int xmax, int ymax, Matrix3f invvercor, Vector3f eins, Vector3f col1, Vector3f col2, Vector3f col3)
	{
		boolean ul = testHline(xb, xb + delta, yb, invvercor, eins, col1, col2, col3);
		boolean ur = testHline(xb + delta, xb + 2*delta, yb, invvercor, eins, col1, col2, col3);
		boolean lu = testVline(xb, yb, yb + delta, invvercor, eins, col1, col2, col3);
		boolean ld = testVline(xb, yb + delta, yb + 2*delta, invvercor, eins, col1, col2, col3);
		boolean dl = testHline(xb, xb + delta, yb, invvercor, eins, col1, col2, col3);
		boolean dr = testHline(xb + delta, xb + 2*delta, yb, invvercor, eins, col1, col2, col3);
		boolean ru = testVline(xb, yb, yb + delta, invvercor, eins, col1, col2, col3);
		boolean rd = testVline(xb, yb + delta, yb + 2*delta, invvercor, eins, col1, col2, col3);
		
		if(ul || ur || lu || ld || dl || dr || ru || rd)
		{
			level2(xb, yb, xmax, ymax, invvercor, eins, col1, col2, col3, (ul || lu)?(5):(1));
			level2(xb + delta, yb, xmax, ymax, invvercor, eins, col1, col2, col3, (ur || ru)?(6):(2));
			level2(xb + delta, yb + delta, xmax, ymax, invvercor, eins, col1, col2, col3, (dr || rd)?(7):(3));
			level2(xb, yb + delta, xmax, ymax, invvercor, eins, col1, col2, col3, (dl || ld)?(8):(4));
		}
	}
	
	private void level2(int xs, int ys, int xmax, int ymax, Matrix3f invvercor, Vector3f eins, Vector3f col1, Vector3f col2, Vector3f col3, int type)
	{
		boolean fill = (type > 4)?(true):(false);
		type = type%4;

		if(type == 0 && type == 1) //right line
		{
			fill = fill || testVline(xs + delta - 1, ys, ys + delta, invvercor, eins, col1, col2, col3);
		}
		if(type == 0 && type == 3) //upper line
		{
			fill = fill || testHline(xs, xs + delta, ys, invvercor, eins, col1, col2, col3);
		}
		if(type == 2 && type == 3) //left line
		{
			fill = fill || testVline(xs, ys, ys + delta, invvercor, eins, col1, col2, col3);
		}
		if(type == 2 && type == 1) //bottom line
		{
			fill = fill || testHline(xs, xs + delta, ys + delta - 1, invvercor, eins, col1, col2, col3);
		}
		
		if(fill)
		{
			for(int y = ys + 1; y < ys + delta; y++)
			{
				for(int x = xs + 1; x < xs + delta; x++)
				{
					if( inside(x, y, invvercor) )	// if inside triangle
					{
						calcPixel(invvercor, eins, col1, col2, col3, y, x);
					}
				}
			}
		}
	}

	private void calcPixel(Matrix3f invvercor, Vector3f eins, Vector3f col1, Vector3f col2, Vector3f col3, int y, int x)
	{
		if(y < 0 || colorBuffer.getHeight() <= y || x < 0 || colorBuffer.getWidth() <= x)
		{
			return;
		}
		
		float antiw = eins.x*x + eins.y*y + eins.z;
		
		if(antiw > zBuffer[x][y])	// depth test
		{
			zBuffer[x][y] = antiw;
		
			Vector3f rv = new Vector3f(col1.x*255, col2.x*255, col3.x*255);
			Vector3f gv = new Vector3f(col1.y*255, col2.y*255, col3.y*255);
			Vector3f bv = new Vector3f(col1.z*255, col2.z*255, col3.z*255);

			invvercor.transform(rv);
			invvercor.transform(gv);
			invvercor.transform(bv);
			int r = calcCol(x, y, rv, antiw);
			int g = calcCol(x, y, gv, antiw);
			int b = calcCol(x, y, bv, antiw);
			
			colorBuffer.setRGB(x, y, (new Color(r, g, b)).getRGB());
		}
	}
	
	private int calcCol(int x, int y, Vector3f col, float antiw)
	{
		int c = (int) ((col.x*x + col.y*y + col.z)/antiw);
		c = (c < 0)?(0):(c);
		c = (c > 255)?(255):(c);
		return c;
	}
	
	private boolean inside(int x, int y, Matrix3f invvercor)
	{
		return (invvercor.m00*(x + 0.5) + invvercor.m10*(y + 0.5) + invvercor.m20 > 0 &&
			    invvercor.m01*(x + 0.5) + invvercor.m11*(y + 0.5) + invvercor.m21 > 0 &&
			    invvercor.m02*(x + 0.5) + invvercor.m12*(y + 0.5) + invvercor.m22 > 0) ||
			   (invvercor.m00*(x + 0.5) + invvercor.m10*(y + 0.5) + invvercor.m20 == 0 &&
			    invvercor.m01*(x + 0.5) + invvercor.m11*(y + 0.5) + invvercor.m21 == 0 &&
			    invvercor.m02*(x + 0.5) + invvercor.m12*(y + 0.5) + invvercor.m22 == 0 &&
			    invvercor.m00*(x + 1.5) + invvercor.m10*(y + 0.5) + invvercor.m20 >= 0 &&
			    invvercor.m01*(x + 1.5) + invvercor.m11*(y + 0.5) + invvercor.m21 >= 0 &&
			    invvercor.m02*(x + 1.5) + invvercor.m12*(y + 0.5) + invvercor.m22 >= 0);
	}
	
	private boolean testHline(int xmin, int xmax, int y, Matrix3f invvercor, Vector3f eins, Vector3f col1, Vector3f col2, Vector3f col3)
	{
		boolean retval = false;
		for(int x = xmin; x < xmax; x++)
		{
			if(inside(x, y, invvercor))
			{
				calcPixel(invvercor, eins, col1, col2, col3, y, x);
				retval = true;
			}
		}
		return retval;
	}
	
	private boolean testVline(int x, int ymin, int ymax, Matrix3f invvercor, Vector3f eins, Vector3f col1, Vector3f col2, Vector3f col3)
	{
		boolean retval = false;
		for(int y = ymin; y < ymax; y++)
		{
			if(inside(x, y, invvercor))
			{
				calcPixel(invvercor, eins, col1, col2, col3, y, x);
				retval = true;
			}
		}
		return retval;
	}
	
	/**
	 * Does nothing. We will not implement shaders for the software renderer.
	 */
	public Shader makeShader()	
	{
		return new SWShader();
	}
	
	/**
	 * Does nothing. We will not implement shaders for the software renderer.
	 */
	public void useShader(Shader s)
	{
	}
	
	/**
	 * Does nothing. We will not implement shaders for the software renderer.
	 */
	public void useDefaultShader()
	{
	}

	/**
	 * Does nothing. We will not implement textures for the software renderer.
	 */
	public Texture makeTexture()
	{
		return new SWTexture();
	}
	
	public VertexData makeVertexData(int n)
	{
		return new SWVertexData(n);		
	}
}
