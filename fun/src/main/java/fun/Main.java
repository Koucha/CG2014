package fun;

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
public class Main
{	
	static RenderPanel renderPanel;
	static RenderContext renderContext;
	static Shader normalColShader;
	static SimpleSceneManager sceneManager;
	static FlyingCam flyCam;
	static RenderShape theThing;
	static final float BASESTEP = 0.1f;
	static float xAngle, yAngle, stepsize;
	static boolean keyDownW, keyDownA, keyDownS, keyDownD, keyDownSpace, fixedF;
	static boolean mouseValid;
	static int mousex, mousey;
	static boolean isobaren;
	

	/**
	 * An extension of {@link GLRenderPanel} or {@link SWRenderPanel} to 
	 * provide a call-back function for initialization. Here we construct
	 * a simple 3D scene and start a timer task to generate an animation.
	 */ 
	public final static class SimpleRenderPanel extends /* / GLRenderPanel /*/ SWRenderPanel //*/
	{
		/**
		 * Initialization call-back. We initialize our renderer here.
		 * 
		 * @param r	the render context that is associated with this render panel
		 */
		public void init(RenderContext r)
		{
			renderContext = r;
			
			xAngle = -0.6f;
			yAngle = 0;
			stepsize = BASESTEP;
			keyDownW = keyDownA = keyDownS = keyDownD = keyDownSpace = false;
			fixedF = true;
			mouseValid = false;
			mousex = 0;
			mousey = 0;
			isobaren = false;
								
			// Make a scene manager and add the object
			sceneManager = new SimpleSceneManager();

			theThing = new TorusRS(null, 40, 30, 14, 7, 360, 0.7f, 0.6f, 0.2f);
			theThing.attachTo(sceneManager);
			
			// create camera
			flyCam = new FlyingCam(new Vector3f(0,30,30), -0.6f, 0);

			// Add the scene to the renderer
			renderContext.setSceneManager(sceneManager);
			sceneManager.setCamera(flyCam);
			sceneManager.setFrustum(new Frustum(0.01f, 100, 1, 60));
			
			// Load a shader
		    normalColShader = renderContext.makeShader();
		    try {
		    	normalColShader.load("../jrtr/shaders/normal_col.vert", "../jrtr/shaders/normal_col.frag");
		    } catch(Exception e) {
		    	System.out.print("Problem with shader:\n");
		    	System.out.print(e.getMessage());
		    }
		    
		    renderContext.useShader(normalColShader);

			// Register a timer task
		    Timer timer = new Timer();
		    timer.scheduleAtFixedRate(new AnimationTask(), 0, 8);

		}
	}

	/**
	 * A timer task that generates an animation. This task triggers
	 * the redrawing of the 3D scene every time it is executed.
	 */
	public static class AnimationTask extends TimerTask
	{
		private boolean notYetWorking;
		
		public AnimationTask()
		{
			super();
			
			notYetWorking = true;
		}

		public void run()
		{
			if(notYetWorking)
			{
				notYetWorking = false;	// repaint blockieren für den fall, dass es länger dauert als die refreshrate
				
				flyCam.setDirection(xAngle, yAngle);
				
				if(keyDownW && !keyDownSpace)
				{
					flyCam.moveFwd(stepsize);
				}else if(keyDownS && !keyDownSpace)
				{
					flyCam.moveFwd(-stepsize);
				}else if(keyDownW)
				{
					flyCam.moveUp(stepsize);
				}else if(keyDownS)
				{
					flyCam.moveUp(-stepsize);
				}
				
				if(keyDownD)
				{
					flyCam.moveSdw(-stepsize);
				}else if(keyDownA)
				{
					flyCam.moveSdw(stepsize);
				}
	    		
	    		// Trigger redrawing of the render window
	    		renderPanel.getCanvas().repaint();
	    		
	    		notYetWorking = true;	// repaint freigeben
			}
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
		
		public void mouseExited(MouseEvent e)
		{
			mouseValid = false;
		}
		
		public void mouseClicked(MouseEvent e) {}
	}

	/**
	 * A mouse listener for the main window of this application. This can be
	 * used to process mouse events.
	 */
	public static class SimpleMouseMotionListener implements MouseMotionListener
	{

		public void mouseDragged(MouseEvent e) {}

		public void mouseMoved(MouseEvent e)
		{
			if(!fixedF)
			{
				if(mouseValid)
				{
					// kleinstes passendes quadrat
					float sq = renderPanel.getCanvas().getHeight();
					if(sq > renderPanel.getCanvas().getWidth())
					{
						sq = renderPanel.getCanvas().getWidth();
					}
					
					yAngle = (float) (yAngle - 2*Math.PI*(e.getX() - mousex)/sq);
					xAngle = (float) (xAngle - Math.PI*(e.getY() - mousey)/sq);
					
					if(yAngle > Math.PI)
					{
						yAngle = (float) (yAngle - 2*Math.PI);
					}else if(yAngle < -Math.PI)
					{
						yAngle = (float) (yAngle + 2*Math.PI);
					}
					
					if(xAngle < -1.55)
					{
						xAngle = -1.55f;
					}else if(xAngle > 1.55)
					{
						xAngle = 1.55f;
					}
					
					mousex = e.getX();
					mousey = e.getY();
				}else
				{
					mousex = e.getX();
					mousey = e.getY();
					mouseValid = true;
				}
			}
		}
	}
	
	/**
	 * A key listener for the main window. Use this to process key events.
	 */
	public static class SimpleKeyListener implements KeyListener
	{
		public void keyPressed(KeyEvent e)
		{
			switch(e.getKeyChar())
			{
				case 'w': {
					keyDownW = true;
					break;
				}
				case 's': {
					keyDownS = true;
					break;
				}
				case 'a': {
					keyDownA = true;
					break;
				}
				case 'd': {
					keyDownD = true;
					break;
				}
				case ' ': {
					keyDownSpace = true;
					break;
				}
			}
		}
		
		public void keyReleased(KeyEvent e)
		{
			switch(e.getKeyChar())
			{
				case 'w': {
					keyDownW = false;
					break;
				}
				case 's': {
					keyDownS = false;
					break;
				}
				case 'a': {
					keyDownA = false;
					break;
				}
				case 'd': {
					keyDownD = false;
					break;
				}
				case ' ': {
					keyDownSpace = false;
					break;
				}
			}
		}

		public void keyTyped(KeyEvent e)
        {
			switch(e.getKeyChar())
			{
				case 'R': {
					sceneManager.clearShapes();
					
					Matrix4f temp = new Matrix4f();
					
					theThing = generateNewTerrain();
					theThing.attachTo(sceneManager);
					temp.setIdentity();
					temp.setTranslation(new Vector3f(-15.1f,0,15.1f));
					theThing.setTransMat(temp);
					theThing.updateMat();
					break;
				}
				case 'f': {
					fixedF = !fixedF;
					mouseValid = false;
					break;
				}
				case '1': {
					stepsize = BASESTEP/2;
					break;
				}
				case '2': {
					stepsize = BASESTEP;
					break;
				}
				case '3': {
					stepsize = BASESTEP*2;
					break;
				}
				case '4': {
					stepsize = BASESTEP*3;
					break;
				}
				case 'i': {
					isobaren = !isobaren;
					break;
				}
			}
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
		JFrame jframe = new JFrame("T3 - A2");
		jframe.setSize(700, 700);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas into a JFrame window

		// Add a mouse and key listener
	    renderPanel.getCanvas().addMouseListener(new SimpleMouseListener());
	    renderPanel.getCanvas().addMouseMotionListener(new SimpleMouseMotionListener());
	    renderPanel.getCanvas().addKeyListener(new SimpleKeyListener());
		renderPanel.getCanvas().setFocusable(true);   	    	    
	    
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jframe.setVisible(true); // show window
	}

	private static RenderShape generateNewTerrain()
	{
		return new QETerrainRS(null, 30, 30, 10, 10, isobaren, null, null, null, null);
	}
}
