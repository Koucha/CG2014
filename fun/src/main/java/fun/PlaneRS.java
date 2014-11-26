package fun;

import jrtr.*;

public final class PlaneRS
{
	private static Shape instance = null;
	
	private static Shape generate()
	{
		Shape shape = null;
		
		float v[] = {-0.5f,-0.5f,0, 0.5f,-0.5f,0, 0.5f,0.5f,0, -0.5f,0.5f,0};
		float n[] = {0,0,1, 0,0,1, 0,0,1, 0,0,1};
		float c[] = {1,1,1, 1,1,1, 1,1,1, 1,1,1};
		float t[] = {0,0, 1,0, 1,1, 0,1};
		VertexData vertexData = Main.renderContext.makeVertexData(4);
		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(n, VertexData.Semantic.NORMAL, 3);
		vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
		vertexData.addElement(t, VertexData.Semantic.TEXCOORD, 2);
		int indices[] = {0,2,3, 0,1,2};
		vertexData.addIndices(indices);
		
		shape = new Shape(vertexData);
		
		return shape;
	}
	
	public static Shape getInstance()
	{
		if(instance == null)
		{
			instance = generate();
		}
		return instance;
	}
}
