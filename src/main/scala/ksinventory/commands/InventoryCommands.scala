package ksinventory.commands

import ksinventory.KSInventory
import ksinventory.cache.MessageSuppressionCache
import ksinventory.utils.Utils
import org.bukkit.ChatColor
import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.entity.Player

class InventoryCommands(plugin: KSInventory) extends CommandExecutor{
  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    sender match {
      case player: Player =>
        if(command.getName.equals("inv")){
          if(args.length == 1){
            if(args(0).equals("quiet") || args(0).equals("q")){
              if(MessageSuppressionCache.getPlayerSuppression(player.getUniqueId.toString)){
                player.sendMessage(ChatColor.GRAY + "You already have messages suppressed")
              }
              else {
                plugin.getConfig.set(player.getUniqueId.toString, true)
                MessageSuppressionCache.setSuppressedPlayer(player.getUniqueId.toString)
                player.sendMessage(ChatColor.GRAY + "Inventory messages suppressed. To re-enable, use /inv verbose")
                plugin.saveConfig()
              }
            }
            else if(args(0).equals("verbose") || args(0).equals("v")){
              if(!MessageSuppressionCache.getPlayerSuppression(player.getUniqueId.toString)){
                player.sendMessage(ChatColor.GRAY + "You are already recieving messages")
              }
              else {
                plugin.getConfig.set(player.getUniqueId.toString, null)
                MessageSuppressionCache.removeSuppressedPlayer(player.getUniqueId.toString)
                player.sendMessage(ChatColor.GRAY + "Inventory messages re-enabled. To suppress them, use /inv quiet")
                plugin.saveConfig()
              }
            }
            else if(args(0).equals("help")){
              val worldNames = Utils.getAllWorldNames.mkString(",")
              player.sendMessage(ChatColor.GRAY + "Available commands: /inv, /retrySave, /retryGet")
              player.sendMessage(ChatColor.GRAY + "types: inv, data, end")
              player.sendMessage(ChatColor.GRAY + "worldNames: " + worldNames)
            }
            else{
              player.sendMessage(ChatColor.RED + "Usage: /inv [help|quiet|q|verbose|v]")
            }
          }
          else{
            player.sendMessage(ChatColor.RED + "Usage: /inv [help|quiet|q|verbose|v]")
          }
          return true
        }
      case _ =>
        sender.sendMessage(ChatColor.RED + "Command can only be used by players")
        return true
    }
    false
  }
}
