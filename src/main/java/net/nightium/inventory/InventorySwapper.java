package net.nightium.inventory;

import net.nightium.util.InventoryData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventorySwapper {
    private static Text getTranslatedText(String key) {
        return Text.translatable("swapinventory.message." + key);
    }

    public static void swapInventories(List<ServerPlayerEntity> players) {
        if (players.size() < 2) return;

        List<Pair<ServerPlayerEntity, InventoryData>> inventories = new ArrayList<>();
        for (ServerPlayerEntity player : players) {
            inventories.add(Pair.of(player, new InventoryData(player)));
        }

        Collections.shuffle(inventories);

        for (int i = 0; i < inventories.size(); i++) {
            int nextIndex = (i + 1) % inventories.size();
            inventories.get(nextIndex).getRight().applyTo(inventories.get(i).getLeft());
        }

        Text message = getTranslatedText("swapped");
        for (ServerPlayerEntity player : players) {
            player.sendMessage(message, false);
        }
    }
}