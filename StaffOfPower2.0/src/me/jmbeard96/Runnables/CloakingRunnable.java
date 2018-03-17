package me.jmbeard96.Runnables;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.jmbeard96.Constants.SkillExhaustion;
import me.jmbeard96.StaffOfPower.PlayerMap;
public class CloakingRunnable extends BukkitRunnable{

	Player p;
	GameMode gm;
	
	public CloakingRunnable(Player p) {
		this.p = p;
		gm = p.getGameMode();
	}
	
	@Override
	public void run() {
		if(p.isOnline() && PlayerMap.staffPlayers.get(p.getUniqueId()).getIsHidden() && gm == GameMode.SURVIVAL) {
			if(p.getFoodLevel() != 0) {
				p.setExhaustion(SkillExhaustion.incrementCloak(p.getExhaustion()));
			}
			else {
				PlayerMap.staffPlayers.get(p.getUniqueId()).toggleCloak();
				p.sendMessage(ChatColor.RED + "You are no longer cloaked");
				cancel();
			}
		}
		else {
			cancel();
		}
	}

}
