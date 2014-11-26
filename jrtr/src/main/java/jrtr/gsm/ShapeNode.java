package jrtr.gsm;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
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
			if(ShapeNodeRequestData.class.isAssignableFrom(nodeRequestData.getClass()))
			{
				if(isBoundingSphereIntersecting(nodeRequestData.getTF(), ((ShapeNodeRequestData)nodeRequestData).getGsm()) == false)
				{
					return false;
				}
			}
			
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
			if(ShapeNodeRequestData.class.isAssignableFrom(nodeRequestData.getClass()))
			{
				if(isBoundingSphereIntersecting(nodeRequestData.getTF(), ((ShapeNodeRequestData)nodeRequestData).getGsm()) == false)
				{
					return null;
				}
			}
			
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
		Matrix4f canonToObjVert = new Matrix4f();
		canonToObjVert.setIdentity();
		canonToObjVert.mul(transformationToWorld);	//obj->world
		canonToObjVert.mul(gsm.getCamera().getCameraMatrix());	//obj->cam
		canonToObjVert.mul(gsm.getFrustum().getProjectionMatrix());	//obj->canonic
		
		Matrix4f canonToObjNormal = new Matrix4f(canonToObjVert);
		canonToObjNormal.transpose();	//transpose(invert(canon->obj))
		
		try {
			canonToObjVert.invert();	//canon->obj
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
		
		if(isBoundingSphereOverPlane(new Vector4f(1,0,0,1), new Vector4f(1,0,0,0), canonToObjVert, canonToObjNormal) ||
		   isBoundingSphereOverPlane(new Vector4f(0,1,0,1), new Vector4f(0,1,0,0), canonToObjVert, canonToObjNormal) ||
		   isBoundingSphereOverPlane(new Vector4f(0,0,1,1), new Vector4f(0,0,1,0), canonToObjVert, canonToObjNormal) ||
		   isBoundingSphereOverPlane(new Vector4f(-1,0,0,1), new Vector4f(-1,0,0,0), canonToObjVert, canonToObjNormal) ||
		   isBoundingSphereOverPlane(new Vector4f(0,-1,0,1), new Vector4f(0,-1,0,0), canonToObjVert, canonToObjNormal) ||
		   isBoundingSphereOverPlane(new Vector4f(0,0,-1,1), new Vector4f(0,0,-1,0), canonToObjVert, canonToObjNormal) )
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
		normal.w = 0;
		normal.normalize();
		Vector3f fuckingNormal = new Vector3f(normal.x, normal.y, normal.z);
		
		float signedDistancePlaneOrigin = vertex.dot(normal);
		float signedDistanceSphereCenterOrigin = shape.getBoundingSphereCenter().dot(fuckingNormal);
		
		if(signedDistanceSphereCenterOrigin - signedDistancePlaneOrigin > shape.getBoundingSphereRadius())
		{
			return false;	//lies above plane
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
