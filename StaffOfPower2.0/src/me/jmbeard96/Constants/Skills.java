package me.jmbeard96.Constants;

//import java.util.LinkedHashMap;

import org.bukkit.ChatColor;

public class Skills {
	
	public static enum Skill{
		FIREBALL ("Fireball", 25),
		LIGHTNING ("Lightning", 30),
		HEAL ("Heal", 10),
		SPLASH_HEAL ("Splash Heal", 15),
		CLOAKING ("Cloaking", 20),
		SHIELD ("Shield", 20),
		TELEPORT ("Teleport", 13),
		PUNCH ("Punch", 20),
		FLIGHT ("Flight", 22),
		CRAFT ("Craft", 25);
		
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
	
	public static final String higherLevelString = ChatColor.RED + "Your level is too low for that skill.";
	
}
