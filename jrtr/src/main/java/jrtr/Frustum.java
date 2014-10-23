package jrtr;

import javax.vecmath.Matrix4f;

/**
 * Stores the specification of a viewing frustum, or a viewing
 * volume. The viewing frustum is represented by a 4x4 projection
 * matrix. You will extend this class to construct the projection 
 * matrix from intuitive parameters.
 * <p>
 * A scene manager (see {@link SceneManagerInterface}, {@link SimpleSceneManager}) 
 * stores a frustum.
 */
public class Frustum {

	private Matrix4f projectionMatrix;
	private float nearPlane;
	private float farPlane;
	private float aspectRatio;
	private float verticalFieldOfView;
	
	/**
	 * Construct a default viewing frustum. The frustum is given by a 
	 * default 4x4 projection matrix.
	 */
	public Frustum()
	{
		nearPlane = 1;
		farPlane = 100;
		aspectRatio = 1;
		verticalFieldOfView = (float) (60.0/180.0*Math.PI);
		
		update();
	}
	
	public Frustum(float nearPlane, float farPlane, float aspectRatio, float verticalFieldOfView)
	{
		this.nearPlane = nearPlane;
		this.farPlane = farPlane;
		this.aspectRatio = aspectRatio;
		this.verticalFieldOfView = (float) (verticalFieldOfView/180.0*Math.PI);
		
		update();
	}
	
	private void update()
	{
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = (float) (1/(aspectRatio * Math.tan(verticalFieldOfView / 2.0)));
		projectionMatrix.m11 = (float) (1/Math.tan(verticalFieldOfView / 2.0));
		projectionMatrix.m22 = (nearPlane + farPlane)/(nearPlane - farPlane);
		projectionMatrix.m23 = 2*(nearPlane*farPlane)/(nearPlane - farPlane);
		projectionMatrix.m32 = -1;
	}
	
	public float getNearPlane()
	{
		return nearPlane;
	}

	public void setNearPlane(float nearPlane)
	{
		this.nearPlane = nearPlane;
		update();
	}

	public float getFarPlane()
	{
		return farPlane;
	}

	public void setFarPlane(float farPlane)
	{
		this.farPlane = farPlane;
		update();
	}

	public float getAspectRatio()
	{
		return aspectRatio;
	}

	public void setAspectRatio(float aspectRatio)
	{
		this.aspectRatio = aspectRatio;
		update();
	}

	public float getVerticalFieldOfView()
	{
		return verticalFieldOfView;
	}

	public void setVerticalFieldOfView(float verticalFieldOfView)
	{
		this.verticalFieldOfView = (float) (verticalFieldOfView/180.0*Math.PI);
		update();
	}

	/**
	 * Return the 4x4 projection matrix, which is used for example by 
	 * the renderer.
	 * 
	 * @return the 4x4 projection matrix
	 */
	public Matrix4f getProjectionMatrix()
	{
		return projectionMatrix;
	}
}
