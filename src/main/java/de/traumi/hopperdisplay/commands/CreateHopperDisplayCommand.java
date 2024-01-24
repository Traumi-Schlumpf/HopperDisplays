package de.traumi.hopperdisplay.commands;

import de.traumi.hopperdisplay.HopperDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class CreateHopperDisplayCommand implements CommandExecutor, TabCompleter {

    private final HopperDisplay plugin;

    public CreateHopperDisplayCommand(HopperDisplay plugin) {
        this.plugin = plugin;
    }



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Dieser Befehl kann nur von Spielern ausgeführt werden.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length == 0){
            sender.sendMessage(Component.text("Du hast nicht die Anzahl angegeben.").color(NamedTextColor.RED));
            return true;
        }

        int maxAmount;
        try {
            maxAmount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("Du musst eine Zahl angeben.").color(NamedTextColor.RED));
            return true;
        }

        if (maxAmount < 1) {
            sender.sendMessage(Component.text("Die Zahl muss mindestens 1 sein.").color(NamedTextColor.RED));
            return true;
        }

        Location playerPos = player.getLocation();

        FileConfiguration config =  plugin.getConfig();
        config.set("display.x", playerPos.getBlockX());
        config.set("display.y", playerPos.getBlockY());
        config.set("display.z", playerPos.getBlockZ());
        config.set("display.world", playerPos.getWorld().getName());
        config.set("display.maxamount", maxAmount);
        if(!config.contains("display.amount")) {
            config.set("display.amount", 0);
        }
        config.set("display.owner", player.getName());
        this.plugin.saveConfig();
        HopperDisplay.setDisplay();
        player.sendMessage(Component.text("Du hast das Display erfolgreich erstellt.").color(NamedTextColor.GREEN));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
