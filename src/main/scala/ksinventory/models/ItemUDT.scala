package ksinventory.models

import com.datastax.driver.mapping.annotations.{Frozen, UDT}

@UDT(keyspace="minecraft", name="itemudt")
class ItemUDT {
  var position: Int = -1
  var amount: Int = -1
  var damage: Int = -1
  var material: String = _
  @Frozen
  var metaUDT: MetaUDT = _

  def this(p: Int, a: Int, d: Int, m: String, meta: MetaUDT){
    this()
    this.position = p
    this.amount = a
    this.damage = d
    this.material = m
    this.metaUDT = meta
  }
}
