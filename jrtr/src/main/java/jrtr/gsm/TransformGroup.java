package jrtr.gsm;

import java.util.Stack;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

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
	
	public TransformGroup scale(float s)
	{
		Matrix4f mat = new Matrix4f();
		mat.setIdentity();
		
		mat.setScale(s);
		
		tfMat.mul(mat, tfMat);
		
		return this;
	}
	
	public TransformGroup translate(Vector3f v)
	{
		Matrix4f mat = new Matrix4f();
		mat.setIdentity();
		
		mat.setTranslation(v);;
		
		tfMat.mul(mat, tfMat);
		
		return this;
	}
	
	public TransformGroup rotate(Vector3f axis, float angle)
	{
		Matrix4f mat = new Matrix4f();
		mat.setIdentity();
		
		mat.setRotation(new AxisAngle4f(axis, angle));
		
		tfMat.mul(mat, tfMat);
		
		return this;
	}

	@Override
	protected void progress(Stack<StackElement> nodeStack, Matrix4f tfMatOnStack)
	{
		Matrix4f mat = new Matrix4f(tfMatOnStack);
		
		mat.mul(tfMat);
		
		super.progress(nodeStack, mat);
	}
}
