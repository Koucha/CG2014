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
	protected <type> boolean is(Class<type> classtype, Matrix4f tfMatOnStack)
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
	protected <type> type get(Class<type> classtype, Matrix4f tfMatOnStack)
	{
		if(classtype == Light.class)	// type = Light
		{
			light.transform = tfMatOnStack;
			return (type) light;
		}else
		{
			return null;
		}
	}
}
