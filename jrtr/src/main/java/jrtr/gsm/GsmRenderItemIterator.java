package jrtr.gsm;

import jrtr.RenderItem;
import jrtr.SceneManagerIterator;

public class GsmRenderItemIterator implements SceneManagerIterator
{
	private GraphSceneIterator intern;
	
	public GsmRenderItemIterator(Node root, GraphSceneManager gsm)
	{
		intern = new GraphSceneIterator(root, new NodeRequestData());
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
