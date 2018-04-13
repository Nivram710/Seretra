from vec2 import Vec2
import random
import matplotlib.pyplot as plt
import glob

from predict import predict
from interpolate import interpolate
from collision import find_collision


data_path = ".data/"

objects = {}
for filename in glob.glob(data_path + "*"):
    name = filename.lstrip(data_path)
    with open(filename, "r") as f:
        for position in f:
            parts = position.split()
            time = int(parts[0])
            x = float(parts[1])
            y = float(parts[2])
            objects.setdefault(name, [])
            objects[name].append((Vec2(x, y), time))

for obj in objects.values():
    obj_interpolated = obj  #interpolate(obj, 100)
    xs = list(map(lambda d: d[0].x, obj_interpolated))
    ys = list(map(lambda d: d[0].y, obj_interpolated))

    plt.axis("equal")
    plt.scatter(xs, ys)
    plt.plot(xs, ys)

plt.show()
