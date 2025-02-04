package me.fishyhyper.smpSaleTest;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class ParticleSpiralPlugin extends JavaPlugin implements Listener {
    private HashMap<UUID, String> playerColors = new HashMap<>();
    private Random random = new Random();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        startParticleTask();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String colorCode = "§" + (random.nextInt(8) + 1);
        playerColors.put(player.getUniqueId(), colorCode);
    }

    private void startParticleTask() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (playerColors.containsKey(player.getUniqueId())) {
                    spawnParticleSpiral(player, playerColors.get(player.getUniqueId()));
                }
            }
        }, 0L, 2L);
    }

    private void spawnParticleSpiral(Player player, String colorCode) {
        double radius = 1.5;
        double time = (System.currentTimeMillis() % 2000) / 2000.0 * Math.PI * 2;

        for (double y = 0; y < 2; y += 0.1) {
            double x = Math.cos(time + y * 5) * radius;
            double z = Math.sin(time + y * 5) * radius;

            Particle.DustOptions dustOptions = new Particle.DustOptions(getColor(colorCode), 1);
            player.getWorld().spawnParticle(
                    Particle.DUST,
                    player.getLocation().add(x, y, z),
                    1,
                    0, 0, 0,
                    0,
                    dustOptions
            );
        }
    }

    private Color getColor(String code) {
        return switch (code) {
            case "§1" -> Color.fromRGB(0, 0, 170);
            case "§2" -> Color.fromRGB(0, 170, 0);
            case "§3" -> Color.fromRGB(0, 170, 170);
            case "§4" -> Color.fromRGB(170, 0, 0);
            case "§5" -> Color.fromRGB(170, 0, 170);
            case "§6" -> Color.fromRGB(255, 170, 0);
            case "§7" -> Color.fromRGB(170, 170, 170);
            case "§8" -> Color.fromRGB(85, 85, 85);
            default -> Color.WHITE;
        };
    }
}