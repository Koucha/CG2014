package jrtr.gsm;

import java.util.Stack;

public class ShapeNodeRequestData extends NodeRequestData
{
	private GraphSceneManager gsm;

	public ShapeNodeRequestData(GraphSceneManager gsm)
	{
		super();
		
		this.gsm = gsm;
	}

	public ShapeNodeRequestData(Stack<StackElement> nodeStack, GraphSceneManager gsm)
	{
		super(nodeStack);
		
		this.gsm = gsm;
	}

	/**
	 * @return the gsm
	 */
	public GraphSceneManager getGsm()
	{
		return gsm;
	}

	/**
	 * @param gsm the gsm to set
	 */
	public void setGsm(GraphSceneManager gsm)
	{
		this.gsm = gsm;
	}
	
}
