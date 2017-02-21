package ksinventory.commands

import java.io.File

import ksinventory.KSInventory
import ksinventory.cache.{MessageSuppressionCache, PlayerWorldInventoryCache}
import org.bukkit.ChatColor
import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.entity.Player

class Commands(plugin: KSInventory) extends CommandExecutor{
  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    sender match {
      case player: Player =>
        if(command.getName.equals("inv")){
          if(args.length != 1){
            player.sendMessage(ChatColor.RED + "Usage: /inv [quiet|q|verbose|v|retry]")
            return true
          }
          else{
            if(args(0).equals("quiet") || args(0).equals("q")){
              plugin.getConfig.set(player.getUniqueId.toString, true)
              MessageSuppressionCache.setSuppressedPlayer(player.getUniqueId.toString)
              player.sendMessage(ChatColor.GRAY + "Inventory nessages suppressed. To re-enable, use /inv verbose")
              plugin.saveConfig()
              return true
            }
            else if(args(0).equals("verbose") || args(0).equals("v")){
              plugin.getConfig.set(player.getUniqueId.toString, null)
              MessageSuppressionCache.removeSuppressedPlayer(player.getUniqueId.toString)
              player.sendMessage(ChatColor.GRAY + "Inventory messages re-enabled. To suppress them, use /inv quiet")
              plugin.saveConfig()
              return true
            }
            else if(args(0).equals("retry")){
              return true
            }
            else{
              player.sendMessage(ChatColor.RED + "Usage: /inv [quiet|q|verbose|v|retry]")
              return true
            }
          }
        }
      case _ =>
        sender.sendMessage(ChatColor.RED + "Command can only be used by players")
        false
    }
    false
  }
}
