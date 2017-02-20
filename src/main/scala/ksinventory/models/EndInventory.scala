package ksinventory.models

import java.util.UUID

import com.datastax.driver.mapping.annotations.{Frozen, Table}

@Table(keyspace = "minecraft", name = "end_inventory")
class EndInventory {
  var player_id: UUID = _
  var world_name: String = _
  var position: Int = -1
  var amount: Int = -1
  var damage: Int = -1
  var material: String = _
  @Frozen
  var metaUDT: MetaUDT = _

  def this(pid: UUID, wid: String, pos: Int, amt: Int, dam:Int, mat:String, meta:MetaUDT){
    this()
    this.player_id = pid
    this.world_name = wid
    this.position = pos
    this.amount = amt
    this.damage = dam
    this.material = mat
    this.metaUDT = meta
  }
}
