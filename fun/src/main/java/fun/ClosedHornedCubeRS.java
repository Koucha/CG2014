package fun;

import jrtr.*;

public final class ClosedHornedCubeRS
{
	private static Shape instance = null;
	
	private static Shape generate()
	{
		Shape shape = null;
		
		float v[] = { 	0.375f,0.375f,0.375f, 		0.375f,0.375f,-0.375f, 	-0.375f,0.375f,-0.375f, 	-0.375f,0.375f,0.375f,		// top
				-0.375f,-0.375f,0.375f,	-0.375f,-0.375f,-0.375f, 	0.375f,-0.375f,-0.375f, 	0.375f,-0.375f,0.375f,     	// bottom
				0,-1.5f,0,		0,1.5f,0, 		1.5f,0,0,				//spikes
				-1.5f,0,0,		0,0,1.5f,		0,0,-1.5f};   			// spikes

				
		float c[] = {	1,0,0,	1,1,0, 	1,0,0, 	1,1,0,
				1,1,0, 	1,0,0, 	1,1,0, 	1,0,0, 
				0,0,1, 	0,0,1, 	0,0,1,	
				0,0,1,	0,0,1,	0,0,1};

		int indices[] = {	7,4,8,	4,5,8,	5,6,8, 	6,7,8, 	//bottom
					0,1,9,	1,2,9,	2,3,9,	3,0,9, 	//top
					6,1,10,	1,0,10,	0,7,10,	7,6,10,	//right
					4,3,11,	3,2,11,	2,5,11,	5,4,11,	//left
					7,0,12,	0,3,12,	3,4,12,	4,7,12,	//front
					5,2,13,	2,1,13,	1,6,13,	6,5,13	//back					
				};
		
		VertexData vertexData = Main.renderContext.makeVertexData(14);
		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
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
