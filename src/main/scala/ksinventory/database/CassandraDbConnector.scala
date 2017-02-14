package ksinventory.database

import com.datastax.driver.core.{Cluster, Session}

trait CassandraDbConnector {

  def getSession: Session
  def createSessionAndInitKeySpace(address: String, port: Int, keySpace: String, userName: String, password: String): Session
  def createSession(address: String, port: Int): Session
  def getKeySpace: String = "minecraft"
  def getPort: Int = 9042
  def getHost: String = "216.224.167.187"
  def getUserName: String = "cassandra"
  def getPassword: String = "cassandra"
}
class CassandraDbConnectorImpl extends CassandraDbConnector {
//  private val logger = Logger(this.getClass)
  private[this] val lock = new Object()
  private var session: Session =_

  def getSession: Session = {
    if(session == null) {
      createSessionAndInitKeySpace(getHost, getPort, getKeySpace, getUserName, getPassword)
    }
    session
  }

  def createSessionAndInitKeySpace(address: String, port: Int, keySpace: String, userName: String, password: String): Session = lock.synchronized {
    try {
      var hosts : Array[String] = Array(address)
      if(address.contains(",")) hosts = address.split(",").map(_.trim)

//      val cluster = Cluster.builder().addContactPoints(hosts:_*).withPort(port).withCredentials(userName, password).build()
      val cluster = Cluster.builder().addContactPoint("216.224.167.187").withPort(9042).withCredentials("cassandra", "cassandra").build()
      session = cluster.connect("minecraft")
    } catch {
      case ex : Exception => println("Unable to create casandra session: "+ex.getMessage)
    }
    session
  }

  def createSession(address: String, port: Int): Session = {
    val cluster = Cluster.builder().addContactPoint(address).withPort(port).build()
    val session = cluster.connect()
    session
  }

}
object CassandraDbConnector extends CassandraDbConnectorImpl
