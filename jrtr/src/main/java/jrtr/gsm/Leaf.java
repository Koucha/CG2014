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
	public <T> boolean isAndProgress(Class<T> classtype, Stack<StackElement> nodeStack, Matrix4f tfMatOnStack)
	{
		return is(classtype, tfMatOnStack);
	}

	public <T> T getAndProgress(Class<T> classtype, Stack<StackElement> nodeStack, Matrix4f tfMatOnStack)
	{
		return get(classtype, tfMatOnStack);
	}
	
	/**
	 * Checks if the Node represents an Object of classtype class.
	 * 
	 * @param classtype class that should be matched
	 * @param nodeStack stack used for iteration
	 * @return true if the Node represents an Object of classtype class
	 */
	protected abstract <T> boolean is(Class<T> classtype, Matrix4f tfMatOnStack);
	
	/**
	 * Gets the Object represented by the Node.
	 * 
	 * @param classtype class that should be matched
	 * @param nodeStack stack used for iteration
	 * @return the Object of classtype class represented by the Node or null if the classtype doesn't match
	 */
	protected abstract <T> T get(Class<T> classtype, Matrix4f tfMatOnStack);
}
