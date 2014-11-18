package jrtr.gsm;

import javax.vecmath.Matrix4f;

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
}
