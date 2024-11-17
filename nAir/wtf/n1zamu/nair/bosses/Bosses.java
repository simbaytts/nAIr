package wtf.n1zamu.nair.bosses;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public abstract class Bosses {
   public String mobId;
   public EntityType type;
   public LivingEntity entity;
   public String name;
   public int health;

   public Bosses(int health, String name, String mobId, EntityType type) {
      this.health = health;
      this.name = name;
      this.mobId = mobId;
      this.type = type;
   }

   public abstract void spawn(Location var1);
}
