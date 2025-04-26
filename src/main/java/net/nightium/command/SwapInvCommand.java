package net.nightium.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.nightium.manager.SwapManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.*;

public class SwapInvCommand {
    private static Text getTranslatedText(String key, Object... args) {
        return Text.translatable("swapinventory.command." + key, args);
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("swapinv")
                .then(literal("start")
                        .executes(context -> {
                            if (SwapManager.isActive()) {
                                context.getSource().sendError(getTranslatedText("start.failure.already_active"));
                                return 0;
                            }

                            if (SwapManager.start(context.getSource().getServer())) {
                                context.getSource().sendFeedback(() ->
                                        getTranslatedText("start.success", SwapManager.getIntervalSeconds()), false);
                                return 1;
                            } else {
                                context.getSource().sendError(getTranslatedText("start.failure.not_enough_players"));
                                return 0;
                            }
                        }))
                .then(literal("stop")
                        .executes(context -> {
                            if (!SwapManager.isActive()) {
                                context.getSource().sendError(getTranslatedText("stop.failure.not_active"));
                                return 0;
                            }

                            SwapManager.stop();
                            context.getSource().sendFeedback(() ->
                                    getTranslatedText("stop.success"), false);
                            return 1;
                        }))
                .then(literal("interval")
                        .then(argument("seconds", IntegerArgumentType.integer(1))
                                .executes(context -> {
                                    int seconds = IntegerArgumentType.getInteger(context, "seconds");
                                    if (SwapManager.setInterval(seconds)) {
                                        context.getSource().sendFeedback(() ->
                                                getTranslatedText("interval.success", seconds), false);
                                        return 1;
                                    } else {
                                        context.getSource().sendError(getTranslatedText("interval.failure"));
                                        return 0;
                                    }
                                }))
                ));
    }
}