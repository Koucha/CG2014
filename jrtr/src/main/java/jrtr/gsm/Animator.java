package jrtr.gsm;

/**
 * Class that handles the animation
 * Has to be implemented (anonymously for example)
 * 
 * @author Florian
 */
public abstract class Animator
{
	protected AnimationGroup group;
	
	/**
	 * Links the Animator to a AnimationGroup
	 * If you use this method, you most probably do something wrong.
	 * 
	 * @param group AnimationGroup to be animated by Animator
	 */
	public void linkTo(AnimationGroup group)
	{
		this.group = group;
	}
	
	/**
	 * Does the actual animation.
	 * This you have to implement!
	 * 
	 * @param aniInf Information needed to do the animation
	 */
	public abstract void doAnimation(AnimationInfo aniInf);
}
