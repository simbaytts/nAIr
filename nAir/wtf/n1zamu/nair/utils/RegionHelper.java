package wtf.n1zamu.nair.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import wtf.n1zamu.nair.NAir;

public class RegionHelper {
   public static void createRegion(Location start, Location end, String name) {
      World world = BukkitAdapter.adapt(start.getWorld());
      BlockVector3 sk89qStart = convertToSk89qBV(start);
      BlockVector3 sk89qEnd = convertToSk89qBV(end);
      ProtectedCuboidRegion protectedCuboidRegion = new ProtectedCuboidRegion(name, sk89qStart, sk89qEnd);
      WorldGuard.getInstance().getPlatform().getRegionContainer().get(world).addRegion(protectedCuboidRegion);
      setRegionFlags(world, name);
   }

   private static void setRegionFlags(World world, String name) {
      String worldName = world.getName();
      List<String> rgFlags = NAir.config().getStringList("region.flags");
      Iterator var4 = rgFlags.iterator();

      while(var4.hasNext()) {
         String flag = (String)var4.next();
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "region flag -w " + worldName + " " + name + " " + flag + " allow");
      }

   }

   public static void regionCreate(Block block, String name) {
      Location min = block.getLocation().clone().add(50.0D, 255.0D, 50.0D);
      Location max = block.getLocation().clone().subtract(50.0D, 100.0D, 50.0D);
      createRegion(min, max, name);
   }

   public static BlockVector3 convertToSk89qBV(Location location) {
      return BlockVector3.at(location.getX(), location.getY(), location.getZ());
   }

   public static void removeRegion(String name, org.bukkit.World w) {
      WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(w)).removeRegion(name);
   }

   public static boolean isInRegion(String rgId, Location location) {
      com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(location);
      RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
      RegionQuery query = container.createQuery();
      ApplicableRegionSet set = query.getApplicableRegions(loc);
      Iterator var6 = set.iterator();

      ProtectedRegion region;
      do {
         if (!var6.hasNext()) {
            return false;
         }

         region = (ProtectedRegion)var6.next();
      } while(!region.getId().equalsIgnoreCase(rgId));

      return true;
   }
}
