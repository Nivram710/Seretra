import sys

assert len(sys.argv) == 5

time = sys.argv[2]
x = sys.argv[3]
y = sys.argv[4]

with open(".data/" + sys.argv[1], "a") as f:
    f.write(int(time) + " " + float(x) + " " + float(y) + "\n")
