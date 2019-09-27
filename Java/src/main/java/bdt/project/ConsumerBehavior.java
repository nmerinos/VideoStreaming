package bdt.project;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import java.util.*;

import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka010.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;
import org.json.JSONObject;

import org.apache.hadoop.hbase.client.Table;



public class ConsumerBehavior 
{
	private Connection connection;
	private String TABLE_NAME = "behavior";

	public static void main(String[] args) throws Exception
    {
		
		HBaseClient hc = new HBaseClient();
		hc.setUp();
		ConsumerBehavior consumer = new ConsumerBehavior();
		consumer.connection = hc.getConnection();
		
		JavaStreamingContext streamingContext = new JavaStreamingContext(
				new SparkConf()
				.setAppName("jsonPRocessing")
				.set("spark.driver.host", "localhost")
				.set("spark.testing.memory", "3147480000")
				.setMaster("local[1]"),				
				new Duration(1000));
		
		streamingContext.sparkContext().setLogLevel("OFF");
		Map<String, Object> kafkaParams = new HashMap<>();
		kafkaParams.put("bootstrap.servers", "kafka:9092");
		kafkaParams.put("key.deserializer", StringDeserializer.class);
		kafkaParams.put("value.deserializer", StringDeserializer.class);
		kafkaParams.put("group.id", "demographics");
		kafkaParams.put("auto.offset.reset", "latest");
		kafkaParams.put("enable.auto.commit", false);

		Collection<String> topics = Arrays.asList("behavior");

		JavaInputDStream<ConsumerRecord<String, String>> stream =
		  KafkaUtils.createDirectStream(
		    streamingContext,
		    LocationStrategies.PreferConsistent(),
		    ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams)
		  );
		
		
		JavaDStream<String> dStream =
				stream.map(a -> a.value());
		
		JavaDStream<String> windowedStream = dStream.window(new Duration(1000));
		
		windowedStream.foreachRDD(new VoidFunction<JavaRDD<String>>() {

			@Override
			public void call(JavaRDD<String> rdd) throws Exception {
				String num = "";
				if(!rdd.take(1).isEmpty()){
					rdd.foreach(r -> {
						
						Configuration config;
					    Connection connection;
					    
					    try {
					    	String json = "{" + r.toString().split("\\{", 2)[1];
							JSONObject obj = new JSONObject(json);
							
							String session_id = obj.get("session_id").toString();
							String visitor_id = obj.get("visitor_id").toString();
							String time = obj.get("time").toString();
							
							String type = obj.get("type").toString();
							String playerkey = obj.get("playerkey").toString();
							String video_id = obj.get("video_id").toString();
							String https = obj.get("https").toString();
							
							String action = obj.get("action").toString();
							String content_time = obj.get("content_time").toString();
							String content_total_time = obj.get("content_total_time").toString();
							System.out.println("content_total_time: " + content_total_time);
							
							config = HBaseConfiguration.create();
					        config.set("hbase.zookeeper.quorum", "zoo");
					        connection = ConnectionFactory.createConnection(config);
							
							try(Admin admin = connection.getAdmin()) {
								try (Table table = connection.getTable(TableName.valueOf("behavior"))) {
									
									String C1 = "1";
									String C2 = "2";
									String C3 = "3";
									
									Put buf = new Put(Bytes.toBytes(visitor_id + "-" + session_id + "-" + time));
									
									buf.addColumn(Bytes.toBytes(C1), Bytes.toBytes("session_id"), Bytes.toBytes(session_id));
									buf.addColumn(Bytes.toBytes(C1), Bytes.toBytes("visitor_id"), Bytes.toBytes(visitor_id));
									buf.addColumn(Bytes.toBytes(C1), Bytes.toBytes("time_event"), Bytes.toBytes(time));
									
									
									buf.addColumn(Bytes.toBytes(C2), Bytes.toBytes("type"), Bytes.toBytes(type));
									buf.addColumn(Bytes.toBytes(C2), Bytes.toBytes("playerkey"), Bytes.toBytes(playerkey));
									buf.addColumn(Bytes.toBytes(C2), Bytes.toBytes("video_id"), Bytes.toBytes(video_id));
									buf.addColumn(Bytes.toBytes(C2), Bytes.toBytes("https"), Bytes.toBytes(https));
									
									
									buf.addColumn(Bytes.toBytes(C3), Bytes.toBytes("action"), Bytes.toBytes(action));
									buf.addColumn(Bytes.toBytes(C3), Bytes.toBytes("content_time"), Bytes.toBytes(content_time));
									buf.addColumn(Bytes.toBytes(C3), Bytes.toBytes("content_total_time"), Bytes.toBytes(content_total_time));
									
									table.put(buf);
									table.close();
								}
							}
					    }
					    catch(Exception e) {
					    	System.out.println("There is a error here ConsumerBehavior: " + r.toString() + " and the error is: " + e.getStackTrace());
					    }
					});
				}
			}});

	    	
		streamingContext.start();
		streamingContext.awaitTermination();
		
    }
}
