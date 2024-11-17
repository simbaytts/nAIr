package wtf.n1zamu.nair.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import wtf.n1zamu.nair.airdrop.AirDrop;
import wtf.n1zamu.nair.utils.RegionHelper;

public class PlayerListener implements Listener {
   @EventHandler
   public void onJoin(PlayerJoinEvent event) {
      if (AirDrop.started) {
         Player player = event.getPlayer();
         AirDrop.airBar.addPlayer(player);
         if (BossListener.spawned) {
            BossListener.bossBar.addPlayer(player);
         }

      }
   }

   @EventHandler
   public void onMove(PlayerMoveEvent event) {
      Player player = event.getPlayer();
      if (!player.hasPermission("nAir.fly")) {
         if (AirDrop.started) {
            if (player.isFlying()) {
               if (RegionHelper.isInRegion(AirDrop.regionName, player.getLocation())) {
                  player.setFlying(false);
               }
            }
         }
      }
   }
}
