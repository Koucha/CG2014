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
	 * Checks if the Node represents an Object of classtype class.
	 * And performs all Node specific actions on the nodeStack.
	 * 
	 * @param classtype class that should be matched
	 * @param nodeStack stack used for iteration
	 * @param tfMatOnStack matrix belonging to this Node
	 * @return true if the Node represents an Object of classtype class
	 */
	public <T> boolean isAndProgress(Class<T> classtype, Stack<StackElement> nodeStack, Matrix4f tfMatOnStack);
	
	/**
	 * Performs all Node specific actions on the nodeStack.
	 * And gets the Object represented by the Node.
	 * 
	 * @param classtype class that should be matched
	 * @param nodeStack stack used for iteration
	 * @param tfMatOnStack matrix belonging to this Node
	 * @return the Object of classtype class represented by the Node or null if the classtype doesn't match
	 */
	public <T> T getAndProgress(Class<T> classtype, Stack<StackElement> nodeStack, Matrix4f tfMatOnStack);
}
