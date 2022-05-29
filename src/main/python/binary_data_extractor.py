import pickle
import sys, os
# required due to the presence of numpy arrays in the dictionary data structure
import numpy
import configparser
import csv

config = configparser.ConfigParser()
config.read(os.path.dirname(__file__) + '\\bde.ini')
print(config["DEFAULT"]["BinaryFilePath"])
print(config["DEFAULT"]["CSVOutputFolder"])

pickle_file = config["DEFAULT"]["BinaryFilePath"]
csv_output_folder = config["DEFAULT"]["CSVOutputFolder"]

def write_csv(file_name, column_name):

    with open(csv_output_folder + '\\ACC.csv', 'w', encoding='UTF8', newline='') as f:
        writer = csv.writer(f)

        header = [column_name]
        data = [111, 222]

        # write the header
        writer.writerow(header)

        # write the data
        writer.writerow(data)

def main():

    # this step is required due to incompatibility of pickle objects created with python 2
    with open(pickle_file, 'rb') as f:
        sensor_dict = pickle.load(f, encoding="bytes")

    # inspect/reverse engineer the internal structure and content of the dictionary dataset
    for key, value in sensor_dict.items():
        print(key, value)
        print(sensor_dict[key])


    print(type(sensor_dict[str.encode("label")]))

    label = sensor_dict[str.encode("label")]
    print(label.shape)

    len_tuple = label.shape
    for x in len_tuple:
        print(x)
    print(label.ndim)

    for x in label:
        print(x)


    sys.exit(0)

    print(sensor_dict[str.encode("signal")][str.encode("chest")][str.encode("ACC")])
    print(type(sensor_dict[str.encode("signal")][str.encode("chest")][str.encode("ACC")]))

    ACC = sensor_dict[str.encode("signal")][str.encode("chest")][str.encode("ACC")]
    print(ACC.shape)
    print(ACC.ndim)





    print(sensor_dict[str.encode("activity")])
    activity = sensor_dict[str.encode("activity")]
    for x in activity:
        #print("dim")
        for y in x:
            print(y)
    print(activity.shape)


if __name__ == '__main__':
    main()
