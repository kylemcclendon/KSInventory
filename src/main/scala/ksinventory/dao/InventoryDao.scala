package ksinventory.dao

import java.util.UUID

import com.datastax.driver.core.querybuilder.{Insert, QueryBuilder, Update}
import com.datastax.driver.core.utils.Bytes
import com.datastax.driver.core._
import com.datastax.driver.mapping.Mapper
import ksinventory.database.CassandraDbConnector
import ksinventory.models.{MetaUDT, PlayerInventory}
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

  def savePlayerInventory(playerId: UUID, worldId: UUID, inventoryList: List[PlayerInventory]): Unit ={
    var batch = new BatchStatement()
    val mapper = CassandraDbConnector.getMapper().mapper(classOf[PlayerInventory])

    inventoryList.map((inventory)=> {
      batch.add(mapper.saveQuery(inventory))
    })

    cassandraDbConnector.getSession.execute(batch)
  }
}

object InventoryDao extends InventoryDao(CassandraDbConnector)
