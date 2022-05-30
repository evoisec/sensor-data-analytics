import pickle
import sys, os
# required due to the presence of numpy arrays in  the dictionary data structure
import numpy
import configparser
import csv
import logging

log = logging.getLogger(__name__)
log.setLevel(logging.DEBUG)

stream = logging.StreamHandler()
stream.setLevel(logging.INFO)
streamformat = logging.Formatter("%(asctime)s:%(levelname)s:%(message)s")
stream.setFormatter(streamformat)
log.addHandler(stream)

config = configparser.ConfigParser()
config.read(os.path.dirname(__file__) + '\\bde.ini')
log.info(config["DEFAULT"]["BinaryFilePath"])
log.info(config["DEFAULT"]["CSVOutputFolder"])

pickle_file = config["DEFAULT"]["BinaryFilePath"]
csv_output_folder = config["DEFAULT"]["CSVOutputFolder"]

def write_1d_csv(file_name, column_name, data, nested_1darray):

    with open(csv_output_folder + file_name, 'w', encoding='UTF8', newline='') as f:
        writer = csv.writer(f)

        header = [column_name]

        # write the header
        writer.writerow(header)

        if nested_1darray:
            record = []
            for e in data:
                for ee in e:
                    record.append(ee)
                # write the data
                writer.writerow(record)
                record = []

        else:
            for e in data:
                record = [e]
                # write the data
                writer.writerow(record)

def write_3d_csv(file_name, column_names, data):

    with open(csv_output_folder + file_name, 'w', encoding='UTF8', newline='') as f:
        writer = csv.writer(f)

        header = column_names

        # write the header
        writer.writerow(header)

        record =[]
        for e in data:
            for ee in e:
                record.append(ee)
            # write the data
            writer.writerow(record)
            record = []

def extract_label(sensor_dict):

    print(type(sensor_dict[str.encode("label")]))

    label = sensor_dict[str.encode("label")]
    print(label.shape)

    len_tuple = label.shape
    for x in len_tuple:
        print(x)
    print(label.ndim)

    write_1d_csv("\\PKL-ECG-LABEL-MAIN-INDEX.csv", "label", label,False)

def extract_acc(sensor_dict):

    print(sensor_dict[str.encode("signal")][str.encode("chest")][str.encode("ACC")])
    print(type(sensor_dict[str.encode("signal")][str.encode("chest")][str.encode("ACC")]))

    ACC = sensor_dict[str.encode("signal")][str.encode("chest")][str.encode("ACC")]
    print(ACC.shape)
    print(ACC.ndim)

    write_3d_csv("\\PKL-RBAN-ACC.csv", ["x","y", "z"], ACC)

def extract_activity(sensor_dict):

    print(sensor_dict[str.encode("activity")])
    activity = sensor_dict[str.encode("activity")]

    print(activity.shape)

    write_1d_csv("\\PKL-ACTIVITY-ENHANCED-TS.csv", "activity", activity,True)

def extract_ecg(sensor_dict):

    print(sensor_dict[str.encode("signal")][str.encode("chest")][str.encode("ECG")])
    print(type(sensor_dict[str.encode("signal")][str.encode("chest")][str.encode("ECG")]))

    ECG = sensor_dict[str.encode("signal")][str.encode("chest")][str.encode("ECG")]
    print(ECG.shape)
    print(ECG.ndim)

    write_1d_csv("\\PKL-RBAN-ECG.csv", "ecg", ECG,True)

def main():

    # this step is required due to  incompatibility of pickle objects created with python 2
    with open(pickle_file, 'rb') as f:
        sensor_dict = pickle.load(f, encoding="bytes")

    # inspect/reverse engineer the internal structure and content of the dictionary dataset
    for key, value in sensor_dict.items():
        print(key, value)
        print(sensor_dict[key])

    #extract_label(sensor_dict)
    #extract_acc(sensor_dict)
    #extract_activity(sensor_dict)
    extract_ecg(sensor_dict)

if __name__ == '__main__':
    main()
