package jrtr.gsm;

import javax.vecmath.Matrix4f;

import jrtr.Material;
import jrtr.RenderItem;
import jrtr.Shape;

public class ShapeMaterialNode extends ShapeNode
{
	Material material;
	
	public ShapeMaterialNode()
	{
		super();
		material = null;
	}
	
	public ShapeMaterialNode(Shape shape)
	{
		super(shape);
		material = null;
	}
	
	public ShapeMaterialNode(Shape shape, Material material)
	{
		super(shape);
		this.material = material;
	}
	
	@Override
	protected RenderItem makeRenderItem(Matrix4f transformationToWorld)
	{
		shape.setMaterial(material);
		return new RenderItem(shape, transformationToWorld);
	}

	public Material getMaterial()
	{
		return material;
	}

	public void setMaterial(Material material)
	{
		this.material = material;
	}
	
}
