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
                val worldAndTime = RetryCache.getRetry(player.getUniqueId,"data")
                if(worldAndTime._2 == -1){
                  player.sendMessage(ChatColor.RED + "Nothing to retry.")
                }
                else if(worldAndTime._2 > System.currentTimeMillis()){
                  player.sendMessage(ChatColor.RED + "You can only retry a save once every minute")
                }
                else{
                  if(RetryCache.getRetryRequest(player.getUniqueId, "data")){
                    player.sendMessage(ChatColor.RED + "Save retry request already in progress...")
                  }
                  else {
                    val playerData = PlayerWorldDataCache.getPlayerData(player.getUniqueId, worldAndTime._1)
                    player.sendMessage(ChatColor.GRAY + "Attempting to retry saving player data for world(s) starting with " + worldAndTime._1)
                    RetryCache.setRetryRequest(player.getUniqueId, "data")
                    DataService.savePlayerData(player.getUniqueId, worldAndTime._1, playerData._1, playerData._2, playerData._3, playerData._4, playerData._5)
                  }
                }
              }
              else if(args(0).equals("inv")){
                val worldAndTime = RetryCache.getRetry(player.getUniqueId, "inv")
                if(worldAndTime._2 == -1){
                  player.sendMessage(ChatColor.RED + "Nothing to retry.")
                }
                else if(worldAndTime._2 > System.currentTimeMillis()){
                  player.sendMessage(ChatColor.RED + "You can only retry a save once every minute")
                }
                else{
                  //retry inv
                  if(RetryCache.getRetryRequest(player.getUniqueId, "inv")){
                    player.sendMessage(ChatColor.RED + "Save retry request already in progress...")
                  }
                  else {
                    val playerInventory = PlayerWorldInventoryCache.getPlayerInventory(player.getUniqueId, worldAndTime._1)
                    player.sendMessage(ChatColor.GRAY + "Attempting to retry saving player inventory for world(s) starting with " + worldAndTime._1)
                    RetryCache.setRetryRequest(player.getUniqueId, "inv")
                    InventoryService.savePlayerInventory(player.getUniqueId, worldAndTime._1, playerInventory)
                  }
                }
              }
              else if(args(0).equals("end")){
                //retry end
                val worldAndTime = RetryCache.getRetry(player.getUniqueId, "end")
                if(worldAndTime._2 == -1){
                  player.sendMessage(ChatColor.RED + "Nothing to retry.")
                }
                else if(worldAndTime._2 > System.currentTimeMillis()){
                  player.sendMessage(ChatColor.RED + "You can only retry a save once every minute")
                }
                else{
                  //retry end inv
                  if(RetryCache.getRetryRequest(player.getUniqueId, "end")){
                    player.sendMessage(ChatColor.RED + "Save retry request already in progress...")
                  }
                  else {
                    val endInventory = EndChestCache.getPlayerEndInventory(player.getUniqueId, worldAndTime._1)
                    player.sendMessage(ChatColor.GRAY + "Attempting to rety saving ender chest inventory for world(s) starting wtih " + worldAndTime._1)
                    RetryCache.setRetryRequest(player.getUniqueId, "end")
                    EndChestInventoryService.savePlayerEndInventory(player.getUniqueId, worldAndTime._1, endInventory)
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