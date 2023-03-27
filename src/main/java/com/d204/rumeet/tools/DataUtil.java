package com.d204.rumeet.tools;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class DataUtil {

    public void load() {

        SparkConf sparkConf = new SparkConf()
                .setAppName("Rumeets")
                .setMaster("spark://j8d204.p.ssafy.io:7077")
                .set("spark.executor.memory", "4g")
                .set("spark.driver.url", "127.0.0.1")
                .set("spark.driver.host", "127.0.0.1");

        SparkSession spark = SparkSession
                .builder()
                .config(sparkConf)
                .getOrCreate();

        System.out.println("1234");
        Dataset<Row> data = spark.read()
                .format("parquet")
                .option("header", "true")
                .load("hdfs://j8d204.p.ssafy.io:9000/user/spark/output");
        data.show();
        System.out.println("90-=");

        spark.stop();

    }

}