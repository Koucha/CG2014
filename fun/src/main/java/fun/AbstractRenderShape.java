package fun;

import javax.vecmath.Matrix4f;

import jrtr.Shape;
import jrtr.SimpleSceneManager;

public abstract class AbstractRenderShape implements RenderShape
{
	RenderShape parent;
	Shape shape;
	Matrix4f transmat;
	
	public AbstractRenderShape(RenderShape parent)
	{
		this.parent = parent;
		
		transmat = new Matrix4f();
		transmat.setIdentity();
	}
	
	public void attachTo(SimpleSceneManager sceneManager)
	{
		sceneManager.addShape(shape);
	}

	public Matrix4f getAbsoluteTransMat()
	{
		return shape.getTransformation();
	}
	
	public Matrix4f getTransMat()
	{
		return transmat;
	}
	
	public void setTransMat(Matrix4f transmat)
	{
		this.transmat = (Matrix4f) transmat.clone();
	}
	
	public void updateMat()
	{
		Matrix4f tm = new Matrix4f();
		tm.setIdentity();
		tm.mul(getParentTransMat(), transmat);
		shape.setTransformation(tm);
	}

	public Matrix4f getParentTransMat()
	{
		if(parent == null)
		{
			Matrix4f bs = new Matrix4f();
			bs.setIdentity();
			return bs;
		}else
		{
			return parent.getAbsoluteTransMat();
		}
	}

}
