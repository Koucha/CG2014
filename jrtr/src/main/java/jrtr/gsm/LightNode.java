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
	protected <T> boolean is(Class<T> classreference, INodeRequestData nodeRequestData, Matrix4f tfMatOnStack)
	{
		if(classreference.isAssignableFrom(Light.class))
		{
			return true;
		}else
		{
			return false;
		}
	}

	@Override
	protected <T> T get(Class<T> classreference, INodeRequestData nodeRequestData, Matrix4f tfMatOnStack)
	{
		if(classreference.isAssignableFrom(Light.class))
		{
			light.transform = tfMatOnStack;
			
			@SuppressWarnings("unchecked")
			T temp = (T) light;
			return temp;
		}else
		{
			return null;
		}
	}
}
