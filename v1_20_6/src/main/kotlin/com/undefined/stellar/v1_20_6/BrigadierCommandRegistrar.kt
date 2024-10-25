package com.undefined.stellar.v1_20_6

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.undefined.stellar.BaseStellarCommand
import net.minecraft.commands.CommandSourceStack
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.CraftServer

object BrigadierCommandRegistrar {

    fun register(stellarCommand: BaseStellarCommand) {
        stellarCommand.aliases.plus(stellarCommand.name).forEach { alias ->
            val mainArgumentBuilder = LiteralArgumentBuilder.literal<CommandSourceStack>(alias)
            mainArgumentBuilder.handleCommand(stellarCommand)
            getCommandDispatcher().register(mainArgumentBuilder)
        }
    }

    private fun LiteralArgumentBuilder<CommandSourceStack>.handleCommand(stellarCommand: BaseStellarCommand) {
        handleSubCommands(stellarCommand)
        handleRequirements(stellarCommand)
        handleExecutions(stellarCommand)
    }

    private fun LiteralArgumentBuilder<CommandSourceStack>.handleAliases(stellarCommand: BaseStellarCommand) {
        stellarCommand.aliases.forEach { alias ->
            val childArgumentBuilder = LiteralArgumentBuilder.literal<CommandSourceStack>(alias)
            childArgumentBuilder.handleCommand(stellarCommand)
            this.then(childArgumentBuilder)
        }
    }

    private fun LiteralArgumentBuilder<CommandSourceStack>.handleSubCommands(stellarCommand: BaseStellarCommand) {
        stellarCommand.subCommands.forEach { subCommand ->
            val subCommandArgument = LiteralArgumentBuilder.literal<CommandSourceStack>(subCommand.name)
            subCommandArgument.handleCommand(subCommand)
            this.handleAliases(subCommand)
            then(subCommandArgument)
        }
    }

    private fun LiteralArgumentBuilder<CommandSourceStack>.handleRequirements(command: BaseStellarCommand) =
        requires { context -> command.requirements.all { it.run(context.bukkitSender) } }

    private fun LiteralArgumentBuilder<CommandSourceStack>.handleExecutions(command: BaseStellarCommand) =
        executes { context ->
            command.executions.forEach { it.run(context.source.bukkitSender) }
            return@executes 1
        }

    private fun getCommandDispatcher(): CommandDispatcher<CommandSourceStack> = (Bukkit.getServer() as CraftServer).server.functions.dispatcher

}