package jrtr;

import jrtr.RenderContext;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.util.Iterator;
import javax.vecmath.Matrix4f;
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
		Iterator<VertexData.VertexElement> vdIt = shape.getVertexData().getElements().descendingIterator();
		
		Matrix4f object = renderItem.getT();
		object.mul(shape.getTransformation());
		
		Matrix4f camera = sceneManager.getCamera().getCameraMatrix();
		Matrix4f projection = sceneManager.getFrustum().getProjectionMatrix();
		
		Matrix4f viewport = new Matrix4f();
		viewport.m00 = colorBuffer.getWidth()/2.0f;
		viewport.m03 = colorBuffer.getWidth()/2.0f;
		viewport.m11 = -colorBuffer.getHeight()/2.0f;
		viewport.m13 = colorBuffer.getHeight()/2.0f;
		viewport.m22 = 0.5f;
		viewport.m23 = 0.5f;
		viewport.m33 = 1;
		
		VertexData.VertexElement ve;
		while(vdIt.hasNext())
		{
			ve = vdIt.next();
			if(ve.getSemantic() == VertexData.Semantic.POSITION)
			{
				float[] vert = ve.getData();
				for(int i = 0; i < shape.getVertexData().getNumberOfVertices(); i++)
				{
					Vector4f vertex = new Vector4f(vert[3*i], vert[3*i + 1], vert[3*i + 2], 1);
					// Object space
					object.transform(vertex);
					// World space
					camera.transform(vertex);
					// Camera space
					projection.transform(vertex);
					// Canonic view volume
					
					if(vertex.w != 0)	// w = 0 ist ausserhalb des canonic view volumes
					{
						// transform to homogeneous coordinates
						vertex.x = vertex.x / vertex.w;
						vertex.y = vertex.y / vertex.w;
						vertex.z = vertex.z / vertex.w;
						vertex.w = 1;
						
						// Canonic view volume cut off
						if(-1 <= vertex.x && vertex.x <= 1 &&
						   -1 <= vertex.y && vertex.y <= 1 &&
						   -1 <= vertex.z && vertex.z <= 1 )
						{
							viewport.transform(vertex);
							//Image space
							
							colorBuffer.setRGB((int)vertex.x, (int)vertex.y, Color.WHITE.getRGB());
						}
					}
				}
				
				break;
			}
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
