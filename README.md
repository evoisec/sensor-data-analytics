## Data Model

"Business Transaction" timeseries Datasets:

a) Body Sensor Signal Timeseries Datasets (a form of "Business Transaction" datasets)

b) Activity Label Timeseries Datasets (timeseries of Activities perfomed by the Subjects over time - Activity Labels which in turn, taken on their own are a form of Reference Data)

Reference Datasets:

a) Activity Labels - used to enrich/tag the Sensor Signal Timeseries data points as belonging to specific type of Subject Activity 

b) Test Subject Anthropometric Data and Test Subject ID

## Data Model Analysis

### Data Indexing/Positioning inside Dataset, Dataset Synchronization and Dataset Join Algorithms 

The "business" analytics objective of the project is improvement of Photoplethysmography (PPG) - optical method widely used nowadays for continuous heart rate monitoring (ie instead of ECG).

Photoplethysmography is still a technically challenging area and hence to enable further research and data analysis, the Photoplethysmography signal data is complemented with CORRELATED/supporting signal datasets of other modalities - body worn accelerometer, temperature, respiration, etc sensor signal timeseries datasets 

My business interpreatation is that it is hoped that the CORRELATED sensor signal datasets, when used together with the PPG signal dataset, would improve the PPG sensor design and signal processing. The correlated signal data would provide CORRECTIVE information enabling that. Hence the alignment of these timeseries dataset becomes key data processing task. This is further complicated by the fact that the signal timeseries lack absolute timestamps for the individual data points they contain and also the fact that the signals are sampled/measured at different frequencies (as expressed in Hz) 

The sensor timeseries are complemented with Reference data consisting of Labels of Type of Activities performed by the test subject during specific time periods within sensor signal timeseries dataset

The presence of several CORRELATED, different, sensor signal, time series datasets requires a method/algorithm for their synchronization/join, to enable their joined analysis / study with the business objective of improvement PPG   

This would require methods / algorithms for Data Indexing and Joining overcoming the lack of absolute timestamps for individual data points and different signal sampling/measurement frequencies

#### Cross Dataset Indexing - Indexing/Synchronization/Join Across Datasets

MAIN INDEX - The Heart Rate (as the key subject of research/"business analytics" here, due to the nature of PPG) is going to be used as the Ground Truth or will provide the Main Index 

#### Internal Dataset Indexing - Indexing within Single Dataset

Then, there is a need for Data Indexing method/algorithm within each sensor signal timeseries dataset 

## System Architecture

### Binary Data Extractor

### Spark/Databricks ETL and Analytics Pipeline Jobs