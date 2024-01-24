package de.traumi.hopperdisplay.commands;

import de.traumi.hopperdisplay.Hopperdisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RemoveHopperCommand implements CommandExecutor, TabCompleter {

    private final Hopperdisplay plugin;

    public RemoveHopperCommand(Hopperdisplay plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        FileConfiguration config =  plugin.getConfig();
        config.set("hopper.x", null);
        config.set("hopper.y", null);
        config.set("hopper.z", null);
        config.set("hopper.world", null);
        config.set("hopper.material", null);
        config.set("display.amount", 0);
        this.plugin.saveConfig();
        sender.sendMessage(Component.text("Du hast erfolgreich die Trichterverbindung gelöscht!").color(NamedTextColor.GREEN));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
