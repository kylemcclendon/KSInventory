package ksinventory.commands

import ksinventory.KSInventory
import ksinventory.cache.RetrieveRetryCache
import ksinventory.services.{DataService, EndChestInventoryService, InventoryService}
import ksinventory.utils.Utils
import org.bukkit.ChatColor
import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.entity.Player

class RetrieveRetryCommands extends CommandExecutor{
  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    sender match {
      case player: Player =>
        if(command.getName.equals("retryGet")){
          if(args.length != 2){
            sender.sendMessage(ChatColor.RED + "Usage: /retryGet [type] [worldName]")
            sender.sendMessage(ChatColor.RED + "See /inv help for types and worldNames")
          }
          else{
            if(!Utils.getAllWorldNames.contains(args(1))){
              sender.sendMessage(ChatColor.RED + "Invalid worldName. See /inv help")
            }
            else{
              if(args(0).equals("data")){
                val time = RetrieveRetryCache.getRetryRefined(player.getUniqueId, args(1), "data")
                if(time == -1){
                  player.sendMessage(ChatColor.RED + "Nothing to retry")
                }
                else if(time > System.currentTimeMillis()){
                  player.sendMessage(ChatColor.RED + "You can only retry a failed retrieve once every minute")
                }
                else{
                  if(RetrieveRetryCache.getRetryRequestRefined(player.getUniqueId, args(1), "data")){
                    player.sendMessage(ChatColor.RED + "Retrieve retry request already in progress...")
                  }
                  else{
                    player.sendMessage(ChatColor.GRAY + "Attempting to retry retrieving player data for world(s) starting with " + args(1))
                    RetrieveRetryCache.setRetryRequestRefined(player.getUniqueId, args(1), "data")
                    DataService.setPlayerWorldData(player.getUniqueId, args(1))
                  }
                }
              }
              else if(args(0).equals("inv")){
                val time = RetrieveRetryCache.getRetryRefined(player.getUniqueId, args(1), "inv")
                if(time == -1){
                  player.sendMessage(ChatColor.RED + "Nothing to retry")
                }
                else if(time > System.currentTimeMillis()){
                  player.sendMessage(ChatColor.RED + "You can only retry a failed retrieve once every minute")
                }
                else{
                  if(RetrieveRetryCache.getRetryRequestRefined(player.getUniqueId, args(1), "inv")){
                    player.sendMessage(ChatColor.RED + "Retrieve retry request already in progress...")
                  }
                  else{
                    player.sendMessage(ChatColor.GRAY + "Attempting to retry retrieving player inventory for world(s) starting with " + args(1))
                    RetrieveRetryCache.setRetryRequestRefined(player.getUniqueId, args(1), "inv")
                    InventoryService.setPlayerWorldInventory(player.getUniqueId, args(1))
                  }
                }
              }
              else if(args(0).equals("end")){
                val time = RetrieveRetryCache.getRetryRefined(player.getUniqueId, args(1), "end")
                if(time == -1){
                  player.sendMessage(ChatColor.RED + "Nothing to retry")
                }
                else if(time > System.currentTimeMillis()){
                  player.sendMessage(ChatColor.RED + "You can only retry a failed retrieve once every minute")
                }
                else{
                  if(RetrieveRetryCache.getRetryRequestRefined(player.getUniqueId, args(1), "end")){
                    player.sendMessage(ChatColor.RED + "Retrieve retry request already in progress...")
                  }
                  else{
                    player.sendMessage(ChatColor.GRAY + "Attempting to rety retrieving ender chest inventory for world(s) starting wtih " + args(1))
                    RetrieveRetryCache.setRetryRequestRefined(player.getUniqueId, args(1), "end")
                    EndChestInventoryService.setPlayerWorldEndInventory(player.getUniqueId, args(1))
                  }
                }
              }
              else{
                player.sendMessage(ChatColor.RED + "Invalid type. See /inv help")
              }
            }
          }
        }
      case _ => sender.sendMessage(ChatColor.RED + "Command can only be used by players")
    }
    true
  }
}
