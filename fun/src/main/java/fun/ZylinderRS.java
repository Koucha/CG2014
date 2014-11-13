package fun;

import java.io.IOException;

import javax.vecmath.Vector3f;

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
			
			v[36*i     ] =  0; v[36*i +  1] =  height; v[36*i +  2] =  0; indices[12*i     ] = 12*i +  0;
			v[36*i +  3] = x1; v[36*i +  4] =  height; v[36*i +  5] = y1; indices[12*i +  1] = 12*i +  1;
			v[36*i +  6] = x2; v[36*i +  7] =  height; v[36*i +  8] = y2; indices[12*i +  2] = 12*i +  2;
			n[36*i     ] = 0; n[36*i +  1] =  1; n[36*i +  2] = 0;
			n[36*i +  3] = 0; n[36*i +  4] =  1; n[36*i +  5] = 0;
			n[36*i +  6] = 0; n[36*i +  7] =  1; n[36*i +  8] = 0;
			tc[24*i     ] = 0.85f; tc[24*i +  1] = 0.15f;
			tc[24*i +  2] = 0.85f + 0.15f*nx1; tc[24*i +  3] = 0.15f - 0.15f*ny1;
			tc[24*i +  4] = 0.85f + 0.15f*nx2; tc[24*i +  5] = 0.15f - 0.15f*ny2;

			v[36*i +  9] = x2; v[36*i + 10] =  height; v[36*i + 11] = y2; indices[12*i +  3] = 12*i +  3;
			v[36*i + 12] = x1; v[36*i + 13] =  height; v[36*i + 14] = y1; indices[12*i +  4] = 12*i +  4;
			v[36*i + 15] = x1; v[36*i + 16] = -height; v[36*i + 17] = y1; indices[12*i +  5] = 12*i +  5;
			n[36*i +  9] = nx2; n[36*i + 10] = 0; n[36*i + 11] = ny2;
			n[36*i + 12] = nx1; n[36*i + 13] = 0; n[36*i + 14] = ny1;
			n[36*i + 15] = nx1; n[36*i + 16] = 0; n[36*i + 17] = ny1;
			tc[24*i +  6] = ((float)i+1)/numSides; tc[24*i +  7] = 1;
			tc[24*i +  8] = ((float)i)/numSides; tc[24*i +  9] = 1;
			tc[24*i + 10] = ((float)i)/numSides; tc[24*i + 11] = 0.31f;

			v[36*i + 18] = x2; v[36*i + 19] =  height; v[36*i + 20] = y2; indices[12*i +  6] = 12*i +  6;
			v[36*i + 21] = x1; v[36*i + 22] = -height; v[36*i + 23] = y1; indices[12*i +  7] = 12*i +  7;
			v[36*i + 24] = x2; v[36*i + 25] = -height; v[36*i + 26] = y2; indices[12*i +  8] = 12*i +  8;
			n[36*i + 18] = nx2; n[36*i + 19] = 0; n[36*i + 20] = ny2;
			n[36*i + 21] = nx1; n[36*i + 22] = 0; n[36*i + 23] = ny1;
			n[36*i + 24] = nx2; n[36*i + 25] = 0; n[36*i + 26] = ny2;
			tc[24*i + 12] = ((float)i+1)/numSides; tc[24*i + 13] = 1;
			tc[24*i + 14] = ((float)i)/numSides; tc[24*i + 15] = 0.31f;
			tc[24*i + 16] = ((float)i+1)/numSides; tc[24*i + 17] = 0.31f;
			
			v[36*i + 27] = x2; v[36*i + 28] = -height; v[36*i + 29] = y2; indices[12*i +  9] = 12*i +  9;
			v[36*i + 30] = x1; v[36*i + 31] = -height; v[36*i + 32] = y1; indices[12*i + 10] = 12*i + 10;
			v[36*i + 33] =  0; v[36*i + 34] = -height; v[36*i + 35] =  0; indices[12*i + 11] = 12*i + 11;
			n[36*i + 27] = 0; n[36*i + 28] = -1; n[36*i + 29] = 0;
			n[36*i + 30] = 0; n[36*i + 31] = -1; n[36*i + 32] = 0;
			n[36*i + 33] = 0; n[36*i + 34] = -1; n[36*i + 35] = 0;
			tc[24*i + 18] = 0.15f + 0.15f*nx2; tc[24*i + 19] = 0.15f - 0.15f*ny2;
			tc[24*i + 20] = 0.15f + 0.15f*nx1; tc[24*i + 21] = 0.15f - 0.15f*ny1;
			tc[24*i + 22] = 0.15f; tc[24*i + 23] = 0.15f;
			
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
		vertexData.addElement(tc, VertexData.Semantic.TEXCOORD, 2);
		
		vertexData.addIndices(indices);
		
		shape = new Shape(vertexData);
		
		Material mat = new Material();
		mat.diffuse = new Vector3f(1,1,1);
		mat.ambient = new Vector3f(0.1f,0.1f,0.1f);
		mat.specular = new Vector3f(1,1,1);
		mat.shininess = 5;
		mat.texture = Main.renderContext.makeTexture();
		try {
			mat.texture.load("../textures/textur1.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		mat.glossTex = Main.renderContext.makeTexture();
		try {
			mat.glossTex.load("../textures/square.jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Shader shader = Main.renderContext.makeShader();
	    try {
	    	shader.load("../jrtr/shaders/mod1.vert", "../jrtr/shaders/mod1.frag");
	    	mat.shader = shader;
	    } catch(Exception e) {
	    	System.out.print("Problem with shader:\n");
	    	System.out.print(e.getMessage());
	    }
		shape.setMaterial(mat);
	}
}
