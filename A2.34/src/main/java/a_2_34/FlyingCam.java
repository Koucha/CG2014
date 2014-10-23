package a_2_34;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import jrtr.Camera;

public class FlyingCam extends Camera
{
	public FlyingCam(Vector3f cameraPos, float xAxisAngle, float yAxisAngle)
	{
		super(cameraPos, new Vector3f(0,0,0), new Vector3f(0,1,0));
		
		setDirection(xAxisAngle, yAxisAngle);
	}
	
	public void moveFwd(float forward)
	{
		Vector3f cop = getCenterOfProjection();
		Vector3f lap = getLookAtPoint();
		lap.sub(cop);
		lap.normalize();
		
		Vector3f trans = (Vector3f) lap.clone();
		trans.scale(forward);
		
		cop.add(trans);
		lap.add(cop);
		
		setCenterOfProjection(cop);
		setLookAtPoint(lap);
	}
	
	public void moveSdw(float left)
	{
		Vector3f cop = getCenterOfProjection();
		Vector3f lap = getLookAtPoint();
		lap.sub(cop);
		lap.normalize();
		
		Vector3f trans = (Vector3f) lap.clone();
		trans.cross(getUpVector(), trans);
		trans.normalize();
		trans.scale(left);
		
		cop.add(trans);
		lap.add(cop);
		
		setCenterOfProjection(cop);
		setLookAtPoint(lap);
	}
	
	public void setDirection(float xAxisAngle, float yAxisAngle)
	{
		Vector3f cop = getCenterOfProjection();
		Vector3f lap = new Vector3f(0,0,-1);
		
		Matrix4f rotx = new Matrix4f();
		rotx.rotX(xAxisAngle);
		
		rotx.transform(lap);
		
		Matrix4f roty = new Matrix4f();
		roty.rotY(yAxisAngle);
		
		roty.transform(lap);
		
		lap.normalize();
		lap.add(cop);
		
		setLookAtPoint(lap);
	}
}
