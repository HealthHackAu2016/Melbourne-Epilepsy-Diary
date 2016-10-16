#!/usr/bin/python2
import os, json
#import pandas as pd
from collections import OrderedDict

#path_to_json = '/var/www/html/healthhack/tester/raw/'
path_to_json = os.getcwd()
json_files = [pos_json for pos_json in os.listdir(path_to_json) if pos_json.endswith('.json')]

timepoints = []
Xs = []
Ys = []
Zs = []

#for json_file_name in json_files:
#    with open(json_file_name) as json_file:
#        data = json.load(json_file, object_pairs_hook=OrderedDict)
#        for timestamp in data:
#            print(int(timestamp))
#            print(data[timestamp])
#            timepoints.append(int(timestamp))
#            Xs.append(data[timestamp]['X'])
#            Ys.append(data[timestamp]['Y'])
#            Zs.append(data[timestamp]['Z'])
import glob
newest_json_filename = max(glob.iglob('*.json'), key=os.path.getctime)
#print(newest_json_filename)

#import datetime
with open(newest_json_filename) as newest_json:
    data = json.load(newest_json, object_pairs_hook=OrderedDict)
    for timestamp in data:
        #print(int(timestamp))
        #print(data[timestamp])
        timepoints.append(int(timestamp))
        #timepoints.append((int(timestamp))/1000.0)
        #timepoints.append(
        #    datetime.datetime.utcfromtimestamp(
        #        (int(timestamp))/1000.0
        #    ).strftime('%Y-%m-%d %H:%M:%S.%f')
        #)
        Xs.append(data[timestamp]['X'])
        Ys.append(data[timestamp]['Y'])
        Zs.append(data[timestamp]['Z'])

#import matplotlib
#matplotlib.use("Agg")
import matplotlib.pyplot as plt
#import pyplot as plt
#import matplotlib.dates as md
#ax = plt.gca()
#xfmt = md.DateFormatter('%Y-%m-%d %H:%M:%S.%f')
#ax.xaxis.set_major_formatter(xfmt)
plt.plot(timepoints, Xs)
plt.plot(timepoints, Ys)
plt.plot(timepoints, Zs)
#plt.show()
#plt.savefig('/var/www/html/healthhack/tester/raw/RawData.png')
plt.savefig('RawData.png')
plt.close()

from scipy import signal
XMeds = signal.medfilt(Xs)
YMeds = signal.medfilt(Ys)
ZMeds = signal.medfilt(Zs)
plt.plot(timepoints, XMeds)
plt.plot(timepoints, YMeds)
plt.plot(timepoints, ZMeds)
plt.savefig('MedData.png')
plt.close()

# Design Butterworth Filter
# Filter Order
N  = 3
# Cutoff Frequency
Wn = 0.5
B, A = signal.butter(N, Wn, output='ba', analog=False)

# Apply the Filter
XButs = signal.filtfilt(B, A, XMeds)
YButs = signal.filtfilt(B, A, YMeds)
ZButs = signal.filtfilt(B, A, ZMeds)
plt.plot(timepoints, XButs)
plt.plot(timepoints, YButs)
plt.plot(timepoints, ZButs)
plt.savefig('ButData.png')
plt.close()

