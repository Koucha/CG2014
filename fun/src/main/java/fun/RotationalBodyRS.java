package fun;

import javax.vecmath.Vector3f;

import jrtr.*;

public final class RotationalBodyRS
{
	public static Shape generate(int bezSegCount, Vector3f points[], int evalPointCount, int angleStepCount)
	{
		int vertexCount = evalPointCount*angleStepCount;

		// The vertex positions of the cube
		float v[] = new float[3*vertexCount];
		
		// The vertex normals
		float n[] = new float[3*vertexCount];

		// The vertex colors
		float c[] = new float[3*vertexCount];
		
		// The vertex normals
		float tc[] = new float[2*vertexCount];

		// The triangles (three vertex indices for each triangle)
		int indices[] = new int[6*vertexCount];
		
		rotateBez(bezSegCount, points, evalPointCount, angleStepCount, v, n, tc, indices);
		
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
	
	private static void rotateBez(int bezSegCount, Vector3f points[], int evalPointCount, int angleStepCount, float v[], float n[], float tc[], int indices[])
	{
		Vector3f bezier;
		Vector3f bezNor;
		
		float t, angle;
		Vector3f a;
		Vector3f b;
		int co;
		for(int i = 0; i < evalPointCount; i++)
		{
			t = ((float)i)/(evalPointCount - 1)*bezSegCount;
			if(t < bezSegCount)
			{
				a = lerp(lerp(points[3*((int)t)], points[3*((int)t) + 1], t), lerp(points[3*((int)t) + 1], points[3*((int)t) + 2], t), t);
				b = lerp(lerp(points[3*((int)t) + 1], points[3*((int)t) + 2], t), lerp(points[3*((int)t) + 2], points[3*((int)t) + 3], t), t);
				bezier = lerp(a, b, t);
				bezNor = new Vector3f(b.y - a.y, a.x - b.x,0);
			}else
			{
				bezier = points[3*bezSegCount];
				bezNor = new Vector3f(points[3*bezSegCount - 1].x - points[3*bezSegCount].x, points[3*bezSegCount].y - points[3*bezSegCount - 1].y,0);
			}

//			bezier = new Vector3f(1, t,0);
//			bezNor = new Vector3f(0, 1,0);
			bezNor.normalize();
			
			for(int j = 0; j < angleStepCount; j++)
			{
				angle = (float) (((float)j)/(angleStepCount - 1)*2*Math.PI);
				co = i * angleStepCount + j;

				v[3*co    ] = (float) (bezier.x * Math.cos(angle));
				v[3*co + 1] = bezier.y;
				v[3*co + 2] = (float) (bezier.x * Math.sin(-angle));

				n[3*co    ] = (float) (bezNor.x * Math.cos(angle));
				n[3*co + 1] = bezNor.y;
				n[3*co + 2] = (float) (bezNor.x * Math.sin(-angle));

				tc[2*co    ] = ((float)i)/(evalPointCount - 1);
				tc[2*co + 1] = ((float)j)/(angleStepCount - 1);

				if(i < evalPointCount - 1 && j < angleStepCount - 1)
				{
					indices[6*co + 0] = co;
					indices[6*co + 1] = co + 1;
					indices[6*co + 2] = co + angleStepCount;
					indices[6*co + 3] = co + 1;
					indices[6*co + 4] = co + angleStepCount + 1;
					indices[6*co + 5] = co + angleStepCount;
				}
			}
		}
	}
	
	private static Vector3f lerp(Vector3f a, Vector3f b, float t)
	{
		t = t%1;
		
		Vector3f v = new Vector3f();

		v.x = (1 - t)*a.x + t*b.x;
		v.y = (1 - t)*a.y + t*b.y;
		v.z = (1 - t)*a.z + t*b.z;
		
		return v;
	}
}
