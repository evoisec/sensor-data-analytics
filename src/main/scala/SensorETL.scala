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

    //join timeseries datasets, already indexed in a compatible way
    var mainDF = tsHrEcgMainIndexDF.join(tsActivityEnhDF, tsHrEcgMainIndexDF("ts_seq_num") ===  tsActivityEnhDF("main_index"),"inner")

    mainDF.printSchema()
    mainDF.show()

    //enrich with Reference Data about the Subject ("Client/Trading Desk etc")
    mainDF = mainDF.crossJoin(subjectRefAntropoDF)
    mainDF.show()

    //enrich with Reference Data about Activity Names
    mainDF = mainDF.join(activityRefDF, mainDF("activity") ===  activityRefDF("activity_id"),"inner")
    mainDF.show()

    //calculate average heart rate (from the ECG sensor) during each Activity Type
    mainDF.groupBy("activity_name").avg("label").show(true)


    spark.stop()

  }
}
