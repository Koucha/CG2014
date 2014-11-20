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
	 * Checks if the Node represents an Object that implements classreference.
	 * And performs all Node specific actions on the nodeStack.
	 * 
	 * @param classreference class the Node content should implement
	 * @param nodeStack stack used for iteration
	 * @param tfMatOnStack matrix belonging to this Node
	 * @return true if the Node represents an Object implementing the given class
	 */
	public <T> boolean isAndProgress(Class<T> classreference, Stack<StackElement> nodeStack, Matrix4f tfMatOnStack);
	
	/**
	 * Performs all Node specific actions on the nodeStack.
	 * And gets the Object represented by the Node (if possible, else returns null).
	 * 
	 * @param classreference class the Node content should implement
	 * @param nodeStack stack used for iteration
	 * @param tfMatOnStack matrix belonging to this Node
	 * @return the Object of represented by the Node or null if the class doesn't match
	 */
	public <T> T getAndProgress(Class<T> classreference, Stack<StackElement> nodeStack, Matrix4f tfMatOnStack);
}
