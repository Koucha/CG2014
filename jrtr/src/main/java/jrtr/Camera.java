package jrtr;

import javax.vecmath.*;

/**
 * Stores the specification of a virtual camera. You will extend
 * this class to construct a 4x4 camera matrix, i.e., the world-to-
 * camera transform from intuitive parameters. 
 * 
 * A scene manager (see {@link SceneManagerInterface}, {@link SimpleSceneManager}) 
 * stores a camera.
 */
public class Camera {

	private Matrix4f cameraMatrix;
	
	private Vector3f centerOfProjection;
	private Vector3f lookAtPoint;
	private Vector3f upVector;
	
	/**
	 * Construct a camera with a default camera matrix. The camera
	 * matrix corresponds to the world-to-camera transform. This default
	 * matrix places the camera at (0,0,10) in world space, facing towards
	 * the origin (0,0,0) of world space, i.e., towards the negative z-axis.
	 */
	public Camera()
	{
		this.centerOfProjection = new Vector3f(0,0,10);
		this.lookAtPoint = new Vector3f(0,0,0);
		this.upVector = new Vector3f(0,1,0);
		
		update();
	}
	
	public Camera(Vector3f centerOfProjection, Vector3f lookAtPoint, Vector3f upVector)
	{
		this.centerOfProjection = centerOfProjection;
		this.lookAtPoint = lookAtPoint;
		this.upVector = (Vector3f) upVector.clone();
		this.upVector.normalize();
		
		update();
	}
	
	private void update()
	{
		Vector3f trans = new Vector3f();
		trans = (Vector3f) centerOfProjection.clone();
		trans.negate();
		
		cameraMatrix = new Matrix4f();
		cameraMatrix.setIdentity();
		cameraMatrix.setTranslation(trans);
		
		Matrix4f roty = new Matrix4f();
		Matrix4f rotx = new Matrix4f();
		Matrix4f rotz = new Matrix4f();

		Vector3f dir = new Vector3f(lookAtPoint.x - centerOfProjection.x,
									lookAtPoint.y - centerOfProjection.y,
									lookAtPoint.z - centerOfProjection.z);
		
		float alpha = (float) Math.atan2(dir.x, -dir.z);	//drehung um y
		roty.rotY(alpha);
		float beta = (float) -Math.asin(dir.y / dir.length());	//drehung um x
		rotx.rotX(beta);
		
		Vector3f temp = (Vector3f) upVector.clone();
		roty.transform(temp);
		rotx.transform(temp);
		
		float gamma = (float) Math.atan2(temp.x, temp.y);//drehung um z
		rotz.rotZ(gamma);

		cameraMatrix.mul(roty, cameraMatrix);
		cameraMatrix.mul(rotx, cameraMatrix);
		cameraMatrix.mul(rotz, cameraMatrix);
	}
	
	public Vector3f getCenterOfProjection()
	{
		return (Vector3f) centerOfProjection.clone();
	}

	public void setCenterOfProjection(Vector3f centerOfProjection)
	{
		this.centerOfProjection = centerOfProjection;
		update();
	}

	public Vector3f getLookAtPoint()
	{
		return (Vector3f) lookAtPoint.clone();
	}

	public void setLookAtPoint(Vector3f lookAtPoint)
	{
		this.lookAtPoint = lookAtPoint;
		update();
	}

	public Vector3f getUpVector()
	{
		return (Vector3f) upVector.clone();
	}

	public void setUpVector(Vector3f upVector)
	{
		this.upVector = upVector;
		update();
	}

	/**
	 * Return the camera matrix, i.e., the world-to-camera transform. For example, 
	 * this is used by the renderer.
	 * 
	 * @return the 4x4 world-to-camera transform matrix
	 */
	public Matrix4f getCameraMatrix()
	{
		return cameraMatrix;
	}
}
