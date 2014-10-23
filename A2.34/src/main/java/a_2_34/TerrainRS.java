package a_2_34;

import jrtr.Shape;
import jrtr.VertexData;

public class TerrainRS extends AbstractRenderShape
{
	private float randomness1;
	private float randomness2;
	private float randomness3;
	private float height;
	private int size;
	
	public TerrainRS(RenderShape parent, float width, float depth, float height, int n, float randomness1, float randomness2, float randomness3)
	{
		super(parent);
		Math.random();
		
		size = (int) (Math.pow(2, n) + 1);
		this.randomness1 = randomness1;
		this.randomness2 = randomness2;
		this.randomness3 = randomness3;
		this.height = height;
		
		float[] heights = new float[size*size];
		
		heights[0] = (float) (height*Math.random());
		heights[size - 1] = (float) (height*Math.random());
		heights[size*(size - 1)] = (float) (height*Math.random());
		heights[size*size - 1] = (float) (height*Math.random());
		
		makeTerrain(heights, n);
		
		float[] ve = makeVertices(heights, width, depth);
		
		float[] no = makeNormals(heights, width/((float)size), depth/((float)size));
		
		float[] co = makeColor(heights);
		
		int[] indices = new int[(size - 1)*(size - 1)*6];
		
		for(int i = 0; i < size - 1; i++)
		{
			for(int j = 0; j < size - 1; j++)
			{
				indices[6*(i*(size - 1) + j)    ] = i*size + j;
				indices[6*(i*(size - 1) + j) + 1] = (i + 1)*size + j;
				indices[6*(i*(size - 1) + j) + 2] = i*size + j + 1;
				indices[6*(i*(size - 1) + j) + 3] = i*size + j + 1;
				indices[6*(i*(size - 1) + j) + 4] = (i + 1)*size + j;
				indices[6*(i*(size - 1) + j) + 5] = (i + 1)*size + j + 1;
			}
		}
		
		// Construct a data structure that stores the vertices, their
		// attributes, and the triangle mesh connectivity
		VertexData vertexData = Main.renderContext.makeVertexData(size*size);
		vertexData.addElement(co, VertexData.Semantic.COLOR, 3);
		vertexData.addElement(ve, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(no, VertexData.Semantic.NORMAL, 3);
		//vertexData.addElement(mesh.getTexcords(), VertexData.Semantic.TEXCOORD, 2);
		
		vertexData.addIndices(indices);
		
		shape = new Shape(vertexData);
	}
	
	private void makeTerrain(float[] heights, int n)
	{
		for(int i = 1; i <= n; i++)
		{
			makeTerrainSquare(heights, 0, size - 1, 0, size - 1, i, 1);
			makeTerrainDiamond(heights, 0, size - 1, 0, size - 1, i, 1);
		}
	}
	
	private void makeTerrainSquare(float[] heights, int xmin, int xmax, int ymin, int ymax, int n, int d)
	{
		int x = (xmin + xmax)/2;
		int y = (ymin + ymax)/2;
		
		if(d < n)
		{
			d++;
			makeTerrainSquare(heights, xmin, x, ymin, y, n, d);
			makeTerrainSquare(heights, x, xmax, ymin, y, n, d);
			makeTerrainSquare(heights, xmin, x, y, ymax, n, d);
			makeTerrainSquare(heights, x, xmax, y, ymax, n, d);
		}else
		{
			heights[y*size + x] = (float) ((heights[ymin*size + xmin] + heights[ymin*size + xmax] + 
					   heights[ymax*size + xmin] + heights[ymax*size + xmax])/4.0f +
					   (2*Math.random() - 1)*height*Math.pow(randomness3,n)*Math.pow(n, randomness2)*randomness1);
		}
	}
	
	private void makeTerrainDiamond(float[] heights, int xmin, int xmax, int ymin, int ymax, int n, int d)
	{
		int x = (xmin + xmax)/2;
		int y = (ymin + ymax)/2;
		
		if(d < n)
		{
			d++;
			makeTerrainDiamond(heights, xmin, x, ymin, y, n, d);
			makeTerrainDiamond(heights, x, xmax, ymin, y, n, d);
			makeTerrainDiamond(heights, xmin, x, y, ymax, n, d);
			makeTerrainDiamond(heights, x, xmax, y, ymax, n, d);
		}else
		{
			// top
			heights[ymin*size + x] = heights[ymin*size + xmin] + heights[ymin*size + xmax] + heights[y*size + x];
			if(ymin > 0)
			{
				heights[ymin*size + x] = heights[ymin*size + x] + heights[(2*ymin - y)*size + x];
				heights[ymin*size + x] = heights[ymin*size + x] / 4f;
			}else
			{
				heights[ymin*size + x] = heights[ymin*size + x] / 3f;
			}
			heights[ymin*size + x] = (float) (heights[ymin*size + x] + (2*Math.random() - 1)*height*Math.pow(randomness3,n)*Math.pow(n, randomness2)*randomness1);

			// left
			heights[y*size + xmin] = heights[ymin*size + xmin] + heights[ymax*size + xmin] + heights[y*size + x];
			if(xmin > 0)
			{
				heights[y*size + xmin] = heights[y*size + xmin] + heights[y*size + (2*xmin - x)];
				heights[y*size + xmin] = heights[y*size + xmin] / 4f;
			}else
			{
				heights[y*size + xmin] = heights[y*size + xmin] / 3f;
			}
			heights[y*size + xmin] = (float) (heights[y*size + xmin] + (2*Math.random() - 1)*height*Math.pow(randomness3,n)*Math.pow(n, randomness2)*randomness1);

			// right
			heights[y*size + xmax] = heights[ymax*size + xmax] + heights[ymin*size + xmax] + heights[y*size + x];
			if(xmax < size - 1)
			{
				heights[y*size + xmax] = heights[y*size + xmax] + heights[y*size + (xmax + x - xmin)];
				heights[y*size + xmax] = heights[y*size + xmax] / 4f;
			}else
			{
				heights[y*size + xmax] = heights[y*size + xmax] / 3f;
			}
			heights[y*size + xmax] = (float) (heights[y*size + xmax] + (2*Math.random() - 1)*height*Math.pow(randomness3,n)*Math.pow(n, randomness2)*randomness1);

			// bottom
			heights[ymax*size + x] = heights[ymax*size + xmin] + heights[ymax*size + xmax] + heights[y*size + x];
			if(ymax < size - 1)
			{
				heights[ymax*size + x] = heights[ymax*size + x] + heights[(ymax + y - ymin)*size + x];
				heights[ymax*size + x] = heights[ymax*size + x] / 4f;
			}else
			{
				heights[ymax*size + x] = heights[ymax*size + x] / 3f;
			}
			heights[ymax*size + x] = (float) (heights[ymax*size + x] + (2*Math.random() - 1)*height*Math.pow(randomness3,n)*Math.pow(n, randomness2)*randomness1);
			
		}
	}
	
	private float[] makeVertices(float[] heights, float width, float depth)
	{
		float[] v = new float[size*size*3];
		float offs = -size/2.0f;
		
		for(int i = 0; i < size; i++)
		{
			for(int j = 0; j < size; j++)
			{
				v[3*(i*size + j)    ] = (offs + j)*width/size;
				v[3*(i*size + j) + 1] = heights[i*size + j];
				v[3*(i*size + j) + 2] = (offs + i)*depth/size;
			}
		}
		
		return v;
	}
	
	private float[] makeNormals(float[] heights, float deltaWidth, float deltaDepth)
	{
		float[] n = new float[size*size*3];
		float temp = 0, tempY = 0;
		
		for(int i = 0; i < size; i++)
		{
			for(int j = 0; j < size; j++)
			{
				n[3*(i*size + j)    ] = 0;
				n[3*(i*size + j) + 1] = 0;
				n[3*(i*size + j) + 2] = 0;
				
				if( j > 0 )
				{
					temp = (heights[i*size + j - 1] - heights[i*size + j])/deltaWidth;
					tempY = (float) Math.sqrt(temp*temp + 1);
					temp = temp/tempY;
					n[3*(i*size + j)    ] = n[3*(i*size + j)    ] + temp;
					n[3*(i*size + j) + 1] = n[3*(i*size + j) + 1] + tempY;
				}
				if( j < size - 1 )
				{
					temp = (heights[i*size + j + 1] - heights[i*size + j])/deltaWidth;
					tempY = (float) Math.sqrt(temp*temp + 1);
					temp = temp/tempY;
					n[3*(i*size + j)    ] = n[3*(i*size + j)    ] - temp;
					n[3*(i*size + j) + 1] = n[3*(i*size + j) + 1] + tempY;
				}
				
				if( i > 0 )
				{
					temp = (heights[(i - 1)*size + j] - heights[i*size + j])/deltaDepth;
					tempY = (float) Math.sqrt(temp*temp + 1);
					temp = temp/tempY;
					n[3*(i*size + j) + 2] = n[3*(i*size + j) + 2] + temp;
					n[3*(i*size + j) + 1] = n[3*(i*size + j) + 1] + tempY;
				}
				if( i < size - 1 )
				{
					temp = (heights[(i + 1)*size + j] - heights[i*size + j])/deltaDepth;
					tempY = (float) Math.sqrt(temp*temp + 1);
					temp = temp/tempY;
					n[3*(i*size + j) + 2] = n[3*(i*size + j) + 2] - temp;
					n[3*(i*size + j) + 1] = n[3*(i*size + j) + 1] + tempY;
				}
				
				float quot = (float) Math.sqrt(n[3*(i*size + j)    ]*n[3*(i*size + j)    ] + 
											   n[3*(i*size + j) + 1]*n[3*(i*size + j) + 1] + 
											   n[3*(i*size + j) + 2]*n[3*(i*size + j) + 2]);
				n[3*(i*size + j)    ] = quot*n[3*(i*size + j)    ];
				n[3*(i*size + j) + 1] = quot*n[3*(i*size + j) + 1];
				n[3*(i*size + j) + 2] = quot*n[3*(i*size + j) + 2];
			
			}
		}
		
		return n;
	}
	
	private float[] makeColor(float[] heights)
	{
		float[] c = new float[size*size*3];
		
		for(int i = 0; i < size*size; i++)
		{
			c[3*i    ] = color(heights[i], 0);
			c[3*i + 1] = color(heights[i], 1);
			c[3*i + 2] = color(heights[i], 2);
		}
		
		return c;
	}
	
	private float color(float h, int rgb)
	{	// (0.4 0.6 0)->(0.6 0.4 0)->(1 1 0.99)
		if(h < height*0.5)
		{
			if(h < 0)
			{
				h = 0;
			}else
			{
				h = h/height*2;
			}
			
			h = (float) Math.sin(h*Math.PI/2.0f);
			h = h*h;
			
			if(rgb == 0)
			{
				return (1 - h)*0.4f + h*0.6f;
			}else if(rgb == 1)
			{
				return (1 - h)*0.6f + h*0.4f;
			}else
			{
				return 0;
			}
		}else
		{
			if(h > height)
			{
				h = 1;
			}else
			{
				h -= height*0.5;
				h = 2*h/height;
			}

			h = (float) Math.sin(h*Math.PI/2.0f);
			h = h*h;
			
			if(rgb == 0)
			{
				return (1 - h)*0.6f + h;
			}else if(rgb == 1)
			{
				return (1 - h)*0.4f + h;
			}else
			{
				return h*0.99f;
			}
		}
	}
	
}
