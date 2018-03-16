package me.jmbeard96.StaffOfPower;

public class SkillIterator {
	private int skillID;
	public final String [] skills;
	
	public SkillIterator(int x, String [] y) {
		skillID = x;
		skills = y;
	}
	
	public int getSkillID() {
		return skillID;
	}
	
	public String nextSkill() {
		if(skillID == skills.length-1) {
			return skills[0];
		}
		else {
			return skills[++skillID];
		}
	}
}
