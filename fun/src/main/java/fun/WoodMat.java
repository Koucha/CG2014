package fun;

import java.io.IOException;

import javax.vecmath.Vector3f;

import jrtr.Material;
import jrtr.Shader;

public final class WoodMat
{
	private static Material matInstance = null;
	
	private static void generate()
	{
		matInstance = new Material();
		matInstance.diffuse = new Vector3f(1,1,1);
		matInstance.ambient = new Vector3f(0.1f,0.1f,0.1f);
		matInstance.specular = new Vector3f(1,1,1);
		matInstance.shininess = 10;
		matInstance.texture = Main.renderContext.makeTexture();
		try {
			matInstance.texture.load("../textures/wood.jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
		matInstance.glossTex = Main.renderContext.makeTexture();
		try {
			matInstance.glossTex.load("../textures/wood.jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Shader shader = Main.renderContext.makeShader();
	    try {
	    	shader.load("../jrtr/shaders/mod1.vert", "../jrtr/shaders/mod1.frag");
	    	matInstance.shader = shader;
	    } catch(Exception e) {
	    	System.out.print("Problem with shader:\n");
	    	System.out.print(e.getMessage());
	    }
	}
	
	public static Material getInstance()
	{
		if(matInstance == null)
		{
			generate();
		}
		return matInstance;
	}
}
