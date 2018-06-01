import sys
import os

MAX_DATAPOINTS = 60

assert len(sys.argv) == 6

imei = sys.argv[1]
time = sys.argv[2]
x = sys.argv[3]
y = sys.argv[4]
z = sys.argv[5]

# Force correct types, else error
int(time)
float(x)
float(y)
float(z)

path = ".data/" + imei

with open(path, "a") as f:
    f.write(time + " " + x + " " + y + " " + z + "\n")

os.system("tail -n " + str(MAX_DATAPOINTS) + " " + path + " > " + ".data/temp")
os.system("mv .data/temp " + path)
