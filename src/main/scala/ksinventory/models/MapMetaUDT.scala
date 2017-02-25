package ksinventory.models

import com.datastax.driver.mapping.annotations.{Frozen, UDT}

@UDT(keyspace="minecraft", name="map_meta")
case class MapMetaUDT(){
  @Frozen
  var color: ColorUDT = _
  var location: String = _
  var scaling: Boolean = false

  def this(c: ColorUDT, l: String, s: Boolean){
    this()
    this.color = c
    this.location = l
    this.scaling = s
  }
}