import org.apache.spark.sql.SparkSession

object SensorETL {

  def main(args: Array[String]): Unit = {

    //"Client Transaction" (sensor timeseries) Datasets
    val tsIndexFilePath = "E:\\project\\data\\PPG_FieldStudy\\S1\\S1-PKL-CSV\\PKL-ECG-LABEL-MAIN-INDEX-INDEXED.csv"
    val tsActivityFilePath = "E:\\project\\data\\PPG_FieldStudy\\S1\\S1-PKL-CSV\\PKL-ACTIVITY-ENHANCED-TS-INDEXED.csv"

    //Reference Datasets
    val subjectRefAntropoPath = "E:\\project\\data\\PPG_FieldStudy\\S1\\S1_quest-transposed.csv"
    val activityRefPath = "E:\\project\\data\\PPG_FieldStudy\\S1\\ACTIVITY-REFERENCE-DATASET.csv"

    val spark = SparkSession.builder
      .master("local[*]")
      .appName("Sensor Timeseries Data ETL")
      .getOrCreate()

    val tsHrEcgMainIndexDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(tsIndexFilePath)

    val tsActivityEnhDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(tsActivityFilePath)

    val subjectRefAntropoDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(subjectRefAntropoPath)

    val activityRefDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(activityRefPath)

    tsHrEcgMainIndexDF.show()
    tsActivityEnhDF.show()

    subjectRefAntropoDF.show()
    activityRefDF.show()

    //join/align ("business transaction") sensor timeseries datasets, already indexed in a compatible way during
    //the pre-processing phase (with python ETL pipelines)
    var mainDF = tsHrEcgMainIndexDF.join(tsActivityEnhDF, tsHrEcgMainIndexDF("ts_seq_num") ===  tsActivityEnhDF("main_index"),"inner")

    mainDF.printSchema()
    mainDF.show()

    //enrich with Reference Data about the Subject ("Client/Trading Desk etc")
    mainDF = mainDF.crossJoin(subjectRefAntropoDF)
    mainDF.show()

    //enrich with Reference Data about Activity Names
    mainDF = mainDF.join(activityRefDF, mainDF("activity") ===  activityRefDF("activity_id"),"inner")
    mainDF.show()

    import org.apache.spark.sql.types._
    import org.apache.spark.sql.functions._

    //convert the ECG heart rate from double to integer
    mainDF = mainDF.withColumn("label",col("label").cast(IntegerType))
    mainDF.show()

    //Aggregation - calculate average heart rate (from the ECG sensor) during each Activity Type
    //mainDF.groupBy("activity_name").avg("label").show(true)
    val aggregatedHrDF = mainDF.groupBy("activity_name", "SUBJECT_ID")
      .agg(
        avg("label").as("avg_heart_rate"),
        max("label").as("max_heart_rate"),
        min("label").as("min_heart_rate"),
        stddev("label").as("standard_deviation_heart_rate"))

    //sorted by subject, by average heart rate
    aggregatedHrDF
      .withColumn("avg_heart_rate",col("avg_heart_rate").cast(IntegerType))
      .withColumn("max_heart_rate",col("max_heart_rate").cast(IntegerType))
      .withColumn("min_heart_rate",col("min_heart_rate").cast(IntegerType))
      .withColumn("standard_deviation_heart_rate",col("standard_deviation_heart_rate").cast(IntegerType))
      .sort("SUBJECT_ID", "avg_heart_rate")
      .show()


    spark.stop()

  }
}
