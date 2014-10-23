package a_2_2;

import java.io.IOException;

import jrtr.ObjReader;
import jrtr.Shape;

public class TeaPotRS extends AbstractRenderShape
{

	public TeaPotRS(RenderShape parent)
	{
		super(parent);

		try {
			shape = new Shape(ObjReader.read("../obj/teapot.obj", 10, Main.renderContext));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}