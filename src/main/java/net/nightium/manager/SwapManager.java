package net.nightium.manager;

import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.nightium.inventory.InventorySwapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SwapManager {
    private static final AtomicBoolean isActive = new AtomicBoolean(false);
    private static final AtomicInteger timer = new AtomicInteger(0);
    private static int interval = 300;
    private static ServerBossBar bossBar;
    private static List<ServerPlayerEntity> players = new ArrayList<>();

    static {
        bossBar = new ServerBossBar(
                Text.translatable("swapinventory.bossbar.countdown", 0),
                BossBar.Color.RED,
                BossBar.Style.PROGRESS
        );
    }

    private static Text getTranslatedText(String key, Object... args) {
        return Text.translatable("swapinventory." + key, args);
    }

    public static boolean start(MinecraftServer server) {
        if (isActive.get()) return false;

        players = server.getPlayerManager().getPlayerList();
        if (players.size() < 2) return false;

        isActive.set(true);
        timer.set(0);
        bossBar.setVisible(true);
        bossBar.setName(getTranslatedText("bossbar.countdown", getIntervalSeconds()));
        players.forEach(bossBar::addPlayer);
        return true;
    }

    public static boolean stop() {
        if (!isActive.get()) return false;

        isActive.set(false);
        bossBar.setVisible(false);
        return true;
    }

    public static void tick() {
        if (!isActive.get()) return;

        if (timer.incrementAndGet() >= interval) {
            InventorySwapper.swapInventories(players);
            timer.set(0);
        }
        updateBossBar();
    }

    public static boolean setInterval(int seconds) {
        if (seconds <= 0) return false;
        interval = seconds * 20;
        return true;
    }

    public static int getIntervalSeconds() {
        return interval / 20;
    }

    public static boolean isActive() {
        return isActive.get();
    }

    private static void updateBossBar() {
        int remaining = interval - timer.get();
        bossBar.setName(getTranslatedText("bossbar.countdown", remaining / 20));
        bossBar.setPercent((float) remaining / interval);
    }
}