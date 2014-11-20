package jrtr.gsm;

import java.util.Iterator;

import jrtr.Light;

public class GsmLightIterator implements Iterator<Light>
{
	private GraphSceneIterator<Light> intern;
	
	public GsmLightIterator(Node root)
	{
		intern = new GraphSceneIterator<Light>(root, new Light());
	}
	
	public boolean hasNext()
	{
		return intern.hasNext();
	}

	public Light next()
	{
		return intern.next();
	}

	public void remove()
	{
		throw new UnsupportedOperationException("The Light iterator may not manipulate the SceneGraph");
	}

}
