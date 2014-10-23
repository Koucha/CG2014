package a_2_2;

import jrtr.*;

import javax.swing.*;

import java.awt.event.*;
import javax.vecmath.*;

/**
 * Implements a simple application that opens a 3D rendering window and 
 * shows a rotating cube.
 */
public class Main
{
	static RenderPanel renderPanel;
	static RenderContext renderContext;
	static Shader normalShader;
	static Shader diffuseShader;
	static Material material;
	static SimpleSceneManager sceneManager;
	static RenderShape theThing;
	
	static Matrix4f baseRot;
	static Vector3f mouseDragStart;
	static final int DRAGRESOLUTION = 64;

	/**
	 * An extension of {@link GLRenderPanel} or {@link SWRenderPanel} to 
	 * provide a call-back function for initialization. Here we construct
	 * a simple 3D scene and start a timer task to generate an animation.
	 */ 
	public final static class SimpleRenderPanel extends GLRenderPanel
	{
		/**
		 * Initialization call-back. We initialize our renderer here.
		 * 
		 * @param r	the render context that is associated with this render panel
		 */
		public void init(RenderContext r)
		{
			renderContext = r;
			
			baseRot = new Matrix4f();
			baseRot.setIdentity();
			mouseDragStart = new Vector3f(0,1,0);
								
			// Make a scene manager and add the object
			sceneManager = new SimpleSceneManager();
			
			theThing = new TeaPotRS(null);
			
			theThing.attachTo(sceneManager);
			sceneManager.setCamera(new Camera(new Vector3f(0,0,40), new Vector3f(0,0,0), new Vector3f(0,1,0)));
			sceneManager.setFrustum(new Frustum(1, 100, 1, 60));

			// Add the scene to the renderer
			renderContext.setSceneManager(sceneManager);
			
			// Load some more shaders
		    normalShader = renderContext.makeShader();
		    try {
		    	normalShader.load("../jrtr/shaders/normal.vert", "../jrtr/shaders/normal.frag");
		    } catch(Exception e) {
		    	System.out.print("Problem with shader:\n");
		    	System.out.print(e.getMessage());
		    }
	
		    diffuseShader = renderContext.makeShader();
		    try {
		    	diffuseShader.load("../jrtr/shaders/diffuse.vert", "../jrtr/shaders/diffuse.frag");
		    } catch(Exception e) {
		    	System.out.print("Problem with shader:\n");
		    	System.out.print(e.getMessage());
		    }

		    // Make a material that can be used for shading
			material = new Material();
			material.shader = diffuseShader;
			material.texture = renderContext.makeTexture();
			try {
				material.texture.load("../textures/plant.jpg");
			} catch(Exception e) {				
				System.out.print("Could not load texture.\n");
				System.out.print(e.getMessage());
			}

			renderContext.useShader(normalShader);
		}
	}

	/**
	 * A mouse listener for the main window of this application. This can be
	 * used to process mouse events.
	 */
	public static class SimpleMouseListener implements MouseListener
	{
		public void mousePressed(MouseEvent e)
		{
			mouseDragStart = toSphere(e.getPoint().x, e.getPoint().y);
		}
		
		public void mouseReleased(MouseEvent e)
		{
			Vector3f pos = toSphere(e.getPoint().x, e.getPoint().y);
			
			Vector3f axis = new Vector3f();
			axis.cross(mouseDragStart, pos);
			axis.normalize();
			float angle = mouseDragStart.angle(pos);
			
			if(angle < 0.0001f)
			{
				return;
			}
			
			Matrix4f temp = new Matrix4f();
			temp.setIdentity();

    		Matrix4f temp1 = new Matrix4f();
    		temp1.setIdentity();
    		temp1.setRotation(new AxisAngle4f(axis, angle));
    		
    		temp.mul(temp1, baseRot);
    		baseRot = temp;
			
			theThing.setTransMat(temp);
    		theThing.updateMat();
    		
    		// Trigger redrawing of the render window
    		renderPanel.getCanvas().repaint();
		}
		
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mouseClicked(MouseEvent e) {}
	}

	/**
	 * A mouse motion listener for the main window of this application. This can be
	 * used to process mouse movement events.
	 */
	public static class SimpleMouseMotionListener implements MouseMotionListener
	{

		public void mouseDragged(MouseEvent e)
		{
			Vector3f pos = toSphere(e.getPoint().x, e.getPoint().y);
			
			Vector3f axis = new Vector3f();
			axis.cross(mouseDragStart, pos);
			axis.normalize();
			float angle = mouseDragStart.angle(pos);
			
			if(angle < 0.0001f)
			{
				return;
			}
			
			Matrix4f temp = new Matrix4f();
			temp.setIdentity();

    		Matrix4f temp1 = new Matrix4f();
    		temp1.setIdentity();
    		temp1.setRotation(new AxisAngle4f(axis, angle));
    		
    		temp.mul(temp1, baseRot);
    		
    		if(angle >= Math.PI/DRAGRESOLUTION)
    		{
    			mouseDragStart = pos;
    			baseRot = temp;
    		}
			
			theThing.setTransMat(temp);
    		theThing.updateMat();
    		
    		// Trigger redrawing of the render window
    		renderPanel.getCanvas().repaint(); 
		}

		public void mouseMoved(MouseEvent e) {}
	}

	/**
	 * The main function opens a 3D rendering window, implemented by the class
	 * {@link SimpleRenderPanel}. {@link SimpleRenderPanel} is then called backed 
	 * for initialization automatically. It then constructs a simple 3D scene, 
	 * and starts a timer task to generate an animation.
	 */
	public static void main(String[] args)
	{		
		// Make a render panel. The init function of the renderPanel
		// (see above) will be called back for initialization.
		renderPanel = new SimpleRenderPanel();
		
		// Make the main window of this application and add the renderer to it
		JFrame jframe = new JFrame("A2");
		jframe.setSize(500, 500);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas into a JFrame window

		// Add a mouse and key listener
	    renderPanel.getCanvas().addMouseListener(new SimpleMouseListener());
	    renderPanel.getCanvas().addMouseMotionListener(new SimpleMouseMotionListener());
		renderPanel.getCanvas().setFocusable(true);   	    	    
	    
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jframe.setVisible(true); // show window
	}
	
	private static Vector3f toSphere(int x, int y)
	{
		Vector3f retval = new Vector3f();
		
		// kleinstes passendes quadrat
		int sq = renderPanel.getCanvas().getHeight();
		if(sq > renderPanel.getCanvas().getWidth())
		{
			sq = renderPanel.getCanvas().getWidth();
		}
		sq = sq/2 - 10;
		
		// zentrum des fensters = 0/0
		x = x - renderPanel.getCanvas().getWidth()/2;
		y = -y + renderPanel.getCanvas().getHeight()/2;
		
		// nächster punkt im kreis
		if(x*x + y*y >= sq*sq)
		{
			retval.x = x;
			retval.y = y;
			retval.z = 0;
		}else
		{
			retval.x = x;
			retval.y = y;
			retval.z = (float) Math.sqrt(sq*sq - x*x - y*y);
		}
		
		retval.normalize();
		
		return retval;
	}
}
