package jrtr.gsm;

import java.util.List;

import javax.vecmath.Matrix4f;

public abstract class Leaf implements Node
{
	public Matrix4f getTFMat()
	{
		return null;
	}

	public List<Node> getChildren()
	{
		return null;
	}
}
