package ksinventory.services

import java.util.UUID

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import ksinventory.cache.PlayerWorldInventoryCache
import ksinventory.dao.InventoryDao
import ksinventory.models.PlayerInventory
import ksinventory.utils.{ItemStackConverter, Utils}
import org.bukkit.Bukkit.getServer
import org.bukkit.inventory.ItemStack

class InventoryService(inventoryDao: InventoryDao) {
  def setPlayerWorldInventory(playerId: UUID, worldName: String){
    if(PlayerWorldInventoryCache.playerInventoryContains(playerId,worldName)){
      getServer.getPlayer(playerId).getInventory.setContents(PlayerWorldInventoryCache.getPlayerInventory(playerId, worldName).toArray)
    }
    else{
      //Get From DB
      Utils.activeRequests += 1
      val f: Future[List[PlayerInventory]] = Future {
        inventoryDao.getPlayerInventory(playerId, worldName)
      }

      f onComplete {
        case Success(playerInventories) =>
          Utils.activeRequests -= 1
          val playerInventory = new ItemStackConverter().convertInventoryToItemStacks(playerInventories)
          getServer.getPlayer(playerId).getInventory.setContents(playerInventory.toArray)
          setPlayerInventoryCache(playerId, worldName, playerInventory)
        case Failure(message) =>
          Utils.activeRequests -= 1
          println(message)
      }
    }
  }

  def savePlayerInventory(playerId: UUID, worldName: String, inventory: List[ItemStack]): Unit ={
    PlayerWorldInventoryCache.setPlayerInventory(playerId, worldName, inventory)

    Utils.activeRequests += 1
    val f: Future[Unit] = Future {
      val dbInventory = new ItemStackConverter().convertItemStacksToInventory(playerId,worldName,inventory)
      inventoryDao.savePlayerInventory(playerId, worldName, dbInventory)
    }

    f onComplete {
      case Success(success) =>
        Utils.activeRequests -= 1
      case Failure(error) =>
        Utils.activeRequests -= 1
        println(error)
    }
  }

  def setPlayerInventoryCache(playerId: UUID, worldName: String, inventory: List[ItemStack]): Unit ={
    PlayerWorldInventoryCache.setPlayerInventory(playerId, worldName, inventory)
  }
}

object InventoryService extends InventoryService(InventoryDao)