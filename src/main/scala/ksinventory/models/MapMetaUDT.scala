package ksinventory.models

import com.datastax.driver.mapping.annotations.{Field, UDT}

@UDT(keyspace="minecraft", name="map_meta")
case class MapMetaUDT(
                     @Field(name="color")
                     color: ColorUDT,
                     @Field(name="location")
                     location: String,
                     @Field(name="scaling")
                     scaling: Boolean
                     )