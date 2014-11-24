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
	
	/**
	 * Scale uniform by factor s
	 * @param s scaling factor
	 * @return the TransformGroup. For chaining methods.
	 */
	public TransformGroup scale(float s)
	{
		Matrix4f mat = new Matrix4f();
		mat.setIdentity();
		
		mat.setScale(s);
		
		tfMat.mul(mat, tfMat);
		
		return this;
	}
	
	/**
	 * Scale in X direction by factor s
	 * @param s scaling factor along X axis
	 * @return the TransformGroup. For chaining methods.
	 */
	public TransformGroup scaleX(float s)
	{
		scale(s,1,1);
		return this;
	}

	/**
	 * Scale in Y direction by factor s
	 * @param s scaling factor along Y axis
	 * @return the TransformGroup. For chaining methods.
	 */
	public TransformGroup scaleY(float s)
	{
		scale(1,s,1);
		return this;
	}

	/**
	 * Scale in Z direction by factor s
	 * @param s scaling factor along Z axis
	 * @return the TransformGroup. For chaining methods.
	 */
	public TransformGroup scaleZ(float s)
	{
		scale(1,1,s);
		return this;
	}
	
	/**
	 * Scale differently along each axis
	 * @param x factor along X axis
	 * @param y factor along Y axis
	 * @param z factor along Z axis
	 * @return the TransformGroup. For chaining methods.
	 */
	public TransformGroup scale(float x, float y, float z)
	{
		Matrix4f mat = new Matrix4f();
		mat.setIdentity();

		mat.m00 = x;
		mat.m11 = y;
		mat.m22 = z;
		
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
