package jrtr.gsm;

public class AnimationInfo
{
	float time;
	
	public AnimationInfo()
	{
		time = 0;
	}

	/**
	 * @return the time
	 */
	public float getTime()
	{
		return time;
	}

	/**
	 * @param time the time to set
	 * @return the AnimationInfo itself
	 */
	public AnimationInfo setTime(float time)
	{
		this.time = time;
		return this;
	}
	
	
}
