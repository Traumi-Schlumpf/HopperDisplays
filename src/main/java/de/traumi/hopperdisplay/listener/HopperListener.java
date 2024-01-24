package de.traumi.hopperdisplay.listener;


import de.traumi.hopperdisplay.Hopperdisplay;
import de.traumi.hopperdisplay.commands.CreateHopperDisplayCommand;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class HopperListener implements Listener {
    private final Hopperdisplay plugin;
    private CreateHopperDisplayCommand createhopperdisplayCommand;


    public HopperListener(Hopperdisplay plugin) {
        this.plugin = plugin;
        this.createhopperdisplayCommand = new CreateHopperDisplayCommand(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onHopperCollect(InventoryPickupItemEvent event) {
        Location loc = event.getInventory().getLocation();
        if (event.getInventory().getType() == InventoryType.HOPPER &&
                loc != null &&
                loc.getBlockX() == this.plugin.getConfig().getInt("hopper.x") &&
                loc.getBlockY() == this.plugin.getConfig().getInt("hopper.y") &&
                loc.getBlockZ() == this.plugin.getConfig().getInt("hopper.z") &&
                loc.getWorld().getName().equals(this.plugin.getConfig().getString("hopper.world"))
        ) {
            String mat = this.plugin.getConfig().getString("hopper.material");
            if (mat == null) return;

            Material material = Material.getMaterial(mat);

            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                int itemCount = 0;
                for (int i = 0; i < 5; ++i) {
                    ItemStack item = event.getInventory().getItem(i);
                    if (item != null && item.getType() == material) {
                        itemCount += item.getAmount();
                    }
                }
                this.plugin.getConfig().set("display.amount", itemCount);
                this.plugin.saveConfig();
                Hopperdisplay.setDisplay();
            }, 1);
        }
    }
}
