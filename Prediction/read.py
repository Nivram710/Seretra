import glob
import os
from vec2 import Vec2

DATA_PATH = ".data/"


def read_data():
    objects = {}
    for filename in glob.glob(DATA_PATH + "*"):
        name = filename.lstrip(DATA_PATH)
        if(os.path.isdir(filename)):
            print("ignoring directory", filename)
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
