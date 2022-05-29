## Data Model

"Business Transaction" timeseries Datasets:

a) Body Sensor Signal Timeseries Datasets (a form of "Business Transaction" datasets)

b) Activity Label Timeseries Datasets (timeseries of Activities performed by the Subjects over time - Activity Labels which in turn, taken on their own are a form of Reference Data)

Reference Datasets:

a) Activity Labels - used to enrich/tag the Sensor Signal Timeseries data points as belonging to specific type of Subject Activity 

b) Test Subject Anthropometric Data and Test Subject ID

## Data Model Analysis

### Data Indexing/Positioning inside Dataset, Dataset Synchronization and Dataset Join Algorithms 

The "business" analytics objective of the project is improvement of Photoplethysmography (PPG) - optical method widely used nowadays for continuous heart rate monitoring (ie instead of ECG).

Photoplethysmography is still a technically challenging area and hence to enable further research and data analysis, the Photoplethysmography signal data is complemented with CORRELATED/supporting signal datasets of other modalities - body worn accelerometer, temperature, respiration, etc sensor signal timeseries datasets 

My business interpretation is that it is hoped that the CORRELATED sensor signal datasets, when used together with the PPG signal dataset, would improve the PPG sensor design and signal processing. The correlated signal data would provide CORRECTIVE information enabling that. Hence the alignment of these timeseries dataset becomes key data processing task. This is further complicated by the fact that the signal timeseries lack absolute timestamps for the individual data points they contain and also the fact that the signals are sampled/measured at different frequencies (as expressed in Hz) 

Once correlated, the signal timeseries can also then be used to build a common (spark) dataframe, which would enable Multivariete Analytics involving all variables/columns of the dataset  

The sensor timeseries are complemented with Reference data consisting of Labels of Type of Activities performed by the test subject during specific time periods within sensor signal timeseries dataset

The presence of several CORRELATED, different, sensor signal, time series datasets requires a method/algorithm for their synchronization/join, to enable their joined analysis / study with the business objective of improvement PPG   

This would require methods / algorithms for Data Indexing and Joining, overcoming the lack of absolute timestamps for individual data points and the different signal sampling/measurement frequencies

#### Cross Dataset Indexing - Indexing/Synchronization/Join Across Datasets

MAIN INDEX - The Heart Rate (as the key subject of research/"business analytics" here, due to the nature of PPG) from the ECG Sensor is going to be used as the Ground Truth or will provide the Main Index 

#### Internal Dataset Indexing - Indexing within Single Dataset

Then, there is a need for Data Indexing method/algorithm within each sensor signal timeseries dataset. As that internal index, would then be further transformed and aligned with the MAIN INDEX 

## Physical Dataset Catalogue

## Use Case Scenarios and Functional Architecture 

Export the timeseries from the binary python dictionary dataset 

Perform internal, sensor timeseries indexing and then derive and add the MAIN/CROSS-DATASET INDEX for each timeseries

Build a common dataframe with all signal timeseries for all test subjects, enriched with the Reference Data about the subjects and the Activities performed by the subjects 

Aggregations - use the common dataframe as a base/input to aggregate it in the following ways:

a) Calculate min,max,avg,standard deviation of ECG Heart Rate by Subject by Activity

b) Calculate max temperature by 1 minute time intervals, by subject (subject here can be viewed as a Client or Trading Desk and 1 minute time period can be viewed as e.g. their VAR for calendar period / as of date) 

Statistical Analysis - statistical summaries of the timeseries, correlations between the timeseries etc

Machine Learning Analytics - fit linear regression and use the learned coefficients to assess the influence of all sensor modalities and subject anthropometric attributes on potentially improving the precision of PPG 

## Technical System Architecture

### Binary Data Extractor

### Spark/Databricks ETL and Analytics Pipeline Jobs