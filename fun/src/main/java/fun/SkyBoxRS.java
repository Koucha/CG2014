package fun;

import jrtr.*;

public final class SkyBoxRS
{
	private static Shape instance = null;
	
	private static Shape generate()
	{
		Shape shape = null;
		
		float v[] = {-0.5f,-0.5f, 0.5f,  0.5f,-0.5f, 0.5f,  0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f,
					 -0.5f,-0.5f,-0.5f,  0.5f,-0.5f,-0.5f,  0.5f, 0.5f,-0.5f, -0.5f, 0.5f,-0.5f,
					 
					 -0.5f, 0.5f, 0.5f,  0.5f, 0.5f, 0.5f,  0.5f, 0.5f,-0.5f, -0.5f, 0.5f,-0.5f,
					 -0.5f,-0.5f, 0.5f,  0.5f,-0.5f, 0.5f,  0.5f,-0.5f,-0.5f, -0.5f,-0.5f,-0.5f,
					 
					  0.5f, 0.5f, 0.5f,  0.5f,-0.5f, 0.5f,  0.5f,-0.5f,-0.5f,  0.5f, 0.5f,-0.5f,
					 -0.5f, 0.5f, 0.5f, -0.5f,-0.5f, 0.5f, -0.5f,-0.5f,-0.5f, -0.5f, 0.5f,-0.5f, };
		
		float n[] = {0,0, 1, 0,0, 1, 0,0, 1, 0,0, 1, 
					 0,0,-1, 0,0,-1, 0,0,-1, 0,0,-1, 
					 
					 0, 1,0, 0, 1,0, 0, 1,0, 0, 1,0,  
					 0,-1,0, 0,-1,0, 0,-1,0, 0,-1,0,  
					 
					  1,0,0,  1,0,0,  1,0,0,  1,0,0,  
					 -1,0,0, -1,0,0, -1,0,0, -1,0,0 };
		
		float c[] = {1,1,1, 1,1,1, 1,1,1, 1,1,1, 
					 1,1,1, 1,1,1, 1,1,1, 1,1,1, 
					 1,1,1, 1,1,1, 1,1,1, 1,1,1, 
					 1,1,1, 1,1,1, 1,1,1, 1,1,1, 
					 1,1,1, 1,1,1, 1,1,1, 1,1,1, 
					 1,1,1, 1,1,1, 1,1,1, 1,1,1 };
		
		float t[] = {0.333333f,0.25f, 0.666666f,0.25f, 0.666666f,0.5f, 0.333333f,0.5f,
					 0.333333f,1, 0.666666f,1, 0.666666f,0.75f, 0.333333f,0.75f,
					 
					 0.333333f,0.5f, 0.666666f,0.5f, 0.666666f,0.75f, 0.333333f,0.75f,
					 0.333333f,0.25f, 0.666666f,0.25f, 0.666666f,0, 0.333333f,0,
					 
					 0.666666f,0.5f, 1,0.5f, 1,0.75f, 0.666666f,0.75f,
					 0.333333f,0.5f, 0,0.5f, 0,0.75f, 0.333333f,0.75f };
		
		VertexData vertexData = Main.renderContext.makeVertexData(24);
		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(n, VertexData.Semantic.NORMAL, 3);
		vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
		vertexData.addElement(t, VertexData.Semantic.TEXCOORD, 2);
		int indices[] = { 0, 3, 2,  0, 2, 1,
						  4, 6, 7,  4, 5, 6,
						  8,11,10,  8,10, 9,
						 12,14,15, 12,13,14,
						 16,19,18, 16,18,17,
						 20,22,23, 20,21,22 };
		vertexData.addIndices(indices);
		
		shape = new Shape(vertexData);
		
		shape.calculateBoundingSphere();
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
