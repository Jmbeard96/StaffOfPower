package me.jmbeard96.StaffOfPower;

import org.bukkit.ChatColor;

public class StaffPlayer {
	public enum Skill{
		FIREBALL ("Fireball", 25),
		LIGHTNING ("Lightning", 30),
		HEAL ("Heal", 10),
		SPLASH_HEAL ("Splash Heal", 15),
		CLOAKING ("Cloaking", 20),
		SHIELD ("Shield", 20),
		TELEPORT ("Teleport", 13),
		PUNCH ("Punch", 20);
		
		private final String name;
		private final int level;
		
		private Skill(String s, int l) {
			name = s;
			level = l;
		}
		
		public String skill() {
			return this.name;
		}
		
		public int level() {
			return this.level;
		}
		
	}
	
	
	private int skillID;
	public final int flightLevel = 22;
	public final int craftLevel = 25;
	private boolean isInvulnerable;
	private boolean isHidden;
	public final String higherLevelString = ChatColor.RED + "You must be higher level to perform this skill.";
	
	public StaffPlayer() {
		skillID = 0;
		isInvulnerable = false;
		isHidden = false;
	}
	
	public int getSkillID() {
		return skillID;
	}
	
	public String nextSkill() {
		if(skillID == Skill.values().length-1) {
			skillID = 0;
			return Skill.values()[0].skill();
		}else {
			return Skill.values()[++skillID].skill();
		}
	}
	
	public boolean getIsHidden() {
		return isHidden;
	}
	
	public boolean getIsInvulnerable() {
		return isInvulnerable;
	}
	
	public void toggleShield() {
		if(isInvulnerable) {
			isInvulnerable = false;
		}
		else {
			isInvulnerable = true;
		}
	}
	
	public void toggleCloak() {
		if(isHidden) {
			isHidden = false;
		}
		else {
			isHidden = true;
		}
	}
}
