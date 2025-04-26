package net.nightium.util;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;

public class InventoryData {
    private final Inventory inventory;
    private final net.minecraft.item.ItemStack[] armor;
    private final net.minecraft.item.ItemStack offHand;
    private final int selectedSlot;

    public InventoryData(ServerPlayerEntity player) {
        this.inventory = player.getInventory();
        this.armor = new net.minecraft.item.ItemStack[4];
        for (int i = 0; i < armor.length; i++) {
            this.armor[i] = player.getInventory().getArmorStack(i).copy();
        }
        this.offHand = player.getInventory().offHand.get(0).copy();
        this.selectedSlot = player.getInventory().selectedSlot;
    }

    public void applyTo(ServerPlayerEntity player) {
        Inventory targetInv = player.getInventory();

        for (int i = 0; i < targetInv.size(); i++) {
            targetInv.setStack(i, inventory.getStack(i).copy());
        }

        for (int i = 0; i < armor.length; i++) {
            player.getInventory().setStack(36 + i, armor[i].copy());
        }

        player.getInventory().offHand.set(0, offHand.copy());
        player.getInventory().selectedSlot = selectedSlot;
    }
}