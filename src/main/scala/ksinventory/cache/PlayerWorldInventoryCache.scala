package ksinventory.cache

import java.util.UUID
import scala.collection.mutable

import org.bukkit.inventory.ItemStack

object PlayerWorldInventoryCache {
  var playerWorldInventoryMap: mutable.Map[(UUID, String),List[ItemStack]] = mutable.Map()
  var playerWorldEndInventoryMap: mutable.Map[(UUID, String),List[ItemStack]] = mutable.Map()
  var playerWorldDataMap: mutable.Map[(UUID, String), (Float, Float, Float, Float, Float)] = mutable.Map()
  var playerEndRequest: mutable.Map[(UUID, String),Boolean] = mutable.Map()
  var playerSuppressMessages: mutable.Map[String, Boolean] = mutable.Map()

  //Containers
  def playerInventoryContains(playerId: UUID, worldName: String): Boolean ={
    playerWorldInventoryMap.contains((playerId,worldName))
  }

  def playerEndInventoryContains(playerId: UUID, worldName: String): Boolean ={
    playerWorldEndInventoryMap.contains((playerId,worldName))
  }

  def playerDataContains(playerId: UUID, worldName: String): Boolean ={
    playerWorldDataMap.contains((playerId,worldName))
  }

  //Getters
  def getPlayerInventory(playerId: UUID, worldName: String): List[ItemStack] = {
    playerWorldInventoryMap.getOrElse((playerId,worldName),Nil)
  }

  def getPlayerData(playerId: UUID, worldName: String): Tuple5[Float, Float, Float, Float, Float]={
    playerWorldDataMap.getOrElse((playerId,worldName),(20,0,0,20,20))
  }

  def getPlayerEndInventory(playerId: UUID, worldName: String): List[ItemStack] = {
    playerWorldEndInventoryMap.getOrElse((playerId,worldName), Nil)
  }

  def getPlayerEndRequest(playerId: UUID, worldName:String): Boolean = {
    playerEndRequest.getOrElse((playerId,worldName),false)
  }

  def getPlayerSuppression(playerId: String): Boolean = {
    playerSuppressMessages.getOrElse(playerId, false)
  }

  //Setters
  def setPlayerData(playerId: UUID, worldName: String, health: Float, experience: Float, level: Float, food: Float, saturation: Float): Unit ={
    playerWorldDataMap((playerId,worldName)) = (health, experience,level,food,saturation)
  }

  def setPlayerInventory(playerId: UUID, worldName: String, inventory: List[ItemStack]): Unit ={
    playerWorldInventoryMap((playerId,worldName)) = inventory
  }

  def setPlayerEndInventory(playerId: UUID, worldName: String, inventory: List[ItemStack]): Unit = {
    playerWorldEndInventoryMap((playerId, worldName)) = inventory
  }

  def setEndRequest(playerId: UUID, worldName: String, request: Boolean): Unit = {
    playerEndRequest((playerId,worldName)) = request
  }

  def setSuppressedPlayers(players: Set[String]): Unit ={
    players.foreach((player) => {
      playerSuppressMessages(player) = true
    })
  }

  def setSuppressedPlayer(player: String): Unit ={
    playerSuppressMessages(player) = true
  }

  def removeSuppressedPlayer(player: String): Unit ={
    playerSuppressMessages(player) = false
  }
}