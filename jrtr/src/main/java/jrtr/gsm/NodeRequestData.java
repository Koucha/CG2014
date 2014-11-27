package jrtr.gsm;

import java.util.Stack;

/**
 * Additional Data passed when requesting Object from Node.
 * Basic implementation; only carries nodeStack and transformationToWorld. 
 * 
 * @author Florian
 */
public class NodeRequestData implements INodeRequestData
{
	private Stack<StackElement> nodeStack;

	public NodeRequestData()
	{
		this.nodeStack = null;
	}
	
	public NodeRequestData(Stack<StackElement> nodeStack)
	{
		this.nodeStack = nodeStack;
	}
	
	public Stack<StackElement> getStack()
	{
		return nodeStack;
	}

	public void setStack(Stack<StackElement> nodeStack)
	{
		this.nodeStack = nodeStack;
	}
	
}
