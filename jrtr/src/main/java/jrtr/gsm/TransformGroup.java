package jrtr.gsm;

import java.util.Stack;

import javax.vecmath.Matrix4f;

/**
 * Group whose children can be transformed via transformation matrix tfMat
 * 
 * @author Florian
 */
public class TransformGroup extends Group
{
	private Matrix4f tfMat;
	
	public TransformGroup()
	{
		tfMat = new Matrix4f();
		tfMat.setIdentity();
	}
	
	public TransformGroup(Matrix4f tfMat)
	{
		this.tfMat = tfMat;
	}
	
	public Matrix4f getTFMat()
	{
		return tfMat;
	}
	
	public TransformGroup setTFMat(Matrix4f tfMat)
	{
		this.tfMat = tfMat;
		return this;
	}

	@Override
	protected void progress(Stack<StackElement> nodeStack, Matrix4f tfMatOnStack)
	{
		Matrix4f mat = new Matrix4f(tfMatOnStack);
		
		mat.mul(tfMat);
		
		for(Node node:getChildren())
		{
			nodeStack.push(new StackElement(node, mat));
		}
	}
}
