package me.jmbeard96.Constants;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.jmbeard96.StaffOfPower.StaffPlayer.Skill;

public class Staff {
	
	private static final String displayName = "The Dragon Staff";
	public static final ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);
	
	private static final ArrayList<String> lore;
	static{
		lore = new ArrayList<String>();
		Skill[] skillArray = Skill.values();
		
		for(Skill skill : skillArray) {
			lore.add(skill.skill() + " required level: " + skill.level());
		}
	}
	
	private static final ItemMeta meta;
	static{
		meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(lore);
	}
	
	static {
		item.setItemMeta(meta);
	}
	
	public static boolean isStaff(ItemStack item) {
		if(item != null && item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().equals(displayName)) {
				return true;
		}
		else {
			
			return false;
		}
	}
}
