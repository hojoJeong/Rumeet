package com.d204.rumeet.tools;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;


@Component
public class SpringApp {

    public void load() {
        SparkSession spark = SparkSession
                .builder()
                .appName("Rumeet")
                .master("spark://j8d204.p.ssafy.io:7077")
                .getOrCreate();

        Dataset<Row> data = spark.read()
                .format("parquet")
                .option("header", "true")
                .load("hdfs://j8d204.p.ssafy.io:9000/user/spark/output");

        data.show();

        spark.stop();

    }

}