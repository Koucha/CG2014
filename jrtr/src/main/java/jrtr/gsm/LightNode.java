package jrtr.gsm;

import javax.vecmath.Matrix4f;

import jrtr.Light;

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

	@Override
	protected <T> boolean is(T classreference)
	{
		if(classreference instanceof Light)
		{
			return true;
		}else
		{
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T get(T classreference, Matrix4f tfMatOnStack)
	{
		if(classreference instanceof Light)
		{
			light.transform = tfMatOnStack;
			return (T) light;
		}else
		{
			return null;
		}
	}
}
