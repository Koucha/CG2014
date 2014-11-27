package jrtr.gsm;

import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Matrix4f;

/**
 * Node that contains a Group of other Nodes
 * 
 * @author Florian
 */
public class Group implements Node
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
	
	public Group add(Node node)
	{
		children.add(node);
		return this;
	}
	
	public Group remove(Node node)
	{
		children.remove(node);
		return this;
	}
	
	public Group clear()
	{
		children.clear();
		return this;
	}

	public <T> boolean isAndProgress(Class<T> classreference, INodeRequestData nodeRequestData, Matrix4f tfMatOnStack)
	{
		progress(nodeRequestData, tfMatOnStack);
		return false;
	}

	public <T> T getAndProgress(Class<T> classreference, INodeRequestData nodeRequestData, Matrix4f tfMatOnStack)
	{
		progress(nodeRequestData, tfMatOnStack);
		return null;
	}

	/**
	 * Performs the Node specific actions on the nodeStack
	 * 
	 * @param nodeRequestData additional data used in iteration
	 * @param tfMatOnStack matrix belonging to this Node
	 */
	protected void progress(INodeRequestData nodeRequestData, Matrix4f tfMatOnStack)
	{
		for(Node node:getChildren())
		{
			nodeRequestData.getStack().push(new StackElement(node, tfMatOnStack));
		}
	}
}
