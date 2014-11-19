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
	protected <T> boolean is(Class<T> classtype, Matrix4f tfMatOnStack)
	{
		if(classtype == Light.class)
		{
			return true;
		}else
		{
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T get(Class<T> classtype, Matrix4f tfMatOnStack)
	{
		if(classtype == Light.class)	// type = Light
		{
			light.transform = tfMatOnStack;
			return (T) light;
		}else
		{
			return null;
		}
	}
}
