package jrtr.gsm;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.vecmath.Matrix4f;

/**
 * Node that contains a Group of other Nodes
 * 
 * @author Florian
 */
public abstract class Group implements Node
{
	List<Node> children;
	
	public Group()
	{
		children = new LinkedList<Node>();
	}
	
	public Group(List<Node> children)
	{
		this.children = children;
	}

	public List<Node> getChildren()
	{
		return children;
	}
	
	public Group setChildren(List<Node> children)
	{
		this.children = children;
		return this;
	}

	public <T> boolean isAndProgress(T classreference, Stack<StackElement> nodeStack, Matrix4f tfMatOnStack)
	{
		progress(nodeStack, tfMatOnStack);
		return false;
	}

	public <T> T getAndProgress(T classreference, Stack<StackElement> nodeStack, Matrix4f tfMatOnStack)
	{
		progress(nodeStack, tfMatOnStack);
		return null;
	}

	/**
	 * Performs the Node specific actions on the nodeStack
	 * 
	 * @param nodeStack stack used for iteration
	 * @param tfMatOnStack matrix belonging to this Node
	 */
	protected abstract void progress(Stack<StackElement> nodeStack, Matrix4f tfMatOnStack);
}
