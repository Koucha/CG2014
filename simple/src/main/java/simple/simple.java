package simple;

import jrtr.*;
import javax.swing.*;
import java.awt.event.*;
import javax.vecmath.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Implements a simple application that opens a 3D rendering window and 
 * shows a rotating cube.
 */
public class simple
{	
	static RenderPanel renderPanel;
	static RenderContext renderContext;
	static Shader normalShader;
	static Shader diffuseShader;
	static Material material;
	static SimpleSceneManager sceneManager;
	static RenderShape plane;
	static RenderShape stand;
	static RenderShape joint1;
	static RenderShape arm1;
	static RenderShape joint2;
	static RenderShape arm2;
	static RenderShape weight;
	static float currentstep, basicstep;
	
	static float winkel1, winkel2, winkelspeed1, winkelspeed2, force2;

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
			
			Math.random();
			
			//init the pendulum
			winkel1 = (float) (Math.random()*3 - 1.5);	// 3 ~ 170° -> +- 85°
			winkel2 = (float) (Math.random()*2 - 1);		// 2 ~ 115° -> +- 56°
			winkelspeed1 = 0;
			winkelspeed2 = 0;
			force2 = (float) Math.cos(winkel1+winkel2);
			//force1 = (float) (force2*Math.cos(winkel2) + Math.cos(winkel1));
								
			// Make a scene manager and add the object
			sceneManager = new SimpleSceneManager();
			
			stand = new QuadRS(null, 6, 1, 0.2f, 0.3f, 0.5f, 0.3f);
			stand.attachTo(sceneManager);
			
			plane = new PlaneRS(stand, 6, 6, 0.2f, 0.2f, 0.2f);
			plane.attachTo(sceneManager);
			
			joint1 = new ZylinderRS(stand, 30, 0.2f, 0.05f, 0.545f, 0.27f, 0.074f);
			joint1.attachTo(sceneManager);
			
			arm1 = new QuadRS(stand, 2.5f, 0.2f, 0.2f, 0.5f, 0.3f, 0.3f);
			arm1.attachTo(sceneManager);
			
			joint2 = new ZylinderRS(arm1, 30, 0.2f, 0.05f, 0.545f, 0.27f, 0.074f);
			joint2.attachTo(sceneManager);
			
			arm2 = new QuadRS(arm1, 2.5f, 0.2f, 0.2f, 0.3f, 0.3f, 0.5f);
			arm2.attachTo(sceneManager);
			
			weight = new TorusRS(arm2, 30, 20, 0.24f,0.11f,  360, 1, 0, 0);
			weight.attachTo(sceneManager);

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

			// Register a timer task
		    Timer timer = new Timer();
		    basicstep = 0.01f;
		    currentstep = basicstep;
		    timer.scheduleAtFixedRate(new AnimationTask(), 0, 10);
		}
	}

	/**
	 * A timer task that generates an animation. This task triggers
	 * the redrawing of the 3D scene every time it is executed.
	 */
	public static class AnimationTask extends TimerTask
	{
		public void run()
		{
			// Pendulum calculations

			float w1 = winkel1;
			float w2 = winkel2;
			float ws1 = winkelspeed1;
			float ws2 = winkelspeed2;
			//float f2 = force2;
			
			force2 = (float)(0.001*Math.abs(ws2) - 0.0001*ws1*Math.sin(w2) + 0.001*Math.cos(w1 + w2));
			float wss2 = (float) (- 0.0005*Math.sin(w1 + w2) - 0.0001*ws1*Math.cos(w2));
			float wss1 = (float) (0.0001*force2*Math.sin(w2) - 0.0005*Math.sin(w1));
			winkelspeed1 += wss1;
			winkelspeed2 += wss2;
			winkel1 += ws1;
			winkel2 += ws2;
			
			// Update transformation by rotating with angle "currentstep"
    		Matrix4f temp = stand.getAbsoluteTransMat();

    		Matrix4f temp1 = new Matrix4f();
    		Matrix4f temp2 = new Matrix4f();
    		
    		//stand
    		temp1.rotX(0.1f*currentstep);
    		temp2.rotY(currentstep);
    		temp.mul(temp1);
    		temp.mul(temp2);
    		stand.setTransMat(temp);
    		
			//plane
    		temp = new Matrix4f(); temp.setIdentity();
    		temp.setTranslation(new Vector3f(0, -3, 0));
    		plane.setTransMat(temp);
    		
			//joint1
    		temp = new Matrix4f(); temp.setIdentity();
    		temp1 = new Matrix4f(); temp1.setIdentity();
    		temp1.rotX((float) (Math.PI/2.0f));
    		temp.setTranslation(new Vector3f(0, 2.6f, 0.15f));
    		temp.mul(temp1);
    		joint1.setTransMat(temp);
    		
			//arm1
    		temp = new Matrix4f(); temp.setIdentity();
    		temp1 = new Matrix4f(); temp1.setIdentity();
    		temp2 = new Matrix4f(); temp2.setIdentity();
    		temp2.setTranslation(new Vector3f(0, -1.1f, 0.15f));
    		temp1.rotZ(winkel1);
    		temp.setTranslation(new Vector3f(0, 2.6f, 0.15f));
    		temp.mul(temp1);
    		temp.mul(temp2);
    		arm1.setTransMat(temp);
    		
			//joint2
    		temp = new Matrix4f(); temp.setIdentity();
    		temp1 = new Matrix4f(); temp1.setIdentity();
    		temp1.rotX((float) (Math.PI/2.0f));
    		temp.setTranslation(new Vector3f(0, -1.1f, 0.15f));
    		temp.mul(temp1);
    		joint2.setTransMat(temp);
    		
			//arm2
    		temp = new Matrix4f(); temp.setIdentity();
    		temp1 = new Matrix4f(); temp1.setIdentity();
    		temp2 = new Matrix4f(); temp2.setIdentity();
    		temp2.setTranslation(new Vector3f(0, -1.1f, 0.15f));
    		temp1.rotZ(winkel2);
    		temp.setTranslation(new Vector3f(0, -1.1f, 0.15f));
    		temp.mul(temp1);
    		temp.mul(temp2);
    		arm2.setTransMat(temp);
    		
    		//weight
    		temp = new Matrix4f(); temp.setIdentity();
    		temp.setTranslation(new Vector3f(0, -1.1f, 0));
    		weight.setTransMat(temp);
    		
    		stand.updateMat();
    		joint1.updateMat();
    		arm1.updateMat();
    		joint2.updateMat();
    		arm2.updateMat();
    		weight.updateMat();
    		plane.updateMat();
    		
    		// Trigger redrawing of the render window
    		renderPanel.getCanvas().repaint(); 
		}
	}

	/**
	 * A mouse listener for the main window of this application. This can be
	 * used to process mouse events.
	 */
	public static class SimpleMouseListener implements MouseListener
	{
    	public void mousePressed(MouseEvent e) {}
    	public void mouseReleased(MouseEvent e) {}
    	public void mouseEntered(MouseEvent e) {}
    	public void mouseExited(MouseEvent e) {}
    	public void mouseClicked(MouseEvent e) {}
	}
	
	/**
	 * A key listener for the main window. Use this to process key events.
	 * Currently this provides the following controls:
	 * 's': stop animation
	 * 'p': play animation
	 * '+': accelerate rotation
	 * '-': slow down rotation
	 * 'd': default shader
	 * 'n': shader using surface normals
	 * 'm': use a material for shading
	 */
	public static class SimpleKeyListener implements KeyListener
	{
		public void keyPressed(KeyEvent e)
		{
			switch(e.getKeyChar())
			{
				case 's': {
					// Stop animation
					currentstep = 0;
					break;
				}
				case 'p': {
					// Resume animation
					currentstep = basicstep;
					break;
				}
				case '+': {
					// Accelerate roation
					currentstep += basicstep;
					break;
				}
				case '-': {
					// Slow down rotation
					currentstep -= basicstep;
					break;
				}
			}
			
			// Trigger redrawing
			renderPanel.getCanvas().repaint();
		}
		
		public void keyReleased(KeyEvent e)
		{
		}

		public void keyTyped(KeyEvent e)
        {
        }

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
		JFrame jframe = new JFrame("simple");
		jframe.setSize(500, 500);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas into a JFrame window

		// Add a mouse and key listener
	    renderPanel.getCanvas().addMouseListener(new SimpleMouseListener());
	    renderPanel.getCanvas().addKeyListener(new SimpleKeyListener());
		renderPanel.getCanvas().setFocusable(true);   	    	    
	    
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jframe.setVisible(true); // show window
	}
}
