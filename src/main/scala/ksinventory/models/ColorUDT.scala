package ksinventory.models

import com.datastax.driver.mapping.annotations.{Field, UDT}

@UDT(keyspace="minecraft", name="color")
case class ColorUDT(
                     @Field(name="red")
                     red: Int,
                     @Field(name="green")
                     green: Int,
                     @Field(name="blue")
                     blue: Int
                   )