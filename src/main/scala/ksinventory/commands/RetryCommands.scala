package ksinventory.commands

import ksinventory.KSInventory
import ksinventory.cache.{EndChestCache, PlayerWorldDataCache, PlayerWorldInventoryCache, RetryCache}
import ksinventory.services.{DataService, EndChestInventoryService, InventoryService}
import ksinventory.utils.Utils
import org.bukkit.ChatColor
import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.entity.Player

class RetryCommands(plugin: KSInventory) extends CommandExecutor {
  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    sender match {
      case player: Player =>
        if(command.getName.equals("retry")){
          if(args.length != 2){
            sender.sendMessage(ChatColor.RED + "Usage: /retry [saveType] [worldName]")
            sender.sendMessage(ChatColor.RED + "See /inv help for saveTypes and worldNames")
          }
          else{
            if(!Utils.getAllWorldNames.contains(args(1))){
              sender.sendMessage(ChatColor.RED + "Invalid worldName. See /inv help")
            }
            else{
              if(args(0).equals("data")){
                val time = RetryCache.getRetryRefined(player.getUniqueId, args(1), "data")
                if(time == -1){
                  player.sendMessage(ChatColor.RED + "Nothing to retry")
                }
                else if(time > System.currentTimeMillis()){
                  player.sendMessage(ChatColor.RED + "You can only retry a failed save once every minute")
                }
                else{
                  if(RetryCache.getRetryRequestRefined(player.getUniqueId, args(1), "data")){
                    player.sendMessage(ChatColor.RED + "Save retry request already in progress...")
                  }
                  else{
                    val playerData = PlayerWorldDataCache.getPlayerData(player.getUniqueId, args(1))
                    player.sendMessage(ChatColor.GRAY + "Attempting to retry saving player data for world(s) starting with " + args(1))
                    RetryCache.setRetryRequestRefined(player.getUniqueId, args(1), "data")
                    DataService.persistPlayerData(player.getUniqueId, args(1))
                  }
                }
              }
              else if(args(0).equals("inv")){
                val time = RetryCache.getRetryRefined(player.getUniqueId, args(1), "inv")
                if(time == -1){
                  player.sendMessage(ChatColor.RED + "Nothing to retry")
                }
                else if(time > System.currentTimeMillis()){
                  player.sendMessage(ChatColor.RED + "You can only retry a failed save once every minute")
                }
                else{
                  if(RetryCache.getRetryRequestRefined(player.getUniqueId, args(1), "inv")){
                    player.sendMessage(ChatColor.RED + "Save retry request already in progress...")
                  }
                  else{
                    val playerInventory = PlayerWorldInventoryCache.getPlayerInventory(player.getUniqueId, args(1))
                    player.sendMessage(ChatColor.GRAY + "Attempting to retry saving player inventory for world(s) starting with " + args(1))
                    RetryCache.setRetryRequestRefined(player.getUniqueId, args(1), "inv")
                    InventoryService.persistPlayerInventory(player.getUniqueId, args(1))
                  }
                }
              }
              else if(args(0).equals("end")){
                val time = RetryCache.getRetryRefined(player.getUniqueId, args(1), "end")
                if(time == -1){
                  player.sendMessage(ChatColor.RED + "Nothing to retry")
                }
                else if(time > System.currentTimeMillis()){
                  player.sendMessage(ChatColor.RED + "You can only retry a failed save once every minute")
                }
                else{
                  if(RetryCache.getRetryRequestRefined(player.getUniqueId, args(1), "end")){
                    player.sendMessage(ChatColor.RED + "Save retry request already in progress...")
                  }
                  else{
                    val endInventory = EndChestCache.getPlayerEndInventory(player.getUniqueId, args(1))
                    player.sendMessage(ChatColor.GRAY + "Attempting to rety saving ender chest inventory for world(s) starting wtih " + args(1))
                    RetryCache.setRetryRequestRefined(player.getUniqueId, args(1), "end")
                    EndChestInventoryService.savePlayerEndInventory(player.getUniqueId, args(1))
                  }
                }
              }
              else{
                player.sendMessage(ChatColor.RED + "Invalid saveType. See /inv help")
              }
            }
          }
        }
      case _ => sender.sendMessage(ChatColor.RED + "Command can only be used by players")
    }
    true
  }
}