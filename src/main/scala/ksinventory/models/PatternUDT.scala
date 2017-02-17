package ksinventory.models

import com.datastax.driver.mapping.annotations.{Field, UDT}

@UDT(keyspace = "minecraft", name = "pattern")
case class PatternUDT(
                  color: String,
                  pattern: String
                )