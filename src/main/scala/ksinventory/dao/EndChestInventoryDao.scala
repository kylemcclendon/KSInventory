package ksinventory.dao

import java.util.UUID

import com.datastax.driver.core.querybuilder.QueryBuilder
import ksinventory.database.CassandraDbConnector

class EndChestInventoryDao(cassandraDbConnector: CassandraDbConnector) {
  //Getter

  def getPlayerEndInventory(playerId: UUID, worldId: UUID): List[String] ={
    try{
      val query = QueryBuilder.select().from(cassandraDbConnector.getKeySpace, "end_inventory")
        .where(QueryBuilder.eq("player_id",playerId)).and(QueryBuilder.eq("world_id",worldId))

      val result = cassandraDbConnector.getSession.execute(query).all()

      if(result == null){
        Nil
      }else{
        List()
      }
    }
    catch{
      case ex: Exception =>
        println(ex)
        Nil
    }
    Nil
  }

  //Saver

  def savePlayerEndInventory(playerId: UUID, worldId: UUID): Unit ={

  }
}

object EndChestInventoryDao extends EndChestInventoryDao(CassandraDbConnector)
