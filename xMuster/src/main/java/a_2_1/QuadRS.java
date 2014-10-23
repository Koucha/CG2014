package a_2_1;

import jrtr.*;

public class QuadRS extends AbstractRenderShape
{
	public QuadRS(RenderShape parent, float height, float width, float depth, float c1, float c2, float c3)
	{
		super(parent);
		
		float h = height/2, w = width/2, d = depth/2;

		// The vertex positions of the cube
		float v[] = new float[3*6*4];
		v[ 0] =  w; v[ 1] =  h; v[ 2] =  d;	// front z
		v[ 3] = -w; v[ 4] =  h; v[ 5] =  d;
		v[ 6] = -w; v[ 7] = -h; v[ 8] =  d;
		v[ 9] =  w; v[10] = -h; v[11] =  d;
		
		v[12] =  w; v[13] =  h; v[14] = -d;	// right x
		v[15] =  w; v[16] =  h; v[17] =  d;
		v[18] =  w; v[19] = -h; v[20] =  d;
		v[21] =  w; v[22] = -h; v[23] = -d;
		
		v[24] = -w; v[25] =  h; v[26] = -d;	// back z
		v[27] =  w; v[28] =  h; v[29] = -d;
		v[30] =  w; v[31] = -h; v[32] = -d;
		v[33] = -w; v[34] = -h; v[35] = -d;
		
		v[36] = -w; v[37] =  h; v[38] =  d;	// left x 
		v[39] = -w; v[40] =  h; v[41] = -d;
		v[42] = -w; v[43] = -h; v[44] = -d;
		v[45] = -w; v[46] = -h; v[47] =  d;
		
		v[48] =  w; v[49] =  h; v[50] =  d;	// top y
		v[51] =  w; v[52] =  h; v[53] = -d;
		v[54] = -w; v[55] =  h; v[56] = -d;
		v[57] = -w; v[58] =  h; v[59] =  d;
		
		v[60] =  w; v[61] = -h; v[62] =  d;	// bottom y
		v[63] = -w; v[64] = -h; v[65] =  d;
		v[66] = -w; v[67] = -h; v[68] = -d;
		v[69] =  w; v[70] = -h; v[71] = -d;

		// The vertex normals
		float n[] = new float[3*6*4];
		n[ 0] =  0; n[ 1] =  0; n[ 2] =  1;	// front z
		n[ 3] =  0; n[ 4] =  0; n[ 5] =  1;
		n[ 6] =  0; n[ 7] =  0; n[ 8] =  1;
		n[ 9] =  0; n[10] =  0; n[11] =  1;
		
		n[12] =  1; n[13] =  0; n[14] =  0;	// right x
		n[15] =  1; n[16] =  0; n[17] =  0;
		n[18] =  1; n[19] =  0; n[20] =  0;
		n[21] =  1; n[22] =  0; n[23] =  0;
		
		n[24] =  0; n[25] =  0; n[26] = -1;	// back z
		n[27] =  0; n[28] =  0; n[29] = -1;
		n[30] =  0; n[31] =  0; n[32] = -1;
		n[33] =  0; n[34] =  0; n[35] = -1;
		
		n[36] = -1; n[37] =  0; n[38] =  0;	// left x 
		n[39] = -1; n[40] =  0; n[41] =  0;
		n[42] = -1; n[43] =  0; n[44] =  0;
		n[45] = -1; n[46] =  0; n[47] =  0;
		
		n[48] =  0; n[49] =  1; n[50] =  0;	// top y
		n[51] =  0; n[52] =  1; n[53] =  0;
		n[54] =  0; n[55] =  1; n[56] =  0;
		n[57] =  0; n[58] =  1; n[59] =  0;
		
		n[60] =  0; n[61] = -1; n[62] =  0;	// bottom y
		n[63] =  0; n[64] = -1; n[65] =  0;
		n[66] =  0; n[67] = -1; n[68] =  0;
		n[69] =  0; n[70] = -1; n[71] =  0;

		// The vertex colors
		float c[] = new float[3*6*4];
		for(int i = 0; i < 6*4; i++)
		{
			c[3*i] = c1; c[3*i + 1] = c2; c[3*i + 2] = c3;
		}

		// The triangles (three vertex indices for each triangle)
		int indices[] = new int[3*12];
		for(int i = 0; i < 6; i++)
		{
			indices[6*i    ] = 4*i + 0;
			indices[6*i + 1] = 4*i + 1;
			indices[6*i + 2] = 4*i + 2;
			indices[6*i + 3] = 4*i + 0;
			indices[6*i + 4] = 4*i + 2;
			indices[6*i + 5] = 4*i + 3;
		}
		
		// Construct a data structure that stores the vertices, their
		// attributes, and the triangle mesh connectivity
		VertexData vertexData = Main.renderContext.makeVertexData(6*4);
		vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(n, VertexData.Semantic.NORMAL, 3);
		//vertexData.addElement(mesh.getTexcords(), VertexData.Semantic.TEXCOORD, 2);
		
		vertexData.addIndices(indices);
		
		shape = new Shape(vertexData);
	}
}
