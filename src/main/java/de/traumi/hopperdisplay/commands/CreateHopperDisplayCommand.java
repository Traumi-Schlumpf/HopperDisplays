package de.traumi.hopperdisplay.commands;

import de.traumi.hopperdisplay.Hopperdisplay;
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

    private final Hopperdisplay plugin;

    public CreateHopperDisplayCommand(Hopperdisplay plugin) {
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
        try {maxAmount = Integer.parseInt(args[0]);}
        catch (NumberFormatException e) {
            sender.sendMessage(Component.text("Du musst eine Zahl angeben.").color(NamedTextColor.RED));
            return true;
        }

        if (maxAmount < 1) {
            sender.sendMessage(Component.text("Die Zahl muss mindestens 1 sein.").color(NamedTextColor.RED));
            return true;
        }

        Location playerpos = player.getLocation();

        FileConfiguration config =  plugin.getConfig();
        config.set("display.x", playerpos.getBlockX());
        config.set("display.y", playerpos.getBlockY());
        config.set("display.z", playerpos.getBlockZ());
        config.set("display.world", playerpos.getWorld().getName());
        config.set("display.maxamount", maxAmount);
        if(!config.contains("display.amount")) {
            config.set("display.amount", 0);
        }
        config.set("display.owner", player.getName());
        this.plugin.saveConfig();
        Hopperdisplay.setDisplay();
        player.sendMessage(Component.text("Du hast den Trichter erfolgereich ausgewählt.").color(NamedTextColor.GREEN));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
