from fastapi import FastAPI
from pyspark.sql import SparkSession
from pyspark.sql.functions import col

app = FastAPI()

@app.get("/load/{id}")
async def rootd(id):
        global data_df

        # 작업 하면됨
        data_df.show()
        return {"message": "dg"}

@app.get("/cache")
async def root():
        global data_df
    # SparkSession 생성
        spark = SparkSession.builder \
        .appName("ReadParquetFromHDFS") \
        .master("spark://j8d204.p.ssafy.io:7077") \
        .getOrCreate()

# HDFS에서 파케이 파일 읽기
        data_df = spark.read \
        .format("parquet") \
        .option("header", "true") \
        .load("hdfs://13.125.218.237:9000/user/spark/output")
        data_df.cache()
        data_df.show()
        return {"message": "hi"}


@app.get("/munang")
async def root():
    return {"message": "무냉"}