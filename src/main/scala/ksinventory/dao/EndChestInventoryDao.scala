package ksinventory.dao

import java.util.UUID

import scala.collection.JavaConverters._
import com.datastax.driver.core.{BatchStatement, ResultSet}
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.mapping.{Mapper, Result}
import ksinventory.database.CassandraDbConnector
import ksinventory.models.EndInventory

class EndChestInventoryDao(cassandraDbConnector: CassandraDbConnector) {
  def getPlayerEndInventory(playerId: UUID, worldName: String): List[EndInventory] = {
    try{
      val mapper: Mapper[EndInventory] = cassandraDbConnector.getMapper.mapper(classOf[EndInventory])

      val query = QueryBuilder.select().from(cassandraDbConnector.getKeySpace, "end_inventory")
        .where(QueryBuilder.eq("player_id", playerId)).and(QueryBuilder.eq("world_name", worldName))
      val results: ResultSet = cassandraDbConnector.getSession.execute(query)
      val inventories: Result[EndInventory] = mapper.map(results)
      inventories.all().asScala.toList
    }
    catch{
      case ex: Exception =>
        println(ex)
        Nil
    }
  }

  def savePlayerEndInventory(playerId: UUID, worldName: String, inventoryList: List[EndInventory]): Unit ={
    val batch = new BatchStatement()
    val mapper = CassandraDbConnector.getMapper.mapper(classOf[EndInventory])

    inventoryList.map((inventory)=> {
      batch.add(mapper.saveQuery(inventory))
    })

    cassandraDbConnector.getSession.execute(batch)
  }
}

object EndChestInventoryDao extends EndChestInventoryDao(CassandraDbConnector)
