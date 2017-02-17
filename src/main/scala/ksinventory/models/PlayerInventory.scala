package ksinventory.models

import java.util.UUID

import com.datastax.driver.mapping.annotations.{Frozen, Table}

@Table(keyspace = "minecraft", name = "player_inventory")
class PlayerInventory(){
  private var player_id: UUID = null
  private var world_id: UUID = null
  private var position: Int = -1
  private var amount: Int = -1
  private var damage: Int = -1
  private var material: String = null
  @Frozen
  private var metaUDT: MetaUDT = null

  def this(pid: UUID, wid: UUID, pos: Int, amt: Int, dam:Int, mat:String, meta:MetaUDT){
    this()
    this.player_id = pid
    this.world_id = wid
    this.position = pos
    this.amount = amt
    this.damage = dam
    this.material = mat
    this.metaUDT = meta
  }
}
