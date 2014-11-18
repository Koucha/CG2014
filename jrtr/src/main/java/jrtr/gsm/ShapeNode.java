package jrtr.gsm;

import jrtr.Light;
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

	public Light getLight()
	{
		return null;
	}
}
