package me.jmbeard96.Runnables;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.jmbeard96.Constants.SkillExhaustion;
import me.jmbeard96.StaffOfPower.PlayerMap;

public class ShieldRunnable extends BukkitRunnable{

	private Player p;
	private GameMode gm;
	
	public ShieldRunnable(Player p) {
		this.p = p;
		gm = p.getGameMode();
	}
	
	@Override
	public void run() {
		if(p.isOnline() && PlayerMap.staffPlayers.get(p.getUniqueId()).getIsInvulnerable() && gm == GameMode.SURVIVAL) {
			if(p.getFoodLevel() != 0) {
				p.setExhaustion(SkillExhaustion.incrementShield(p.getExhaustion()));
			}
			else {
				PlayerMap.staffPlayers.get(p.getUniqueId()).toggleShield();;
				p.sendMessage(ChatColor.RED + "You are no longer invulnerable");
				cancel();
			}
		}
		else {
			cancel();
		}
		
	}

}
