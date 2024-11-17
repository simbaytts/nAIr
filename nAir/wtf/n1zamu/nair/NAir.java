package wtf.n1zamu.nair;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import java.io.IOException;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.n1zamu.nair.airdrop.AirDrop;
import wtf.n1zamu.nair.airdrop.AirDropType;
import wtf.n1zamu.nair.commands.AirDropCreator;
import wtf.n1zamu.nair.commands.CreateCommand;
import wtf.n1zamu.nair.config.Config;
import wtf.n1zamu.nair.listeners.BossListener;
import wtf.n1zamu.nair.listeners.PlayerListener;
import wtf.n1zamu.nair.utils.BossBarManager;
import wtf.n1zamu.nair.utils.Holograms;
import wtf.n1zamu.nair.utils.Hooks;
import wtf.n1zamu.nair.utils.RegionHelper;
import wtf.n1zamu.nair.utils.Schematic;

public final class NAir extends JavaPlugin {
   public void onEnable() {
      Hooks hooks = new Hooks();
      this.saveResource("airdrop.yml", false);
      this.saveDefaultConfig();
      Config.runData();
      if (hooks.setupDecentHolograms() && hooks.setupWorldGuard()) {
         this.getLogger().info("=====NAir successfully loaded=====");
         this.getLogger().info("=====Author: github.com/n1zamu=====");
         this.getCommand("nAir").setExecutor(new CreateCommand());
         Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
         Bukkit.getPluginManager().registerEvents(new BossListener(), this);
         Bukkit.getPluginManager().registerEvents(new AirDropCreator(), this);
         Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if (!AirDrop.started) {
               AirDrop.createAirDrop(this.randomType());
            }

         }, 0L, (long)config().getInt("spawnCooldown") * 20L);
      }

   }

   public void onDisable() {
      try {
         Config.writeData();
         this.reloadAir();
      } catch (IOException var2) {
         var2.printStackTrace();
      }

      this.getLogger().info("=====NAir successfully unloaded=====");
      this.getLogger().info("=====Author: github.com/n1zamu=====");
   }

   private AirDropType randomType() {
      int cumulativeChance = 0;
      AirDropType[] var2 = AirDrop.AIRDROP_TYPES;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         AirDropType airType = var2[var4];
         cumulativeChance = (int)((float)cumulativeChance + airType.getChance());
         if ((int)(Math.random() * 100.0D) < cumulativeChance) {
            return airType;
         }
      }

      return AirDropType.COMMON;
   }

   public static NAir getInstance() {
      return (NAir)getPlugin(NAir.class);
   }

   public static FileConfiguration config() {
      return getInstance().getConfig();
   }

   private void reloadAir() {
      Hologram[] holograms = new Hologram[]{AirDrop.airHologram, BossListener.bossHologram, BossBarManager.tickingHologram, BossBarManager.openHologram};
      Schematic.removeSchematic();
      RegionHelper.removeRegion(AirDrop.regionName, Bukkit.getWorld(config().getString("worldName")));
      AirDrop.started = false;
      AirDrop.airBar.setVisible(false);
      BossListener.bossBar.setVisible(false);
      BossListener.spawned = false;
      if (BossListener.boss != null) {
         Iterator var2 = AirDrop.airBlock.getWorld().getEntities().iterator();

         while(var2.hasNext()) {
            Entity entity = (Entity)var2.next();
            if (entity.hasMetadata(BossListener.boss.mobId)) {
               entity.remove();
            }
         }
      }

      Hologram[] var6 = holograms;
      int var7 = holograms.length;

      for(int var4 = 0; var4 < var7; ++var4) {
         Hologram hologram = var6[var4];
         if (hologram != null) {
            Holograms.removeHologram(hologram);
         }
      }

      AirDrop.airBlock = null;
   }
}
