import math
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


@app.get("/ghost/{user_id}/{mode}")
async def get_ghost_user(user_id, mode):
    global pace1_avg, pace2_avg, pace3_avg, pace5_avg
    
    distances = [1, 2, 3, 5]
    distance_idx = int(mode) % 4
    km = distances[distance_idx]

    if km == 1:
        result = search_similarity(1, user_id, pace1_avg)
    elif km == 2:
        result = search_similarity(2, user_id, pace2_avg)
    elif km == 3:
        result = search_similarity(3, user_id, pace3_avg)
    elif km == 5:
        result = search_similarity(5, user_id, pace5_avg)
    return result


def search_similarity(km, user_id, cached_pace_list):
    space = km
    users_paces = get_user_paces(user_id, cached_pace_list, km)
    other_users_paces = get_other_users_paces(user_id, cached_pace_list, km)
    top_val = 0
    ghost_id = 0
    for pace in range(0, len(other_users_paces), space):
        similarities = calculateEuclideanSimilarity(users_paces, other_users_paces[pace:pace+space])
        if similarities >= 0.01 and top_val < similarities:
            top_val = similarities
            ghost_users_paces = other_users_paces[pace:pace+space]
            ghost_id = pace / space            
    result = {
        'ghost_user_id': ghost_id,
        'ghost_users_paces': ghost_users_paces,
    }
    return result


def get_user_paces(user_id, cached_pace_list, km):
    paces_col = [f"avg_pace{i}" for i in range(1, km+1)]
    users_paces = cached_pace_list.filter(cached_pace_list["user_id"] == user_id).select(*paces_col).rdd.flatMap(lambda x: x).collect()
    return users_paces


def get_other_users_paces(user_id, cached_pace_list, km):
    paces_col = [f"avg_pace{i}" for i in range(1, km+1)]
    other_users_paces = cached_pace_list.filter(cached_pace_list["user_id"] != user_id).select(*paces_col).rdd.flatMap(lambda x: x).collect()
    return other_users_paces


def calculateEuclideanSimilarity(user1_pace_list, user2_pace_list):
    distance = 0
    for i in range(len(user1_pace_list)):
        distance += math.pow(user1_pace_list[i] - user2_pace_list[i], 2)
    return 1 / (1 + math.sqrt(distance))


