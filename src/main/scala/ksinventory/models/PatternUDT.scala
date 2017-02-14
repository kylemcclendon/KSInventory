package ksinventory.models

import com.datastax.driver.mapping.annotations.{Field, UDT}

@UDT(keyspace = "minecraft", name = "pattern")
case class PatternUDT(
                  @Field(name="color") color: String,
                  @Field(name="pattern") pattern: String
                )