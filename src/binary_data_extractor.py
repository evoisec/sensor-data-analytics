import pickle
import sys
# required due to the presence of numpy arrays in the dictionary data structure
import numpy

def main():

    # this step is required due to incompatibility of pickle objects created with python 2
    with open('E:\\project\\data\\PPG_FieldStudy\\S1\\S1.pkl', 'rb') as f:
        sensor_dict = pickle.load(f, encoding="bytes")

    # inspect/reverse engineer the internal structure and content of the dictionary dataset
    for key, value in sensor_dict.items():
        print(key, value)
        print(sensor_dict[key])

    print(sensor_dict[str.encode("signal")][str.encode("chest")][str.encode("ACC")])
    print(type(sensor_dict[str.encode("signal")][str.encode("chest")][str.encode("ACC")]))

    ACC = sensor_dict[str.encode("signal")][str.encode("chest")][str.encode("ACC")]
    print(ACC.shape)
    print(ACC.ndim)

    sys.exit(0)

    print(type(sensor_dict[str.encode("label")]))

    label = sensor_dict[str.encode("label")]
    print(label.shape)

    len_tuple = label.shape
    for x in len_tuple:
        print(x)

    print(label.ndim)

    for x in label:
        print(x)

    print(sensor_dict[str.encode("activity")])
    activity = sensor_dict[str.encode("activity")]
    for x in activity:
        #print("dim")
        for y in x:
            print(y)
    print(activity.shape)


if __name__ == '__main__':
    main()
