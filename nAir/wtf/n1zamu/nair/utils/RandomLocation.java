package wtf.n1zamu.nair.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import wtf.n1zamu.nair.NAir;

public class RandomLocation {
   public static Location generateLocation() {
      Location location;
      do {
         int x = ThreadLocalRandom.current().nextInt(NAir.config().getInt("xCords")) + 1;
         int z = ThreadLocalRandom.current().nextInt(NAir.config().getInt("zCords")) + 1;
         int y = findValidY(Bukkit.getWorld(NAir.config().getString("worldName")), x, z, 256);
         location = new Location(Bukkit.getWorld(NAir.config().getString("worldName")), (double)x, (double)y, (double)z);
      } while(!isValidLocation(location));

      return location;
   }

   private static int findValidY(World world, int x, int z, int startY) {
      while(startY > 60) {
         Location location = new Location(world, (double)x, (double)startY, (double)z);
         if (location.getBlock().getType() == Material.GRASS_BLOCK && location.clone().add(0.0D, 1.0D, 0.0D).getBlock().isPassable()) {
            return startY + 1;
         }

         --startY;
      }

      return startY + 1;
   }

   private static boolean isValidLocation(Location location) {
      Block block = location.getBlock();
      com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(location);
      RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
      RegionQuery query = container.createQuery();
      ApplicableRegionSet set = query.getApplicableRegions(loc);
      Iterator var6 = set.iterator();
      if (var6.hasNext()) {
         ProtectedRegion ignored = (ProtectedRegion)var6.next();
         return false;
      } else if (block.getType() == Material.AIR && block.getRelative(BlockFace.UP).getType() == Material.AIR) {
         BlockFace[] var10 = BlockFace.values();
         int var7 = var10.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            BlockFace face = var10[var8];
            if (face != BlockFace.DOWN && face != BlockFace.UP && !location.getBlock().getRelative(face).isPassable()) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }
}
