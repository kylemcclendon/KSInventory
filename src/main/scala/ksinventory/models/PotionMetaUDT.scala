package ksinventory.models

import com.datastax.driver.mapping.annotations.{Field, UDT}

@UDT(keyspace = "minecraft", name="potion_meta")
class PotionMetaUDT(){
  private var potion_type: String = null
  private var upgraded: Boolean = false
  private var extended: Boolean = false

  def this(pt: String, u: Boolean, e: Boolean){
    this()
    this.potion_type = pt
    this.upgraded = u
    this.extended = e
  }
}
