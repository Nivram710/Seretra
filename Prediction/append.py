import sys

assert len(sys.argv) == 5

time = sys.argv[2]
x = sys.argv[3]
y = sys.argv[4]

int(time)
float(x)
float(y)

path = ".data/" + sys.argv[1]

with open(path, "a") as f:
    f.write(time + " " + x + " " + y + "\n")
