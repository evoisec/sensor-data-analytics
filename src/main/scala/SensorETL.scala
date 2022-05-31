import org.apache.spark.sql.SparkSession

object SensorETL {

  def main(args: Array[String]): Unit = {

    //"Client Transaction" (sensor timeseries) Datasets
    val tsIndexFilePath = "E:\\project\\data\\PPG_FieldStudy\\S1\\S1-PKL-CSV\\PKL-ECG-LABEL-MAIN-INDEX-INDEXED.csv"
    val tsActivityFilePath = "E:\\project\\data\\PPG_FieldStudy\\S1\\S1-PKL-CSV\\PKL-ACTIVITY-ENHANCED-TS-INDEXED.csv"
    val tsPpgHrPath = "E:\\project\\data\\PPG_FieldStudy\\S1\\S1-PKL-CSV\\PPG-HR-INDEXED.csv"
    val tsTempPath = "E:\\project\\data\\PPG_FieldStudy\\S1\\S1-PKL-CSV\\TEMP-INDEXED.csv"

    //Reference Datasets
    val subjectRefAntropoPath = "E:\\project\\data\\PPG_FieldStudy\\S1\\S1_quest-transposed.csv"
    val activityRefPath = "E:\\project\\data\\PPG_FieldStudy\\S1\\ACTIVITY-REFERENCE-DATASET.csv"

    //Output datasets
    val mainDFPath = "E:\\project\\data\\PPG_FieldStudy\\S1\\S1-PKL-CSV\\mainDF.csv"

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

    var tsHrPpgMainIndexDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(tsPpgHrPath)

    var tsTempIndexDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(tsTempPath)

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

    //write the main dataframe to CSV to serve as input to other ETLs
    mainDF = mainDF.select("label", "activity", "main_index", "SUBJECT_ID", "AGE",
      "GENDER", "HEIGHT", "WEIGHT", "SKIN", "SPORT", "activity_name", "activity_id")
    //mainDF.write.option("header",true).csv(mainDFPath)

    //Aggregation - calculate average heart rate (from the ECG sensor) during each Activity Type
    //mainDF.groupBy("activity_name").avg("label").show(true)
    val aggregatedHrDF = mainDF.groupBy("activity_name", "SUBJECT_ID")
      .agg(
        avg("label").as("avg_heart_rate"),
        max("label").as("max_heart_rate"),
        min("label").as("min_heart_rate"),
        stddev("label").as("standard_deviation_heart_rate"))

    //The aggregation, sorted by subject, then by average heart rate
    aggregatedHrDF
      .withColumn("avg_heart_rate",col("avg_heart_rate").cast(IntegerType))
      .withColumn("max_heart_rate",col("max_heart_rate").cast(IntegerType))
      .withColumn("min_heart_rate",col("min_heart_rate").cast(IntegerType))
      .withColumn("standard_deviation_heart_rate",col("standard_deviation_heart_rate").cast(IntegerType))
      .sort("SUBJECT_ID", "avg_heart_rate")
      .show()


    tsHrPpgMainIndexDF.show()

    tsHrPpgMainIndexDF = tsHrPpgMainIndexDF.groupBy("main_index").avg("ppg_hr").sort("main_index")
    tsHrPpgMainIndexDF.show()

    val corrDF = tsHrEcgMainIndexDF.join(tsHrPpgMainIndexDF, tsHrEcgMainIndexDF("ts_seq_num") ===  tsHrPpgMainIndexDF("main_index"),"inner")
    corrDF.show()

    printf("The Correlation Coefficient between ECG HR and PPG HR = %f", corrDF.stat.corr("label", "avg(ppg_hr)"))

    println(tsHrPpgMainIndexDF.count())
    println(tsHrEcgMainIndexDF.count())

    tsTempIndexDF.show()
    tsTempIndexDF = tsTempIndexDF.groupBy("main_index").avg("temperature").sort("main_index")
    tsTempIndexDF.show()

    //var corrMainDF = corrDF.join(tsTempIndexDF, corrDF("main_index") ===  tsTempIndexDF("main_index"),"inner")
    //corrMainDF.show()
    var corrMainDF = corrDF.join(tsTempIndexDF, Seq("main_index"))
    corrMainDF.show()

    corrMainDF = corrMainDF.withColumnRenamed("label","ecg_hr").drop("ts_seq_num").sort("main_index")
    corrMainDF.show()

    spark.stop()

  }
}
