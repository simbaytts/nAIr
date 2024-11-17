package wtf.n1zamu.nair.config;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import wtf.n1zamu.nair.commands.AirDropCreator;

public class Config {
   public static final String PATH = "plugins/nAir/";

   public static void runData() {
      File configFile = new File("plugins/nAir/", "airdrop.yml");
      FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
      AirDropCreator.LEGENDARY = fileConfiguration.getList("LEGENDARY.items");
      AirDropCreator.MYTHIC = fileConfiguration.getList("MYTHIC.items");
      AirDropCreator.EPIC = fileConfiguration.getList("EPIC.items");
      AirDropCreator.RARE = fileConfiguration.getList("RARE.items");
      AirDropCreator.COMMON = (List)fileConfiguration.get("COMMON.items");
   }

   public static void writeData() throws IOException {
      File configFile = new File("plugins/nAir/", "airdrop.yml");
      FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
      fileConfiguration.set("LEGENDARY.items", AirDropCreator.LEGENDARY);
      fileConfiguration.set("MYTHIC.items", AirDropCreator.MYTHIC);
      fileConfiguration.set("EPIC.items", AirDropCreator.EPIC);
      fileConfiguration.set("RARE.items", AirDropCreator.RARE);
      fileConfiguration.set("COMMON.items", AirDropCreator.COMMON);
      fileConfiguration.save(configFile);
   }
}
