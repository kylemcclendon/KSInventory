package ksinventory.models

import java.util
import com.datastax.driver.mapping.annotations.{Field, UDT}

@UDT(keyspace="minecraft", name="firework_effect")
case class FireworkEffectUDT(
                            @Field(name="type")
                            fireworkType: String,
                            @Field(name="flicker")
                            flicker: Boolean,
                            @Field(name="trail")
                            trail: Boolean,
                            @Field(name="colors")
                            colors: util.List[ColorUDT],
                            @Field(name="fade_colors")
                            fadeColors: util.List[ColorUDT]
                            )
