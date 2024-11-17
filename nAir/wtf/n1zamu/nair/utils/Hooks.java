package wtf.n1zamu.nair.utils;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Hooks {
   public boolean setupWorldGuard() {
      Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
      if (plugin instanceof WorldGuardPlugin) {
         Bukkit.getLogger().info("WorldGuard успешно загружен!");
         return true;
      } else {
         return false;
      }
   }

   public boolean setupDecentHolograms() {
      Plugin decentHolograms = Bukkit.getServer().getPluginManager().getPlugin("DecentHolograms");
      if (decentHolograms != null && decentHolograms.isEnabled()) {
         Bukkit.getLogger().info("DecentHolograms успешно загружен!");

         try {
            DecentHologramsAPI.get();
            Bukkit.getLogger().info("DecentHolograms API успешно инициализировано!");
            return true;
         } catch (IllegalStateException var3) {
            Bukkit.getLogger().severe("DecentHolograms API не инициализировано! Причина: " + var3.getMessage());
            var3.printStackTrace();
         } catch (Exception var4) {
            Bukkit.getLogger().severe("Неизвестная ошибка при инициализации DecentHolograms API: " + var4.getMessage());
            var4.printStackTrace();
         }
      } else {
         Bukkit.getLogger().warning("DecentHolograms не найден или не загружен!");
      }

      return false;
   }
}
