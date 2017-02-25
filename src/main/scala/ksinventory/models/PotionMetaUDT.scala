package ksinventory.models

import com.datastax.driver.mapping.annotations.UDT

@UDT(keyspace = "minecraft", name="potion_meta")
class PotionMetaUDT(){
  var potion_type: String = _
  var upgraded: Boolean = false
  var extended: Boolean = false

  def this(pt: String, u: Boolean, e: Boolean){
    this()
    this.potion_type = pt
    this.upgraded = u
    this.extended = e
  }
}
