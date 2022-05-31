import org.apache.spark.sql.SparkSession

object SensorETL {

  def main(args: Array[String]): Unit = {

    val tsIndexFilePath = "E:\\project\\data\\PPG_FieldStudy\\S1\\S1-PKL-CSV\\PKL-ECG-LABEL-MAIN-INDEX-INDEXED.csv"
    val tsActivityFilePath = "E:\\project\\data\\PPG_FieldStudy\\S1\\S1-PKL-CSV\\PKL-ACTIVITY-ENHANCED-TS-INDEXED.csv"
    val subjectAntropoPath = "E:\\project\\data\\PPG_FieldStudy\\S1\\S1_quest-transposed.csv"

    val spark = SparkSession.builder
      .master("local[*]")
      .appName("Sensor Data ETL")
      .getOrCreate()

    val tsHrEcgMainIndexDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(tsIndexFilePath)

    val tsActivityEnhDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(tsActivityFilePath)

    val subjectAntropoDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(subjectAntropoPath)

    tsHrEcgMainIndexDF.show()
    tsActivityEnhDF.show()
    subjectAntropoDF.show()

    val mainDF = tsHrEcgMainIndexDF.join(tsActivityEnhDF, tsHrEcgMainIndexDF("ts_seq_num") ===  tsActivityEnhDF("main_index"),"inner")

    mainDF.printSchema()
    mainDF.show()

    //enrich with Reference Data about the Subject ("Client/Trading Desk etc")
    mainDF.crossJoin(subjectAntropoDF).show()

    spark.stop()

  }
}
