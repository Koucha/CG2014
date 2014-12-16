package fun;

import jrtr.Material;
import jrtr.Shader;

public final class SimpleMat
{
	private static Material matInstance = null;
	
	private static void generate()
	{
		matInstance = new Material();
		Shader shader = Main.renderContext.makeShader();
	    try {
	    	shader.load("../jrtr/shaders/simple.vert", "../jrtr/shaders/simple.frag");
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
