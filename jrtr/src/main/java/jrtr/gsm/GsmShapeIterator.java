package jrtr.gsm;

import java.util.NoSuchElementException;
import java.util.Stack;

import javax.vecmath.Matrix4f;

import jrtr.RenderItem;
import jrtr.SceneManagerIterator;

public class GsmShapeIterator implements SceneManagerIterator
{
	Stack<StackElement> nodeStack;
	
	public GsmShapeIterator(Node root)
	{
		nodeStack = new Stack<StackElement>();
		Matrix4f identity = new Matrix4f();
		identity.setIdentity();
		nodeStack.push(new StackElement(root, identity));
	}

	public boolean hasNext()
	{
		if(nodeStack.empty())
		{
			return false;
		}
		
		StackElement stckel = null;
		
		while(nodeStack != null)
		{
			stckel = nodeStack.pop();
			stckel.node.isOrProgress(RenderItem.class, nodeStack);
		}
		
		return false;
	}

	public RenderItem next()
	{
		if(nodeStack.empty())
		{
			throw new NoSuchElementException();
		}
		
		StackElement stckel = nodeStack.pop();
		
		//TODO
	}
}
