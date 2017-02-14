package ksinventory.services

import java.util.UUID

import ksinventory.cache.PlayerWorldInventoryCache
import ksinventory.dao.{EndChestInventoryDao, InventoryDao, PlayerDataDao}
import ksinventory.utils.ItemStackConverter
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack

class InventoryService(inventoryDao: InventoryDao, playerDataDao: PlayerDataDao, endChestInventoryDao: EndChestInventoryDao) {

  // Player Inventory

  def setPlayerWorldInventory(playerId: UUID, worldId: UUID){
    if(PlayerWorldInventoryCache.playerInventoryContains(playerId,worldId)){
      Bukkit.getServer.getPlayer(playerId).getInventory.setContents(PlayerWorldInventoryCache.getPlayerInventory(playerId, worldId).toArray)
    }
    else{
      //Get From DB
      val result = inventoryDao.getPlayerInventory(playerId, worldId)
      //savePlayerInventory(playerId,worldId,inventory)
    }
  }

  def savePlayerInventory(playerId: UUID, worldId: UUID, inventory: List[ItemStack]): Unit ={
    PlayerWorldInventoryCache.setPlayerInventory(playerId, worldId, inventory)

    println("INVENTORY:")
    //Attempt Save To DB
    try{
      val dbInventory = new ItemStackConverter().convertItemStacksToInventory(playerId,worldId,inventory)
      println(dbInventory.length)
      dbInventory.foreach((inv)=>{
        println(inv)
      })
    }
    catch{
      case ex: Exception => println("UNABLE TO SAVE PLAYER INVENTORY TO DATABASE!!! REASON: " + ex.getMessage)
    }
  }

  // Ender Chest Inventory

  def getPlayerWorldEndInventory(playerId: UUID, worldId: UUID): List[ItemStack] ={
    PlayerWorldInventoryCache.getPlayerEndInventory(playerId,worldId)
  }

  def setPlayerWorldEndInventory(playerId: UUID, worldId: UUID): Unit ={
    if(!PlayerWorldInventoryCache.playerEndInventoryContains(playerId,worldId)){
      //set ender chest inventory from DB or empty list
      //savePlayerEndInventory(playerId, worldId, inventory)
      PlayerWorldInventoryCache.setPlayerEndInventory(playerId, worldId, List())
    }
  }

  def savePlayerEndInventory(playerId: UUID, worldId: UUID, inventory: List[ItemStack]): Unit ={
    PlayerWorldInventoryCache.setPlayerEndInventory(playerId, worldId,inventory)

    //Attempt Save To DB
  }

  // Player Data

  def setPlayerWorldData(playerId: UUID, worldId: UUID): Unit ={
    if(PlayerWorldInventoryCache.playerDataContains(playerId,worldId)){
      val player = Bukkit.getServer.getPlayer(playerId)
      val playerData = PlayerWorldInventoryCache.getPlayerData(playerId,worldId)

      player.setHealth(playerData._1)
      player.setExp(playerData._2)
      player.setLevel(playerData._3.toInt)
      player.setFoodLevel(playerData._4.toInt)
      player.setSaturation(playerData._5)
    }
    else{
      //Get From DB
      val player = Bukkit.getServer.getPlayer(playerId)
      val playerData = playerDataDao.getPlayerData(playerId,worldId)
      player.setHealth(playerData._1)
      player.setExp(playerData._2)
      player.setLevel(playerData._3.toInt)
      player.setFoodLevel(playerData._4.toInt)
      player.setSaturation(playerData._5)

      //Set cache
      PlayerWorldInventoryCache.setPlayerData(playerId, worldId, playerData._1, playerData._2, playerData._3, playerData._4, playerData._5)
    }
  }

  def savePlayerData(playerId: UUID, worldId: UUID, health: Float, experience: Float, level: Float, food: Float, saturation: Float): Unit ={
    PlayerWorldInventoryCache.setPlayerData(playerId,worldId,health,experience,level,food,saturation)

    //Attempt Save To DB
    try{
      playerDataDao.savePlayerWorldData(playerId,worldId,health,experience,level,food,saturation)
    }
    catch {
      case ex: Exception => println("UNABLE TO SAVE PLAYER DATA TO DATABASE!!! REASON: " + ex.getMessage)
    }
  }
}

object InventoryService extends InventoryService(InventoryDao, PlayerDataDao, EndChestInventoryDao)