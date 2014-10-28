package fun;

import java.io.IOException;

import jrtr.*;

public class TorusRS extends AbstractRenderShape
{
	public TorusRS(RenderShape parent, int toroidalSegNum, int poloidalSegCNum, float majorRad, float minorRad, float sliceAngle, float c1, float c2, float c3)
	{
		super(parent);
		
		assert(poloidalSegCNum > 2);
		assert(toroidalSegNum > 2);
		assert(sliceAngle > 0);
		assert(sliceAngle < 361);	//ungenau, aber float ist meist nicht genau (problem bei <= 360)
		
		sliceAngle = sliceAngle/360f;
		
		int vertexCount = ((poloidalSegCNum + 1)*(toroidalSegNum + 1));
		
		// The vertex positions of the cube
		float v[] = new float[3*vertexCount];
		
		// The vertex normals
		float n[] = new float[3*vertexCount];

		// The vertex colors
		float c[] = new float[3*vertexCount];
		
		// The vertex colors
		float uv[] = new float[2*vertexCount];

		// The triangles (three vertex indices for each triangle)
		int indices[] = new int[6*vertexCount];
		
		float phi = 0, theta = 0, tempLen = 0;
		for(int i = 0; i < toroidalSegNum + 1; i++)
		{
			phi = (float) (Math.PI + 2*Math.PI*i/(toroidalSegNum)*sliceAngle);
			
			for(int j = 0; j < poloidalSegCNum + 1; j++)
			{
				theta = (float) (Math.PI + 2*Math.PI*j/(poloidalSegCNum));
				v[3*(i*(poloidalSegCNum + 1) + j)    ] = (float) ((majorRad + minorRad*Math.cos(theta))*Math.sin(phi));
				v[3*(i*(poloidalSegCNum + 1) + j) + 1] = (float) (minorRad*Math.sin(theta));
				v[3*(i*(poloidalSegCNum + 1) + j) + 2] = (float) ((majorRad + minorRad*Math.cos(theta))*Math.cos(phi));
				
				n[3*(i*(poloidalSegCNum + 1) + j)    ] = (float) (v[3*(i*(poloidalSegCNum + 1) + j)    ] - majorRad*Math.sin(phi));
				n[3*(i*(poloidalSegCNum + 1) + j) + 1] = v[3*(i*(poloidalSegCNum + 1) + j) + 1];
				n[3*(i*(poloidalSegCNum + 1) + j) + 2] = (float) (v[3*(i*(poloidalSegCNum + 1) + j) + 2] - majorRad*Math.cos(phi));
				
				tempLen = (float) Math.sqrt( n[3*(i*(poloidalSegCNum + 1) + j)    ] * n[3*(i*(poloidalSegCNum + 1) + j)    ] +
						                     n[3*(i*(poloidalSegCNum + 1) + j) + 1] * n[3*(i*(poloidalSegCNum + 1) + j) + 1] + 
						                     n[3*(i*(poloidalSegCNum + 1) + j) + 2] * n[3*(i*(poloidalSegCNum + 1) + j) + 2] );
				n[3*(i*(poloidalSegCNum + 1) + j)    ] = n[3*(i*(poloidalSegCNum + 1) + j)    ] / tempLen;
				n[3*(i*(poloidalSegCNum + 1) + j) + 1] = n[3*(i*(poloidalSegCNum + 1) + j) + 1] / tempLen;
				n[3*(i*(poloidalSegCNum + 1) + j) + 2] = n[3*(i*(poloidalSegCNum + 1) + j) + 2] / tempLen;
				
				c[3*(i*(poloidalSegCNum + 1) + j)    ] = (i%4 < 2)?(0.2f):(1);//c1;
				c[3*(i*(poloidalSegCNum + 1) + j) + 1] = (i%4 < 2)?(0.2f):(1);//c2;
				c[3*(i*(poloidalSegCNum + 1) + j) + 2] = (i%4 < 2)?(0.2f):(1);//c3;
				
				uv[2*(i*(poloidalSegCNum + 1) + j)    ] = ((float)i)/toroidalSegNum;
				uv[2*(i*(poloidalSegCNum + 1) + j) + 1] = ((float)j)/poloidalSegCNum;
			}
		}
		
		for(int i = 0; i < toroidalSegNum; i++)
		{
			for(int j = 0; j < poloidalSegCNum; j++)
			{
				indices[6*(i*(poloidalSegCNum) + j)    ] = (i*(poloidalSegCNum + 1) + j);
				indices[6*(i*(poloidalSegCNum) + j) + 1] = ((i + 1)*(poloidalSegCNum + 1) + j);
				indices[6*(i*(poloidalSegCNum) + j) + 2] = (i*(poloidalSegCNum + 1) + j) + 1;
				indices[6*(i*(poloidalSegCNum) + j) + 3] = (i*(poloidalSegCNum + 1) + j) + 1;
				indices[6*(i*(poloidalSegCNum) + j) + 4] = ((i + 1)*(poloidalSegCNum + 1) + j);
				indices[6*(i*(poloidalSegCNum) + j) + 5] = ((i + 1)*(poloidalSegCNum + 1) + j) + 1;
			}
		}
		
		// Construct a data structure that stores the vertices, their
		// attributes, and the triangle mesh connectivity
		VertexData vertexData = Main.renderContext.makeVertexData(vertexCount);
		vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(n, VertexData.Semantic.NORMAL, 3);
		vertexData.addElement(uv, VertexData.Semantic.TEXCOORD, 2);
		
		vertexData.addIndices(indices);
		
		shape = new Shape(vertexData);
		
		Material mat = new Material();
		mat.texture = new SWTexture();
		try {
			mat.texture.load("../textures/square.jpg");
			shape.setMaterial(mat);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
