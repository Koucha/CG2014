package jrtr.gsm;

/**
 * Node that contains SceneGraph data (e.g. Shape, Light, Camera).
 * (Does not manipulate the nodeStack)
 * 
 * @author Florian
 */
public abstract class Leaf implements Node
{
	public <T> boolean isAndProgress(Class<T> classreference, INodeRequestData nodeRequestData, Matrix4f tfMatOnStack)
	{
		return is(classreference, nodeRequestData, tfMatOnStack);
	}

	public <T> T getAndProgress(Class<T> classreference, INodeRequestData nodeRequestData, Matrix4f tfMatOnStack)
	{
		return get(classreference, nodeRequestData, tfMatOnStack);
	}
	
	/**
	 * Checks if the Node represents an Object that implements classreference.
	 * 
	 * @param classreference class the Node content should implement
	 * @param nodeRequestData additional data used in iteration
	 * @param tfMatOnStack matrix belonging to this Node
	 * @return true if the Node represents an Object implementing the given class
	 */
	protected abstract <T> boolean is(Class<T> classreference, INodeRequestData nodeRequestData, Matrix4f tfMatOnStack);
	
	/**
	 * Gets the Object represented by the Node (if possible, else returns null).
	 * 
	 * @param classreference class the Node content should implement
	 * @param nodeRequestData additional data used in iteration
	 * @param tfMatOnStack matrix belonging to this Node
	 * @return the Object of represented by the Node or null if the class doesn't match
	 */
	protected abstract <T> T get(Class<T> classreference, INodeRequestData nodeRequestData, Matrix4f tfMatOnStack);
}
