package ksinventory.dao

import java.util.UUID

import com.datastax.driver.core.querybuilder.{Insert, QueryBuilder, Update}
import com.datastax.driver.core.utils.Bytes
import com.datastax.driver.core._
import ksinventory.database.CassandraDbConnector
import ksinventory.models.MetaUDT
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class InventoryDao(cassandraDbConnector: CassandraDbConnector) {
  //Getter

  def getPlayerInventory(playerId: UUID, worldId: UUID): List[String] ={
    try {
      val query = QueryBuilder.select().from(cassandraDbConnector.getKeySpace, "player_inventory")
        .where(QueryBuilder.eq("player_id", playerId)).and(QueryBuilder.eq("world_id", worldId))

      val result = cassandraDbConnector.getSession.execute(query).all()

      if (result == null) {
        Nil
      }
      else {
        List()
      }
    }
    catch{
      case ex: Exception =>
        println(ex)
        Nil
    }
  }

  //Saver

  def savePlayerInventory(playerId: UUID, worldId: UUID, inventoryList: List[Tuple5[Int, Int, Int, String, MetaUDT]]): Unit ={
    var batch = new BatchStatement()
    inventoryList.map((inventory)=>{
      batch.add(QueryBuilder.insertInto("player_inventory")
        .value("player_id", playerId)
        .value("world_id", worldId)
        .value("position", inventory._1)
        .value("amount", inventory._2)
        .value("damage", inventory._3)
        .value("material", inventory._4)
        .value("meta", inventory._5))
    })

    cassandraDbConnector.getSession.execute(batch)
  }
}

object InventoryDao extends InventoryDao(CassandraDbConnector)
