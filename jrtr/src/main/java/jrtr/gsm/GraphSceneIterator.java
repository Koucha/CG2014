package jrtr.gsm;

import java.util.NoSuchElementException;
import java.util.Stack;

import javax.vecmath.Matrix4f;

public class GraphSceneIterator<T>
{
	Stack<StackElement> nodeStack;
	T classreference;
	
	public GraphSceneIterator(Node root, T classreference)
	{
		nodeStack = new Stack<StackElement>();
		Matrix4f identity = new Matrix4f();
		identity.setIdentity();
		nodeStack.push(new StackElement(root, identity));
		
		this.classreference = classreference;
	}

	public boolean hasNext()
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

	public T next()
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
