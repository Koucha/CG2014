package jrtr.gsm;

import java.util.NoSuchElementException;
import java.util.Stack;

import javax.vecmath.Matrix4f;

public class GraphSceneIterator
{
	Stack<StackElement> nodeStack;
	
	public GraphSceneIterator(Node root)
	{
		nodeStack = new Stack<StackElement>();
		Matrix4f identity = new Matrix4f();
		identity.setIdentity();
		nodeStack.push(new StackElement(root, identity));
	}

	public <T> boolean hasNext(Class<T> classreference)
	{
		if(nodeStack.empty())
		{
			return false;
		}
		
		StackElement stckel = null;
		
		while(!nodeStack.empty())
		{
			stckel = nodeStack.pop();
			
			if(stckel.isAndProgress(classreference, nodeStack))
			{
				nodeStack.push(stckel);
				return true;
			}
		}
		
		return false;
	}

	public <T> T next(Class<T> classreference)
	{
		if(nodeStack.empty())
		{
			throw new NoSuchElementException();
		}
		
		StackElement stckel = null;
		T retObj = null;
		
		while(!nodeStack.empty())
		{
			stckel = nodeStack.pop();
			retObj = stckel.getAndProgress(classreference, nodeStack);
			
			if(retObj != null)
			{
				return retObj;
			}
		}
		
		throw new NoSuchElementException();
	}
}
