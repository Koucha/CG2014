package jrtr;
import javax.vecmath.*;

import jrtr.VertexData.VertexElement;

/**
 * Represents a 3D object. The shape references its geometry, 
 * that is, a triangle mesh stored in a {@link VertexData} 
 * object, its {@link Material}, and a transformation {@link Matrix4f}.
 */
public class Shape {

	private Material material;
	private VertexData vertexData;
	private Matrix4f t;

	private Vector3f boundingSphereCenter;
	private float boundingSphereRadius;
	
	/**
	 * Make a shape from {@link VertexData}. A shape contains the geometry 
	 * (the {@link VertexData}), material properties for shading (a 
	 * refernce to a {@link Material}), and a transformation {@link Matrix4f}.
	 *  
	 *  
	 * @param vertexData the vertices of the shape.
	 */
	public Shape(VertexData vertexData)
	{
		this.vertexData = vertexData;
		t = new Matrix4f();
		t.setIdentity();
		
		material = null;
		
		boundingSphereCenter = null;
		setBoundingSphereRadius(0);
	}
	
	public VertexData getVertexData()
	{
		return vertexData;
	}
	
	public void setTransformation(Matrix4f t)
	{
		this.t = t;
	}
	
	public Matrix4f getTransformation()
	{
		return t;
	}
	
	/**
	 * Set a reference to a material for this shape.
	 * 
	 * @param material
	 * 		the material to be referenced from this shape
	 */
	public void setMaterial(Material material)
	{
		this.material = material;
	}

	/**
	 * To be implemented in the "Textures and Shading" project.
	 */
	public Material getMaterial()
	{
		return material;
	}

	public float getBoundingSphereRadius()
	{
		if(boundingSphereRadius == 0)
		{
			calculateBoundingSphere();
		}
		
		return boundingSphereRadius;
	}

	public void setBoundingSphereRadius(float boundingSphereRadius)
	{
		this.boundingSphereRadius = boundingSphereRadius;
	}

	public Vector3f getBoundingSphereCenter()
	{
		if(boundingSphereCenter == null)
		{
			calculateBoundingSphere();
		}
		
		return boundingSphereCenter;
	}

	public void setBoundingSphereCenter(Vector3f boundingSphereCenter)
	{
		this.boundingSphereCenter = boundingSphereCenter;
	}

	public void calculateBoundingSphere()
	{
		boundingSphereCenter = new Vector3f(0,0,0);
		boundingSphereRadius = 0;
		
		for(VertexElement el:vertexData.getElements())
		{
			if(el.getSemantic() == VertexData.Semantic.POSITION)
			{
				float[] dat = el.getData();
				
				for(int i = 0; i < vertexData.getNumberOfVertices(); i++)
				{
					boundingSphereCenter.add(new Vector3f(dat[3*i], dat[3*i + 1], dat[3*i + 2]));
				}
				boundingSphereCenter.scale(1.0f/vertexData.getNumberOfVertices());
				
				for(int i = 0; i < vertexData.getNumberOfVertices(); i++)
				{
					Vector3f vec = new Vector3f(dat[3*i], dat[3*i + 1], dat[3*i + 2]);
					vec.sub(boundingSphereCenter);
					boundingSphereRadius = Math.max(boundingSphereRadius, vec.length());
				}
				break;
			}
		}
	}

}
