package wtf.n1zamu.nair.commands;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import wtf.n1zamu.nair.NAir;
import wtf.n1zamu.nair.airdrop.AirDrop;
import wtf.n1zamu.nair.airdrop.AirDropType;
import wtf.n1zamu.nair.listeners.BossListener;
import wtf.n1zamu.nair.utils.BossBarManager;
import wtf.n1zamu.nair.utils.Holograms;
import wtf.n1zamu.nair.utils.RegionHelper;
import wtf.n1zamu.nair.utils.Schematic;

public class CreateCommand implements CommandExecutor {
   public static Inventory airInventory;
   public static boolean edit = false;

   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
      Player player = (Player)commandSender;
      if (strings.length < 2) {
         return false;
      } else if (!commandSender.hasPermission("nAir.create")) {
         return false;
      } else {
         AirDropType airDropType;
         if (strings[0].equals("create")) {
            edit = false;
            airDropType = AirDropType.valueOf(strings[1]);
            airInventory = Bukkit.createInventory((InventoryHolder)null, 54, airDropType.toString());
            player.openInventory(airInventory);
            airInventory.addItem((ItemStack[])airDropType.getItems().toArray(new ItemStack[0]));
            this.sendMessage(commandSender, "Меню " + airDropType.getName() + " открыто!");
         }

         if (strings[0].equals("clear")) {
            airDropType = AirDropType.valueOf(strings[1]);
            airDropType.getItems().clear();
            this.sendMessage(commandSender, "Предметы типа " + airDropType.getName() + " очищены!");
         }

         if (strings[0].equals("edit")) {
            edit = true;
            airDropType = AirDropType.valueOf(strings[1]);
            airInventory = Bukkit.createInventory((InventoryHolder)null, 54, airDropType.toString());
            player.openInventory(airInventory);
            airInventory.addItem((ItemStack[])airDropType.getItems().toArray(new ItemStack[0]));
            this.sendMessage(commandSender, "Меню " + airDropType.getName() + " редактируется!");
         }

         if (strings[0].equals("start")) {
            this.reloadAir();
            airDropType = AirDropType.valueOf(strings[1]);
            AirDrop airDrop = new AirDrop();
            AirDrop.createAirDrop(airDropType);
         }

         return true;
      }
   }

   private void sendMessage(CommandSender commandSender, String text) {
      commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', text));
   }

   private void reloadAir() {
      Hologram[] holograms = new Hologram[]{AirDrop.airHologram, BossListener.bossHologram, BossBarManager.tickingHologram, BossBarManager.openHologram};
      AirDrop.airBlock = null;
      Schematic.removeSchematic();
      RegionHelper.removeRegion(AirDrop.regionName, Bukkit.getWorld(NAir.config().getString("worldName")));
      AirDrop.started = false;
      AirDrop.airBar.setVisible(false);
      BossListener.bossBar.setVisible(false);
      BossListener.spawned = false;
      if (BossListener.boss != null) {
         Iterator var2 = AirDrop.airBlock.getWorld().getEntities().iterator();

         while(var2.hasNext()) {
            Entity entity = (Entity)var2.next();
            if (entity.hasMetadata(BossListener.boss.mobId)) {
               entity.remove();
            }
         }
      }

      Hologram[] var6 = holograms;
      int var7 = holograms.length;

      for(int var4 = 0; var4 < var7; ++var4) {
         Hologram hologram = var6[var4];
         if (hologram != null) {
            Holograms.removeHologram(hologram);
         }
      }

   }
}
