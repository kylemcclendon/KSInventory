package ksinventory.cache

import java.util.UUID

import org.bukkit.inventory.ItemStack

import scala.collection.mutable

object EndChestCache {
  var playerWorldEndInventoryMap: mutable.Map[(UUID, String),List[ItemStack]] = mutable.Map()
  var playerEndRequest: mutable.Map[(UUID, String),Boolean] = mutable.Map()

  def playerEndInventoryContains(playerId: UUID, worldName: String): Boolean ={
    playerWorldEndInventoryMap.contains((playerId,worldName))
  }

  def getPlayerEndInventory(playerId: UUID, worldName: String): List[ItemStack] = {
    playerWorldEndInventoryMap.getOrElse((playerId,worldName), Nil)
  }

  def getPlayerEndRequest(playerId: UUID, worldName:String): Boolean = {
    playerEndRequest.getOrElse((playerId,worldName),false)
  }

  def setPlayerEndInventory(playerId: UUID, worldName: String, inventory: List[ItemStack]): Unit = {
    playerWorldEndInventoryMap((playerId, worldName)) = inventory
  }

  def setEndRequest(playerId: UUID, worldName: String, request: Boolean): Unit = {
    playerEndRequest((playerId,worldName)) = request
  }
}
