package jrtr.gsm;

import jrtr.Light;
import jrtr.Shape;

public class LightNode extends Leaf
{
	Light light;
	
	public LightNode()
	{
		light = null;
	}
	
	public LightNode(Light light)
	{
		this.light = light;
	}
	
	public Light getLight()
	{
		return light;
	}
	
	public LightNode setLight(Light light)
	{
		this.light = light;
		return this;
	}

	public Shape getShape()
	{
		return null;
	}
}
