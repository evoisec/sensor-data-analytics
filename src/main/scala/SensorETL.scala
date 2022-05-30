import org.apache.spark.sql.SparkSession

object SensorETL {

  def main(args: Array[String]): Unit = {

    val tsIndexFilePath = "E:\\project\\data\\PPG_FieldStudy\\CSV\\PKL-ECG-LABEL-MAIN-INDEX-INDEXED.csv"
    val tsActivityFilePath = "E:\\project\\data\\PPG_FieldStudy\\CSV\\PKL-ACTIVITY-ENHANCED-TS-INDEXED.csv"

    val spark = SparkSession.builder.master("local[*]").appName("Sensor Data ETL").getOrCreate()

    val tsHrEcgMainIndexDF = spark.read.option("header", "true").csv(tsIndexFilePath)
    val tsActivityEnhDF = spark.read.option("header", "true").csv(tsActivityFilePath)

    tsHrEcgMainIndexDF.show()
    tsActivityEnhDF.show()

    val mainDF = tsHrEcgMainIndexDF.join(tsActivityEnhDF, tsHrEcgMainIndexDF("ts_seq_num") ===  tsActivityEnhDF("main_index"),"inner")

    mainDF.show()

    spark.stop()

  }
}
