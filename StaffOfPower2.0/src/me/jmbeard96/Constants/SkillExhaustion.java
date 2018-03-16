package me.jmbeard96.Constants;

public class SkillExhaustion {

	//Exhaustion values
	private static final float fireball = (float)1;
	private static final float lightning = (float)2;
	private static final float heal = (float)0.5;
	private static final float splashHeal = (float)0.75;
	private static final float cloak = (float)0.5; //per second;
	private static final float flight = (float)0.5; //per second;
	private static final float shield = (float)0.5;
	public static final float teleport = (float)0.25;
	
	//Incrementing/accessing methods
	public static float incrementFireball(float exhaustion) {
		return fireball + exhaustion;
	}
	public static float incrementLightning(float exhaustion) {
		return lightning + exhaustion;
	}
	public static float incrementHeal(float exhaustion) {
		return heal + exhaustion;
	}
	public static float incrementSplashHeal(float exhaustion) {
		return splashHeal + exhaustion;
	}
	public static float incrementCloak(float exhaustion) {
		return cloak + exhaustion;
	}
	public static float incrementFlight(float exhaustion) {
		return flight + exhaustion;
	}
	public static float incrementShield(float exhaustion) {
		return shield + exhaustion;
	}
}
