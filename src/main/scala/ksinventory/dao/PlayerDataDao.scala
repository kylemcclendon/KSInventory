package ksinventory.dao

import java.util.UUID

import com.datastax.driver.core.querybuilder.QueryBuilder
import ksinventory.database.CassandraDbConnector

class PlayerDataDao(cassandraDbConnector: CassandraDbConnector) {
  //Getter

  def getPlayerData(playerId: UUID, worldName: String): (Float, Float, Float, Float, Float)={
    val query = QueryBuilder.select()
      .from(cassandraDbConnector.getKeySpace, "player_data")
      .where(QueryBuilder.eq("player_id", playerId))
      .and(QueryBuilder.eq("world_name",worldName))

    val result = cassandraDbConnector.getSession.execute(query).one()

    if(result == null){
      Tuple5(20,0,0,20,20)
    }else{
      Tuple5(result.getFloat("health"),result.getFloat("experience"),result.getFloat("level"),result.getFloat("food"),result.getFloat("saturation"))
    }
  }

  //Saver

  def savePlayerWorldData(playerId: UUID, worldName: String, health: Float, experience: Float, level: Float, food: Float, saturation: Float): Unit ={
    cassandraDbConnector.getSession.execute(QueryBuilder.insertInto("player_data")
      .value("player_id",playerId)
      .value("world_name",worldName)
      .value("health",health)
      .value("experience",experience)
      .value("level",level)
      .value("food",food)
      .value("saturation",saturation))
  }
}

object PlayerDataDao extends PlayerDataDao(CassandraDbConnector)