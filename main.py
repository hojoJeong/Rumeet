from fastapi import FastAPI
from pyspark.sql import SparkSession
from pyspark.sql.functions import col
from pyspark.sql.functions import mean

app = FastAPI()

pace1_avg = None
df_1km = None
df_2km = None
df_3km = None
df_5km = None
pace2_avg = None
pace3_avg = None
pace5_avg = None

@app.get("/load/{km}/{id}")
async def rootd(km, id):
        global pace1_avg, df_1km, df_2km, df_3km, df_5km, pace2_avg, pace3_avg, pace5_avg
        pace = []

        if km == "1": # 1km
            pace_value = pace1_avg.filter(pace1_avg["user_id"] == id) \
                    .select('avg_pace1') \
                    .collect()[0][0]
            pace.append(int(pace_value))
            print(pace_value)
        elif km =="2": # 2km
            print("km : 5")
            pace2_avg.filter(pace2_avg["user_id"] == id) \
                .select('avg_pace1', 'avg_pace2')\
                .show()
            filtered = pace2_avg.filter(pace2_avg["user_id"] == id) \
                .select('avg_pace1', 'avg_pace2') \
                .rdd.flatMap(lambda x: x) \
                .collect()
            pace = filtered
            print(filtered)
        elif km == "3": # 3km
            filtered = pace3_avg.filter(pace3_avg["user_id"] == id) \
                .select('avg_pace1', 'avg_pace2', 'avg_pace3') \
                .rdd.flatMap(lambda x: x) \
                .collect()
            pace = filtered
            print(filtered)
        elif km == "5": # 5km
            filtered = pace5_avg.filter(pace5_avg["user_id"] == id) \
                .select('avg_pace1', 'avg_pace2', 'avg_pace3', 'avg_pace4', 'avg_pace5') \
                .rdd.flatMap(lambda x: x) \
                .collect()
            pace = filtered
            print(filtered)
        return {"id": id,
                "pace": pace}

@app.get("/cache")
async def root():
        global pace1_avg, df_1km, df_2km, df_3km, df_5km, pace2_avg, pace3_avg, pace5_avg
    # SparkSession 생성
        spark = SparkSession.builder \
        .appName("ReadParquetFromHDFS2") \
        .master("spark://j8d204.p.ssafy.io:7077") \
        .getOrCreate()

    # HDFS에서 파케이 파일 읽기
        if df_1km is not None:
            df_1km.unpersist()
        new_df_1km = spark.read \
        .format("parquet") \
        .option("header", "true") \
        .load("hdfs://13.125.218.237:9000/user/spark/output/1km")
        df_1km = new_df_1km.cache()

        if pace1_avg is not None:
            pace1_avg.unpersist()
        new_pace1_avg = df_1km.groupBy('user_id') \
                .agg((mean('pace1').cast('integer')).alias('avg_pace1'))
        pace1_avg = new_pace1_avg.cache()
        pace1_avg.show()

        if df_2km is not None:
            df_2km.unpersist()
        new_df_2km = spark.read \
                .format("parquet") \
                .option("header", "true") \
                .load("hdfs://13.125.218.237:9000/user/spark/output/2km")
        df_2km = new_df_2km.cache()

        if pace2_avg is not None:
            pace2_avg.unpersist()
        new_pace2_avg = df_2km.groupBy('user_id') \
                .agg((mean('pace1').cast('integer')).alias('avg_pace1'),
                     (mean('pace2').cast('integer')).alias('avg_pace2'))
        pace2_avg = new_pace2_avg.cache()
        pace2_avg.show()

        if df_3km is not None:
            df_3km.unpersist()
        new_df_3km = spark.read \
                .format("parquet") \
                .option("header", "true") \
                .load("hdfs://13.125.218.237:9000/user/spark/output/3km")
        df_3km = new_df_3km.cache()

        if pace3_avg is not None:
            pace3_avg.unpersist()
        new_pace3_avg = df_3km.groupBy('user_id') \
            .agg((mean('pace1').cast('integer')).alias('avg_pace1'),
                 (mean('pace2').cast('integer')).alias('avg_pace2'),
                 (mean('pace3').cast('integer')).alias('avg_pace3'))
        pace3_avg = new_pace3_avg.cache()
        pace3_avg.show()

        if df_5km is not None:
            df_5km.unpersist()
        new_df_5km = spark.read \
                .format("parquet") \
                .option("header", "true") \
                .load("hdfs://13.125.218.237:9000/user/spark/output/5km")
        df_5km = new_df_5km.cache()

        if pace5_avg is not None:
            pace5_avg.unpersist()
        new_pace5_avg = df_5km.groupBy('user_id') \
            .agg((mean('pace1').cast('integer')).alias('avg_pace1'),
                 (mean('pace2').cast('integer')).alias('avg_pace2'),
                 (mean('pace3').cast('integer')).alias('avg_pace3'),
                 (mean('pace4').cast('integer')).alias('avg_pace4'),
                 (mean('pace5').cast('integer')).alias('avg_pace5'))
        pace5_avg = new_pace5_avg.cache()
        pace5_avg.show()

        return {"flag":"success"}


@app.get("/munang")
async def root():
    return {"message": "무냉"}

~
