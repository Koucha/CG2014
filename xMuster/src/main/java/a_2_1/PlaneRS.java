package a_2_1;

import jrtr.*;

public class PlaneRS extends AbstractRenderShape
{
	public PlaneRS(RenderShape parent, float width, float depth, float c1, float c2, float c3)
	{
		super(parent);
		
		float w = width/2, d = depth/2;
		
		// The vertex positions of the cube
		float v[] = new float[3*4];
		v[ 0] =  w; v[ 1] =  0; v[ 2] =  d;
		v[ 3] =  w; v[ 4] =  0; v[ 5] = -d;
		v[ 6] = -w; v[ 7] =  0; v[ 8] = -d;
		v[ 9] = -w; v[10] =  0; v[11] =  d;
		
		// The vertex normals
		float n[] = new float[3*4];
		n[ 0] = 0; n[ 1] = 1; n[ 2] = 0;
		n[ 3] = 0; n[ 4] = 1; n[ 5] = 0;
		n[ 6] = 0; n[ 7] = 1; n[ 8] = 0;
		n[ 9] = 0; n[10] = 1; n[11] = 0;

		// The vertex colors
		float c[] = new float[3*4];
		for(int i = 0; i < 4; i++)
		{
			c[3*i] = c1; c[3*i + 1] = c2; c[3*i + 2] = c3;
		}

		// The triangles (three vertex indices for each triangle)
		int indices[] = new int[3*2];
		indices[0] = 0; indices[1] = 1; indices[2] = 2;
		indices[3] = 0; indices[4] = 2; indices[5] = 3;
		
		// Construct a data structure that stores the vertices, their
		// attributes, and the triangle mesh connectivity
		VertexData vertexData = Main.renderContext.makeVertexData(4);
		vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(n, VertexData.Semantic.NORMAL, 3);
		//vertexData.addElement(mesh.getTexcords(), VertexData.Semantic.TEXCOORD, 2);
		
		vertexData.addIndices(indices);
		
		shape = new Shape(vertexData);
	}
}
