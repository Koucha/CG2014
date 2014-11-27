package jrtr.gsm;

import javax.vecmath.Matrix4f;

/**
 * Container for one Node and its "to World" transformation Matrix
 * 
 * @author Florian
 */
public class StackElement
{
	public Node node;
	public Matrix4f tfMat;
	
	public StackElement(Node node, Matrix4f tfMat)
	{
		this.node = node;
		this.tfMat = tfMat;
	}
	
	/**
	 * Neatly wraps Node.isAndProgress and feeds it with the rfMat:
	 * Checks if the Node represents an Object that implements classreference.
	 * And performs all Node specific actions on the nodeStack.
	 * 
	 * @param classreference class the Node content should implement
	 * @param nodeRequestData additional data used in iteration
	 * @return true if the Node represents an Object implementing the given class
	 */
	public <T> boolean isAndProgress(Class<T> classreference, INodeRequestData nodeRequestData)
	{
		return node.isAndProgress(classreference, nodeRequestData, tfMat);
	}
	
	/**
	 * Neatly wraps Node.getAndProgress and feeds it with the rfMat:
	 * Performs all Node specific actions on the nodeStack.
	 * And gets the Object represented by the Node (if possible, else returns null).
	 * 
	 * @param classreference class the Node content should implement
	 * @param nodeRequestData additional data used in iteration
	 * @return the Object of represented by the Node or null if the class doesn't match
	 */
	public <T> T getAndProgress(Class<T> classreference, INodeRequestData nodeRequestData)
	{
		return node.getAndProgress(classreference, nodeRequestData, tfMat);
	}
}
