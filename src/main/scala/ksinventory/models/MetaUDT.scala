package ksinventory.models

import java.util

import com.datastax.driver.mapping.annotations.{Field, UDT}

@UDT(keyspace="minecraft", name="metaudt")
case class MetaUDT(
                    @Field(name="banner")
                  banner: BannerMetaUDT,
                    @Field(name="book")
                  book: BookMetaUDT,
                    @Field(name="display_name")
                  displayName: String,
                    @Field(name="egg")
                  egg: String,
                    @Field(name="enchantments")
                  enchantments: util.Map[String, Int],
                    @Field(name="firework")
                  firework: FireworkMetaUDT,
                    @Field(name="internals")
                  internals: String,
                    @Field(name="leather")
                  leather: String,
                    @Field(name="lore")
                  lore: util.List[String],
                    @Field(name="map")
                  map: MapMetaUDT,
                    @Field(name="potion")
                  potion: Int, skull: String
                  )
