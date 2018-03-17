package me.jmbeard96.StaffOfPower;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerMap implements Listener{

	public static HashMap<UUID, StaffPlayer> staffPlayers;
	
	public PlayerMap(JavaPlugin plugin) {
		int maxPlayers = plugin.getServer().getMaxPlayers();
		staffPlayers = new HashMap<UUID, StaffPlayer>(maxPlayers);
		
		for(Player p : plugin.getServer().getOnlinePlayers()) {
			staffPlayers.put(p.getUniqueId(), new StaffPlayer());
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		staffPlayers.put(e.getPlayer().getUniqueId(), new StaffPlayer()); 
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		staffPlayers.remove(e.getPlayer().getUniqueId());
	}
}
