package ksinventory.models

import com.datastax.driver.mapping.annotations.UDT

@UDT(keyspace = "minecraft", name = "pattern")
class PatternUDT(){
  var color: String = _
  var pattern: String = _

  def this(c: String, p: String){
    this()
    this.color = c
    this.pattern = p
  }
}