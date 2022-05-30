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

def main():
    with open('E:\project\data\PPG_FieldStudy\CSV\PKL-ECG-LABEL-MAIN-INDEX.csv', encoding="utf8") as f:
        csv_reader = csv.reader(f)
        for line_no, line in enumerate(csv_reader, 1):
            if line_no == 1:
                print('Header:')
                print(line)  # header
                print('Data:')
            else:
                print(line_no-1)
                print(line)  # data

if __name__ == '__main__':
    main()