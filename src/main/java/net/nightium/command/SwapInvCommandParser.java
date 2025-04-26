package net.nightium.command;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;

import java.util.concurrent.CompletableFuture;

public class SwapInvCommandParser {
    public static CompletableFuture<Suggestions> suggestCommands(SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(new String[]{
                "start",
                "stop",
                "interval"
        }, builder);
    }

    public static CompletableFuture<Suggestions> suggestInterval(SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(new String[]{
                "10",
                "30",
                "60",
                "120",
                "300"
        }, builder);
    }
}