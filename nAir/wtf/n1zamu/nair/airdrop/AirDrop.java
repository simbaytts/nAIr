package wtf.n1zamu.nair.airdrop;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import java.io.File;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import wtf.n1zamu.nair.NAir;
import wtf.n1zamu.nair.utils.Holograms;
import wtf.n1zamu.nair.utils.RandomLocation;
import wtf.n1zamu.nair.utils.RegionHelper;
import wtf.n1zamu.nair.utils.Schematic;

public class AirDrop {
   public static final AirDropType[] AIRDROP_TYPES;
   public static AirDropType airDropType;
   public static String regionName;
   public static boolean started;
   public static Block airBlock;
   public static Hologram airHologram;
   public static BossBar airBar;

   public static void createAirDrop(AirDropType air) {
      airDropType = air;
      started = true;
      regionName = "nAirs" + UUID.randomUUID();
      World world = Bukkit.getWorld(NAir.config().getString("worldName"));
      airBlock = world.getBlockAt(RandomLocation.generateLocation());
      airBar.setVisible(true);
      airBar.setTitle(ChatColor.translateAlternateColorCodes('&', NAir.config().getString("spawnName").replace("%airType%", airDropType.getName()).replace("%xCord%", Integer.toString(airBlock.getX())).replace("%zCord%", Integer.toString(airBlock.getZ()))));
      airBar.setProgress(1.0D);
      airBar.setColor(BarColor.valueOf(NAir.config().getString("spawnBarColor")));
      Iterator var2 = Bukkit.getOnlinePlayers().iterator();

      while(var2.hasNext()) {
         Player p = (Player)var2.next();
         airBar.addPlayer(p);
         airNotification(p);
      }

      Schematic.loadSchematic(airBlock.getLocation(), new File(NAir.config().getString("schematicPath")));
      String hologramText = ChatColor.translateAlternateColorCodes('&', NAir.config().getString("hologram.airdropClosed").replace("%airType%", airDropType.getName()));
      airHologram = Holograms.createHologram(hologramText);
      RegionHelper.regionCreate(airBlock, regionName);
   }

   private static void airNotification(Player player) {
      if (NAir.config().getBoolean("notification.sound")) {
         player.playSound(player.getLocation(), Sound.valueOf(NAir.config().getString("notification.soundType")), 1.0F, 1.0F);
      }

      if (NAir.config().getBoolean("notification.title")) {
         player.sendTitle(ChatColor.translateAlternateColorCodes('&', NAir.config().getString("titleName")), ChatColor.translateAlternateColorCodes('&', NAir.config().getString("notification.titleText").replace("%xCord%", Integer.toString(airBlock.getX())).replace("%zCord%", Integer.toString(airBlock.getZ()))).replace("%airType%", airDropType.getName()));
      }

      if (NAir.config().getBoolean("notification.message")) {
         Iterator var1 = NAir.config().getStringList("notification.messageText").iterator();

         while(var1.hasNext()) {
            String line = (String)var1.next();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', line).replace("%xCord%", Integer.toString(airBlock.getX())).replace("%zCord%", Integer.toString(airBlock.getZ())).replace("%airType%", airDropType.getName()));
         }
      }

   }

   static {
      AIRDROP_TYPES = new AirDropType[]{AirDropType.LEGENDARY, AirDropType.MYTHIC, AirDropType.EPIC, AirDropType.RARE, AirDropType.COMMON};
      airBar = Bukkit.createBossBar(ChatColor.GOLD + "АирДроп", BarColor.valueOf(NAir.config().getString("spawnBarColor")), BarStyle.valueOf(NAir.config().getString("spawnBarStyle")), new BarFlag[0]);
   }
}
