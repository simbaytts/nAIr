package wtf.n1zamu.nair.listeners;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import io.papermc.paper.event.entity.EntityMoveEvent;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.EntityTransformEvent.TransformReason;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import wtf.n1zamu.nair.NAir;
import wtf.n1zamu.nair.airdrop.AirDrop;
import wtf.n1zamu.nair.bosses.Bosses;
import wtf.n1zamu.nair.bosses.CatBoss;
import wtf.n1zamu.nair.bosses.SpiderBoss;
import wtf.n1zamu.nair.bosses.ZombieBoss;
import wtf.n1zamu.nair.utils.BossBarManager;
import wtf.n1zamu.nair.utils.Holograms;
import wtf.n1zamu.nair.utils.RegionHelper;

public class BossListener implements Listener {
   public static Hologram bossHologram;
   BossBarManager bossBarManager = new BossBarManager(NAir.getInstance());
   public static Bosses boss;
   public static BossBar bossBar;
   private final Bosses[] BOSSES = new Bosses[]{new CatBoss(), new ZombieBoss(), new SpiderBoss()};
   public static boolean spawned;

   @EventHandler
   public void onInteract(PlayerInteractEvent event) {
      Action action = event.getAction();
      Block block = event.getClickedBlock();
      if (AirDrop.started && action == Action.RIGHT_CLICK_BLOCK && block != null && !spawned && block.getType() == Material.valueOf(NAir.config().getString("activateMaterial")) && AirDrop.airBlock.getLocation().getWorld().getNearbyEntities(AirDrop.airBlock.getLocation(), 10.0D, 5.0D, 10.0D).contains(event.getPlayer())) {
         Holograms.removeHologram(AirDrop.airHologram);
         String bossText = ChatColor.translateAlternateColorCodes('&', NAir.config().getString("hologram.killBoss"));
         bossHologram = Holograms.createHologram(bossText);
         event.setCancelled(true);
         boss = this.BOSSES[(new Random()).nextInt(this.BOSSES.length)];
         AirDrop.airBar.setVisible(false);
         spawned = true;
         boss.spawn(event.getPlayer().getLocation());
         bossBar.setVisible(true);
         bossBar.setTitle(ChatColor.translateAlternateColorCodes('&', NAir.config().getString("bossDontKilled").replace("%airType%", AirDrop.airDropType.getName()).replace("%xCord%", Integer.toString(AirDrop.airBlock.getX())).replace("%zCord%", Integer.toString(AirDrop.airBlock.getZ()))));
         bossBar.setProgress(1.0D);
         Iterator var5 = Bukkit.getOnlinePlayers().iterator();

         Player p;
         while(var5.hasNext()) {
            p = (Player)var5.next();
            bossBar.addPlayer(p);
         }

         var5 = Bukkit.getOnlinePlayers().iterator();

         while(var5.hasNext()) {
            p = (Player)var5.next();
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', NAir.config().getString("activateAir").replace("%player%", event.getPlayer().getName())));
         }

      }
   }

   @EventHandler
   public void onEntityMove(EntityMoveEvent event) {
      if (AirDrop.started) {
         if (boss != null) {
            LivingEntity entity = event.getEntity();
            if (entity.hasMetadata(boss.mobId) && !RegionHelper.isInRegion(AirDrop.regionName, entity.getLocation())) {
               entity.teleport(AirDrop.airBlock.getLocation().add(0.0D, 5.0D, 0.0D));
            }

            if (entity.hasMetadata("CatBoss")) {
               int random = ThreadLocalRandom.current().nextInt(100);
               if (random > 90) {
                  Location entityLocation = entity.getLocation();
                  entityLocation.getBlock().setType(Material.FIRE);
               }
            }

         }
      }
   }

   @EventHandler
   public void onBossDeath(EntityDeathEvent event) {
      if (AirDrop.started) {
         Entity entity = event.getEntity();
         if (entity.hasMetadata("SpiderBoss") || entity.hasMetadata("ZombieKing") || entity.hasMetadata("CatBoss")) {
            this.bossBarManager.startAir();
            bossBar.setVisible(false);
         }

      }
   }

   @EventHandler
   public void onZombieTransform(EntityTransformEvent event) {
      if (AirDrop.started) {
         if (event.getTransformReason() == TransformReason.DROWNED && event.getEntity() instanceof Zombie) {
            Zombie zombie = (Zombie)event.getEntity();
            if (zombie.hasMetadata("ZombieKing")) {
               event.setCancelled(true);
            }
         }

      }
   }

   @EventHandler
   public void onEntityAttack(EntityDamageByEntityEvent event) {
      if (spawned) {
         if (AirDrop.started) {
            if (event.getEntity() instanceof LivingEntity) {
               if (event.getDamager() instanceof LivingEntity) {
                  LivingEntity entity = (LivingEntity)event.getEntity();
                  if (entity.hasMetadata(boss.mobId)) {
                     bossBar.setProgress(entity.getHealth() / (double)boss.health);
                  }

                  Player player;
                  int random;
                  if (event.getDamager().hasMetadata("ZombieKing")) {
                     player = (Player)entity;
                     random = ThreadLocalRandom.current().nextInt(5);
                     if (random == 0) {
                        player.setVelocity(new Vector(0.0F, 1.5F, 0.0F));
                     }

                     if (random == 2) {
                        this.spawnMinion(EntityType.ZOMBIE, player.getLocation());
                     }
                  }

                  if (event.getDamager().hasMetadata("SpiderBoss")) {
                     player = (Player)entity;
                     random = ThreadLocalRandom.current().nextInt(5);
                     if (random == 0) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 3));
                     }

                     if (random == 2) {
                        this.spawnMinion(EntityType.CAVE_SPIDER, player.getLocation());
                     }

                     if (random == 3) {
                        player.getLocation().getBlock().setType(Material.COBWEB);
                     }
                  }

               }
            }
         }
      }
   }

   private void spawnMinion(EntityType minionType, Location centerLocation) {
      World world = centerLocation.getWorld();
      double radius = 4.0D;
      int minionsCount = 5;
      double angleIncrement = 6.283185307179586D / (double)minionsCount;

      for(int i = 0; i < minionsCount; ++i) {
         double angle = angleIncrement * (double)i;
         double offsetX = radius * Math.cos(angle);
         double offsetZ = radius * Math.sin(angle);
         Location spawnLocation = centerLocation.clone().add(offsetX, 0.0D, offsetZ);
         LivingEntity minion = (LivingEntity)world.spawnEntity(spawnLocation, minionType);
         if (minion.getType() == EntityType.CAVE_SPIDER) {
            CaveSpider spider = (CaveSpider)minion;
            spider.getNearbyEntities(20.0D, 20.0D, 20.0D).forEach((nearby) -> {
               if (nearby instanceof Player && ((Player)nearby).getGameMode() == GameMode.SURVIVAL) {
                  spider.setTarget((LivingEntity)nearby);
               }

            });
         }

         if (minion.getType() == EntityType.ZOMBIE) {
            Zombie zombie = (Zombie)minion;
            zombie.setBaby();
            zombie.getNearbyEntities(20.0D, 20.0D, 20.0D).forEach((nearby) -> {
               if (nearby instanceof Player && ((Player)nearby).getGameMode() == GameMode.SURVIVAL) {
                  zombie.setTarget((LivingEntity)nearby);
               }

            });
         }
      }

   }

   static {
      bossBar = Bukkit.createBossBar(ChatColor.GOLD + "АирДроп", BarColor.valueOf(NAir.config().getString("bossBarColor")), BarStyle.valueOf(NAir.config().getString("bossBarStyle")), new BarFlag[0]);
      spawned = false;
   }
}
