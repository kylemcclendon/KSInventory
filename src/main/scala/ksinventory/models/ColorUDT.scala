package ksinventory.models

import com.datastax.driver.mapping.annotations.{Field, UDT}

@UDT(keyspace="minecraft", name="color")
class ColorUDT(){
  private var red: Int = -1
  private var green: Int = -1
  private var blue: Int = -1

  def this(r: Int, g: Int, b: Int){
    this()
    this.red = r
    this.green = g
    this.blue = b
  }
}