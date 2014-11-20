package jrtr.gsm;

import java.util.Stack;

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
	 * Checks if the Node represents an Object of the same class as classreference.
	 * And performs all Node specific actions on the nodeStack.
	 * 
	 * @param classreference Object of the class that should be matched
	 * @param nodeStack stack used for iteration
	 * @return true if the Node represents an Object of the given class
	 */
	public <T> boolean isAndProgress(T classreference, Stack<StackElement> nodeStack)
	{
		return node.isAndProgress(classreference, nodeStack, tfMat);
	}
	
	/**
	 * Neatly wraps Node.getAndProgress and feeds it with the rfMat:
	 * Performs all Node specific actions on the nodeStack.
	 * And gets the Object represented by the Node.
	 * 
	 * @param classreference Object of the class that should be matched
	 * @param nodeStack stack used for iteration
	 * @return the Object of represented by the Node or null if the class doesn't match
	 */
	public <T> T getAndProgress(T classreference, Stack<StackElement> nodeStack)
	{
		return node.getAndProgress(classreference, nodeStack, tfMat);
	}
}
