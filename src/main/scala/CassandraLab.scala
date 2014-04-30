import com.netflix.astyanax.model.ColumnFamily
import com.netflix.astyanax.serializers.StringSerializer
import com.netflix.astyanax.{Keyspace, AstyanaxContext}
import com.netflix.astyanax.connectionpool.impl.{CountingConnectionPoolMonitor, ConnectionPoolConfigurationImpl}
import com.netflix.astyanax.connectionpool.NodeDiscoveryType
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl
import com.netflix.astyanax.thrift.ThriftFamilyFactory
import scala.collection.JavaConverters._


object CassandraLab {
  def main(args: Array[String]): Unit = {
    println("Hello, world!")

    /*
    val cluster = Cluster.builder()
      .addContactPoint("data01")
      .withPoolingOptions(
        new PoolingOptions()
              .setMaxConnectionsPerHost( HostDistance.REMOTE, 10 )
      ).build()

    val metadata = cluster.getMetadata()
    printf("Connected to cluster: %s\n", metadata.getClusterName())

    metadata.getAllHosts().asScala.foreach { host =>
      printf("Datacenter: %s; Host: %s; Rack: %s\n",
        host.getDatacenter(), host.getAddress(), host.getRack())
    }


    val session = cluster.connect("event")

    session.execute("")
run

    cluster.close()
    */

    val context = new AstyanaxContext.Builder()
      .forCluster("data01")
      .forKeyspace("event")
      .withAstyanaxConfiguration(
        new AstyanaxConfigurationImpl()
              .setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE)
      ).withConnectionPoolConfiguration(
        new ConnectionPoolConfigurationImpl("MyConnectionPool")
          .setPort(9160)
          .setMaxConnsPerHost(2)
          .setSeeds("data01:9160")
      ).withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
      .buildKeyspace(ThriftFamilyFactory.getInstance())

    context.start()
    val keyspace = context.getClient()

    val CF_TEST:ColumnFamily[String, String] = ColumnFamily
      .newColumnFamily(
        "test1"
        , StringSerializer.get()
        , StringSerializer.get()
      )

    keyspace.dropColumnFamily(CF_TEST)
    val tt = Map(
      "key_validation_class"     -> "UTF8Type",
      "comparator_type"          -> "UTF8Type",
      "default_validation_class" -> "UTF8Type"
    )
    keyspace.createColumnFamily(CF_TEST, tt.toMap[String,Object].asJava)
    keyspace.create


    val m = keyspace.prepareMutationBatch()

    m.withRow(CF_TEST, "1")
      .putColumn("c1-1","cv1-1",null)
      .putColumn("c1-2","cv1-2",null)
      //.putColumn("c1-3","cv1-3",null)

    m.execute()

  }
}
