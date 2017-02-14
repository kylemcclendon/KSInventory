package ksinventory.cache

import java.util.UUID
import scala.collection.mutable

import org.bukkit.inventory.ItemStack

object PlayerWorldInventoryCache {
  var playerWorldInventoryMap: mutable.Map[(UUID, UUID),List[ItemStack]] = mutable.Map()
  var playerWorldEndInventoryMap: mutable.Map[(UUID, UUID),List[ItemStack]] = mutable.Map()
  var playerWorldDataMap: mutable.Map[(UUID, UUID), (Float, Float, Float, Float, Float)] = mutable.Map()

  //Containers
  def playerInventoryContains(playerId: UUID, worldId: UUID): Boolean ={
    playerWorldInventoryMap.contains((playerId,worldId))
  }

  def playerEndInventoryContains(playerId: UUID, worldId: UUID): Boolean ={
    playerWorldEndInventoryMap.contains((playerId,worldId))
  }

  def playerDataContains(playerId: UUID, worldId: UUID): Boolean ={
    playerWorldDataMap.contains((playerId,worldId))
  }

  //Getters
  def getPlayerInventory(playerId: UUID, worldId: UUID): List[ItemStack] = {
    playerWorldInventoryMap.getOrElse((playerId,worldId),Nil)
  }

  def getPlayerData(playerId: UUID, worldId: UUID): Tuple5[Float, Float, Float, Float, Float]={
    playerWorldDataMap.getOrElse((playerId,worldId),(20,0,0,20,20))
  }

  def getPlayerEndInventory(playerId: UUID, worldId: UUID): List[ItemStack] = {
    playerWorldEndInventoryMap.getOrElse((playerId,worldId), Nil)
  }

  //Setters
  def setPlayerData(playerId: UUID, worldId: UUID, health: Float, experience: Float, level: Float, food: Float, saturation: Float): Unit ={
    playerWorldDataMap((playerId,worldId)) = (health, experience,level,food,saturation)
  }

  def setPlayerInventory(playerId: UUID, worldId: UUID, inventory: List[ItemStack]): Unit ={
    playerWorldInventoryMap((playerId,worldId)) = inventory
  }

  def setPlayerEndInventory(playerId: UUID, worldId: UUID, inventory: List[ItemStack]): Unit = {
    playerWorldEndInventoryMap((playerId, worldId)) = inventory
  }
}