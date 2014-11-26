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
		
		//head
		roboroot.add(new TransformGroup().translate(new Vector3f(0,1.7f,0))
										 .add(makeHead(new Animator(){ @Override public void doAnimation(AnimationInfo aniInf)
													{
														Matrix4f trafo = new Matrix4f();
														trafo.rotZ((float) (0.2*Math.sin(aniInf.getTime()*10)));
														
														group.setTFMat(trafo);
													} })));
		
		//right arm
		roboroot.add(new TransformGroup().translate(new Vector3f(0.25f,1.6f,0))
										 .add(makeArm(new Animator(){ @Override public void doAnimation(AnimationInfo aniInf)
													{
														Matrix4f trafo = new Matrix4f();
														trafo.rotX((float) (0.6*Math.sin(aniInf.getTime()*10)));
														
														group.setTFMat(trafo);
													} }, new Animator(){ @Override public void doAnimation(AnimationInfo aniInf)
													{
														Matrix4f trafo = new Matrix4f();
														trafo.rotX((float) (0.6*Math.sin(aniInf.getTime()*10 - 0.5) - 0.3));
														
														group.setTFMat(trafo);
													} }, 0.05f)));
		
		//left arm
		roboroot.add(new TransformGroup().translate(new Vector3f(-0.25f,1.6f,0))
										 .add(makeArm(new Animator(){ @Override public void doAnimation(AnimationInfo aniInf)
													{
														Matrix4f trafo = new Matrix4f();
														trafo.rotX((float) (0.6*Math.sin(aniInf.getTime()*10 + Math.PI)));
														
														group.setTFMat(trafo);
													} }, new Animator(){ @Override public void doAnimation(AnimationInfo aniInf)
													{
														Matrix4f trafo = new Matrix4f();
														trafo.rotX((float) (0.6*Math.sin(aniInf.getTime()*10 - 0.5  + Math.PI) - 0.3));
														
														group.setTFMat(trafo);
													} }, -0.05f)));
		
		//right leg
		roboroot.add(new TransformGroup().translate(new Vector3f(0.15f,1,0))
										 .add(makeLeg(new Animator(){ @Override public void doAnimation(AnimationInfo aniInf)
													{
														Matrix4f trafo = new Matrix4f();
														trafo.rotX((float) (0.6*Math.sin(aniInf.getTime()*10 + Math.PI) - 0.2));
														
														group.setTFMat(trafo);
													} }, new Animator(){ @Override public void doAnimation(AnimationInfo aniInf)
													{
														Matrix4f trafo = new Matrix4f();
														trafo.rotX((float) (0.7*Math.sin(aniInf.getTime()*10 - 0.5 + Math.PI) + 0.7));
														
														group.setTFMat(trafo);
													} } )));
		
		//left leg
		roboroot.add(new TransformGroup().translate(new Vector3f(-0.15f,1,0))
										 .add(makeLeg(new Animator(){ @Override public void doAnimation(AnimationInfo aniInf)
													{
														Matrix4f trafo = new Matrix4f();
														trafo.rotX((float) (0.6*Math.sin(aniInf.getTime()*10) - 0.2));
														
														group.setTFMat(trafo);
													} }, new Animator(){ @Override public void doAnimation(AnimationInfo aniInf)
													{
														Matrix4f trafo = new Matrix4f();
														trafo.rotX((float) (0.7*Math.sin(aniInf.getTime()*10 - 0.5) + 0.7));
														
														group.setTFMat(trafo);
													} } )));
				
		
		return roboroot;
	}

	private static Node makeHead(Animator animator)
	{
		AnimationGroup head = new AnimationGroup(animator);

		//throat
		head.add(new TransformGroup().scale(0.08f,0.08f,0.08f)
				.translate(new Vector3f(0,0.03f,0))
				.add(new ShapeMaterialNode(ZylinderRS.getInstance(), WoodMat.getInstance())));
		
		//face
		Light light = new Light();
		light.diffuse = new Vector3f(0.0003f, 0.00005f, 0.00005f);
		light.ambient = new Vector3f(0.f, 0.f, 0.f);
		light.type = Light.Type.POINT;
		head.add(new TransformGroup().scale(0.15f,0.15f,0.15f)
				.translate(new Vector3f(0,0.13f,0))
				.add(new ShapeMaterialNode(ZylinderRS.getInstance(), WoodMat.getInstance()))
				.add(new TransformGroup().translate(new Vector3f(0.25f,0,0.433015f))
										 .add(new LightNode(light)))
				.add(new TransformGroup().translate(new Vector3f(-0.25f,0,0.433015f))
						 .add(new LightNode(light))));
		

		//hat
		head.add(new TransformGroup().scale(0.18f,0.13f,0.18f)
				.translate(new Vector3f(0,0.27f,0))
				.add(new ShapeMaterialNode(ConeRS.getInstance(), WoodMat.getInstance())));
		
		return head;
	}

	private static Node makeArm(Animator shoulder, Animator elbow, float offset)
	{
		AnimationGroup arm = new AnimationGroup(shoulder);
		//bolt
		arm.add(new TransformGroup().scale(0.05f,offset,0.05f)
									.rotate(new Vector3f(0,0,1), (float) (-Math.PI/2))
									.translate(new Vector3f(offset/2f,0,0))
									.add(new ShapeMaterialNode(ZylinderRS.getInstance(), WoodMat.getInstance())));
		
		//upper arm
		arm.add(new TransformGroup().scale(0.1f, 0.5f, 0.1f)
									.translate(new Vector3f(offset + ((offset > 0)?(0.05f):(-0.05f)),-0.2f,0))
									.add(new ShapeMaterialNode(CubeRS.getInstance(), WoodMat.getInstance())));
		
		AnimationGroup lowerarm = new AnimationGroup(elbow);
		
		//elbow
		lowerarm.add(new TransformGroup().scale(0.07f, 0.06f, 0.07f)
										 .rotate(new Vector3f(0,0,1), (float) (Math.PI/2))
										 .add(new ShapeMaterialNode(TorusRS.getInstance(), WoodMat.getInstance())));
		
		//lowerarm
		lowerarm.add(new TransformGroup().scale(0.1f,0.4f,0.1f)
										 .translate(new Vector3f(0,-0.24f,0))
										 .add(new ShapeMaterialNode(CubeRS.getInstance(), WoodMat.getInstance())));
		
		//hand
		lowerarm.add(new TransformGroup().scale(0.05f,0.15f,0.1f)
										 .translate(new Vector3f(0,-0.5f,0))
										 .add(new ShapeMaterialNode(TorusRS.getSphereInstance(), WoodMat.getInstance())));
		
		arm.add(new TransformGroup().translate(new Vector3f(offset + ((offset > 0)?(0.05f):(-0.05f)),-0.49f,0))
									.add(lowerarm));
		return arm;
	}

	private static Node makeLeg(Animator hip, Animator knee)
	{
		AnimationGroup leg = new AnimationGroup(hip);
		//hip
		leg.add(new TransformGroup().scale(0.12f)
									.add(new ShapeMaterialNode(TorusRS.getSphereInstance(), WoodMat.getInstance())));
		
		//upper leg
		leg.add(new TransformGroup().scale(0.1f, 0.5f, 0.1f)
									.translate(new Vector3f(0,-0.25f,0))
									.add(new ShapeMaterialNode(CubeRS.getInstance(), WoodMat.getInstance())));
		
		AnimationGroup lowerleg = new AnimationGroup(knee);
		
		//knee
		lowerleg.add(new TransformGroup().scale(0.1f)
										 .add(new ShapeMaterialNode(TorusRS.getSphereInstance(), WoodMat.getInstance())));
		
		//lower leg
		lowerleg.add(new TransformGroup().scale(0.1f,0.45f,0.1f)
										 .translate(new Vector3f(0,-0.225f,0))
										 .add(new ShapeMaterialNode(CubeRS.getInstance(), WoodMat.getInstance())));
		
		//foot
		lowerleg.add(new TransformGroup().scale(0.11f,0.05f,0.15f)
										 .translate(new Vector3f(0,-0.475f,0.045f))
										 .add(new ShapeMaterialNode(CubeRS.getInstance(), WoodMat.getInstance())));
		
		leg.add(new TransformGroup().translate(new Vector3f(0,-0.5f,0))
									.add(lowerleg));
		return leg;
	}
}
