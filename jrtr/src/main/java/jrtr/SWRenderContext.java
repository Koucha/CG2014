package jrtr;

import jrtr.RenderContext;
import jrtr.SWTexture.Interpolation;

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
	private BufferedImage colorBackBuffer;
	private float[][] zBuffer;
	
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
		colorBackBuffer = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
	}
		
	/**
	 * Clear the framebuffer here.
	 */
	private void beginFrame()
	{
		zBuffer = new float[colorBuffer.getWidth()][colorBuffer.getHeight()];
	}
	
	private void endFrame()
	{
		// swap image buffers
		BufferedImage temp = colorBuffer;
		colorBuffer = colorBackBuffer;
		colorBackBuffer = temp;
		
		// clear background and z buffer
		Graphics2D graphics = (Graphics2D) colorBackBuffer.getGraphics();
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, colorBuffer.getWidth(), colorBuffer.getHeight());
		zBuffer = null;
	}
	
	/**
	 * The main rendering method. You will need to implement this to draw
	 * 3D objects.
	 */
	private void draw(RenderItem renderItem)
	{
		VertexData vd = renderItem.getShape().getVertexData();
		
		TriRenderer triRenderer = new TriRenderer(renderItem, 1);
		
		int[] indices = vd.getIndices();
		Vector4f vertex1 = null, vertex2 = null, vertex3 = null;
		Vector4f normal1 = null, normal2 = null, normal3 = null;
		Vector2f teco1 = null, teco2 = null, teco3 = null;
		Vector3f col1 = null, col2 = null, col3 = null;
		
		boolean skip = false;
		for(int i = 0; i < indices.length; i = i + 3)
		{
			skip = false;
			triRenderer.reset();
			
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
					
					skip = !triRenderer.setVertices(vertex1, vertex2, vertex3);
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
					
					triRenderer.setTexcor(teco1, teco2, teco3);
					break;
				case COLOR:
					col1 = new Vector3f(data[3*indices[i]], data[3*indices[i] + 1], data[3*indices[i] + 2]);
					col2 = new Vector3f(data[3*indices[i + 1]], data[3*indices[i + 1] + 1], data[3*indices[i + 1] + 2]);
					col3 = new Vector3f(data[3*indices[i + 2]], data[3*indices[i + 2] + 1], data[3*indices[i + 2] + 2]);
					
					triRenderer.setColor(col1, col2, col3);
					break;
				}
			}
			
			if(skip)
			{
				continue;
			}
			
			triRenderer.render();
			
		}
	}
	
	private class TriRenderer{
		
		private boolean valid;
		private int optimisation;
		private final int delta = 6;
		
		private Matrix4f toPixelspace;
		private Matrix3f invvercor;
		private Vector3f eins;
		private RenderItem renderItem;
		
		private Vector4f vertex1;
		private Vector4f vertex2;
		private Vector4f vertex3;
		
		private Vector3f col1;
		private Vector3f col2;
		private Vector3f col3;
		private Vector2f uv1;
		private Vector2f uv2;
		private Vector2f uv3;
		
		public TriRenderer(RenderItem renderItem, int optimisation)
		{
			valid = false;
			this.optimisation = optimisation;
			this.renderItem = renderItem;
			
			toPixelspace = new Matrix4f();
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
			toPixelspace.mul(renderItem.getShape().getTransformation());
		}
		
		public void reset()
		{
			valid = false;
		}

		public boolean setVertices(Vector4f vertex1, Vector4f vertex2, Vector4f vertex3)
		{
			this.vertex1 = vertex1;
			this.vertex2 = vertex2;
			this.vertex3 = vertex3;
			
			toPixelspace.transform(vertex1);
			toPixelspace.transform(vertex2);
			toPixelspace.transform(vertex3);
			
			if(vertex1.w < 0 && vertex2.w < 0 && vertex3.w < 0)	// triangle behind eye
			{
				return false;
			}
			
			Matrix3f vercor = new Matrix3f();
			vercor.m00 = vertex1.x; vercor.m01 = vertex1.y; vercor.m02 = vertex1.w;
			vercor.m10 = vertex2.x; vercor.m11 = vertex2.y; vercor.m12 = vertex2.w;
			vercor.m20 = vertex3.x; vercor.m21 = vertex3.y; vercor.m22 = vertex3.w;
			
			if(vercor.determinant() > 0)	// backface culling
			{
				return false;
			}
			
			invvercor = new Matrix3f(vercor);
			try{
				invvercor.invert();
			}catch(SingularMatrixException e){	// singular
				return false;
			}
			
			valid = true;
			return true;
		}
		
		public void setColor(Vector3f col1, Vector3f col2, Vector3f col3)
		{
			this.col1 = col1;
			this.col2 = col2;
			this.col3 = col3;
		}
		
		public void setTexcor(Vector2f teco1, Vector2f teco2, Vector2f teco3)
		{
			this.uv1 = teco1;
			this.uv2 = teco2;
			this.uv3 = teco3;
		}

		public void render()
		{
			if(!valid)
			{
				return;
			}
			
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
					return;
				}
			}
			
			// iterate over bounding box
			eins = new Vector3f(1, 1, 1);
			invvercor.transform(eins);
			
			if(optimisation == 2)
			{
				for(int yb = ymin; yb < ymax + 2*delta; yb += 2*delta)
				{
					for(int xb = xmin; xb < xmax + 2*delta; xb += 2*delta)
					{
						level1(xb, yb, xmax, ymax);
					}
				}
			}else if(optimisation == 1)
			{
				for(int yb = ymin; yb < ymax + 2*delta; yb += 2*delta)
				{
					for(int xb = xmin; xb < xmax + 2*delta; xb += 2*delta)
					{
						level1Save(xb, yb, xmax, ymax);
					}
				}
			}else
			{
				for(int y = ymin; y < ymax; y++)
				{
					for(int x = xmin; x < xmax; x++)
					{
						if( inside(x, y) )	// if inside triangle
						{
							calcPixel(y, x);
						}
					}
				}
			}
		}
		
		private void level1(int xb, int yb, int xmax, int ymax)
		{
			boolean ci = false, tl = false, tr = false, bl = false, br = false;
			
			if(cornerInside(xb, yb, 2*delta))
			{
				ci = true;
			}
			
			tl = inside(xb, yb);
			tr = inside(xb + 2*delta - 1, yb);
			bl = inside(xb, yb + 2*delta - 1);
			br = inside(xb + 2*delta - 1, yb + 2*delta - 1);
			
			if(ci || tl || tr || bl || br)
			{
				level2(xb, yb, xmax, ymax, tl?(0):(ci?(5):(1)));
				level2(xb + delta, yb, xmax, ymax, tr?(0):(ci?(6):(2)));
				level2(xb + delta, yb + delta, xmax, ymax, br?(0):(ci?(7):(3)));
				level2(xb, yb + delta, xmax, ymax, bl?(0):(ci?(8):(4)));
			}
		}

		private void level2(int xs, int ys, int xmax, int ymax, int type)
		{
			boolean fill = false;
			
			if(type > 0)
			{
				if(type > 4)
				{
					fill = cornerInside(xs, ys, delta);
				}
				type = type%4;
				if(type != 1)
				{
					fill = fill || inside(xs, ys);
				}
				if(type != 2)
				{
					fill = fill || inside(xs + delta - 1, ys);
				}
				if(type != 3)
				{
					fill = fill || inside(xs + delta - 1, ys + delta - 1);
				}
				if(type != 0)
				{
					fill = fill || inside(xs, ys + delta - 1);
				}
			}else
			{
				fill = true;
			}
			
			if(fill)
			{
				for(int y = ys; y < ys + delta; y++)
				{
					for(int x = xs; x < xs + delta; x++)
					{
						if( inside(x, y) )	// if inside triangle
						{
							calcPixel(y, x);
						}
					}
				}
			}
		}
		
		private void level1Save(int xb, int yb, int xmax, int ymax)
		{
			boolean ul = testHline(xb, xb + delta, yb);
			boolean ur = testHline(xb + delta, xb + 2*delta, yb);
			boolean lu = testVline(xb, yb, yb + delta);
			boolean ld = testVline(xb, yb + delta, yb + 2*delta);
			boolean dl = testHline(xb, xb + delta, yb + 2*delta - 1);
			boolean dr = testHline(xb + delta, xb + 2*delta, yb + 2*delta - 1);
			boolean ru = testVline(xb + 2*delta - 1, yb, yb + delta);
			boolean rd = testVline(xb + 2*delta - 1, yb + delta, yb + 2*delta);
			
			if(ul || ur || lu || ld || dl || dr || ru || rd || cornerInside(xb, yb, 2*delta))
			{
				level2Save(xb, yb, xmax, ymax, (ul || lu)?(5):(1));
				level2Save(xb + delta, yb, xmax, ymax, (ur || ru)?(6):(2));
				level2Save(xb + delta, yb + delta, xmax, ymax, (dr || rd)?(7):(3));
				level2Save(xb, yb + delta, xmax, ymax, (dl || ld)?(8):(4));
			}
		}
		
		private void level2Save(int xs, int ys, int xmax, int ymax, int type)
		{
			boolean fill = (type > 4)?(true):(false);
			type = type%4;
			
			if(type == 0 || type == 1) //right line
			{
				fill = testVline(xs + delta - 1, ys, ys + delta) || fill;
			}
			if(type == 0 || type == 3) //upper line
			{
				fill = testHline(xs, xs + delta, ys) || fill;
			}
			if(type == 2 || type == 3) //left line
			{
				fill = testVline(xs, ys, ys + delta) || fill;
			}
			if(type == 2 || type == 1) //bottom line
			{
				fill = testHline(xs, xs + delta, ys + delta - 1) || fill;
			}
			
			fill = fill || cornerInside(xs, ys, delta);
			
			if(fill)
			{
				for(int y = ys + 1; y < ys + delta; y++)
				{
					for(int x = xs + 1; x < xs + delta; x++)
					{
						if( inside(x, y) )	// if inside triangle
						{
							calcPixel(y, x);
						}
					}
				}
			}
		}

		private void calcPixel(int y, int x)
		{
			if(y < 0 || colorBuffer.getHeight() <= y || x < 0 || colorBuffer.getWidth() <= x)
			{
				return;
			}
			
			float antiw = eins.x*x + eins.y*y + eins.z;
			
			if(antiw > zBuffer[x][y])	// depth test
			{
				zBuffer[x][y] = antiw;
			
				Vector3f rv = new Vector3f(col1.x, col2.x, col3.x);
				Vector3f gv = new Vector3f(col1.y, col2.y, col3.y);
				Vector3f bv = new Vector3f(col1.z, col2.z, col3.z);

				invvercor.transform(rv);
				invvercor.transform(gv);
				invvercor.transform(bv);
				float r = projectInterpol(x, y, rv, antiw);
				float g = projectInterpol(x, y, gv, antiw);
				float b = projectInterpol(x, y, bv, antiw);
				
				Vector3f uve = new Vector3f(uv1.x, uv2.x, uv3.x);
				Vector3f vve = new Vector3f(uv1.y, uv2.y, uv3.y);

				invvercor.transform(uve);
				invvercor.transform(vve);
				//* Switch here //TODO
				Vector3f texcol = ((SWTexture)(renderItem.getShape().getMaterial().texture)).interpol(projectInterpol(x, y, uve, antiw), projectInterpol(x, y, vve, antiw), Interpolation.NEAREST_NEIGHBOR);
				/*/
				Vector3f texcol = ((SWTexture)(renderItem.getShape().getMaterial().texture)).interpol(projectInterpol(x, y, uve, antiw), projectInterpol(x, y, vve, antiw), Interpolation.BILINEAR);
				// */
				r = r*texcol.x;
				g = g*texcol.y;
				b = b*texcol.z;
				
				colorBackBuffer.setRGB(x, y, (new Color(r, g, b)).getRGB());
			}
		}
		
		private float projectInterpol(int x, int y, Vector3f col, float antiw)
		{
			float c = (col.x*x + col.y*y + col.z)/antiw;
			c = (c < 0)?(0):(c);
			c = (c > 1)?(1):(c);
			return c;
		}
		
		private boolean inside(int x, int y)
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
		
		private boolean cornerInside(int xb, int yb, int delta)
		{
			boolean test = false;

			test = test || (xb*vertex1.w - 0.1 < vertex1.x && vertex1.x < (xb + delta)*vertex1.w + 0.1);
			test = test || (yb*vertex1.w - 0.1 < vertex1.y && vertex1.y < (yb + delta)*vertex1.w + 0.1);
			test = test || (xb*vertex2.w - 0.1 < vertex2.x && vertex2.x < (xb + delta)*vertex2.w + 0.1);
			test = test || (yb*vertex2.w - 0.1 < vertex2.y && vertex2.y < (yb + delta)*vertex2.w + 0.1);
			test = test || (xb*vertex3.w - 0.1 < vertex3.x && vertex3.x < (xb + delta)*vertex3.w + 0.1);
			test = test || (yb*vertex3.w - 0.1 < vertex3.y && vertex3.y < (yb + delta)*vertex3.w + 0.1);
			
			return test;
		}
		
		private boolean testHline(int xmin, int xmax, int y)
		{
			boolean retval = false;
			for(int x = xmin; x < xmax; x++)
			{
				if(inside(x, y))
				{
					calcPixel(y, x);
					retval = true;
				}
			}
			return retval;
		}
		
		private boolean testVline(int x, int ymin, int ymax)
		{
			boolean retval = false;
			for(int y = ymin; y < ymax; y++)
			{
				if(inside(x, y))
				{
					calcPixel(y, x);
					retval = true;
				}
			}
			return retval;
		}
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
