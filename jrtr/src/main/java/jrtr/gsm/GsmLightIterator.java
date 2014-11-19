package jrtr.gsm;

import java.util.Iterator;
import jrtr.Light;

public class GsmLightIterator implements Iterator<Light>
{
	private GraphSceneIterator intern;
	
	public GsmLightIterator(Node root)
	{
		intern = new GraphSceneIterator(root);
	}
	
	public boolean hasNext()
	{
		return intern.hasNext(Light.class);
	}

	public Light next()
	{
		return intern.next(Light.class);
	}

	public void remove()
	{
		throw new UnsupportedOperationException("The Light iterator may not manipulate the SceneGraph");
	}

}
