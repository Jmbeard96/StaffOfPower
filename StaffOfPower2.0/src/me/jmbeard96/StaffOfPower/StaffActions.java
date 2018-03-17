package me.jmbeard96.StaffOfPower;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import me.jmbeard96.Constants.SkillExhaustion;
import me.jmbeard96.Constants.Staff;
import me.jmbeard96.Runnables.CloakingRunnable;
import me.jmbeard96.Runnables.FlightExhaustionRunnable;
import me.jmbeard96.Runnables.ShieldRunnable;
import me.jmbeard96.StaffOfPower.StaffPlayer.Skill;

public class StaffActions implements Listener {

	private final Plugin mainPlugin;

	public StaffActions(Plugin plugin) {
		mainPlugin = plugin;
	}
	
	/*******************************************************************************************************************************************************/
	//Actions
	public void fireBall(Player p, Vector direction, float exhaustion, GameMode gm) {
		LargeFireball f = p.launchProjectile(LargeFireball.class, direction.multiply(5));
		f.setIsIncendiary(false);
		f.setYield(0);

		if (gm == GameMode.SURVIVAL) {
			p.setExhaustion(SkillExhaustion.incrementFireball(exhaustion));
		}
	}

	public void summonLightning(Player p, GameMode gm, float exhaustion) {

		World currentWorld = p.getWorld();
		Block targetBlock = p.getTargetBlock(null, 100);
		Location targetLocation = targetBlock.getLocation();
		currentWorld.strikeLightning(targetLocation);
		if (gm == GameMode.SURVIVAL) {
			p.setExhaustion(SkillExhaustion.incrementLightning(exhaustion));
		}
	}
	
	public void healSelf(Player p, float exhaustion) {
		double maxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		double currentHealth = p.getHealth();
		currentHealth += 2;

		String formatMaxHealth = Integer.toString((int) maxHealth);
		String formatCurrentHealth = Integer.toString((int) currentHealth);

		if (currentHealth > maxHealth) {
			p.setHealth(maxHealth);
			p.setExhaustion(SkillExhaustion.incrementHeal(exhaustion));
			p.sendMessage(ChatColor.GREEN + p.getName() + " health: " + formatMaxHealth + "/"
					+ formatMaxHealth);
		} else {
			p.setHealth(currentHealth);
			p.sendMessage(ChatColor.GREEN + p.getName() + " health: " + formatCurrentHealth + "/"
					+ formatMaxHealth);
			p.setExhaustion(SkillExhaustion.incrementHeal(exhaustion));
		}
	}
	
	public void splashHeal(Player p, float exhaustion) {
		List<Entity> allies = p.getNearbyEntities(10, 10, 10);

		for (Entity entity : allies) {

			if (entity instanceof Player) {
				Player entityHealed = (Player) entity;
				double allyCurrentHealth = entityHealed.getHealth();
				double allyMaxHealth = entityHealed.getAttribute(Attribute.GENERIC_MAX_HEALTH)
						.getValue();
				String formatAllyMaxHealth = Integer.toString((int) allyMaxHealth);
				String formatAllyCurrentHealth = Integer.toString((int) allyCurrentHealth);
				allyCurrentHealth += 2;

				if (allyCurrentHealth > allyMaxHealth) {
					entityHealed.setHealth(allyMaxHealth);
					p.setExhaustion(SkillExhaustion.incrementSplashHeal(exhaustion));
					p.sendMessage(ChatColor.GREEN + entityHealed.getName() + " health: "
							+ formatAllyMaxHealth + "/" + formatAllyMaxHealth);
				} else {
					entityHealed.setHealth(allyCurrentHealth);
					p.setExhaustion(SkillExhaustion.incrementSplashHeal(exhaustion));
					p.sendMessage(ChatColor.GREEN + entityHealed.getName() + " health: "
							+ formatAllyCurrentHealth + "/" + formatAllyMaxHealth);
				}
			}
		}
	}
	
	public void cloaking(Player p) {
		
		StaffPlayer sp = PlayerMap.staffPlayers.get(p.getUniqueId());
		
		if (sp.getIsHidden()) {
			sp.toggleCloak();
			for (Player player : mainPlugin.getServer().getOnlinePlayers()) {
				if (player.getUniqueId() != p.getUniqueId()) {
					player.showPlayer(p);
				}
			}
			p.sendMessage(ChatColor.RED + "You are no longer cloaked");
		} else {
			sp.toggleCloak();
			for (Player player : mainPlugin.getServer().getOnlinePlayers()) {
				if (player.getUniqueId() != p.getUniqueId()) {
					player.hidePlayer(p);
				}
			}
			for (Entity entity : p.getNearbyEntities(16, 16, 16)) {
				if (entity instanceof Monster) {
					Monster monster = (Monster) entity;
					if (monster.getTarget() != null && monster.getTarget().equals(p)) {
						monster.setTarget(null);
					}
				}
			}
			CloakingRunnable cr = new CloakingRunnable(p);
			cr.runTaskTimer(mainPlugin, 20, 20);
			p.sendMessage(ChatColor.GREEN + "You are now cloaked");
		}
	}
	
	public void shield(Player p) {
		StaffPlayer sp = PlayerMap.staffPlayers.get(p.getUniqueId());
		if (sp.getIsInvulnerable()) {
			sp.toggleShield();;
			p.sendMessage(ChatColor.RED + "You are no longer invulnerable");
		} else {
			sp.toggleShield();
			ShieldRunnable sr = new ShieldRunnable(p);
			sr.runTaskTimer(mainPlugin, 20, 20);
			p.sendMessage(ChatColor.GREEN + "You are now invulverable");
		}
	}
	
	public void teleport(Player p) {
		float yaw = p.getLocation().getYaw();
		float pitch = p.getLocation().getPitch();

		Block targetBlock = p.getTargetBlock(null, 100);

		Location targetLocation = targetBlock.getLocation();

		Material block = targetLocation.getBlock().getType();
		double x = p.getLocation().getDirection().getX();
		double z = p.getLocation().getDirection().getZ();

		if (block != Material.AIR) {
			targetLocation.add(0, 1, 0);
			if (x > 0) {
				if (z > 0) {
					if (x > z) {
						targetLocation.add(-1, 0, 0);
						// p.sendMessage("Positive x");
					} else {
						targetLocation.add(0, 0, -1);
						// p.sendMessage("Positive z");
					}
				} else if (z < 0) {
					if (x > Math.abs(z)) {
						targetLocation.add(-1, 0, 0);
						// p.sendMessage("Positive x");
					} else {
						targetLocation.add(0, 0, 1);
						// p.sendMessage("Negative z");
					}
				}
			} else if (x < 0) {
				if (z > 0) {
					if (Math.abs(x) > z) {
						targetLocation.add(1, 0, 0);
						// p.sendMessage("Negative x");
					} else {
						targetLocation.add(0, 0, -1);
						// p.sendMessage("Positive z");
					}
				} else if (z < 0) {
					if (Math.abs(x) > Math.abs(z)) {
						targetLocation.add(1, 0, 0);
						// p.sendMessage("Negative x");
					} else {
						targetLocation.add(0, 0, 1);
						// p.sendMessage("Negative z");
					}
				}
			}
		}

		targetLocation.setYaw(yaw);
		targetLocation.setPitch(pitch);

		double distance = targetLocation.distance(p.getLocation());
		float teleportExhaustion = (float) distance * SkillExhaustion.teleport;
		float playerExhaustion = p.getExhaustion();
		p.setExhaustion(teleportExhaustion + playerExhaustion);
		if (p.getFoodLevel() != 0) {
			p.teleport(targetLocation);
		} else {
			p.setExhaustion(playerExhaustion);
			p.sendMessage(ChatColor.RED + "You are too tired to teleport that far.");
		}
	}

	/*******************************************************************************************************************************************************/
	// Schedule flight exhaustion timer
	@EventHandler
	public void toggleFlight(PlayerToggleFlightEvent e) {

		Player p = e.getPlayer();
		GameMode gm = p.getGameMode();

		if (gm == GameMode.SURVIVAL) {
			if (p.getLevel() >= PlayerMap.staffPlayers.get(p.getUniqueId()).flightLevel) {
				FlightExhaustionRunnable incrementExhaust = new FlightExhaustionRunnable(p);
				incrementExhaust.runTaskTimer(mainPlugin, 20, 20);
			} else {
				p.sendMessage("");
			}
		}
	}

	/*******************************************************************************************************************************************************/
	// Allow flight on gamemode change
	@EventHandler
	public void gameModeChange(PlayerGameModeChangeEvent e) {
		Player p = e.getPlayer();
		ItemStack itemInHand = p.getInventory().getItemInMainHand();
		GameMode gm = p.getGameMode();

		if (gm == GameMode.SURVIVAL) {
			if (Staff.isStaff(itemInHand)) {
				if (p.getFoodLevel() != 0) {
					if (p.getLevel() >= PlayerMap.staffPlayers.get(p.getUniqueId()).flightLevel) {
						p.setAllowFlight(true);
					} else {
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

	/*******************************************************************************************************************************************************/
	// Allow flight while holding staff and disable cloak and shield when change
	// from staff
	@EventHandler
	public void onItemChange(PlayerItemHeldEvent e) {
		Player p = e.getPlayer();
		StaffPlayer sp = PlayerMap.staffPlayers.get(p.getUniqueId());
		ItemStack newItem = p.getInventory().getItem(e.getNewSlot());
		GameMode gm = p.getGameMode();

		if (gm == GameMode.SURVIVAL) {
			if (Staff.isStaff(newItem)) {
				if (p.getFoodLevel() != 0) {
					if (p.getLevel() >= sp.flightLevel) {
						p.setAllowFlight(true);
					} else {
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

		if (sp.getIsInvulnerable()) {
			if (!Staff.isStaff(newItem)) {
				sp.toggleShield();
				p.sendMessage(ChatColor.RED + "You are no longer invulnerable");
			}
		}
		if (sp.getIsHidden()) {
			if (!Staff.isStaff(newItem)) {
				sp.toggleCloak();
				p.sendMessage(ChatColor.RED + "You are no longer cloaked");
			}
		}
	}

	/*******************************************************************************************************************************************************/
	// Main Skill Execution
	@EventHandler
	public void onClick(PlayerInteractEvent e) {

		Player p = e.getPlayer();
		StaffPlayer sp = PlayerMap.staffPlayers.get(p.getUniqueId());
		PlayerInventory i = p.getInventory();
		GameMode gm = p.getGameMode();
		Vector direction = p.getLocation().getDirection().normalize();
		float exhaustion = p.getExhaustion();
		// Material offhand = p.getInventory().getItemInOffHand().getType();

		if (Staff.isStaff(i.getItemInMainHand()) && i.getItemInOffHand().getType() == Material.AIR) {

			if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {

				if (p.getFoodLevel() != 0 || gm == GameMode.CREATIVE) {
					// Skills
					/*****************************************************************************************************************************/
					e.setCancelled(true);
					int currentSkill = sp.getSkillID();
					List<Skill> playerSkills = Arrays.asList(Skill.values());
					int fireballIndex = playerSkills.indexOf(Skill.FIREBALL);
					int lightningIndex = playerSkills.indexOf(Skill.LIGHTNING);
					int healIndex = playerSkills.indexOf(Skill.HEAL);
					int splashHealIndex = playerSkills.indexOf(Skill.SPLASH_HEAL);
					int cloakingIndex = playerSkills.indexOf(Skill.CLOAKING);
					int shieldIndex = playerSkills.indexOf(Skill.SHIELD);
					int teleportIndex = playerSkills.indexOf(Skill.TELEPORT);

					// Launch fire ball
					if (currentSkill == fireballIndex) {
						if (p.getLevel() >= Skill.FIREBALL.level()
								|| gm == GameMode.CREATIVE) {
							
							fireBall(p, direction, exhaustion, gm);
						} else {
							p.sendMessage(sp.higherLevelString);
						}
					}

					// Summon lightning
					else if (currentSkill == lightningIndex) {
						if (p.getLevel() >= Skill.LIGHTNING.level()
								|| gm == GameMode.CREATIVE) {
							summonLightning(p, gm, exhaustion);
						} else {
							p.sendMessage(sp.higherLevelString);
						}
					}

					// Heal self
					else if (currentSkill == healIndex) {
						if (p.getLevel() >= Skill.HEAL.level() || gm == GameMode.CREATIVE) {
							
							healSelf(p,exhaustion);
						} else {
							p.sendMessage(sp.higherLevelString);
						}
					}

					// Splash Heal
					else if (currentSkill == splashHealIndex) {
						if (p.getLevel() >= Skill.SPLASH_HEAL.level()
								|| gm == GameMode.CREATIVE) {
							
							splashHeal(p, exhaustion);
						}
						else {
							p.sendMessage(sp.higherLevelString);
						}
					}

					// Cloaking
					else if (currentSkill == cloakingIndex) {
						if (p.getLevel() >= Skill.CLOAKING.level()
								|| gm == GameMode.CREATIVE) {
							
							cloaking(p);
						} 
						else {
							p.sendMessage(sp.higherLevelString);
						}
					}

					// Shield
					else if (currentSkill == shieldIndex) {
						if (p.getLevel() >= Skill.SHIELD.level()
								|| gm == GameMode.CREATIVE) {
							
							shield(p);
						} 
						else {
							p.sendMessage(sp.higherLevelString);
						}
					}

					// Teleport
					else if (currentSkill == teleportIndex) {
						if (p.getLevel() >= Skill.TELEPORT.level()
								|| gm == GameMode.CREATIVE) {
							
							teleport(p);
						} else {
							p.sendMessage(sp.higherLevelString);
						}
					}
					// Punch
					else {
						p.setLevel(30);
						if (p.getLevel() >= Skill.PUNCH.level()
								|| gm == GameMode.CREATIVE) {

						} else {
							p.sendMessage(sp.higherLevelString);
						}
					}
					/***********************************************************************************************************************************************************/
				}
			}
			// Cycle through skills
			else if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

				if (e.getHand() == EquipmentSlot.HAND) {
					p.sendMessage((ChatColor.WHITE + Integer.toString(sp.getSkillID() + 1) + ". ") + (ChatColor.YELLOW + sp.nextSkill()));
				}
			}
		}
		// Prevent offhand items
		else if (Staff.isStaff(i.getItemInMainHand()) && i.getItemInOffHand().getType() != Material.AIR) {
			if (e.getHand().equals(EquipmentSlot.HAND)) {
				p.sendMessage(ChatColor.RED + "The Dragon Staff is a two-handed weapon");
			}
		}

	}
/*******************************************************************************************************************************************************/
	// Heal Player
	@EventHandler
	public void healPlayer(EntityDamageByEntityEvent e) {

		if (e.getDamager() instanceof Player) {
			if (e.getEntity() instanceof LivingEntity) {
				Player healer = (Player) e.getDamager();
				StaffPlayer sp = PlayerMap.staffPlayers.get(healer.getUniqueId());
				
				PlayerInventory i = healer.getInventory();
				LivingEntity entityHealed = (LivingEntity) e.getEntity();
				if (Staff.isStaff(i.getItemInMainHand()) && i.getItemInOffHand().getType() == Material.AIR) {
					if (healer.getFoodLevel() != 0 || healer.getGameMode() == GameMode.CREATIVE) {
						if (sp.getSkillID() == 2) {
							e.setCancelled(true);

							float healerExhaustion = healer.getExhaustion();
							double maxHealth = entityHealed.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
							double currentHealth = entityHealed.getHealth();
							String formatMaxHealth = Integer.toString((int) maxHealth);
							String formatCurrentHealth = Integer.toString((int) currentHealth);

							currentHealth += 2;

							if (currentHealth > maxHealth) {
								entityHealed.setHealth(maxHealth);
								healer.sendMessage(ChatColor.GREEN + entityHealed.getName() + " health: "
										+ formatMaxHealth + "/" + formatMaxHealth);
							} else {
								entityHealed.setHealth(currentHealth);
								healer.sendMessage(ChatColor.GREEN + entityHealed.getName() + " health: "
										+ formatCurrentHealth + "/" + formatMaxHealth);
							}

							if (healer.getGameMode() == GameMode.SURVIVAL) {
								healer.setExhaustion(SkillExhaustion.incrementHeal(healerExhaustion));
							}
						}
					}
				} else if (Staff.isStaff(i.getItemInMainHand()) && i.getItemInOffHand().getType() != Material.AIR) {
					healer.sendMessage(ChatColor.RED + "The Dragon Staff is a two-handed weapon");
				}
			}
		}
	}

	/*******************************************************************************************************************************************************/
	// Shield effect
	@EventHandler
	public void shield(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (PlayerMap.staffPlayers.get(p.getUniqueId()).getIsInvulnerable()) {
				e.setCancelled(true);
			}

			// Disable flight when food level gets to 0
			if (e.getCause() == DamageCause.STARVATION) {
				p.setAllowFlight(false);
			}
		}
	}

	/*******************************************************************************************************************************************************/
	// Cloak
	@EventHandler
	public void cloak(EntityTargetEvent e) {
		if (e.getTarget() instanceof Player) {
			Player p = (Player) e.getTarget();

			if (PlayerMap.staffPlayers.get(p.getUniqueId()).getIsHidden()) {
				e.setCancelled(true);
			}
		}
	}
}
