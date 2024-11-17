package wtf.n1zamu.nair.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import wtf.n1zamu.nair.NAir;
import wtf.n1zamu.nair.config.Config;

public class AirDropCreator implements Listener {
   public static List<ItemStack> LEGENDARY = new ArrayList();
   public static List<ItemStack> MYTHIC = new ArrayList();
   public static List<ItemStack> EPIC = new ArrayList();
   public static List<ItemStack> RARE = new ArrayList();
   public static List<ItemStack> COMMON = new ArrayList();

   @EventHandler
   public void onInventoryClick(InventoryClickEvent event) {
      if (event.getWhoClicked().hasPermission("nAir.create")) {
         ItemStack clickedItem = event.getCurrentItem();
         if (clickedItem != null && clickedItem.getType() != Material.AIR) {
            try {
               String title = event.getView().getTitle();
               boolean editMode = CreateCommand.edit;
               byte var6 = -1;
               switch(title.hashCode()) {
               case -2005755334:
                  if (title.equals("MYTHIC")) {
                     var6 = 1;
                  }
                  break;
               case 2134789:
                  if (title.equals("EPIC")) {
                     var6 = 2;
                  }
                  break;
               case 2507938:
                  if (title.equals("RARE")) {
                     var6 = 3;
                  }
                  break;
               case 705031963:
                  if (title.equals("LEGENDARY")) {
                     var6 = 0;
                  }
                  break;
               case 1993481707:
                  if (title.equals("COMMON")) {
                     var6 = 4;
                  }
               }

               switch(var6) {
               case 0:
                  if (editMode) {
                     LEGENDARY.remove(clickedItem);
                     CreateCommand.airInventory.removeItem(new ItemStack[]{clickedItem});
                     event.setCancelled(true);
                     event.getWhoClicked().sendMessage("Предмет удален успешно!");
                  } else {
                     LEGENDARY.add(clickedItem);
                     CreateCommand.airInventory.addItem(new ItemStack[]{clickedItem});
                     event.setCancelled(true);
                     event.getWhoClicked().sendMessage("Предмет добавлен успешно!");
                  }

                  Config.writeData();
                  event.setCancelled(true);
                  break;
               case 1:
                  if (editMode) {
                     MYTHIC.remove(clickedItem);
                     CreateCommand.airInventory.removeItem(new ItemStack[]{clickedItem});
                     event.setCancelled(true);
                     event.getWhoClicked().sendMessage("Предмет удален успешно!");
                  } else {
                     MYTHIC.add(clickedItem);
                     CreateCommand.airInventory.addItem(new ItemStack[]{clickedItem});
                     event.setCancelled(true);
                     event.getWhoClicked().sendMessage("Предмет добавлен успешно!");
                  }

                  Config.writeData();
                  event.setCancelled(true);
                  break;
               case 2:
                  if (editMode) {
                     EPIC.remove(clickedItem);
                     CreateCommand.airInventory.removeItem(new ItemStack[]{clickedItem});
                     event.setCancelled(true);
                     event.getWhoClicked().sendMessage("Предмет удален успешно!");
                  } else {
                     EPIC.add(clickedItem);
                     CreateCommand.airInventory.addItem(new ItemStack[]{clickedItem});
                     event.setCancelled(true);
                     event.getWhoClicked().sendMessage("Предмет добавлен успешно!");
                  }

                  Config.writeData();
                  event.setCancelled(true);
                  break;
               case 3:
                  if (editMode) {
                     RARE.remove(clickedItem);
                     CreateCommand.airInventory.removeItem(new ItemStack[]{clickedItem});
                     event.getWhoClicked().sendMessage("Предмет удален успешно!");
                  } else {
                     RARE.add(clickedItem);
                     CreateCommand.airInventory.addItem(new ItemStack[]{clickedItem});
                     event.getWhoClicked().sendMessage("Предмет добавлен успешно!");
                  }

                  Config.writeData();
                  event.setCancelled(true);
                  break;
               case 4:
                  if (editMode) {
                     COMMON.remove(clickedItem);
                     CreateCommand.airInventory.removeItem(new ItemStack[]{clickedItem});
                     event.getWhoClicked().sendMessage("Предмет удален успешно!");
                  } else {
                     COMMON.add(clickedItem);
                     CreateCommand.airInventory.addItem(new ItemStack[]{clickedItem});
                     event.getWhoClicked().sendMessage("Предмет добавлен успешно!");
                  }

                  event.setCancelled(true);
                  Config.writeData();
               }
            } catch (IOException var7) {
               var7.printStackTrace();
            }

         }
      }
   }

   public static void blowItems(Location location, List<ItemStack> items) {
      BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
      if (!items.isEmpty()) {
         List<ItemStack> itemsToDrop = new ArrayList(items);
         Collections.shuffle(itemsToDrop);
         int itemsToDropCount = Math.min(NAir.config().getInt("dropItems"), itemsToDrop.size());

         for(int i = 0; i < itemsToDropCount; ++i) {
            scheduler.runTaskLater(NAir.getPlugin(NAir.class), () -> {
               double angle = ThreadLocalRandom.current().nextDouble() * 3.141592653589793D * 2.0D;
               double radius = ThreadLocalRandom.current().nextDouble() * 10.0D;
               double x = location.getX() + radius * Math.cos(angle);
               double z = location.getZ() + radius * Math.sin(angle);
               double y = location.getY() + 10.0D;
               Location newItemLocation = new Location(location.getWorld(), x, y, z);
               location.getWorld().playSound(location, Sound.ENTITY_ITEM_PICKUP, 1.0F, 1.0F);
               Item droppedItem = location.getWorld().dropItem(newItemLocation, (ItemStack)itemsToDrop.get(i));
               if (NAir.config().getBoolean("glowItems")) {
                  droppedItem.setGlowing(true);
               }

            }, 5L * (long)(i + 1));
         }
      }

   }
}
