package bdt.project;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;



public class HBaseClient{

    private final String ZOOKEEPER_QUORUM = "zoo";
    private static String TABLE_NAME_BEHAVIOR = "behavior";
    private static String TABLE_NAME_USER = "demographic";
    private static final String CF1 = "1";
    private static final String CF2 = "2";
    private static final String CF3 = "3";
    
    private Configuration config;
    private Connection connection;

    /*
     * Create a new table. If it is already existed, re-create a new one.
     */
    public void createTableIfNotExists(String tableName) throws IOException {

        try(Admin admin = connection.getAdmin()) {
            HTableDescriptor table = new HTableDescriptor(TableName.valueOf(tableName));

            table.addFamily(new HColumnDescriptor(CF1).setCompressionType(Algorithm.NONE));
            table.addFamily(new HColumnDescriptor(CF2).setCompressionType(Algorithm.NONE));
            table.addFamily(new HColumnDescriptor(CF3).setCompressionType(Algorithm.NONE));

            if (admin.tableExists(table.getTableName())) {
                System.out.println("Table [" + table.getTableName().getNameAsString() + "] is already existed.");
            } else {
                System.out.print("Creating new table... " + tableName);
                admin.createTable(table);
                System.out.println("Done.");
           }
        }
    }
    
    
    public Connection getConnection() {
    	return connection;
    }

    public void setUp() throws IOException {
        config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", ZOOKEEPER_QUORUM);
        connection = ConnectionFactory.createConnection(config);
    }
	
    public void close() throws IOException {
        if( (connection != null) && (!connection.isClosed())) connection.close();
    }    

    public static void main(String[] args) throws IOException {
    	HBaseClient hc = new HBaseClient();
        hc.setUp();
        hc.createTableIfNotExists(TABLE_NAME_BEHAVIOR);
        hc.createTableIfNotExists(TABLE_NAME_USER);
        hc.close();
    }
}
