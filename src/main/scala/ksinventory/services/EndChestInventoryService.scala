package ksinventory.services

import java.util.UUID

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import ksinventory.cache.{EndChestCache, RetryCache}
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

      val f: Future[List[EndInventory]] = Future {
        EndChestInventoryDao.getPlayerEndInventory(playerId, worldName)
      }

      f onComplete {
        case Success(endInventories) =>
          Utils.activeRequests -= 1
          val endInventory = new ItemStackConverter().convertEndInventoryToItemStacks(endInventories)
          setEndInventoryCache(playerId, worldName, endInventory)
          EndChestCache.setEndRequest(playerId,worldName,request = false)
        case Failure(error) =>
          Utils.activeRequests -= 1
          println(error)
          EndChestCache.setEndRequest(playerId,worldName,request = false)
      }
    }
  }

  def savePlayerEndInventory(playerId: UUID, worldName: String): Unit ={
    val inventory = EndChestCache.getPlayerEndInventory(playerId, worldName)
    RetryCache.clearRetryRefined(playerId, worldName, "end")

    Utils.activeRequests += 1
    val f: Future[Unit] = Future {
      val dbInventory = new ItemStackConverter().convertItemStacksToEndInventory(playerId,worldName,inventory)
      endChestInventoryDao.savePlayerEndInventory(playerId, worldName, dbInventory)
    }

    f onComplete {
      case Success(success) =>
        Utils.activeRequests -= 1
        RetryCache.clearRetryRequestRefined(playerId, worldName, "end")
        Messager.messagePlayerSuccess(playerId, "Chest Inventory Saved. You can quiet this message with /inv quiet")
      case Failure(error) =>
        Utils.activeRequests -= 1
        RetryCache.setRetryRefined(playerId,worldName, "end")
        RetryCache.clearRetryRequestRefined(playerId, worldName, "end")
        Messager.messagePlayerFailure(playerId, "Ender Chest Inventory Save Failed! You can retry the save with /inv retry end")
        println(error)
    }
  }

  def setEndInventoryCache(playerId: UUID, worldName: String, inventory: List[ItemStack]): Unit ={
    EndChestCache.setPlayerEndInventory(playerId,worldName,inventory)
  }
}

object EndChestInventoryService extends EndChestInventoryService(EndChestInventoryDao)