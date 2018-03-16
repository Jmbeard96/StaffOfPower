package me.jmbeard96.StaffOfPower;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import me.jmbeard96.Constants.Skills;
import me.jmbeard96.Constants.Skills.Skill;
import me.jmbeard96.Constants.Staff;

public class Main extends JavaPlugin implements Listener{
	
	private ShapedRecipe staffRecipe;
	
	public void addStaffRecipe() {
		ItemStack dragonStaff = Staff.item;
		NamespacedKey staffKey = new NamespacedKey(this, "DragonStaff");
		staffRecipe = new ShapedRecipe(staffKey, dragonStaff);
		staffRecipe.shape("%D%", "%D%", "%B%");
		
		staffRecipe.setIngredient('B', Material.BLAZE_ROD);
		staffRecipe.setIngredient('D', Material.DIRT);
		staffRecipe.setIngredient('%', Material.AIR);
		
		this.getServer().addRecipe(staffRecipe);
	}
	
	@Override
	public void onEnable() {
		addStaffRecipe();
		this.getServer().getPluginManager().registerEvents(new StaffActions(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerHashMaps(getServer().getMaxPlayers(), this), this);
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@EventHandler
	public void staffCraft(CraftItemEvent e) {
		
		if(e.getRecipe().getResult().equals(staffRecipe.getResult())) {
			if(e.getWhoClicked() instanceof Player) {
				Player p = (Player)e.getWhoClicked();
				
				if(p.getGameMode() == GameMode.SURVIVAL) {
					if(p.getLevel() < Skill.CRAFT.level()) {
						e.setCancelled(true);
						p.sendMessage(Skills.higherLevelString);
					}
					else {
						p.setLevel(p.getLevel() - 10);
					}
				}
			}
		}
	}
}
