package a_2_1;

import javax.vecmath.Matrix4f;

import jrtr.SimpleSceneManager;

public interface RenderShape
{
	public void attachTo(SimpleSceneManager sceneManager);
	public Matrix4f getAbsoluteTransMat();
	public Matrix4f getTransMat();
	public void setTransMat(Matrix4f transmat);
	public void updateMat();
	public Matrix4f getParentTransMat();
}
