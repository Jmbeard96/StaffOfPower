package me.jmbeard96.StaffOfPower;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.jmbeard96.Constants.Skills;
import me.jmbeard96.Constants.Skills.Skill;
import me.jmbeard96.Constants.Staff;

public class PlayerHashMaps implements Listener{
	
	public static HashMap<UUID, Boolean> playerIsCloaked;
	public static HashMap<UUID, Boolean> playerIsInvulnerable;
	public static HashMap<UUID, SkillIterator> playerSkills;
	
	private final String[] skills = new String[Skill.values().length];
	
	public PlayerHashMaps(int maxPlayers, JavaPlugin plugin) {
		playerIsCloaked = new HashMap<UUID, Boolean> (maxPlayers);
		playerIsInvulnerable = new HashMap<UUID, Boolean> (maxPlayers);
		playerSkills = new HashMap<UUID, SkillIterator> (maxPlayers);
		
		//Exclude Flight from skill cycle
		for(int i = 0; i < Skill.values().length; i++) {
			if(Skill.values()[i] != Skill.FLIGHT && Skill.values()[i] != Skill.CRAFT) {
				skills[i] = Skill.values()[i].skill();
			}
		}
		
		for(Player p : plugin.getServer().getOnlinePlayers()) {
			playerSkills.put(p.getUniqueId(), new SkillIterator(0, skills));
			playerIsCloaked.put(p.getUniqueId(), false);
			playerIsInvulnerable.put(p.getUniqueId(), false);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		playerSkills.put(p.getUniqueId(), new SkillIterator(0, skills));
		playerIsCloaked.put(p.getUniqueId(), false);
		playerIsInvulnerable.put(p.getUniqueId(), false);
		
		ItemStack itemInHand = p.getInventory().getItemInMainHand();
		GameMode gm = p.getGameMode();

		if (gm == GameMode.SURVIVAL) {
			if (Staff.isStaff(itemInHand)) {
				if (p.getFoodLevel() != 0) {
					if(p.getLevel() >= Skills.Skill.FLIGHT.level()) {
						p.setAllowFlight(true);
					}
					else {
						p.setAllowFlight(false);
					}
				} else {
					p.setAllowFlight(false);
				}
			} else {
				p.setAllowFlight(false);
			}
		} else {
			p.setAllowFlight(true);
		}
		
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
				
		playerSkills.remove(p.getUniqueId());
		playerIsCloaked.remove(p.getUniqueId());
		playerIsInvulnerable.remove(p.getUniqueId());
	}
}
