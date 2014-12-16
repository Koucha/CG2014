package fun;

import jrtr.*;

public final class ClosedCubeRS
{
	private static Shape instance = null;
	
	private static Shape generate()
	{
		Shape shape = null;
		
		float v[] = { 	1,1,1, 		1,1,-1, 	-1,1,-1, 	-1,1,1,		// top
				-1,-1,1,	-1,-1,-1, 	1,-1,-1, 	1,-1,1};	// bottom

		float c[] = {	1,0,0,	1,1,0, 	1,0,0, 	1,1,0,
			 	1,1,0, 	1,0,0, 	1,1,0, 	1,0,0 }; 


		int indices[] = {	0,2,3, 	0,1,2, //top
					7,3,4, 	7,0,3, //front
					6,0,7, 	6,1,0, //right
					5,1,6, 	5,2,1, //back
					4,2,5, 	4,3,2, //left
					6,4,5, 	6,7,4  //bottom
				};
		
		VertexData vertexData = Main.renderContext.makeVertexData(8);
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
