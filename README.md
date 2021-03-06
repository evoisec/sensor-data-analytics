## Data Model

"Business Transaction" timeseries Datasets:

a) Body Sensor Signal Timeseries Datasets (a form of "Business Transaction" datasets)

b) Activity Label Timeseries Datasets (timeseries of Activities performed by the Subjects over time - Activity Labels which in turn, taken on their own are a form of Reference Data)

Reference Datasets:

a) Activity Labels - used to enrich/tag the Sensor Signal Timeseries data points as belonging to specific type of Subject Activity 

b) Test Subject Anthropometric Data and Test Subject ID

click on the following image to enlarge it:

![Data Model](https://github.com/evoisec/sensor-data-analytics/blob/master/doc/data-model2.jpg)

## Data Model Analysis

### Front Office Quant Trading / Risk Management, Analytical Model of the Data

The datasets can be interpreted and organized along a Predictive/Analytical Model intended to quantify what factors influence the correlation/deviation between ECH HR and PPG HR. As the key objective is getting the PPG HR as close to ECG HR (the ground truth) as possible 

The ECH HR provides the Ground Truth ie as the Prediction/Optimization Objective/Variable/Label

Sensor Timeseries Data - Provides the Numerical Predictive Variables in the Model. PPG HR is of key interest in terms of identifying how the rest of the variables influence its correlation / close match with the ECG HR. This would lead to improved PPG devices/sensors - the key objective of the research.

Reference Data - Provides the Categorical Predictive Variables in the Model

The data can then be interpreted and organized as part of the following types of quant models:

1. Predictive / Regression Models, quantifying the influence of each predictive variable on the predictive error when trying to match/fit the ECG HR as close as possible

Regression Model Equation (cam be implemented with predictive models ranging from standard linear regression all the way up to Markov Models and Deep Learning Neural Networks):

EcgHR = a\*PpgHR + b\*Temperature + c\*RespiratoryRate + d\*Acceleration + e\*ElectroDermalActivity + f\*ActivityType + g\*SubjectCharacteristics

from a data processing /wrangling point of view, such model (including the following models) would require the alignment and incorporation of all timeseries dataset into a common/joine dataframe

2. Correlations (including Conditional Correlations) between input and output variables

3. Conditional Probabilities of e.g. the Photoplethysmograph HR being correlated with the ECG HR, subject to e.g. Body Acceleration

4. Timeseries Regimes yielding different predictive models

5. Statistical Summaries/Aggregations/Distributions etc providing complementary insights about how to improve the Photoplethysmograph HR device


click on the image to enlarge

![Data Model](https://github.com/evoisec/sensor-data-analytics/blob/master/doc/fo-model2.jpg)

example of sensor timeseries aligned through common, main index into single dataframe, produced by spark/databricks job and ready for machine learning modeling and statistical analyses

![Data Model](https://github.com/evoisec/sensor-data-analytics/blob/master/doc/aligned-ts.jpg)

### Data Indexing/Positioning inside Dataset, Dataset Synchronization and Dataset Join Algorithms 

The "business" analytics objective of the project is improvement of Photoplethysmography (PPG) - optical method widely used nowadays for continuous heart rate monitoring (ie instead of ECG).

Photoplethysmography is still a technically challenging area and hence to enable further research and data analysis, the Photoplethysmography signal data is complemented with CORRELATED/supporting signal datasets of other modalities - body worn accelerometer, temperature, respiration, etc sensor signal timeseries datasets 

My business interpretation is that it is hoped that the CORRELATED sensor signal datasets, when used together with the PPG signal dataset, would improve the PPG sensor design and signal processing. The correlated signal data would provide CORRECTIVE information enabling that. Hence the alignment of these timeseries dataset becomes key data processing task. This is further complicated by the fact that the signal timeseries lack absolute timestamps for the individual data points they contain and also the fact that the signals are sampled/measured at different frequencies (as expressed in Hz) 

Once correlated, the signal timeseries can also then be used to build a common (spark) dataframe, which would enable Multivariete Analytics involving all variables/columns of the dataset  

The sensor timeseries are complemented with Reference data consisting of Labels of Type of Activities performed by the test subject during specific time periods within sensor signal timeseries dataset

The presence of several CORRELATED, different, sensor signal, time series datasets requires a method/algorithm for their synchronization/join, to enable their joined analysis / study with the business objective of improvement PPG   

This would require methods / algorithms for Data Indexing and Joining, overcoming the lack of absolute timestamps for individual data points and the different signal sampling/measurement frequencies

#### Cross Dataset Indexing - Indexing/Synchronization/Join Across Datasets

MAIN INDEX - The Heart Rate (as the key subject of research/"business analytics" here, due to the nature of PPG) from the ECG Sensor is going to be used as the Ground Truth or will provide the Main Index for joining/aligning all timeseries datasets

The ECG Heart Rate based Cross-Timeseries Main Index will be provided as the mean of the ECG-based instantaneous heart rate, given on a sliding window of 8 seconds, shifted with 2 seconds. Each such derived ECG timeseries datapoint would then become a common Reference OFFSET (and hence Index) within all other timeseries datasets 

The ECH Heart Rate signal dataset is generated by the RespiBAN, chest worn device 

#### Internal Dataset Indexing - Indexing within Single Dataset

Then, there is a need for Data Indexing method/algorithm within each sensor signal timeseries dataset. As that internal index, would then be further transformed into and aligned with the MAIN INDEX 

all timeseries datasets for the same subject/person have the same start time, the data points can be indexed as offsets from the start time 

secondly the following formula will be used: sampling-rate(Hz) = number of timeseries data points per 1 second   

for example if the sampling rate is 2 Hz, then the first 2 data points in the timeseries will be indexed with the key 1. This would signify that these data points were generated during second 1 (as offest from the start)

the next 2 data points will be indexed with 2 (as having occurred during second 2) and so on and so forth 

finally, each data point will have a sequence number (#) attribute 

|#| Timeseries Data Point Value | Internal Index |
|--| -- | ------------- |
|1| 12 | 1  |
|2| 14 | 1  |
|3| 45 | 2  |
|4| 10 | 2  |
|5| 465 | 3  |
|6| 106 | 3  |

The Main Index will be assigned at every 2 seconds as monotonically increasing value to match the ECG Main Index, 2 seconds shift step of the 8 seconds sliding window

|#| Timeseries Data Point Value | Internal Index | Main Index |
|--| -- | ------------- |-------------|
|1| 12 | 1  |1|
|2| 14 | 1  |1|
|3| 45 | 2  |1|
|4| 10 | 2  |1|
|5| 465 | 3  |2|
|6| 106 | 3  |2|

Spark/Databricks Dataframe consisting of joined timeseries datasets (the join leverages the above Timeseries Indexing Model), also enriched with Reference Data about Subjects and Activities

(click on the image to enlarge)

![Spark Dataframe](https://github.com/evoisec/sensor-data-analytics/blob/master/doc/spark-dataframe.jpg)

## Physical Dataset Catalogue

https://github.com/evoisec/sensor-data-analytics/blob/master/doc/data-model2.jpg

in a real project, textual description can be provided as well 

## Use Case Scenarios and Functional Architecture 

Export ib csv, the timeseries which reside in the binary (serialized) python dictionary dataset in pickle format - these the timeseries from the ResBAN chest device. They are also available in h5 serialized format. The pickle binary file, however contains some other valuable timeseries such as the ECG Ground Truth, which will be exported yo csv as well.   

Perform internal, sensor timeseries indexing and then derive and add the MAIN/CROSS-DATASET INDEX for each timeseries. This requires strictly, sequentially ordered data point processing

Build a common dataframe with all signal timeseries (averaged by using a sliding window of 8 seconds, shifted by 2 seconds) for all test subjects, enriched with the Reference Data about the subjects and the Activities performed by the subjects 

Build a common dataframe with all signal timeseries (non-averaged, original values) for all test subjects, enriched with the Reference Data about the subjects and the Activities performed by the subjects

Aggregations - use the common dataframe as a base/input to aggregate it in the following ways:

a) Calculate min,max,avg,standard deviation of ECG Heart Rate by Subject by Activity

example result produced by the spark job, sorted by subject, by average heart rate 

![Data Model](https://github.com/evoisec/sensor-data-analytics/blob/master/doc/aggregated-hr5.jpg)

b) Calculate max temperature by 1 minute time intervals, by subject (subject here can be viewed as a Client or Trading Desk and 1 minute time period can be viewed as e.g. their VAR for calendar period / as of date) 

Statistical Analysis - statistical summaries of the timeseries, correlations between the timeseries etc

![Data Model](https://github.com/evoisec/sensor-data-analytics/blob/master/doc/corr.jpg)

Machine Learning Analytics - fit linear regression and use the learned coefficients to assess the influence of all sensor modalities and subject anthropometric attributes on potentially improving the precision of PPG 

## Technical System Architecture

### Binary Data Extractor

The extraction (not all timeseries are in csv format) of timeseries datasets from Python binary files in pickle format (Serialized python dictionary object) to CSV files is performed by (non-spark) Python ETL Pipeline

This is a data pre-processing step

https://github.com/evoisec/sensor-data-analytics/tree/master/src/main/python 

The extracted data is available as CSV in:

https://github.com/evoisec/sensor-data-analytics/tree/master/data

### Timeseries Sequential Indexer

Performs internal, sensor timeseries indexing and then derive and add the MAIN/CROSS-DATASET INDEX for each timeseries. This requires strictly, sequentially ordered data point processing

Hence this system component will be implemented with (non-spark) Python ETL Pipeline operating in a strictly sequential fashion to preserve the order of timeseries data points during the indexing. Spark/Databricks is a parallel compute, distributed data processing framework, which distributes data points/records on different cluster nodes where their processing can and will get out of (strictly sequential) order - hence not appropriate for the kind of indexing required here  

https://github.com/evoisec/sensor-data-analytics/tree/master/src/main/python

### Spark/Databricks ETL and Analytics Pipeline Jobs

The rest of the Functionality of the solution is implemented with Spark/Databricks ETL Jobs written in scala

the spark scala jobs code
https://github.com/evoisec/sensor-data-analytics/tree/master/src/main/scala