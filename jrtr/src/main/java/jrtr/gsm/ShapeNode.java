package jrtr.gsm;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector4f;

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
//			if(ShapeNodeRequestData.class.isAssignableFrom(nodeRequestData.getClass()))
//			{
//				if(isBoundingSphereIntersecting(tfMatOnStack, ((ShapeNodeRequestData)nodeRequestData).getGsm()) == false)
//				{
//					return false;
//				}
//			}
			
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
//			if(ShapeNodeRequestData.class.isAssignableFrom(nodeRequestData.getClass()))
//			{
//				if(isBoundingSphereIntersecting(tfMatOnStack, ((ShapeNodeRequestData)nodeRequestData).getGsm()) == false)
//				{
//					return null;
//				}
//			}
			
			@SuppressWarnings("unchecked")
			T temp = (T) makeRenderItem(tfMatOnStack);
			return temp;
		}else
		{
			return null;
		}
	}
	
	/**
	 * 
	 * @param transformationToWorld objToWorld transformation matrix
	 * @param gsm GraphSceneManager containing Camera and Frustum (World -> CamSpace -> Canonic view vol)
	 * @return true if the Sphere intersects the view volume
	 */
	private boolean isBoundingSphereIntersecting(Matrix4f transformationToWorld, GraphSceneManager gsm)
	{
		Matrix4f canonToObjVert = new Matrix4f(gsm.getFrustum().getProjectionMatrix());
		canonToObjVert.mul(gsm.getCamera().getCameraMatrix());
		canonToObjVert.mul(transformationToWorld);
		
		Matrix4f canonToObjNormal = new Matrix4f(canonToObjVert);
		canonToObjNormal.transpose();	//transpose(invert(canon->obj))
		
		try
		{
			canonToObjVert.invert();	//canon->obj
		} catch (Exception e)
		{
			return true;
		}
		
		if(isBoundingSphereOverPlane(new Vector4f(1,1,1,1), new Vector4f(1,0,0,0), canonToObjVert, canonToObjNormal) ||
		   isBoundingSphereOverPlane(new Vector4f(-1,1,1,1), new Vector4f(0,1,0,0), canonToObjVert, canonToObjNormal) ||
		   isBoundingSphereOverPlane(new Vector4f(-1,1,1,1), new Vector4f(0,0,1,0), canonToObjVert, canonToObjNormal) ||
		   isBoundingSphereOverPlane(new Vector4f(-1,1,1,1), new Vector4f(-1,0,0,0), canonToObjVert, canonToObjNormal) ||
		   isBoundingSphereOverPlane(new Vector4f(-1,-1,1,1), new Vector4f(0,-1,0,0), canonToObjVert, canonToObjNormal) ||
		   isBoundingSphereOverPlane(new Vector4f(-1,1,-1,1), new Vector4f(0,0,-1,0), canonToObjVert, canonToObjNormal) )
		{
			return false;	// Sphere is outside at least one plane
		}

		return true;
	}
	
	/**
	 * Tests if the bounding sphere is above the given plane
	 * @param vertex some point on the plane (canonic view volume coordinates)
	 * @param normal normal of the plane (canonic view volume coordinates)
	 * @param canonToObjVert canonic view volume -> object space transformation matrix
	 * @param canonToObjNormal normal transformation matrix (= transpose(invert(canonToObjVert)) )
	 * @return true if above else false
	 */
	private boolean isBoundingSphereOverPlane(Vector4f vertex, Vector4f normal, Matrix4f canonToObjVert, Matrix4f canonToObjNormal)
	{
		canonToObjVert.transform(vertex);
		canonToObjNormal.transform(normal);
		//normal.w = 0;
		//normal.normalize();
		
		Vector4f dist = new Vector4f(shape.getBoundingSphereCenter().x - vertex.x,
									 shape.getBoundingSphereCenter().y - vertex.y,
									 shape.getBoundingSphereCenter().z - vertex.z,
									 0 );
		
		float signedDistance = dist.dot(normal);
		
		if(signedDistance > shape.getBoundingSphereRadius())
		{
			return true;	//lies above plane
		}else
		{
			return false;	//lies beyond plane
		}
	}

	protected RenderItem makeRenderItem(Matrix4f transformationToWorld)
	{
		return new RenderItem(shape, transformationToWorld);
	}
}
