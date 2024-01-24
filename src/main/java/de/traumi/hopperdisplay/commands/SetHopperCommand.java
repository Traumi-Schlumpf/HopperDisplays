package de.traumi.hopperdisplay.commands;


import de.traumi.hopperdisplay.Hopperdisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SetHopperCommand implements CommandExecutor, TabCompleter {
    private final Hopperdisplay plugin;

    public SetHopperCommand(Hopperdisplay plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Dieser Befehl kann nur von Spielern ausgeführt werden.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length == 0){
            player.sendMessage(Component.text("Du hast nicht das zu sammelnde Item angegeben.").color(NamedTextColor.RED));
            return true;
        }

        if(player.getTargetBlock(null, 5).getType() != Material.HOPPER){
            player.sendMessage(Component.text("Du schaust keinen Trichter an!").color(NamedTextColor.RED));
            return true;
        }
        Material itemMaterial = Material.getMaterial(args[0].toUpperCase());
        if (itemMaterial == null){
            player.sendMessage(Component.text("Nicht existierendes Material ausgewählt.").color(NamedTextColor.RED));
            return true;
        }

        FileConfiguration config =  plugin.getConfig();
        Block target = player.getTargetBlock(null, 5);
        Location targetLoc = target.getLocation();
        config.set("hopper.x", targetLoc.getBlockX());
        config.set("hopper.y", targetLoc.getBlockY());
        config.set("hopper.z", targetLoc.getBlockZ());
        config.set("hopper.world", targetLoc.getWorld().getName());
        config.set("hopper.material", args[0].toUpperCase().toString());
        this.plugin.saveConfig();
        player.sendMessage(Component.text("Du hast erfolgreich Trichter Ausgewählt.").color(NamedTextColor.GREEN));
        player.sendMessage(Component.text("X:" + config.getString("hopper.x") + " Y: " +  config.getString("hopper.y") + " Z: " +  config.getString("hopper.z") + " gesuchtes Item: " +  config.getString("hopper.material") + ".").color(NamedTextColor.GREEN));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return Arrays.stream(Material.values())
                .map(Enum::toString)
                .filter(material -> material.toLowerCase().startsWith(args[0].toLowerCase()))
                .toList();

        return new ArrayList<>();
    }
}
