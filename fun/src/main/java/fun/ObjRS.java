package fun;

import java.io.IOException;

import jrtr.Material;
import jrtr.ObjReader;
import jrtr.SWTexture;
import jrtr.Shape;

public class ObjRS extends AbstractRenderShape {

	public ObjRS(RenderShape parent, String filename, float scale)
	{
		super(parent);

		try {
			shape = new Shape(ObjReader.read(filename, scale, Main.renderContext));
			
			Material mat = new Material();
			mat.texture = new SWTexture();
			try {
				mat.texture.load("C:\\Users\\Florian\\Desktop\\Kriss_AGAIN_D8_by_Viddharta.jpg");
				shape.setMaterial(mat);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
