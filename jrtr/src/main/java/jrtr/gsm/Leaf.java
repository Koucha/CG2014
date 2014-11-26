package jrtr.gsm;

import java.util.Stack;

import javax.vecmath.Matrix4f;

/**
 * Node that contains SceneGraph data (e.g. Shape, Light, Camera).
 * (Does not manipulate the nodeStack)
 * 
 * @author Florian
 */
public abstract class Leaf implements Node
{
	public <T> boolean isAndProgress(Class<T> classreference, Stack<StackElement> nodeStack, Matrix4f tfMatOnStack)
	{
		return is(classreference);
	}

	public <T> T getAndProgress(Class<T> classreference, Stack<StackElement> nodeStack, Matrix4f tfMatOnStack)
	{
		return get(classreference, tfMatOnStack);
	}
	
	/**
	 * Checks if the Node represents an Object that implements classreference.
	 * 
	 * @param classreference class the Node content should implement
	 * @param nodeStack stack used for iteration
	 * @return true if the Node represents an Object implementing the given class
	 */
	protected abstract <T> boolean is(Class<T> classreference);
	
	/**
	 * Gets the Object represented by the Node (if possible, else returns null).
	 * 
	 * @param classreference class the Node content should implement
	 * @param nodeStack stack used for iteration
	 * @return the Object of represented by the Node or null if the class doesn't match
	 */
	protected abstract <T> T get(Class<T> classreference, Matrix4f tfMatOnStack);
}
