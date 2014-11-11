package fun;

import java.io.IOException;

import javax.vecmath.Vector3f;

import jrtr.*;

public class TestRS extends AbstractRenderShape
{
	public TestRS(RenderShape parent)
	{
		super(parent);
		
		float v[] = {-5,-5,0, 5,-5,0, 5,5,0, -5,5,0};
		float n[] = {0,0,1, 0,0,1, 0,0,1, 0,0,1};
		float c[] = {1,1,1, 1,1,1, 1,1,1, 1,1,1};
		float t[] = {0,0, 1,0, 1,1, 0,1};
		VertexData vertexData = Main.renderContext.makeVertexData(4);
		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(n, VertexData.Semantic.NORMAL, 3);
		vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
		vertexData.addElement(t, VertexData.Semantic.TEXCOORD, 2);
		int indices[] = {0,2,3, 0,1,2};
		vertexData.addIndices(indices);
		
		shape = new Shape(vertexData);
		
		Material mat = new Material();
		mat.diffuse = new Vector3f(0.2f,0.2f,0.2f);
		mat.texture = Main.renderContext.makeTexture();
		try {
			mat.texture.load("C:\\Users\\Florian\\Desktop\\Kriss_AGAIN_D8_by_Viddharta.jpg");
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
