package jrtr.gsm;

import java.util.Iterator;

import jrtr.*;

public class GraphSceneManager implements SceneManagerInterface
{
	private Node root;
	private Camera camera;
	private Frustum frustum;
	
	public SceneManagerIterator iterator()
	{
		return new GraphSceneIteratorIterator(root);
	}

	public Iterator<Light> lightIterator()
	{
		return new GsmLightIterator(root);
	}

	public Camera getCamera()
	{
		return camera;
	}

	public Frustum getFrustum()
	{
		return frustum;
	}
	
	public void setCamera(Camera camera)
	{
		this.camera = camera;
	}

	public void setFrustum(Frustum frustum)
	{
		this.frustum = frustum;
	}
	
	public Node getGraph()
	{
		return root;
	}
	
	public void setGraph(Node root)
	{
		this.root = root;
	}

}
