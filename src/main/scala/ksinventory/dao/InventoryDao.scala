package ksinventory.dao

import java.util.UUID

import scala.collection.JavaConverters._
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core._
import com.datastax.driver.mapping.{Mapper, Result}
import ksinventory.database.CassandraDbConnector
import ksinventory.models.PlayerInventory

class InventoryDao(cassandraDbConnector: CassandraDbConnector) {
  def getPlayerInventory(playerId: UUID, worldName: String): List[PlayerInventory] ={
    try {
      val mapper: Mapper[PlayerInventory] = cassandraDbConnector.getMapper.mapper(classOf[PlayerInventory])

      val query = QueryBuilder.select().from(cassandraDbConnector.getKeySpace, "player_inventory")
        .where(QueryBuilder.eq("player_id", playerId)).and(QueryBuilder.eq("world_name", worldName))

      val results: ResultSet = cassandraDbConnector.getSession.execute(query)
      val inventories: Result[PlayerInventory] = mapper.map(results)
      inventories.all().asScala.toList
    }
    catch{
      case ex: Exception =>
        println(ex)
        Nil
    }
  }

  def savePlayerInventory(playerId: UUID, worldName: String, inventoryList: List[PlayerInventory]): Unit ={
    val batch = new BatchStatement()
    val mapper = CassandraDbConnector.getMapper.mapper(classOf[PlayerInventory])

    inventoryList.map((inventory)=> {
      batch.add(mapper.saveQuery(inventory))
    })

    cassandraDbConnector.getSession.execute(batch)
  }
}

object InventoryDao extends InventoryDao(CassandraDbConnector)
