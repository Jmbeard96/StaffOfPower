package me.jmbeard96.Runnables;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.jmbeard96.Constants.SkillExhaustion;
import me.jmbeard96.StaffOfPower.PlayerHashMaps;

public class CloakingRunnable extends BukkitRunnable{

	Player p;
	GameMode gm;
	
	public CloakingRunnable(Player p) {
		this.p = p;
		gm = p.getGameMode();
	}
	
	@Override
	public void run() {
		if(p.isOnline() && PlayerHashMaps.playerIsCloaked.get(p.getUniqueId()) && gm == GameMode.SURVIVAL) {
			if(p.getFoodLevel() != 0) {
				p.setExhaustion(SkillExhaustion.incrementCloak(p.getExhaustion()));
			}
			else {
				PlayerHashMaps.playerIsCloaked.replace(p.getUniqueId(), false);
				p.sendMessage(ChatColor.RED + "You are no longer cloaked");
				cancel();
			}
		}
		else {
			cancel();
		}
	}

}
