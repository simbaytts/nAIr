package wtf.n1zamu.nair.bosses;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import wtf.n1zamu.nair.NAir;

import java.util.List;

public class CatBoss extends Bosses {
   public CatBoss() {
      super(NAir.config().getInt("catBossHP"), ChatColor.translateAlternateColorCodes('&', NAir.config().getString("catBossName")), "CatBoss", EntityType.OCELOT);
   }

   @Override
   public void spawn(Location location) {
      this.entity = (LivingEntity) location.getWorld().spawnEntity(location, this.type);
      this.entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 5000, 1));
      this.entity.setMaxHealth((double) this.health);
      this.entity.setHealth((double) this.health);
      this.entity.setCustomName(this.name);
      this.entity.setRemoveWhenFarAway(false);
      this.entity.setMetadata(this.mobId, new FixedMetadataValue(NAir.getInstance(), true));
      this.entity.setCustomNameVisible(true);
      this.entity.setAI(true);
      this.entity.setGlowing(true);

      // Добавление поведения атаки
      new BukkitRunnable() {
         @Override
         public void run() {
            if (entity == null || entity.isDead()) {
               this.cancel();
               return;
            }

            Player target = findNearestPlayer(location);
            if (target != null) {
               attackPlayer(target);
            }
         }
      }.runTaskTimer(NAir.getInstance(), 0L, 20L); // Каждую секунду (20 тиков)
   }

   private Player findNearestPlayer(Location location) {
      List<Player> players = location.getWorld().getPlayers();
      Player nearest = null;
      double nearestDistance = Double.MAX_VALUE;

      for (Player player : players) {
         double distance = player.getLocation().distance(location);
         if (distance < nearestDistance) {
            nearestDistance = distance;
            nearest = player;
         }
      }
      return nearest;
   }

   private void attackPlayer(Player player) {
      if (entity != null && entity instanceof LivingEntity) {
         LivingEntity livingEntity = (LivingEntity) entity;
         livingEntity.setTarget(player);
         player.damage(0.3, livingEntity); // Наносит 0.3 урона игроку
      }
   }
}
