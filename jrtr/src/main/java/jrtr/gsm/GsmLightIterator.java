package jrtr.gsm;

import java.util.Iterator;
import java.util.Stack;

import javax.vecmath.Matrix4f;

import jrtr.Light;
import jrtr.RenderItem;

public class GsmLightIterator implements Iterator<Light>
{
	Stack<StackElement> nodeStack;
	
	public GsmLightIterator(Node root)
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
			
			if(stckel.node instanceof LightNode)
			{
				nodeStack.push(stckel);
				return true;
			}else if(stckel.node instanceof Group)
			{
				Matrix4f mat = new Matrix4f(stckel.tfMat);
				
				if(stckel.node.getTFMat() != null)
				{
					mat.mul(stckel.node.getTFMat());
				}
				
				for(Node node:stckel.node.getChildren())
				{
					if(node instanceof Group || node instanceof LightNode)
					{
						nodeStack.push(new StackElement(node, mat));
					}
				}
			}
		}
		
		return false;
	}

	public Light next()
	{
		if(nodeStack.empty())
		{
			return null;
		}
		
		StackElement stckel = nodeStack.pop();
		
		if(stckel.node instanceof Group)
		{
			Matrix4f mat = new Matrix4f(stckel.tfMat);
			
			if(stckel.node.getTFMat() != null)
			{
				mat.mul(stckel.node.getTFMat());
			}
			
			for(Node node:stckel.node.getChildren())
			{
				if(node instanceof Group || node instanceof LightNode)
				{
					nodeStack.push(new StackElement(node, mat));
				}
			}
			return next();
		}else if(stckel.node instanceof LightNode)
		{
			stckel.node.getLight().transform = stckel.tfMat;
			return stckel.node.getLight();
		}else
		{
			return next();
		}
	}

	public void remove()
	{
		// TODO Auto-generated method stub??
	}

}
