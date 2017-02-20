package ksinventory.models

import com.datastax.driver.mapping.annotations.{Field, Frozen, UDT}

@UDT(keyspace="minecraft", name="map_meta")
case class MapMetaUDT(){
  @Frozen
  var color: ColorUDT = null
  var location: String = null
  var scaling: Boolean = false

  def this(c: ColorUDT, l: String, s: Boolean){
    this()
    this.color = c
    this.location = l
    this.scaling = s
  }
}