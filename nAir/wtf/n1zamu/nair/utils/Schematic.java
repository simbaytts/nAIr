package wtf.n1zamu.nair.utils;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.SideEffectSet;
import java.io.File;
import java.nio.file.Files;
import org.bukkit.Location;
import org.bukkit.World;

public class Schematic {
   public static EditSession sessionV;

   public static void loadSchematic(Location to, File schematicFile) {
      if (!schematicFile.exists()) {
         System.err.println("Файл схемы не найден: " + schematicFile);
      } else if (!schematicFile.canRead()) {
         System.err.println("Файл схемы нечитаем: " + schematicFile);
      } else {
         try {
            EditSession session = createEditSession(to.getWorld());

            label89: {
               try {
                  sessionV = session;
                  ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
                  if (format == null) {
                     System.err.println("Неподдерживаемый формат файла схемы: " + schematicFile);
                     break label89;
                  }

                  ClipboardReader reader = format.getReader(Files.newInputStream(schematicFile.toPath()));

                  try {
                     Clipboard schematic = reader.read();
                     Operation operation = (new ClipboardHolder(schematic)).createPaste(session).to(BukkitAdapter.asBlockVector(to)).build();
                     Operations.complete(operation);
                  } catch (Throwable var9) {
                     if (reader != null) {
                        try {
                           reader.close();
                        } catch (Throwable var8) {
                           var9.addSuppressed(var8);
                        }
                     }

                     throw var9;
                  }

                  if (reader != null) {
                     reader.close();
                  }
               } catch (Throwable var10) {
                  if (session != null) {
                     try {
                        session.close();
                     } catch (Throwable var7) {
                        var10.addSuppressed(var7);
                     }
                  }

                  throw var10;
               }

               if (session != null) {
                  session.close();
               }

               return;
            }

            if (session != null) {
               session.close();
            }

         } catch (Throwable var11) {
            var11.printStackTrace();
         }
      }
   }

   public static void removeSchematic() {
      sessionV.undo(sessionV);
   }

   public static EditSession createEditSession(World bukkitWorld) {
      EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(bukkitWorld));
      session.setSideEffectApplier(SideEffectSet.defaults());
      return session;
   }
}
