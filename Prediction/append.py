import sys
import os

max_datapoints = 60

assert len(sys.argv) == 6

imei = sys.argv[1]
time = sys.argv[2]
x = sys.argv[3]
y = sys.argv[4]
z = sys.argv[5]

int(time)
float(x)
float(y)
float(z)

path = ".data/" + imei

with open(path, "a") as f:
    f.write(time + " " + x + " " + y + " " + z + "\n")

os.system("tail -n " + str(max_datapoints) + " " + path + " > " + ".data/temp")
os.system("mv .data/temp " + path)
