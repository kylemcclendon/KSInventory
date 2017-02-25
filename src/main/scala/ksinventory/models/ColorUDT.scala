package ksinventory.models

import com.datastax.driver.mapping.annotations.{Field, UDT}

@UDT(keyspace="minecraft", name="color")
class ColorUDT(){
  @Field(name="red")
  var red: Int = -1
  @Field(name="green")
  var green: Int = -1
  @Field(name="blue")
  var blue: Int = -1

  def this(r: Int, g: Int, b: Int){
    this()
    this.red = r
    this.green = g
    this.blue = b
  }
}