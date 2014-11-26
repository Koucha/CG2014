package fun;

import jrtr.*;

public final class CubeRS
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
		int indices[] = { 0, 2, 3,  0, 1, 2,
						  4, 7, 6,  4, 6, 5,
						  8,10,11,  8, 9,10,
						 12,15,14, 12,14,13,
						 16,18,19, 16,17,18,
						 20,23,22, 20,22,21 };
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
