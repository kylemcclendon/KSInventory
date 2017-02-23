package ksinventory.commands

import ksinventory.KSInventory
import ksinventory.cache.{EndChestCache, MessageSuppressionCache, PlayerWorldDataCache, PlayerWorldInventoryCache}
import ksinventory.services.{DataService, EndChestInventoryService, InventoryService}
import org.bukkit.ChatColor
import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.entity.Player

class Commands(plugin: KSInventory) extends CommandExecutor{
  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    sender match {
      case player: Player =>
        if(command.getName.equals("inv")){
          if(args.length == 1){
            if(args(0).equals("quiet") || args(0).equals("q")){
              plugin.getConfig.set(player.getUniqueId.toString, true)
              MessageSuppressionCache.setSuppressedPlayer(player.getUniqueId.toString)
              player.sendMessage(ChatColor.GRAY + "Inventory nessages suppressed. To re-enable, use /inv verbose")
              plugin.saveConfig()
            }
            else if(args(0).equals("verbose") || args(0).equals("v")){
              plugin.getConfig.set(player.getUniqueId.toString, null)
              MessageSuppressionCache.removeSuppressedPlayer(player.getUniqueId.toString)
              player.sendMessage(ChatColor.GRAY + "Inventory messages re-enabled. To suppress them, use /inv quiet")
              plugin.saveConfig()
            }
            else if(args(0).equals("retry")){
              player.sendMessage(ChatColor.RED + "You must provide the item you want to retry (inv, data, end).")
            }
            else{
              player.sendMessage(ChatColor.RED + "Usage: /inv [quiet|q|verbose|v] or /inv [retry inv|data|end]")
            }
          }
          else if(args.length == 2){
            if(args(0).equals("retry")){
              if(args(1).equals("data")){
                val worldAndTime = PlayerWorldDataCache.getPlayerDataRetry(player.getUniqueId)
                if(worldAndTime._2 == -1){
                  player.sendMessage(ChatColor.RED + "Nothing to retry.")
                }
                else if(worldAndTime._2 > System.currentTimeMillis()){
                  player.sendMessage(ChatColor.RED + "You can only retry a save once every minute")
                }
                else{
                  if(PlayerWorldDataCache.getRetryRequest(player.getUniqueId)){
                    player.sendMessage(ChatColor.RED + "Save retry request already in progress...")
                  }
                  else {
                    val playerData = PlayerWorldDataCache.getPlayerData(player.getUniqueId, worldAndTime._1)
                    player.sendMessage(ChatColor.GRAY + "Attempting to retry saving player data for world(s) starting with " + worldAndTime._1)
                    PlayerWorldDataCache.setRetryRequest(player.getUniqueId)
                    DataService.savePlayerData(player.getUniqueId, worldAndTime._1, playerData._1, playerData._2, playerData._3, playerData._4, playerData._5)
                  }
                }
              }
              else if(args(1).equals("inv")){
                val worldAndTime = PlayerWorldInventoryCache.getPlayerInventoryRetry(player.getUniqueId)
                if(worldAndTime._2 == -1){
                  player.sendMessage(ChatColor.RED + "Nothing to retry.")
                }
                else if(worldAndTime._2 > System.currentTimeMillis()){
                  player.sendMessage(ChatColor.RED + "You can only retry a save once every minute")
                }
                else{
                  //retry inv
                  if(PlayerWorldInventoryCache.getPlayerInventoryRetryRequest(player.getUniqueId)){
                    player.sendMessage(ChatColor.RED + "Save retry request already in progress...")
                  }
                  else {
                    val playerInventory = PlayerWorldInventoryCache.getPlayerInventory(player.getUniqueId, worldAndTime._1)
                    player.sendMessage(ChatColor.GRAY + "Attempting to retry saving player inventory for world(s) starting with " + worldAndTime._1)
                    PlayerWorldInventoryCache.setRetryRequest(player.getUniqueId)
                    InventoryService.savePlayerInventory(player.getUniqueId, worldAndTime._1, playerInventory)
                  }
                }
              }
              else if(args(1).equals("end")){
                //retry end
                val worldAndTime = EndChestCache.getEndRetry(player.getUniqueId)
                if(worldAndTime._2 == -1){
                  player.sendMessage(ChatColor.RED + "Nothing to retry.")
                }
                else if(worldAndTime._2 > System.currentTimeMillis()){
                  player.sendMessage(ChatColor.RED + "You can only retry a save once every minute")
                }
                else{
                  //retry end inv
                  if(EndChestCache.getRetryRequest(player.getUniqueId)){
                    player.sendMessage(ChatColor.RED + "Save retry request already in progress...")
                  }
                  else {
                    val endInventory = EndChestCache.getPlayerEndInventory(player.getUniqueId, worldAndTime._1)
                    player.sendMessage(ChatColor.GRAY + "Attempting to rety saving ender chest inventory for world(s) starting wtih " + worldAndTime._1)
                    EndChestCache.setRetryRequest(player.getUniqueId)
                    EndChestInventoryService.savePlayerEndInventory(player.getUniqueId, worldAndTime._1, endInventory)
                  }
                }
              }
              else{
                player.sendMessage(ChatColor.RED + "Usage: /inv retry [inv|data|end]")
              }
            }
            else{
              player.sendMessage(ChatColor.RED + "Usage: /inv [quiet|q|verbose|v] or /inv [retry inv|data|end]")
            }
          }
          else{
            player.sendMessage(ChatColor.RED + "Usage: /inv [quiet|q|verbose|v] or /inv [retry inv|data|end]")
          }
          true
        }
      case _ =>
        sender.sendMessage(ChatColor.RED + "Command can only be used by players")
        false
    }
    false
  }
}
