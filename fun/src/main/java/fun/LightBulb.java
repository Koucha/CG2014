package fun;

import javax.vecmath.Vector3f;

import jrtr.Light;
import jrtr.Material;
import jrtr.Shader;
import jrtr.Shape;
import jrtr.VertexData;

public class LightBulb
{
	private Light light;
	private Shape shape;
	private Material mat;
	private Vector3f diffuseColor;

	public LightBulb(Vector3f pos, Vector3f diffuseColor, Vector3f ambientColor)
	{
		this.diffuseColor = diffuseColor;
		
		light = new Light();
		light.position = pos;
		light.diffuse = diffuseColor;
		light.type = Light.Type.POINT;
		light.ambient = ambientColor;

		// The vertex positions of the cube
		float v[] = {0+pos.x,0.1f+pos.y,0+pos.z, 0+pos.x,0+pos.y,0.1f+pos.z, 0.1f+pos.x,0+pos.y,0+pos.z, 0+pos.x,0+pos.y,-0.1f+pos.z, -0.1f+pos.x,0+pos.y,0+pos.z, 0+pos.x,-0.1f+pos.y,0+pos.z};
		
		// The vertex normals
		float n[] = {0,1,0, 0,0,1, 1,0,0, 0,0,-1, -1,0,0, 0,-1,0};
		
		// The triangles (three vertex indices for each triangle)
		int indices[] = {0,1,2, 0,2,3, 0,3,4, 0,4,1, 5,2,1, 5,3,2, 5,4,3, 5,1,4};
		
		// Construct a data structure that stores the vertices, their
		// attributes, and the triangle mesh connectivity
		VertexData vertexData = Main.renderContext.makeVertexData(6);
		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(n, VertexData.Semantic.NORMAL, 3);
		
		vertexData.addIndices(indices);
		
		shape = new Shape(vertexData);
		
		mat = new Material();
		mat.diffuse = new Vector3f(diffuseColor);
		mat.diffuse.normalize();
		Shader shader = Main.renderContext.makeShader();
	    try {
	    	shader.load("../jrtr/shaders/light.vert", "../jrtr/shaders/light.frag");
	    	mat.shader = shader;
	    } catch(Exception e) {
	    	System.out.print("Problem with shader:\n");
	    	System.out.print(e.getMessage());
	    }
		shape.setMaterial(mat);
	}
	
	public void switchOnOff()
	{
		if(light.type == Light.Type.POINT)
		{
			light.type = Light.Type.NONE;
			mat.diffuse = new Vector3f(0.5f*diffuseColor.x, 0.5f*diffuseColor.y, 0.5f*diffuseColor.z);
		}else
		{
			light.type = Light.Type.POINT;
			mat.diffuse = diffuseColor;
		}
	}
	
	public Shape getShape()
	{
		return shape;
	}
	
	public Light getLight()
	{
		return light;
	}
}
