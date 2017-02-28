package ksinventory.database

import com.datastax.driver.core.{Cluster, Session}
import com.datastax.driver.mapping.MappingManager

trait CassandraDbConnector {

  def getSession: Session
  def createSessionAndInitKeySpace(address: String, port: Int, keySpace: String, userName: String, password: String): Session
  def createSession(address: String, port: Int): Session
  def getKeySpace: String = "minecraft"
  def getPort: Int = 9042
  def getHost: String = sys.env("MCCassandra_Host")
  def getUserName: String =  sys.env("MCCassandra_User")
  def getPassword: String =  sys.env("MCCassandra_Password")
  def getMapper: MappingManager
}
class CassandraDbConnectorImpl extends CassandraDbConnector {
//  private val logger = Logger(this.getClass)
  private[this] val lock = new Object()
  private var session: Session =_
  private var mappingManager: MappingManager = _

  def getSession: Session = {
    if(session == null) {
      createSessionAndInitKeySpace(getHost, getPort, getKeySpace, getUserName, getPassword)
    }
    session
  }

  def closeSession(): Unit ={
    if(session != null) {
      session.close()
      mappingManager = null
      session = null
    }
  }

  def createSessionAndInitKeySpace(address: String, port: Int, keySpace: String, userName: String, password: String): Session = lock.synchronized {
    try {
      var hosts : Array[String] = Array(address)
      if(address.contains(",")) hosts = address.split(",").map(_.trim)

      val cluster = Cluster.builder().addContactPoint(getHost).withPort(9042).withCredentials(getUserName, getPassword).build()
      session = cluster.connect("minecraft")
      mappingManager = new MappingManager(session);
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

  def getMapper: MappingManager = {
    if(mappingManager == null){
      if(session == null){
        createSessionAndInitKeySpace(getHost, getPort, getKeySpace, getUserName, getPassword)
      }
      else{
        mappingManager = new MappingManager(session)
      }
    }
    mappingManager
  }

}
object CassandraDbConnector extends CassandraDbConnectorImpl
