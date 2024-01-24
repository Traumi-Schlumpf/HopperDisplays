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
    private static String displayworld = null;
    private static Integer displayx = null;
    private static Integer displayy = null;
    private static Integer displayz = null;
    private static Integer displayamount = null;
    private static Integer displaymaxamount = null;
    private static HolographicDisplaysAPI api = null;
    private static Location displayposition = null;
    private static Hologram display = null;
    private final List<String> registeredCommands = new ArrayList<>();

    @Override
    public void onEnable() {
        plugin = this;
        config = plugin.getConfig();
        displayworld = config.getString("display.world");
        displayx = config.getInt("display.x");
        displayy = config.getInt("display.y");
        displayz = config.getInt("display.z");
        displayamount = config.getInt("display.amount");
        displaymaxamount = config.getInt("display.maxamount");
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
            Integer displayx = config.getInt("display.x");
            Integer displayy = config.getInt("display.y");
            Integer displayz = config.getInt("display.z");
            displayposition = new Location(Bukkit.getWorld(displayworld), displayx, displayy, displayz);
            if(display==null){
                api = HolographicDisplaysAPI.get(plugin);
                display = api.createHologram(displayposition);
            }else{
                display.getLines().clear();
                display.setPosition(displayposition);
            }
            display.setPosition(displayposition);
            String displayworld = config.getString("display.world");

            Integer displayamount = config.getInt("display.amount");
            Integer displaymaxamount = config.getInt("display.maxamount");
            String material = config.getString("hopper.material");


            TextHologramLine ersteZeile = display.getLines().appendText(displayamount + " von " + displaymaxamount);
            TextHologramLine  zweiteZeile = display.getLines().appendText("Gesuchtes Item: " + ChatColor.AQUA + material.toLowerCase());
        }
    }

    @Override
    public void onDisable() {
        unregisterCommands();
    }
}

