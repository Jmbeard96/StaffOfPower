package me.jmbeard96.Runnables;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.jmbeard96.Constants.SkillExhaustion;

public class FlightExhaustionRunnable extends BukkitRunnable{
	
	private final Player player;
	private GameMode gm;

	//Constructor for instantiating objects of this class.
	public FlightExhaustionRunnable(Player p) {
		
		player = p;
		gm = player.getGameMode();
	}
	
	//Increments exhaustion while the player is flying.
	@Override
	public void run() {
		float exhaustion = player.getExhaustion();
		if (player.isOnline() && player.isFlying() && gm == GameMode.SURVIVAL) {
			
			player.setExhaustion(SkillExhaustion.incrementFlight(exhaustion));
		}
		else {
			cancel();
		}
	}
}
