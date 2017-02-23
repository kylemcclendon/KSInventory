package ksinventory.cache

import java.util.UUID

import org.bukkit.inventory.ItemStack

import scala.collection.mutable

object EndChestCache {
  var playerWorldEndInventoryMap: mutable.Map[(UUID, String),List[ItemStack]] = mutable.Map()
  var playerEndRequest: mutable.Map[(UUID, String),Boolean] = mutable.Map()
  var playerWorldEndRetryMap: mutable.Map[UUID, Tuple2[String, Long]] = mutable.Map()
  var playerWorldEndRetryRequests: mutable.Map[UUID, Boolean] = mutable.Map()

  //Contains

  def playerEndInventoryContains(playerId: UUID, worldName: String): Boolean ={
    playerWorldEndInventoryMap.contains((playerId,worldName))
  }

  //Getters

  def getPlayerEndInventory(playerId: UUID, worldName: String): List[ItemStack] = {
    playerWorldEndInventoryMap.getOrElse((playerId,worldName), Nil)
  }

  def getPlayerEndRequest(playerId: UUID, worldName:String): Boolean = {
    playerEndRequest.getOrElse((playerId,worldName),false)
  }

  def getEndRetry(playerId: UUID): Tuple2[String, Long] ={
    playerWorldEndRetryMap.getOrElse(playerId, Tuple2(null, -1))
  }

  def getRetryRequest(playerId: UUID): Boolean = {
    playerWorldEndRetryRequests.getOrElse(playerId, false)
  }

  //Setters

  def setPlayerEndInventory(playerId: UUID, worldName: String, inventory: List[ItemStack]): Unit = {
    playerWorldEndInventoryMap((playerId, worldName)) = inventory
  }

  def setEndRequest(playerId: UUID, worldName: String, request: Boolean): Unit = {
    playerEndRequest((playerId,worldName)) = request
  }

  def setEndRetry(playerId: UUID, worldName: String): Unit ={
    playerWorldEndRetryMap(playerId) = Tuple2(worldName, System.currentTimeMillis() + 60000)
  }

  def setRetryRequest(playerId: UUID): Unit ={
    playerWorldEndRetryRequests(playerId) = true
  }

  //Removers

  def clearEndRetry(playerId: UUID): Unit ={
    playerWorldEndRetryMap.remove(playerId)
  }

  def clearRetryRequest(playerId: UUID): Unit ={
    playerWorldEndRetryRequests.remove(playerId)
  }
}
