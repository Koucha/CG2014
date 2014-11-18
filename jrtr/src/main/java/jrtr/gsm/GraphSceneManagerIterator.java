package jrtr.gsm;

import java.util.Stack;

import javax.vecmath.Matrix4f;

import jrtr.RenderItem;
import jrtr.SceneManagerIterator;

public class GraphSceneManagerIterator implements SceneManagerIterator
{
	Stack<StackElement> nodeStack;
	
	public GraphSceneManagerIterator(Node root)
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
			
			if(stckel.node instanceof ShapeNode)
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
					if(node instanceof Group || node instanceof ShapeNode)
					{
						nodeStack.push(new StackElement(node, mat));
					}
				}
			}
		}
		
		return false;
	}

	public RenderItem next()
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
				if(node instanceof Group || node instanceof ShapeNode)
				{
					nodeStack.push(new StackElement(node, mat));
				}
			}
			return next();
		}else if(stckel.node instanceof ShapeNode)
		{
			return new RenderItem(stckel.node.getShape(), stckel.tfMat);
		}else
		{
			return next();
		}
	}
}
