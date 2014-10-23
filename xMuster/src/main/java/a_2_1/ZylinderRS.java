package a_2_1;

import jrtr.*;

public class ZylinderRS extends AbstractRenderShape
{
	public ZylinderRS(RenderShape parent, int numSides, float height, float radius, float c1, float c2, float c3)
	{
		super(parent);
		
		assert(numSides > 2);
		
		int vertexCount = 12*numSides;

		// The vertex positions of the cube
		float v[] = new float[3*vertexCount];
		
		// The vertex normals
		float n[] = new float[3*vertexCount];

		// The vertex colors
		float c[] = new float[3*vertexCount];

		// The triangles (three vertex indices for each triangle)
		int indices[] = new int[vertexCount];
		
		float x1 = radius, x2 = radius, y1 = 0, y2 = 0, nx1 = 1, nx2 = 1, ny1 = 0, ny2 = 0;
		for(int i = 0; i < numSides; i++)
		{
			nx2 = (float) Math.cos( 2*Math.PI*( ((float)i+1)/numSides ) );
			ny2 = (float) Math.sin( 2*Math.PI*( ((float)i+1)/numSides ) );
			x2 = nx2 * radius;
			y2 = nx1 * radius;
			
			v[36*i     ] =  0; v[36*i +  1] =  height; v[36*i +  2] =  0; indices[12*i     ] = 12*i +  0;
			v[36*i +  3] = x1; v[36*i +  4] =  height; v[36*i +  5] = y1; indices[12*i +  1] = 12*i +  1;
			v[36*i +  6] = x2; v[36*i +  7] =  height; v[36*i +  8] = y2; indices[12*i +  2] = 12*i +  2;
			n[36*i     ] = 0; n[36*i +  1] =  1; n[36*i +  2] = 0;
			n[36*i +  3] = 0; n[36*i +  4] =  1; n[36*i +  5] = 0;
			n[36*i +  6] = 0; n[36*i +  7] =  1; n[36*i +  8] = 0;

			v[36*i +  9] = x2; v[36*i + 10] =  height; v[36*i + 11] = y2; indices[12*i +  3] = 12*i +  3;
			v[36*i + 12] = x1; v[36*i + 13] =  height; v[36*i + 14] = y1; indices[12*i +  4] = 12*i +  4;
			v[36*i + 15] = x1; v[36*i + 16] = -height; v[36*i + 17] = y1; indices[12*i +  5] = 12*i +  5;
			n[36*i +  9] = nx2; n[36*i + 10] = 0; n[36*i + 11] = ny2;
			n[36*i + 12] = nx1; n[36*i + 13] = 0; n[36*i + 14] = ny1;
			n[36*i + 15] = nx1; n[36*i + 16] = 0; n[36*i + 17] = ny1;

			v[36*i + 18] = x2; v[36*i + 19] =  height; v[36*i + 20] = y2; indices[12*i +  6] = 12*i +  6;
			v[36*i + 21] = x1; v[36*i + 22] = -height; v[36*i + 23] = y1; indices[12*i +  7] = 12*i +  7;
			v[36*i + 24] = x2; v[36*i + 25] = -height; v[36*i + 26] = y2; indices[12*i +  8] = 12*i +  8;
			n[36*i + 18] = nx2; n[36*i + 19] = 0; n[36*i + 20] = ny2;
			n[36*i + 21] = nx1; n[36*i + 22] = 0; n[36*i + 23] = ny1;
			n[36*i + 24] = nx2; n[36*i + 25] = 0; n[36*i + 26] = ny2;
			
			v[36*i + 27] = x2; v[36*i + 28] = -height; v[36*i + 29] = y2; indices[12*i +  9] = 12*i +  9;
			v[36*i + 30] = x1; v[36*i + 31] = -height; v[36*i + 32] = y1; indices[12*i + 10] = 12*i + 10;
			v[36*i + 33] =  0; v[36*i + 34] = -height; v[36*i + 35] =  0; indices[12*i + 11] = 12*i + 11;
			n[36*i + 27] = 0; n[36*i + 28] = -1; n[36*i + 29] = 0;
			n[36*i + 30] = 0; n[36*i + 31] = -1; n[36*i + 32] = 0;
			n[36*i + 33] = 0; n[36*i + 34] = -1; n[36*i + 35] = 0;
			
			x1 = x2;
			y1 = y2;
			nx1 = nx2;
			ny1 = ny2;
			
			for(int j = 0; j < 12; j++)
			{
				c[36*i + 3*j] =  c1; c[36*i + 3*j + 1] =  c2; c[36*i + 3*j + 2] =  c3;
			}
			
		}
		
		// Construct a data structure that stores the vertices, their
		// attributes, and the triangle mesh connectivity
		VertexData vertexData = Main.renderContext.makeVertexData(vertexCount);
		vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(n, VertexData.Semantic.NORMAL, 3);
		//vertexData.addElement(mesh.getTexcords(), VertexData.Semantic.TEXCOORD, 2);
		
		vertexData.addIndices(indices);
		
		shape = new Shape(vertexData);
	}
}
