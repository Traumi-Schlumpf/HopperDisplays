package de.traumi.hopperdisplay;


import de.traumi.hopperdisplay.commands.CreateHopperDisplayCommand;
import de.traumi.hopperdisplay.commands.RemoveHopperCommand;
import de.traumi.hopperdisplay.commands.SetHopperCommand;
import de.traumi.hopperdisplay.listener.HopperListener;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.line.TextHologramLine;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;


public final class Hopperdisplay extends JavaPlugin {

    private static Hopperdisplay plugin;
    private static FileConfiguration config = null;
    private static String displayWorld = null;
    private static Integer displayX = null;
    private static Integer displayY = null;
    private static Integer displayZ = null;
    private static Integer displayAmount = null;
    private static Integer displayMaxAmount = null;
    private static HolographicDisplaysAPI api = null;
    private static Location displayPosition = null;
    private static Hologram display = null;
    private final List<String> registeredCommands = new ArrayList<>();

    @Override
    public void onEnable() {
        plugin = this;
        config = plugin.getConfig();
        displayWorld = config.getString("display.world");
        displayX = config.getInt("display.x");
        displayY = config.getInt("display.y");
        displayZ = config.getInt("display.z");
        displayAmount = config.getInt("display.amount");
        displayMaxAmount = config.getInt("display.maxamount");
        setDisplay();
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            getLogger().severe("*** HolographicDisplays is not installed or not enabled. ***");
            getLogger().severe("*** This plugin will be disabled. ***");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        this.registerCommand("sethopper", new SetHopperCommand(this));
        this.registerCommand("createhoppperdisplay", new CreateHopperDisplayCommand(this));
        this.registerCommand("removehopper", new RemoveHopperCommand(this));

        Bukkit.getPluginManager().registerEvents(new HopperListener(this), this);
    }

    private <T extends CommandExecutor & TabCompleter> void registerCommand(String command, T obj) {
        registeredCommands.add(command);
        PluginCommand pc = Bukkit.getPluginCommand(command);
        if (pc == null) return;
        pc.setExecutor(obj);
        pc.setTabCompleter(obj);
    }

    private void unregisterCommands() {
        for (String command : registeredCommands) {
            PluginCommand pc = Bukkit.getPluginCommand(command);
            if (pc == null) continue;
            pc.setExecutor(null);
            pc.setTabCompleter(null);
        }
    }


    public static void setDisplay(){
        if(config.contains("display.world") && config.contains("hopper.material") && config.contains("display.world")) {
            displayX = config.getInt("display.x");
            displayY = config.getInt("display.y");
            displayZ = config.getInt("display.z");
            displayPosition = new Location(Bukkit.getWorld(displayWorld), displayX, displayY, displayZ);
            if(display==null){
                api = HolographicDisplaysAPI.get(plugin);
                display = api.createHologram(displayPosition);
            }else{
                display.getLines().clear();
                display.setPosition(displayPosition);
            }
            display.setPosition(displayPosition);
            displayWorld = config.getString("display.world");

            displayAmount = config.getInt("display.amount");
            displayMaxAmount = config.getInt("display.maxamount");
            String material = config.getString("hopper.material");


            display.getLines().appendText(displayAmount + " von " + displayMaxAmount);
            display.getLines().appendText("Gesuchtes Item: " + ChatColor.AQUA + material.toLowerCase());
        }
    }

    @Override
    public void onDisable() {
        unregisterCommands();
    }
}

