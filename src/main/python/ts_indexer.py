import sys, os
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
config.read(os.path.dirname(__file__) + '\\tsi.ini')

def index_label():

    with open('E:\project\data\PPG_FieldStudy\S1\S1-PKL-CSV\PKL-ECG-LABEL-MAIN-INDEX.csv', encoding="utf8") as f:
        with open('E:\project\data\PPG_FieldStudy\S1\S1-PKL-CSV\PKL-ECG-LABEL-MAIN-INDEX-INDEXED.csv', 'w', encoding='UTF8',newline='') as ff:
            csv_writer = csv.writer(ff)
            csv_reader = csv.reader(f)

            header = ["ts_seq_num", "label"]

            # write the header
            csv_writer.writerow(header)

            for line_no, line in enumerate(csv_reader, 1):
                if line_no == 1:
                    print('Header:')
                    print(line)  # header
                    print('Data:')
                else:
                    print(line_no-1)
                    print(line)  # data
                    print(line[0])

                    record =[]
                    record.append(line_no-1)
                    record.append(line[0])
                    csv_writer.writerow(record)

def index_activity():

    with open('E:\project\data\PPG_FieldStudy\S1\S1-PKL-CSV\PKL-ACTIVITY-ENHANCED-TS.csv', encoding="utf8") as f:
        with open('E:\project\data\PPG_FieldStudy\S1\S1-PKL-CSV\PKL-ACTIVITY-ENHANCED-TS-INDEXED.csv', 'w', encoding='UTF8',newline='') as ff:
            csv_writer = csv.writer(ff)
            csv_reader = csv.reader(f)

            header = ["ts_seq_num", "activity", "internal_index", "main_index"]

            # write the header
            csv_writer.writerow(header)

            internal_index = 1
            main_index = 1
            ichanged = False

            for line_no, line in enumerate(csv_reader, 1):
                if line_no == 1:
                    print('Header:')
                    print(line)  # header
                    print('Data:')
                else:
                    print(line_no-1)
                    print(line)  # data
                    print(line[0])

                    ln = line_no-1
                    record =[]
                    record.append(ln)
                    record.append(line[0])
                    record.append(internal_index)
                    record.append(main_index)

                    if ln % 4 == 0:
                        internal_index = internal_index + 1
                        ichanged = True

                    if internal_index % 2 != 0 and ichanged:
                        main_index = main_index + 1
                        ichanged = False


                    csv_writer.writerow(record)

def index_ppg_hr():

    with open('E:\project\data\PPG_FieldStudy\S1\S1-PKL-CSV\HR.csv', encoding="utf8") as f:
        with open('E:\project\data\PPG_FieldStudy\S1\S1-PKL-CSV\PPG-HR-INDEXED.csv', 'w', encoding='UTF8',newline='') as ff:
            csv_writer = csv.writer(ff)
            csv_reader = csv.reader(f)

            next(csv_reader)

            header = ["ts_seq_num", "ppg_hr", "internal_index", "main_index"]

            # write the header
            csv_writer.writerow(header)

            main_index = 1

            for line_no, line in enumerate(csv_reader, 1):
                if line_no == 1:
                    print('Header:')
                    print(line)  # header
                    print('Data:')
                else:
                    print(line_no-1)
                    print(line)  # data
                    print(line[0])

                    ln = line_no-1
                    record =[]
                    record.append(ln)
                    record.append(line[0])
                    record.append(ln)
                    record.append(main_index)

                    if ln % 2 == 0:
                        main_index = main_index + 1

                    csv_writer.writerow(record)

def index_temp():

    with open('E:\project\data\PPG_FieldStudy\S1\S1-PKL-CSV\TEMP.csv', encoding="utf8") as f:
        with open('E:\project\data\PPG_FieldStudy\S1\S1-PKL-CSV\TEMP-INDEXED.csv', 'w', encoding='UTF8',newline='') as ff:
            csv_writer = csv.writer(ff)
            csv_reader = csv.reader(f)

            next(csv_reader)

            header = ["ts_seq_num", "temperature", "internal_index", "main_index"]

            # write the header
            csv_writer.writerow(header)

            internal_index = 1
            main_index = 1
            ichanged = False

            for line_no, line in enumerate(csv_reader, 1):
                if line_no == 1:
                    print('Header:')
                    print(line)  # header
                    print('Data:')
                else:
                    print(line_no-1)
                    print(line)  # data
                    print(line[0])

                    ln = line_no-1
                    record =[]
                    record.append(ln)
                    record.append(line[0])
                    record.append(internal_index)
                    record.append(main_index)

                    if ln % 4 == 0:
                        internal_index = internal_index + 1
                        ichanged = True

                    if internal_index % 2 != 0 and ichanged:
                        main_index = main_index + 1
                        ichanged = False

                    csv_writer.writerow(record)


def main():

    index_label()
    index_activity()
    index_ppg_hr()
    index_temp()

if __name__ == '__main__':
    main()