package fun;

import java.io.IOException;

import javax.vecmath.Vector3f;

import jrtr.Material;
import jrtr.ObjReader;
import jrtr.Shader;
import jrtr.Shape;

public class ObjRS
{
	public static Shape generate(String filename, float scale, float shininess)
	{
		Shape shape = null;
		
		try {
			shape = new Shape(ObjReader.read(filename, scale, Main.renderContext));
			
			Material mat = new Material();
			mat.diffuse = new Vector3f(1,1,1);
			mat.ambient = new Vector3f(0.1f,0.1f,0.1f);
			mat.specular = new Vector3f(1,1,1);
			mat.shininess = shininess;
			mat.texture = Main.renderContext.makeTexture();
			try {
				mat.texture.load("../textures/wood.jpg");
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
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println(e1.getLocalizedMessage());
		}
		
		return shape;
	}

}
