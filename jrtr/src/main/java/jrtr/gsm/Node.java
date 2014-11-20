package jrtr.gsm;

import java.util.Stack;

import javax.vecmath.Matrix4f;

/**
 * Basic Node containing scene data
 * 
 * @author Florian
 */
public interface Node
{
	/**
	 * Checks if the Node represents an Object of the same class as classreference.
	 * And performs all Node specific actions on the nodeStack.
	 * 
	 * @param classreference Object of the class that should be matched
	 * @param nodeStack stack used for iteration
	 * @param tfMatOnStack matrix belonging to this Node
	 * @return true if the Node represents an Object of the given class
	 */
	public <T> boolean isAndProgress(T classreference, Stack<StackElement> nodeStack, Matrix4f tfMatOnStack);
	
	/**
	 * Performs all Node specific actions on the nodeStack.
	 * And gets the Object represented by the Node.
	 * 
	 * @param classreference Object of the class that should be matched
	 * @param nodeStack stack used for iteration
	 * @param tfMatOnStack matrix belonging to this Node
	 * @return the Object of represented by the Node or null if the class doesn't match
	 */
	public <T> T getAndProgress(T classreference, Stack<StackElement> nodeStack, Matrix4f tfMatOnStack);
}
