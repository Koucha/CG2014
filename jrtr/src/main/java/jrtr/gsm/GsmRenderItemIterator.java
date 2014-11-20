package jrtr.gsm;

import jrtr.RenderItem;
import jrtr.SceneManagerIterator;

public class GsmRenderItemIterator implements SceneManagerIterator
{
	private GraphSceneIterator<RenderItem> intern;
	
	public GsmRenderItemIterator(Node root)
	{
		intern = new GraphSceneIterator<RenderItem>(root, new RenderItem(null, null));
	}
	
	public boolean hasNext()
	{
		return intern.hasNext();
	}

	public RenderItem next()
	{
		return intern.next();
	}

}
