package ksinventory.services

import java.util.UUID

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import ksinventory.cache.{PlayerWorldInventoryCache, RetrieveRetryCache, SaveRetryCache}
import ksinventory.dao.InventoryDao
import ksinventory.messager.Messager
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
      RetrieveRetryCache.clearRetryRefined(playerId, worldName, "inv")
      val f: Future[List[PlayerInventory]] = Future {
        inventoryDao.getPlayerInventory(playerId, worldName)
      }

      f onComplete {
        case Success(playerInventories) =>
          Utils.activeRequests -= 1
          RetrieveRetryCache.clearRetryRequestRefined(playerId, worldName, "inv")
          val playerInventory = new ItemStackConverter().convertInventoryToItemStacks(playerInventories)
          getServer.getPlayer(playerId).getInventory.setContents(playerInventory.toArray)
          setPlayerInventoryCache(playerId, worldName, playerInventory)
        case Failure(message) =>
          Utils.activeRequests -= 1
          RetrieveRetryCache.setRetryRefined(playerId,worldName, "inv")
          RetrieveRetryCache.clearRetryRequestRefined(playerId, worldName, "inv")
          Messager.messagePlayerFailure(playerId, "Player Inventory Save Failed! You can retry the retrieve with /retryGet " + worldName + " inv")
          println(message)
      }
    }
  }

  def persistPlayerInventory(playerId: UUID, worldName: String): Unit ={
    val inventory = PlayerWorldInventoryCache.getPlayerInventory(playerId, worldName)
    SaveRetryCache.clearRetryRefined(playerId, worldName, "inv")

    Utils.activeRequests += 1
    val f: Future[Unit] = Future {
      val dbInventory = new ItemStackConverter().convertItemStacksToInventory(playerId,worldName,inventory)
      inventoryDao.savePlayerInventory(playerId, worldName, dbInventory)
    }

    f onComplete {
      case Success(_) =>
        Utils.activeRequests -= 1
        SaveRetryCache.clearRetryRequestRefined(playerId, worldName, "inv")
        Messager.messagePlayerSuccess(playerId, "Inventory Saved. You can quiet this message with /inv quiet")
      case Failure(error) =>
        Utils.activeRequests -= 1
        SaveRetryCache.setRetryRefined(playerId,worldName, "inv")
        SaveRetryCache.clearRetryRequestRefined(playerId, worldName, "inv")
        Messager.messagePlayerFailure(playerId, "Player Inventory Save Failed! You can retry the save with /retrySave " + worldName + " inv")
        println(error)
    }
  }

  def setPlayerInventoryCache(playerId: UUID, worldName: String, inventory: List[ItemStack]): Unit ={
    PlayerWorldInventoryCache.setPlayerInventory(playerId, worldName, inventory)
  }
}

object InventoryService extends InventoryService(InventoryDao)