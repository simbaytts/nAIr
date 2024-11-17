package wtf.n1zamu.nair.bosses;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import wtf.n1zamu.nair.NAir;

public class SpiderBoss extends Bosses {
   public SpiderBoss() {
      super(NAir.config().getInt("spiderBossHP"), ChatColor.translateAlternateColorCodes('&', NAir.config().getString("spiderBossName")), "SpiderBoss", EntityType.SPIDER);
   }

   public void spawn(Location location) {
      this.entity = (LivingEntity)location.getWorld().spawnEntity(location, this.type);
      this.entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5000, 2));
      this.entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5000, 2));
      this.entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5000, 2));
      this.entity.setMaxHealth((double)this.health);
      this.entity.setHealth((double)this.health);
      this.entity.setCustomName(this.name);
      this.entity.setRemoveWhenFarAway(false);
      this.entity.setMetadata(this.mobId, new FixedMetadataValue(NAir.getInstance(), true));
      this.entity.setCustomNameVisible(true);
      this.entity.setAI(true);
      this.entity.setGlowing(true);
   }
}
