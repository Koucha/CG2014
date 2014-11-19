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
	protected <type> boolean is(Class<type> classtype, Matrix4f tfMatOnStack)
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
	protected <type> type get(Class<type> classtype, Matrix4f tfMatOnStack)
	{
		if(classtype == RenderItem.class)	// classtype = Shape
		{
			return (type) new RenderItem(shape, tfMatOnStack);
		}else
		{
			return null;
		}
	}
}
