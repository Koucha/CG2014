package fun;

import jrtr.*;

public final class ConeRS
{
	private static Shape instance = null;
	
	private static Shape generate(int numSides)
	{
		assert(numSides > 2);
		
		final float radius = 0.5f;
		
		int vertexCount = 6*numSides;

		// The vertex positions of the cube
		float v[] = new float[3*vertexCount];
		
		// The vertex normals
		float n[] = new float[3*vertexCount];

		// The vertex colors
		float c[] = new float[3*vertexCount];
		
		// The vertex normals
		float tc[] = new float[2*vertexCount];

		// The triangles (three vertex indices for each triangle)
		int indices[] = new int[vertexCount];
		
		float x1 = 0, x2 = 0, y1 = -radius, y2 = -radius, nx1 = 0, nx2 = 0, ny1 = -1, ny2 = -1;
		for(int i = 0; i < numSides; i++)
		{
			nx2 = (float) -Math.sin( 2*Math.PI*( ((float)i+1)/numSides ) );
			ny2 = (float) -Math.cos( 2*Math.PI*( ((float)i+1)/numSides ) );
			x2 = nx2 * radius;
			y2 = ny2 * radius;
			
			v[18*i     ] =  0; v[18*i +  1] =  0.5f; v[18*i +  2] =  0; indices[6*i     ] = 6*i +  0;
			v[18*i +  3] = x1; v[18*i +  4] = -0.5f; v[18*i +  5] = y1; indices[6*i +  1] = 6*i +  1;
			v[18*i +  6] = x2; v[18*i +  7] = -0.5f; v[18*i +  8] = y2; indices[6*i +  2] = 6*i +  2;
			n[18*i     ] = 0; n[18*i +  1] = 0.8944271f; n[18*i +  2] = 0;
			n[18*i +  3] = 0; n[18*i +  4] = 0.8944271f; n[18*i +  5] = 0;
			n[18*i +  6] = 0; n[18*i +  7] = 0.8944271f; n[18*i +  8] = 0;
			tc[12*i     ] = 0.25f; tc[12*i +  1] = 0.25f;
			tc[12*i +  2] = 0.25f + 0.25f*nx1; tc[12*i +  3] = 0.25f - 0.25f*ny1;
			tc[12*i +  4] = 0.25f + 0.25f*nx2; tc[12*i +  5] = 0.25f - 0.25f*ny2;

			v[18*i +  9] = 0; v[18*i + 10] = -0.5f; v[18*i + 11] = 0; indices[6*i +  3] = 6*i +  3;
			v[18*i + 12] = x1; v[18*i + 13] = -0.5f; v[18*i + 14] = y1; indices[6*i +  4] = 6*i +  5;
			v[18*i + 15] = x2; v[18*i + 16] = -0.5f; v[18*i + 17] = y2; indices[6*i +  5] = 6*i +  4;
			n[18*i +  9] = 0; n[18*i + 10] = -1; n[18*i + 11] = 0;
			n[18*i + 12] = 0; n[18*i + 13] = -1; n[18*i + 14] = 0;
			n[18*i + 15] = 0; n[18*i + 16] = -1; n[18*i + 17] = 0;
			tc[12*i +  6] = 0.75f; tc[12*i +  7] = 0.25f;
			tc[12*i +  8] = 0.75f + 0.25f*nx1; tc[12*i +  9] = 0.25f - 0.25f*ny1;
			tc[12*i + 10] = 0.75f + 0.25f*nx2; tc[12*i + 11] = 0.25f - 0.25f*ny2;
			
			x1 = x2;
			y1 = y2;
			nx1 = nx2;
			ny1 = ny2;
			
			for(int j = 0; j < 6; j++)
			{
				c[18*i + 3*j] =  1; c[18*i + 3*j + 1] =  1; c[18*i + 3*j + 2] =  1;
			}
			
		}
		
		// Construct a data structure that stores the vertices, their
		// attributes, and the triangle mesh connectivity
		VertexData vertexData = Main.renderContext.makeVertexData(vertexCount);
		vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(n, VertexData.Semantic.NORMAL, 3);
		vertexData.addElement(tc, VertexData.Semantic.TEXCOORD, 2);
		
		vertexData.addIndices(indices);
		
		Shape shape = new Shape(vertexData);
		shape.calculateBoundingSphere();
		return shape;
	}
	
	public static Shape getInstance()
	{
		if(instance == null)
		{
			instance = generate(30);
		}
		return instance;
	}
}
