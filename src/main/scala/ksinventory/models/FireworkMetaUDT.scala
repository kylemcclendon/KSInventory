package ksinventory.models

import java.util
import com.datastax.driver.mapping.annotations.{Field, UDT}

@UDT(keyspace="minecraft", name="firework_meta")
case class FireworkMetaUDT(
                            @Field(name="power")
                            power: Int,
                            @Field(name="effects")
                            effects: util.List[FireworkEffectUDT]
                          )