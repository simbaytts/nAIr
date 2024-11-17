package wtf.n1zamu.nair.airdrop;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import wtf.n1zamu.nair.NAir;
import wtf.n1zamu.nair.commands.AirDropCreator;

public enum AirDropType {
   LEGENDARY(ChatColor.translateAlternateColorCodes('&', NAir.config().getString("airDrops.legendaryName")), NAir.config().getInt("airDrops.legendaryChance"), AirDropCreator.LEGENDARY),
   MYTHIC(ChatColor.translateAlternateColorCodes('&', NAir.config().getString("airDrops.mythicName")), NAir.config().getInt("airDrops.mythicChance"), AirDropCreator.MYTHIC),
   EPIC(ChatColor.translateAlternateColorCodes('&', NAir.config().getString("airDrops.epicName")), NAir.config().getInt("airDrops.epicChance"), AirDropCreator.EPIC),
   RARE(ChatColor.translateAlternateColorCodes('&', NAir.config().getString("airDrops.rareName")), NAir.config().getInt("airDrops.rareName"), AirDropCreator.RARE),
   COMMON(ChatColor.translateAlternateColorCodes('&', NAir.config().getString("airDrops.commonName")), NAir.config().getInt("airDrops.commonName"), AirDropCreator.COMMON);

   private String name;
   private List<ItemStack> items;
   private int chance;

   private AirDropType(String name, int chance, List<ItemStack> items) {
      this.name = name;
      this.items = items;
      this.chance = chance;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public List<ItemStack> getItems() {
      return this.items;
   }

   public void setItems(List<ItemStack> items) {
      this.items = items;
   }

   public float getChance() {
      return (float)this.chance;
   }

   public void setChance(int chance) {
      this.chance = chance;
   }

   // $FF: synthetic method
   private static AirDropType[] $values() {
      return new AirDropType[]{LEGENDARY, MYTHIC, EPIC, RARE, COMMON};
   }
}
