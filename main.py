from fastapi import FastAPI
from pyspark.sql import SparkSession
from pyspark.sql.functions import col
from pyspark.sql.functions import mean

app = FastAPI()

@app.get("/load/{mode}/{id}")
async def rootd(mode, id):
        global data_df, pace1_avg, df_1km, df_2km, df_3km, df_5km, pace2_avg, pace3_avg, pace5_avg
        pace = []

        print(type(mode))
        pace1_avg.show()

        # pace_value = pace1_avg.filter(pace1_avg["user_id"] == id) \
        #         .select('avg_pace1') \
        #         .collect()[0][0]
        # pace.append(int(pace_value))
        # print(pace_value)
        #
        # return {"id": id , "pace":pace}

        if mode == "4":
            pace1_avg.show()
            pace_value = pace1_avg.filter(pace1_avg["user_id"] == id) \
                    .select('avg_pace1') \
                    .collect()[0][0]
            pace.append(int(pace_value))
            print(pace_value)
        elif mode =="5":
            print(pace2_avg.columns)
            print("mode : 5")
            pace2_avg.filter(pace2_avg["user_id"] == id) \
                .select('avg_pace1', 'avg_pace2')\
                .show()
            filtered = pace2_avg.filter(pace2_avg["user_id"] == id) \
                .select('avg_pace1', 'avg_pace2') \
                .collect()[0][0]
            pace.append(filtered)
            print(filtered)
        elif mode == "6":
            filtered = pace3_avg.filter(pace3_avg["user_id"] == id) \
                .select('avg_pace1', 'avg_pace2', 'avg_pace3') \
                .collect()[0][0]
            pace.append(int(filtered))
            print(filtered)
        elif mode == "7":
            print('5km data is empty')
            #df_5km.show()

        return {"id": id,
                "mode": mode,
                "pace": pace}

        pace_value = pace1_avg.filter(pace1_avg["user_id"] == id) \
                .select('avg_pace1') \
                .collect()[0][0]
        pace.append(int(pace_value))

        return {"id": id , "pace":pace}

@app.get("/cache")
async def root():
        global data_df, pace1_avg, df_1km, df_2km, df_3km, df_5km, pace2_avg, pace3_avg, pace5_avg
    # SparkSession 생성
        spark = SparkSession.builder \
        .appName("ReadParquetFromHDFS") \
        .master("spark://j8d204.p.ssafy.io:7077") \
        .getOrCreate()

# HDFS에서 파케이 파일 읽기
        data_df = spark.read \
        .format("parquet") \
        .option("header", "true") \
        .load("hdfs://13.125.218.237:9000/user/spark/output/1km")
        data_df.cache()

        pace1_avg = data_df.groupBy('user_id') \
                .agg((mean('pace1').cast('integer')).alias('avg_pace1'),
                     (mean('elapsed_time').cast('integer')).alias('avg_elapsed_time'),
                     (mean('average_heart_rate').cast('integer')).alias('avg_heart_rate'))
        pace1_avg.cache()

        pace1_avg.show()

        df_2km = spark.read \
                .format("parquet") \
                .option("header", "true") \
                .load("hdfs://13.125.218.237:9000/user/spark/output/2km")
        df_2km.cache()

        pace2_avg = df_2km.groupBy('user_id') \
                .agg((mean('pace1').cast('integer')).alias('avg_pace1'),
                     (mean('pace2').cast('integer')).alias('avg_pace2'),
                     (mean('elapsed_time').cast('integer')).alias('avg_elapsed_time'),
                     (mean('average_heart_rate').cast('integer')).alias('avg_heart_rate'))
        pace2_avg.cache()

        pace2_avg.show()

        df_3km = spark.read \
                .format("parquet") \
                .option("header", "true") \
                .load("hdfs://13.125.218.237:9000/user/spark/output/3km")
        df_3km.cache()

        pace3_avg = df_3km.groupBy('user_id') \
            .agg((mean('pace1').cast('integer')).alias('avg_pace1'),
                 (mean('pace2').cast('integer')).alias('avg_pace2'),
                 (mean('pace3').cast('integer')).alias('avg_pace3'),
                 (mean('elapsed_time').cast('integer')).alias('avg_elapsed_time'),
                 (mean('average_heart_rate').cast('integer')).alias('avg_heart_rate'))
        pace3_avg.cache()

        pace3_avg.show()

        # df_5km = spark.read \
        #         .format("parquet") \
        #         .option("header", "true") \
        #         .load("hdfs://13.125.218.237:9000/user/spark/output/5km")
        # df_5km.cache()
        #
        # pace5_avg = df_5km.groupBy('user_id') \
        #     .agg(mean('pace1').alias('avg_pace1'),
        #          mean('elapsed_time').alias('avg_elapsed_time'),
        #          mean('average_heart_rate').alias('avg_heart_rate'))
        # pace5_avg.cache()

        return {"pace":"dd"}


@app.get("/munang")
async def root():
    return {"message": "무냉"}

