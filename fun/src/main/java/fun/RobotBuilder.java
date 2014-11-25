package fun;

import javax.vecmath.Vector3f;

import jrtr.Light;
import jrtr.gsm.Group;
import jrtr.gsm.LightNode;
import jrtr.gsm.Node;
import jrtr.gsm.ShapeMaterialNode;
import jrtr.gsm.TransformGroup;

public final class RobotBuilder
{
	public static Node makeRobot()
	{
		Group roboroot = new Group();
		
		// torso
		roboroot.add(new TransformGroup().scale(0.5f, 0.7f, 0.3f)
				   .translate(new Vector3f(0,1.35f,0))
				   .add(new ShapeMaterialNode(CubeRS.getInstance(), WoodMat.getInstance())));
		
		Light light = new Light();
		light.diffuse = new Vector3f(0.5f, 0.5f, 0.5f);
		light.ambient = new Vector3f(0.01f, 0.01f, 0.01f);
		light.type = Light.Type.POINT;
		roboroot.add(new TransformGroup().translate(new Vector3f(0,0.1f,0))
										 .add(new LightNode(light)));
		
		return roboroot;
	}
}
