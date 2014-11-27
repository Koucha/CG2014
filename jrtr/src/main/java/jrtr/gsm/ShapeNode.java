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
	protected <T> boolean is(Class<T> classreference, INodeRequestData nodeRequestData, Matrix4f tfMatOnStack)
	{
		if(classreference.isAssignableFrom(RenderItem.class))
		{
			return true;
		}else
		{
			return false;
		}
	}

	@Override
	protected <T> T get(Class<T> classreference, INodeRequestData nodeRequestData, Matrix4f tfMatOnStack)
	{
		if(classreference.isAssignableFrom(RenderItem.class))
		{
			@SuppressWarnings("unchecked")
			T temp = (T) makeRenderItem(tfMatOnStack);
			return temp;
		}else
		{
			return null;
		}
	}
	
	protected RenderItem makeRenderItem(Matrix4f transformationToWorld)
	{
		return new RenderItem(shape, transformationToWorld);
	}
}
