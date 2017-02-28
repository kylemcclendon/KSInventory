package ksinventory.services

import java.util.UUID

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import ksinventory.cache.{EndChestCache, RetrieveRetryCache, SaveRetryCache}
import ksinventory.dao.EndChestInventoryDao
import ksinventory.messager.Messager
import ksinventory.models.EndInventory
import ksinventory.utils.{ItemStackConverter, Utils}
import org.bukkit.inventory.ItemStack

class EndChestInventoryService(endChestInventoryDao: EndChestInventoryDao) {
  def getPlayerWorldEndInventory(playerId: UUID, worldName: String): List[ItemStack] ={
    EndChestCache.getPlayerEndInventory(playerId,worldName)
  }

  def setPlayerWorldEndInventory(playerId: UUID, worldName: String): Unit ={
    if(!EndChestCache.playerEndInventoryContains(playerId,worldName)){
      EndChestCache.setEndRequest(playerId,worldName,request = true)
      Utils.activeRequests += 1
      RetrieveRetryCache.clearRetryRefined(playerId, worldName, "end")

      val f: Future[List[EndInventory]] = Future {
        EndChestInventoryDao.getPlayerEndInventory(playerId, worldName)
      }

      f onComplete {
        case Success(endInventories) =>
          Utils.activeRequests -= 1
          RetrieveRetryCache.clearRetryRequestRefined(playerId, worldName, "end")
          val endInventory = new ItemStackConverter().convertEndInventoryToItemStacks(endInventories)
          setEndInventoryCache(playerId, worldName, endInventory)
          EndChestCache.setEndRequest(playerId,worldName,request = false)
        case Failure(error) =>
          Utils.activeRequests -= 1
          RetrieveRetryCache.setRetryRefined(playerId,worldName, "end")
          RetrieveRetryCache.clearRetryRequestRefined(playerId, worldName, "end")
          Messager.messagePlayerFailure(playerId, "Ender Chest Inventory Retrieve Failed! You can retry the retrieve with /retryGet " + worldName + " end")
          println(error)
          EndChestCache.setEndRequest(playerId,worldName,request = false)
      }
    }
  }

  def savePlayerEndInventory(playerId: UUID, worldName: String): Unit ={
    val inventory = EndChestCache.getPlayerEndInventory(playerId, worldName)
    SaveRetryCache.clearRetryRefined(playerId, worldName, "end")

    Utils.activeRequests += 1
    val f: Future[Unit] = Future {
      val dbInventory = new ItemStackConverter().convertItemStacksToEndInventory(playerId,worldName,inventory)
      endChestInventoryDao.savePlayerEndInventory(playerId, worldName, dbInventory)
    }

    f onComplete {
      case Success(_) =>
        Utils.activeRequests -= 1
        SaveRetryCache.clearRetryRequestRefined(playerId, worldName, "end")
        Messager.messagePlayerSuccess(playerId, "Chest Inventory Saved. You can quiet this message with /inv quiet")
      case Failure(error) =>
        Utils.activeRequests -= 1
        SaveRetryCache.setRetryRefined(playerId,worldName, "end")
        SaveRetryCache.clearRetryRequestRefined(playerId, worldName, "end")
        Messager.messagePlayerFailure(playerId, "Ender Chest Inventory Save Failed! You can retry the save with /retrySave " + worldName + " end")
        println(error)
    }
  }

  def setEndInventoryCache(playerId: UUID, worldName: String, inventory: List[ItemStack]): Unit ={
    EndChestCache.setPlayerEndInventory(playerId,worldName,inventory)
  }
}

object EndChestInventoryService extends EndChestInventoryService(EndChestInventoryDao)