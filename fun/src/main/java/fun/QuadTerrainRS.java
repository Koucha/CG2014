package fun;

import jrtr.Shape;
import jrtr.VertexData;

public class QuadTerrainRS extends AbstractRenderShape
{
	private float randomness1;
	private float randomness2;
	private float randomness3;
	private float height;
	private int size;
	private boolean isobaren;
	float[] heights;
	
	public QuadTerrainRS(RenderShape parent, float width, float depth, float height, int n, float randomness1, float randomness2, float randomness3, boolean isobaren, float[] hiL, float[] hiD, float[] hiR, float[] hiU)
	{
		super(parent);
		Math.random();
		
		size = (int) (Math.pow(2, n) + 1);
		this.randomness1 = randomness1;
		this.randomness2 = randomness2;
		this.randomness3 = randomness3;
		this.height = height;
		this.isobaren = isobaren;
		
		heights = new float[size*size];
		
		heights[0] = (float) (height*Math.random());
		heights[size - 1] = (float) (height*Math.random());
		heights[size*(size - 1)] = (float) (height*Math.random());
		heights[size*size - 1] = (float) (height*Math.random());
		
		for(int i = 0; i < size; i++)
		{
			if(hiL != null)
				heights[size*i] = hiL[size*i + size - 1];
			if(hiD != null)
				heights[size*(size - 1) + i] = hiD[i];
			if(hiR != null)
				heights[size*i + size - 1] = hiR[size*i];
			if(hiU != null)
				heights[i] = hiU[size*(size - 1) + i];
		}
		
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
	
	public void switchIsobaren()
	{
		isobaren = !isobaren;
	}
	
	public float[] getHeights() {
		return heights;
	}

	private void makeTerrain(float[] heights, int n)
	{
		makeTerrainSquare(heights, 0,0, 0,size - 1, size - 1,size - 1, size - 1,0, n, 1);
	}
	
	private void makeTerrainSquare(float[] heights, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, int n, int d)
	{
		if(2*n <= d)
		{	// innermost square (center point)
			d--;
			int x = (x1 + x2 + x3 + x4)/4;
			int y = (y1 + y2 + y3 + y4)/4;
			if(heights[y*size + x] == 0.0f)
			{
				heights[y*size + x] = (float) ((heights[y1*size + x1] + heights[y2*size + x2] + 
												heights[y3*size + x3] + heights[y4*size + x4])/4.0f +
												random()*height*Math.pow(randomness3,d)*Math.pow(d, randomness2)*randomness1);
			}
			return;
		}
		
		// half sides
		int xplus = (x2 - x3)/2;
		int yplus = (y2 - y3)/2;
		float pow = 0.1f/n/n/n;
		
		int xl = (x1 + x2)/2;
		int yl = (y1 + y2)/2;
		if(heights[yl*size + xl] == 0.0f)
		{
			heights[yl*size + xl] = heights[y1*size + x1] + heights[y2*size + x2];
			if(yl + yplus >= 0 && yl + yplus < size && xl + xplus >= 0 && xl + xplus < size)
				heights[yl*size + xl] = (heights[yl*size + xl] + pow*heights[(yl + yplus)*size + (xl + xplus)])/(2 + pow);
			else
				heights[yl*size + xl] = heights[yl*size + xl]/2.0f;
			heights[yl*size + xl] = (float) (heights[yl*size + xl] + random()*height*Math.pow(randomness3,d)*Math.pow(d, randomness2)*randomness1);
		}
		
		xplus = (x2 - x1)/2;
		yplus = (y2 - y1)/2;
		
		int xd = (x3 + x2)/2;
		int yd = (y3 + y2)/2;
		if(heights[yd*size + xd] == 0.0f)
		{
			heights[yd*size + xd] = heights[y3*size + x3] + heights[y2*size + x2];
			if(yd + yplus >= 0 && yd + yplus < size && xd + xplus >= 0 && xd + xplus < size)
				heights[yd*size + xd] = (heights[yd*size + xd] + pow*heights[(yd + yplus)*size + (xd + xplus)])/(2 + pow);
			else
				heights[yd*size + xd] = heights[yd*size + xd]/2.0f;
			heights[yd*size + xd] = (float) (heights[yd*size + xd] + random()*height*Math.pow(randomness3,d)*Math.pow(d, randomness2)*randomness1);
		}
		
		xplus = (x4 - x1)/2;
		yplus = (y4 - y1)/2;

		int xr = (x3 + x4)/2;
		int yr = (y3 + y4)/2;
		if(heights[yr*size + xr] == 0.0f)
		{
			heights[yr*size + xr] = heights[y3*size + x3] + heights[y4*size + x4];
			if(yr + yplus >= 0 && yr + yplus < size && xr + xplus >= 0 && xr + xplus < size)
				heights[yr*size + xr] = (heights[yr*size + xr] + pow*heights[(yr + yplus)*size + (xr + xplus)])/(2 + pow);
			else
				heights[yr*size + xr] = heights[yr*size + xr]/2.0f;
			heights[yr*size + xr] = (float) (heights[yr*size + xr] + random()*height*Math.pow(randomness3,d)*Math.pow(d, randomness2)*randomness1);
		}
		
		xplus = (x4 - x3)/2;
		yplus = (y4 - y3)/2;

		int xu = (x1 + x4)/2;
		int yu = (y1 + y4)/2;
		if(heights[yu*size + xu] == 0.0f)
		{
			heights[yu*size + xu] = heights[y1*size + x1] + heights[y4*size + x4];
			if(yu + yplus >= 0 && yu + yplus < size && xu + xplus >= 0 && xu + xplus < size)
				heights[yu*size + xu] = (heights[yu*size + xu] + pow*heights[(yu + yplus)*size + (xu + xplus)])/(2 + pow);
			else
				heights[yu*size + xu] = heights[yu*size + xu]/2.0f;
			heights[yu*size + xu] = (float) (heights[yu*size + xu] + random()*height*Math.pow(randomness3,d)*Math.pow(d, randomness2)*randomness1);
		}
		
		// inner square
		makeTerrainSquare(heights, xl, yl, xd, yd, xr, yr, xu, yu, n, d + 1);
		
		// triangles
		if(2*n > d)
		{
			makeTerrainTriangle(heights, x1, y1, xl, yl, xu, yu, n, d + 2);
			makeTerrainTriangle(heights, x2, y2, xd, yd, xl, yl, n, d + 2);
			makeTerrainTriangle(heights, x3, y3, xr, yr, xd, yd, n, d + 2);
			makeTerrainTriangle(heights, x4, y4, xu, yu, xr, yr, n, d + 2);
		}
		
	}
	
	private void makeTerrainTriangle(float[] heights, int x1, int y1, int x2, int y2, int x3, int y3, int n, int d)
	{
		int xu = (x1 + x3)/2;
		int yu = (y1 + y3)/2;
		if(heights[yu*size + xu] == 0.0f)
		{
			heights[yu*size + xu] = (float) ((heights[y1*size + x1] + heights[y3*size + x3])/2.0f +
					random()*height*Math.pow(randomness3,d)*Math.pow(d, randomness2)*randomness1);
		}
		
		int xl = (x1 + x2)/2;
		int yl = (y1 + y2)/2;
		if(heights[yl*size + xl] == 0.0f)
		{
			heights[yl*size + xl] = (float) ((heights[y1*size + x1] + heights[y2*size + x2])/2.0f +
				random()*height*Math.pow(randomness3,d)*Math.pow(d, randomness2)*randomness1);
		}
		
		int xm = (x3 + x2)/2;
		int ym = (y3 + y2)/2;
		
		if(2*n > d + 1)
		{
			makeTerrainSquare(heights, x1, y1, xl, yl, xm, ym, xu, yu, n, d + 2);
		}
		
		if(2*n > d + 2)
		{
			makeTerrainTriangle(heights, xu, yu, xm, ym, x3, y3, n, d + 2);
			makeTerrainTriangle(heights, xl, yl, x2, y2, xm, ym, n, d + 2);
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
	
	private float random()
	{
		float ran = (float) Math.random();
		return 1.16f*ran*ran + 0.38f*ran -0.54f;
	}
	
	private float color(float h, int rgb)
	{	// (0.4 0.6 0)->(0.6 0.4 0)->(1 1 0.99)
		final float br1 = 0.21f, br2 = 0.23f, br3 = 0.6f;
		
		float iso = 1;
		
		h = h/height;
		
		if( isobaren &&
		   (0.0 < h && h < 0.003 ||
		    0.1 < h && h < 0.103 ||
		    0.2 < h && h < 0.203 ||
		    0.3 < h && h < 0.303 ||
		    0.4 < h && h < 0.403 ||
		    0.5 < h && h < 0.503 ||
		    0.6 < h && h < 0.603 ||
		    0.7 < h && h < 0.703 ||
		    0.8 < h && h < 0.803 ||
		    0.9 < h && h < 0.903 ||
		    1.0 < h && h < 1.003 ))
		{
			iso = 0.75f;
		}
		
		if(h < br1)
		{
			if(rgb == 0)
			{
				return iso*(0.2f);
			}else if(rgb == 1)
			{
				return iso*(0.4f);
			}else
			{
				return iso*(0.6f);
			}
		}else if(h < br2)
		{
			h = (h - br1)/(br2 - br1);
			h = (float) Math.sin(h*Math.PI/2.0f);
			h = h*h;
			
			if(rgb == 0)
			{
				return iso*((1 - h)*0.2f + h*0.4f);
			}else if(rgb == 1)
			{
				return iso*((1 - h)*0.4f + h*0.6f);
			}else
			{
				return iso*((1 - h)*0.6f);
			}
		}else if(h < br3)
		{
			h = (h - br2)/(br3 - br2);
			
			h = (float) Math.sin(h*Math.PI/2.0f);
			h = h*h;
			
			if(rgb == 0)
			{
				return iso*((1 - h)*0.4f + h*0.6f);
			}else if(rgb == 1)
			{
				return iso*((1 - h)*0.6f + h*0.4f);
			}else
			{
				return 0;
			}
		}else
		{
			if(h > 1)
			{
				h = 1;
			}else
			{
				h = (h - br3)/(1 - br3);
			}

			h = (float) Math.sin(h*Math.PI/2.0f);
			h = h*h;
			
			if(rgb == 0)
			{
				return iso*((1 - h)*0.6f + h);
			}else if(rgb == 1)
			{
				return iso*((1 - h)*0.4f + h);
			}else
			{
				return iso*(h*0.99f);
			}
		}
	}
	
}
