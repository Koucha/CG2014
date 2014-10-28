package fun;

import java.io.IOException;

import jrtr.*;

public class TestRS extends AbstractRenderShape
{
	public TestRS(RenderShape parent)
	{
		super(parent);
		
		float v[] = {-1,-1,1, 1,-1,1, 1,1,1, -1,1,1};
		float c[] = {1,0.5f,1, 0.5f,1,1, 1,1,0.5f, 0.5f,0.5f,0.5f};
		float t[] = {0,0, 1,0, 1,1, 0,1};
		VertexData vertexData = Main.renderContext.makeVertexData(4);
		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
		vertexData.addElement(t, VertexData.Semantic.TEXCOORD, 2);
		int indices[] = {0,2,3, 0,1,2};
		vertexData.addIndices(indices);
		
		shape = new Shape(vertexData);
		
		Material mat = new Material();
		mat.texture = new SWTexture();
		try {
			mat.texture.load("C:\\Users\\Florian\\Desktop\\Kriss_AGAIN_D8_by_Viddharta.jpg");
			shape.setMaterial(mat);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
