package ksinventory.cache

import java.util.UUID
import scala.collection.mutable

import org.bukkit.inventory.ItemStack

object PlayerWorldInventoryCache {
  var playerWorldInventoryMap: mutable.Map[(UUID, String),List[ItemStack]] = mutable.Map()

  def playerInventoryContains(playerId: UUID, worldName: String): Boolean ={
    playerWorldInventoryMap.contains((playerId,worldName))
  }

  def getPlayerInventory(playerId: UUID, worldName: String): List[ItemStack] = {
    playerWorldInventoryMap.getOrElse((playerId,worldName),Nil)
  }

  def setPlayerInventory(playerId: UUID, worldName: String, inventory: List[ItemStack]): Unit ={
    playerWorldInventoryMap((playerId,worldName)) = inventory
  }
}