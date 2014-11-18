package jrtr.gsm;

import java.util.List;

import javax.vecmath.Matrix4f;

import jrtr.Light;
import jrtr.Shape;

public interface Node
{
	public Shape getShape();
	public Light getLight();
	public Matrix4f getTFMat();
	public List<Node> getChildren();
}
