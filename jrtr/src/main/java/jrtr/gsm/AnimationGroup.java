package jrtr.gsm;

import javax.vecmath.Matrix4f;

/**
 * Group whose children can be animated via animation task
 * 
 * Overrides isAndProgress and getAndProgress to respond to the animation iterator
 * 
 * @author Florian
 */
public class AnimationGroup extends TransformGroup
{
	Animator animator;
	
	public AnimationGroup()
	{
		animator = null;
	}
	
	public AnimationGroup(Animator animator)
	{
		animator.linkTo(this);
		this.animator = animator;
	}
	
	@Override
	public <T> boolean isAndProgress(Class<T> classreference, INodeRequestData nodeRequestData, Matrix4f tfMatOnStack)
	{
		progress(nodeRequestData, tfMatOnStack);
		
		if(classreference.isAssignableFrom(Animator.class))
		{
			return true;
		}else
		{
			return false;
		}
	}
	
	@Override
	public <T> T getAndProgress(Class<T> classreference, INodeRequestData nodeRequestData, Matrix4f tfMatOnStack)
	{
		progress(nodeRequestData, tfMatOnStack);
		
		if(classreference.isAssignableFrom(Animator.class))
		{
			@SuppressWarnings("unchecked")
			T temp = (T) animator;
			return temp;
		}else
		{
			return null;
		}
	}

	/**
	 * @return the animator
	 */
	public Animator getAnimator()
	{
		return animator;
	}

	/**
	 * @param animator the animator to set
	 */
	public void setAnimator(Animator animator)
	{
		animator.linkTo(this);
		this.animator = animator;
	}
}
