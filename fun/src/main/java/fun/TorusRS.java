package fun;

import jrtr.*;

public final class TorusRS
{
	private static Shape instance = null;
	private static Shape sphereInstance = null;
	
	private static Shape generate(float minorRad, int poloidalSegCNum, int toroidalSegNum, float sliceAngle)
	{
		Shape shape = null;
		
		float majorRad = 1 - minorRad;

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
				v[3*(i*(poloidalSegCNum + 1) + j) ] = (float) ((majorRad + minorRad*Math.cos(theta))*Math.sin(phi));
				v[3*(i*(poloidalSegCNum + 1) + j) + 1] = (float) (minorRad*Math.sin(theta));
				v[3*(i*(poloidalSegCNum + 1) + j) + 2] = (float) ((majorRad + minorRad*Math.cos(theta))*Math.cos(phi));
				n[3*(i*(poloidalSegCNum + 1) + j) ] = (float) (v[3*(i*(poloidalSegCNum + 1) + j) ] - majorRad*Math.sin(phi));
				n[3*(i*(poloidalSegCNum + 1) + j) + 1] = v[3*(i*(poloidalSegCNum + 1) + j) + 1];
				n[3*(i*(poloidalSegCNum + 1) + j) + 2] = (float) (v[3*(i*(poloidalSegCNum + 1) + j) + 2] - majorRad*Math.cos(phi));
				tempLen = (float) Math.sqrt( n[3*(i*(poloidalSegCNum + 1) + j) ] * n[3*(i*(poloidalSegCNum + 1) + j) ] +
				n[3*(i*(poloidalSegCNum + 1) + j) + 1] * n[3*(i*(poloidalSegCNum + 1) + j) + 1] +
				n[3*(i*(poloidalSegCNum + 1) + j) + 2] * n[3*(i*(poloidalSegCNum + 1) + j) + 2] );
				n[3*(i*(poloidalSegCNum + 1) + j) ] = n[3*(i*(poloidalSegCNum + 1) + j) ] / tempLen;
				n[3*(i*(poloidalSegCNum + 1) + j) + 1] = n[3*(i*(poloidalSegCNum + 1) + j) + 1] / tempLen;
				n[3*(i*(poloidalSegCNum + 1) + j) + 2] = n[3*(i*(poloidalSegCNum + 1) + j) + 2] / tempLen;
				c[3*(i*(poloidalSegCNum + 1) + j) ] = 1;
				c[3*(i*(poloidalSegCNum + 1) + j) + 1] = 1;
				c[3*(i*(poloidalSegCNum + 1) + j) + 2] = 1;
				uv[2*(i*(poloidalSegCNum + 1) + j) ] = ((float)i)/toroidalSegNum;
				uv[2*(i*(poloidalSegCNum + 1) + j) + 1] = ((float)j)/poloidalSegCNum;
			}
		}
		for(int i = 0; i < toroidalSegNum; i++)
		{
			for(int j = 0; j < poloidalSegCNum; j++)
			{
				indices[6*(i*(poloidalSegCNum) + j) ] = (i*(poloidalSegCNum + 1) + j);
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
		
		shape.calculateBoundingSphere();
		return shape;
	}
	
	public static Shape getInstance()
	{
		if(instance == null)
		{
			instance = generate(0.333333f, 20, 30, 360);
		}
		return instance;
	}
	
	public static Shape getSphereInstance()
	{
		if(sphereInstance == null)
		{
			sphereInstance = generate(1, 20, 30, 360);
		}
		return sphereInstance;
	}
}
