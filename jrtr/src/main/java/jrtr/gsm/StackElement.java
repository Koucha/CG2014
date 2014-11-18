package jrtr.gsm;

import javax.vecmath.Matrix4f;

public class StackElement
{
	public Node node;
	public Matrix4f tfMat;
	public StackElement(Node node, Matrix4f tfMat)
	{
		this.node = node;
		this.tfMat = tfMat;
	}
}
