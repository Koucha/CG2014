package fun;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import jrtr.Light;
import jrtr.gsm.AnimationGroup;
import jrtr.gsm.AnimationInfo;
import jrtr.gsm.Animator;
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
		Light light = new Light();
		light.diffuse = new Vector3f(0.0005f, 0.0005f, 0.004f);
		light.ambient = new Vector3f(0.f, 0.f, 0.f);
		light.type = Light.Type.POINT;
		roboroot.add(new TransformGroup().scale(0.5f, 0.7f, 0.3f)
				   .translate(new Vector3f(0,1.35f,0))
				   .add(new ShapeMaterialNode(CubeRS.getInstance(), WoodMat.getInstance())));
		roboroot.add(new TransformGroup().translate(new Vector3f(0,1.5f,0.16f)).add(new LightNode(light)));
		
		roboroot.add(new TransformGroup().translate(new Vector3f(0.25f,1.6f,0))
										 .add(makeArm(new Animator(){ @Override public void doAnimation(AnimationInfo aniInf)
													{
														Matrix4f trafo = new Matrix4f();
														trafo.rotX((float) (0.6*Math.sin(aniInf.getTime())));
														
														group.setTFMat(trafo);
													} }, new Animator(){ @Override public void doAnimation(AnimationInfo aniInf)
													{
														Matrix4f trafo = new Matrix4f();
														trafo.rotX((float) (0.6*Math.sin(aniInf.getTime() - 0.5) + 0.3));
														
														group.setTFMat(trafo);
													} }, 0.05f)));
				
		
		return roboroot;
	}

	private static Node makeArm(Animator shoulder, Animator elbow, float offset)
	{
		AnimationGroup arm = new AnimationGroup(shoulder);
		//bolt
		arm.add(new TransformGroup().scale(0.05f)
									.rotate(new Vector3f(0,0,1), (float) (-Math.PI/2))
									.translate(new Vector3f(offset/2f + ((offset > 0)?(0.025f):(-0.025f)),0,0))
									.add(new ShapeMaterialNode(ZylinderRS.getInstance(), WoodMat.getInstance())));
		
		//upper arm
		arm.add(new TransformGroup().scale(0.2f, 0.7f, 0.2f)
									.translate(new Vector3f(offset + ((offset > 0)?(0.01f):(-0.01f)),-0.25f,0))
									);
		
		AnimationGroup lowerarm = new AnimationGroup(elbow);
		
		//TODO
		
		arm.add(new TransformGroup().translate(new Vector3f(0,-0.7f,0))
									.add(lowerarm));
		return arm;
	}
}
