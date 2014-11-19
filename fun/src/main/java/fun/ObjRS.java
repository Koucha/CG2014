package fun;

import java.io.IOException;

import jrtr.ObjReader;
import jrtr.Shape;

public final class ObjRS
{
	private static Shape instanceTeaPot = null;
	
	private static Shape generate(String filename, float scale)
	{
		Shape shape = null;
		
		try {
			shape = new Shape(ObjReader.read(filename, scale, Main.renderContext));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println(e1.getLocalizedMessage());
		}
		
		return shape;
	}

	public static Shape getInstanceTeaPot()
	{
		if(instanceTeaPot == null)
		{
			instanceTeaPot = generate("../obj/teapot_texcoords.obj", 1);
		}
		return instanceTeaPot;
	}

}
