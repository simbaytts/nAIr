package wtf.n1zamu.nair.utils;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.n1zamu.nair.NAir;
import wtf.n1zamu.nair.airdrop.AirDrop;
import wtf.n1zamu.nair.commands.AirDropCreator;
import wtf.n1zamu.nair.listeners.BossListener;

public class BossBarManager {
   public static Hologram tickingHologram;
   public static Hologram openHologram;
   private int timeLeft;
   public BossBar tickingBar;
   public BossBar openBar;
   private final JavaPlugin plugin;
   private int airTicks = -1;

   public BossBarManager(JavaPlugin plugin) {
      this.plugin = plugin;
      this.tickingBar = Bukkit.createBossBar("АирДроп Начался", BarColor.valueOf(NAir.config().getString("tickingBarColor")), BarStyle.valueOf(NAir.config().getString("tickingBarStyle")), new BarFlag[0]);
      this.tickingBar.setVisible(false);
      this.openBar = Bukkit.createBossBar("АирДроп", BarColor.valueOf(NAir.config().getString("openBarColor")), BarStyle.valueOf(NAir.config().getString("openBarStyle")), new BarFlag[0]);
      this.openBar.setVisible(false);
   }

   public void startAir() {
      this.tickingBar.setVisible(true);
      this.tickingBar.setProgress(1.0D);
      this.openBar.setVisible(false);
      Iterator var1 = Bukkit.getOnlinePlayers().iterator();

      while(var1.hasNext()) {
         Player player = (Player)var1.next();
         this.tickingBar.addPlayer(player);
      }

      this.timeLeft = NAir.config().getInt("openCooldown");
      if (this.airTicks != -1) {
         this.plugin.getServer().getScheduler().cancelTask(this.airTicks);
      }

      Holograms.removeHologram(BossListener.bossHologram);
      String tickingText = ChatColor.translateAlternateColorCodes('&', NAir.config().getString("hologram.killedBoss")).replace("%time%", Integer.toString(this.timeLeft));
      tickingHologram = Holograms.createHologram(tickingText);
      this.airTicks = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
         if (this.timeLeft == 0) {
            this.endAir();
         } else {
            String newText = ChatColor.translateAlternateColorCodes('&', NAir.config().getString("hologram.killedBoss")).replace("%time%", Integer.toString(this.timeLeft));
            tickingHologram.getPage(0).getLine(0).setText(newText);
            this.tickingBar.setTitle(ChatColor.translateAlternateColorCodes('&', NAir.config().getString("tickingName").replace("%airType%", AirDrop.airDropType.getName()).replace("%xCord%", Integer.toString(AirDrop.airBlock.getX())).replace("%zCord%", Integer.toString(AirDrop.airBlock.getZ())).replace("%time%", Integer.toString(this.timeLeft))));
            this.tickingBar.setProgress((double)this.timeLeft / (double)NAir.config().getInt("openCooldown"));
            --this.timeLeft;
         }
      }, 0L, 20L);
   }

   public void endAir() {
      Iterator var1 = Bukkit.getOnlinePlayers().iterator();

      while(var1.hasNext()) {
         Player player = (Player)var1.next();
         this.openBar.addPlayer(player);
      }

      Holograms.removeHologram(tickingHologram);
      String tickingText = ChatColor.translateAlternateColorCodes('&', NAir.config().getString("hologram.openedAir"));
      openHologram = Holograms.createHologram(tickingText);
      AirDropCreator.blowItems(AirDrop.airBlock.getLocation(), AirDrop.airDropType.getItems());
      AirDrop.airBlock.getWorld().playSound(AirDrop.airBlock.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
      AirDrop.airBlock.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, AirDrop.airBlock.getLocation(), 10);
      AirDrop.airBlock.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, AirDrop.airBlock.getLocation(), 30);
      AirDrop.airBlock.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, AirDrop.airBlock.getLocation(), 50);
      Iterator var5 = AirDrop.airBlock.getLocation().getWorld().getNearbyEntities(AirDrop.airBlock.getLocation(), 10.0D, 5.0D, 10.0D).iterator();

      while(var5.hasNext()) {
         Entity entity = (Entity)var5.next();
         if (entity instanceof Player) {
            entity.setVelocity(entity.getLocation().getDirection().multiply(-1));
         }
      }

      this.openBar.setTitle(ChatColor.translateAlternateColorCodes('&', NAir.config().getString("openedName").replace("%airType%", AirDrop.airDropType.getName()).replace("%xCord%", Integer.toString(AirDrop.airBlock.getX())).replace("%zCord%", Integer.toString(AirDrop.airBlock.getZ())).replace("%time%", Integer.toString(this.timeLeft))));
      this.tickingBar.setVisible(false);
      this.openBar.setVisible(true);
      this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
         if (BossListener.boss != null) {
            Iterator var1 = AirDrop.airBlock.getWorld().getEntities().iterator();

            while(var1.hasNext()) {
               Entity entity = (Entity)var1.next();
               if (entity.hasMetadata(BossListener.boss.mobId)) {
                  entity.remove();
               }
            }
         }

         Holograms.removeHologram(openHologram);
         this.openBar.setVisible(false);
         Schematic.removeSchematic();
         RegionHelper.removeRegion(AirDrop.regionName, Bukkit.getWorld(NAir.config().getString("worldName")));
         AirDrop.started = false;
         BossListener.spawned = false;
         AirDrop.airBlock = null;
      }, (long)NAir.config().getInt("deleteCooldown") * 20L);
      this.plugin.getServer().getScheduler().cancelTask(this.airTicks);
      this.airTicks = -1;
   }
}
