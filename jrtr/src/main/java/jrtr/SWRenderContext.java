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
		
		for(int i = 0; i < indices.length; i = i + 3)
		{
			Iterator<VertexData.VertexElement> vdIt = vd.getElements().descendingIterator();
			VertexData.VertexElement ve;
			while(vdIt.hasNext())
			{
				ve = vdIt.next();
				float[] data = ve.getData();
				switch(ve.getSemantic())
				{
				case POSITION:
					vertex1 = new Vector4f(data[3*indices[i]], data[3*indices[i] + 1], data[3*indices[i] + 2], 1);
					vertex2 = new Vector4f(data[3*indices[i + 1]], data[3*indices[i + 1] + 1], data[3*indices[i + 1] + 2], 1);
					vertex3 = new Vector4f(data[3*indices[i + 2]], data[3*indices[i + 2] + 1], data[3*indices[i + 2] + 2], 1);
					break;
				case NORMAL:
					normal1 = new Vector4f(data[3*indices[i]], data[3*indices[i] + 1], data[3*indices[i] + 2], 1);
					normal2 = new Vector4f(data[3*indices[i + 1]], data[3*indices[i + 1] + 1], data[3*indices[i + 1] + 2], 1);
					normal3 = new Vector4f(data[3*indices[i + 2]], data[3*indices[i + 2] + 1], data[3*indices[i + 2] + 2], 1);
					break;
				case TEXCOORD:
					teco1 = new Vector2f(data[2*indices[i]], data[2*indices[i] + 1]);
					teco2 = new Vector2f(data[2*indices[i + 1]], data[2*indices[i + 1] + 1]);
					teco3 = new Vector2f(data[2*indices[i + 2]], data[2*indices[i + 2] + 1]);
					break;
				case COLOR:
					col1 = new Vector3f(data[3*indices[i]], data[3*indices[i] + 1], data[3*indices[i] + 2]);
					col2 = new Vector3f(data[3*indices[i + 1]], data[3*indices[i + 1] + 1], data[3*indices[i + 1] + 2]);
					col3 = new Vector3f(data[3*indices[i + 2]], data[3*indices[i + 2] + 1], data[3*indices[i + 2] + 2]);
					break;
				}
			}

			toPixelspace.transform(vertex1);
			toPixelspace.transform(vertex2);
			toPixelspace.transform(vertex3);
			
			if(vertex1.w < 0 && vertex2.w < 0 && vertex3.w < 0)	// triangle behind eye
			{
				continue;
			}
			
			Matrix3f vercor = new Matrix3f();
			vercor.m00 = vertex1.x; vercor.m01 = vertex1.y; vercor.m02 = vertex1.w;
			vercor.m10 = vertex2.x; vercor.m11 = vertex2.y; vercor.m12 = vertex2.w;
			vercor.m20 = vertex3.x; vercor.m21 = vertex3.y; vercor.m22 = vertex3.w;
			
			Matrix3f invvercor = new Matrix3f(vercor);
			try{
				invvercor.invert();
			}catch(SingularMatrixException e){	// singular
				continue;
			}
			
			
			float a_a = invvercor.m00, a_b = invvercor.m01, a_g = invvercor.m02;
			float b_a = invvercor.m10, b_b = invvercor.m11, b_g = invvercor.m12;
			float c_a = invvercor.m20, c_b = invvercor.m21, c_g = invvercor.m22;
			
			int xmin = 0, xmax = colorBuffer.getWidth() - 1, ymin = 0, ymax = colorBuffer.getHeight() - 1;
			
			if(vertex1.w > 0 && vertex2.w > 0 && vertex3.w > 0)
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
								
			for(int y = ymin; y <= ymax; y++)
			{
				for(int x = xmin; x <= xmax; x++)
				{
					if( inside(x, y, a_a, b_a, c_a,  a_b, b_b, c_b,  a_g, b_g, c_g) )	// if inside triangle
					{
						Vector3f r = new Vector3f(col1.x, col2.x, col3.x);
						Vector3f g = new Vector3f(col1.y, col2.y, col3.y);
						Vector3f b = new Vector3f(col1.z, col2.z, col3.z);
						Vector3f eins = new Vector3f(1, 1, 1);
						Vector3f bscor = new Vector3f(x, y, 1);

						invvercor.transform(r);
						invvercor.transform(g);
						invvercor.transform(b);
						invvercor.transform(eins);
						float antiw = eins.dot(bscor);
						
						colorBuffer.setRGB(x, y, (new Color(r.dot(bscor)/antiw, g.dot(bscor)/antiw, b.dot(bscor)/antiw)).getRGB());
					}
				}
			}
		}
	}
	
	private boolean inside(int x, int y, float a_a, float b_a, float c_a,  float a_b, float b_b, float c_b,  float a_g, float b_g, float c_g)
	{
		return (a_a*(x + 0.5) + b_a*(y + 0.5) + c_a > 0 &&
			    a_b*(x + 0.5) + b_b*(y + 0.5) + c_b > 0 &&
			    a_g*(x + 0.5) + b_g*(y + 0.5) + c_g > 0) ||
			   (a_a*(x + 0.5) + b_a*(y + 0.5) + c_a == 0 &&
			    a_b*(x + 0.5) + b_b*(y + 0.5) + c_b == 0 &&
			    a_g*(x + 0.5) + b_g*(y + 0.5) + c_g == 0 &&
			    a_a*(x + 1.5) + b_a*(y + 0.5) + c_a >= 0 &&
			    a_b*(x + 1.5) + b_b*(y + 0.5) + c_b >= 0 &&
			    a_g*(x + 1.5) + b_g*(y + 0.5) + c_g >= 0);
	}
	
	private boolean contained(Vector4f point, int xmin, int xmax, int ymin, int ymax)
	{
		return xmin <= point.x && point.x <= xmax && ymin <= point.y && point.y <= ymax;
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
