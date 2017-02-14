package ksinventory.models

import java.util
import com.datastax.driver.mapping.annotations.{Field, UDT}

@UDT(keyspace="minecraft",name="book_meta")
case class BookMetaUDT(
                        @Field(name="author") author: String,
                        @Field(name="title") title: String,
                        @Field(name="pages") pages: util.List[String]
                      )