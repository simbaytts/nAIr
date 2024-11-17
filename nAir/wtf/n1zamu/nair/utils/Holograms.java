package wtf.n1zamu.nair.utils;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import java.util.Arrays;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import wtf.n1zamu.nair.NAir;
import wtf.n1zamu.nair.airdrop.AirDrop;

public class Holograms {
   public static void removeHologram(Hologram hologram) {
      try {
         DHAPI.removeHologram(hologram.getName());
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static Hologram createHologram(String... lines) {
      String hologramName = "hologram_" + UUID.randomUUID();

      try {
         Block nearestBlock = findNearestBlock(AirDrop.airBlock.getLocation(), Material.valueOf(NAir.config().getString("activateMaterial")), 10);
         AirDrop.airBlock = nearestBlock;
         Location hologramLocation = nearestBlock.getLocation().add(0.5D, 1.5D, 0.5D);
         Hologram hologram = DHAPI.createHologram(hologramName, hologramLocation);
         Arrays.stream(lines).forEach((line) -> {
            DHAPI.addHologramLine(hologram, line);
         });
         Bukkit.getLogger().info("Голограмма успешно создана: " + hologramName);
         return hologram;
      } catch (Exception var5) {
         Bukkit.getLogger().severe("Ошибка при создании голограммы: " + hologramName + ", причина: " + var5.getMessage());
         var5.printStackTrace();
         return null;
      }
   }

   public static Block findNearestBlock(Location center, Material material, int radius) {
      for(int dx = -radius; dx <= radius; ++dx) {
         for(int dy = -radius; dy <= radius; ++dy) {
            for(int dz = -radius; dz <= radius; ++dz) {
               Block block = center.getWorld().getBlockAt(center.getBlockX() + dx, center.getBlockY() + dy, center.getBlockZ() + dz);
               if (block.getType() == material) {
                  return block;
               }
            }
         }
      }

      return null;
   }
}
