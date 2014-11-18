package jrtr.gsm;

import java.util.LinkedList;
import java.util.List;

import jrtr.Light;
import jrtr.Shape;

public abstract class Group implements Node
{
	List<Node> children;
	
	public Group()
	{
		children = new LinkedList<Node>();
	}
	
	public Group(List<Node> children)
	{
		this.children = children;
	}

	public List<Node> getChildren()
	{
		return children;
	}
	
	public Group setChildren(List<Node> children)
	{
		this.children = children;
		return this;
	}
	
	public Shape getShape()
	{
		return null;
	}
	
	public Light getLight()
	{
		return null;
	}
}
