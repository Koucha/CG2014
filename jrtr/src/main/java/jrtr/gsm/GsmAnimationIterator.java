package jrtr.gsm;

public class GsmAnimationIterator
{
	private GraphSceneIterator intern;
	
	public GsmAnimationIterator(Node root)
	{
		intern = new GraphSceneIterator(root);
	}
	
	public boolean hasNext()
	{
		return intern.hasNext(Animator.class);
	}

	public Animator next()
	{
		return intern.next(Animator.class);
	}
	
	public boolean feedAnimationWith(AnimationInfo aniInfo)
	{
		if(!hasNext())
		{
			return false;
		}
		
		while(hasNext())
		{
			Animator ani = next();
			ani.doAnimation(aniInfo);
		}
		
		return true;
		
	}
}
