package jrtr.gsm;

import java.util.Stack;

import javax.vecmath.Matrix4f;

/**
 * Additional Data passed when requesting Object from Node.
 * Carries at least nodeStack and transformationToWorld. 
 * 
 * @author Florian
 */
public interface INodeRequestData
{
	/**
	 * @return the nodeStack
	 */
	public Stack<StackElement> getStack();
	
	/**
	 * @param nodeStack the nodeStack to set
	 */
	public void setStack(Stack<StackElement> nodeStack);
	
}