import glob
import os
from vec2 import Vec2

data_path = ".data/"
objects = {}


def read_data():
    for filename in glob.glob(data_path + "*"):
        name = filename.lstrip(data_path)
        if(os.path.isdir(filename)):
            print("dir")
            continue

        with open(filename, "r") as f:
            for position in f:
                parts = position.split()
                time = int(parts[0])
                x = float(parts[1])
                y = float(parts[2])
                objects.setdefault(name, [])
                objects[name].append((Vec2(x, y), time))
    return objects
