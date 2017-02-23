package ksinventory.cache

import java.util.UUID
import scala.collection.mutable

import org.bukkit.inventory.ItemStack

object PlayerWorldInventoryCache {
  var playerWorldInventoryMap: mutable.Map[(UUID, String),List[ItemStack]] = mutable.Map()
  var playerInventoryRetryMap: mutable.Map[UUID, Tuple2[String, Long]] = mutable.Map()
  var playerInventoryRetryRequests: mutable.Map[UUID, Boolean] = mutable.Map()

  //Contains

  def playerInventoryContains(playerId: UUID, worldName: String): Boolean ={
    playerWorldInventoryMap.contains((playerId,worldName))
  }

  //Getters

  def getPlayerInventory(playerId: UUID, worldName: String): List[ItemStack] = {
    playerWorldInventoryMap.getOrElse((playerId,worldName),Nil)
  }

  def getPlayerInventoryRetry(playerId: UUID): Tuple2[String, Long] = {
    playerInventoryRetryMap.getOrElse(playerId, Tuple2(null, -1))
  }

  def getPlayerInventoryRetryRequest(playerId: UUID): Boolean ={
    playerInventoryRetryRequests.getOrElse(playerId, false)
  }

  //Setters

  def setPlayerInventory(playerId: UUID, worldName: String, inventory: List[ItemStack]): Unit ={
    playerWorldInventoryMap((playerId,worldName)) = inventory
  }

  def setPlayerInventoryRetry(playerId: UUID, worldName: String): Unit ={
    playerInventoryRetryMap(playerId) = Tuple2(worldName,System.currentTimeMillis() + 60000)
  }

  def setRetryRequest(playerId: UUID): Unit ={
    playerInventoryRetryRequests(playerId) = true
  }

  //Removers

  def clearPlayerInventoryRetry(playerId: UUID): Unit ={
    playerInventoryRetryMap.remove(playerId)
  }

  def clearRetryRequest(playerId: UUID): Unit ={
    playerInventoryRetryRequests.remove(playerId)
  }
}