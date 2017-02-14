package ksinventory.models

import java.util
import com.datastax.driver.mapping.annotations.{Field, UDT}

@UDT(keyspace = "minecraft", name="banner_meta")
case class BannerMetaUDT(
                          @Field(name="base_color") baseColor: String,
                          @Field(name="patterns") patterns: util.List[PatternUDT]
                        )
