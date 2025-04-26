package net.nightium;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.nightium.command.SwapInvCommand;

public class SwapInventory implements ModInitializer {
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			SwapInvCommand.register(dispatcher);
		});
	}
}