from fastapi import FastAPI
from pyspark.sql import SparkSession
from pyspark.sql.functions import col
from pyspark.sql.functions import mean

app = FastAPI()

@app.get("/load/{id}")
async def rootd(id):
        global data_df
        global pace1_avg
        pace = []
        # 작업 하면됨
        pace1_avg.filter(pace1_avg["user_id"] == id).show()
        pace.append(int(pace1_avg.select('avg_pace1').collect()[0][0]))


        return {"id": id , "pace":pace}

@app.get("/cache")
async def root():
        global data_df ,pace1_avg
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
                .agg(mean('pace1').alias('avg_pace1'),
                     mean('elapsed_time').alias('avg_elapsed_time'),
                     mean('average_heart_rate').alias('avg_heart_rate'))
        # pace1_avg.cache()
        return {"pace":"dd"}


@app.get("/munang")
async def root():
    return {"message": "무냉"}
