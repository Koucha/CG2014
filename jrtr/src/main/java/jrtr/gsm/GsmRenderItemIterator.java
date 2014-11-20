package jrtr.gsm;

import jrtr.RenderItem;
import jrtr.SceneManagerIterator;

public class GsmRenderItemIterator implements SceneManagerIterator
{
	private GraphSceneIterator intern;
	
	public GsmRenderItemIterator(Node root)
	{
		intern = new GraphSceneIterator(root);
	}
	
	public boolean hasNext()
	{
		return intern.hasNext(RenderItem.class);
	}

	public RenderItem next()
	{
		return intern.next(RenderItem.class);
	}

}
