import org.apache.spark.sql.SparkSession

object SensorETL {

  def main(args: Array[String]): Unit = {

    val tsIndexFilePath = "E:\\project\\data\\PPG_FieldStudy\\CSV\\PKL-ECG-LABEL-MAIN-INDEX-INDEXED.csv"

    val spark = SparkSession.builder.master("local[*]").appName("Sensor Data ETL").getOrCreate()

    val tsHRECGMainIndexDF = spark.read.option("header", "true").csv(tsIndexFilePath)

    tsHRECGMainIndexDF.show()

    spark.stop()

  }
}
