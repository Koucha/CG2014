package jrtr.gsm;

import javax.vecmath.Matrix4f;

import jrtr.RenderItem;
import jrtr.Shape;

public class ShapeNode extends Leaf
{
	Shape shape;
	
	public ShapeNode()
	{
		shape = null;
	}
	
	public ShapeNode(Shape shape)
	{
		this.shape = shape;
	}
	
	public Shape getShape()
	{
		return shape;
	}
	
	public ShapeNode setShape(Shape shape)
	{
		this.shape = shape;
		return this;
	}

	@Override
	protected <T> boolean is(Class<T> classtype, Matrix4f tfMatOnStack)
	{
		if(classtype == RenderItem.class)
		{
			return true;
		}else
		{
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T get(Class<T> classtype, Matrix4f tfMatOnStack)
	{
		if(classtype == RenderItem.class)	// classtype = Shape
		{
			return (T) makeRenderItem(tfMatOnStack);
		}else
		{
			return null;
		}
	}
	
	protected RenderItem makeRenderItem(Matrix4f tfMatOnStack)
	{
		return new RenderItem(shape, tfMatOnStack);
	}
}
