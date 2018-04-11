import glob
from vec2 import Vec2
from collision import find_collision
from interpolate import interpolate

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

keys = list(objects.keys())

objects_interpolated = objects
for obj in objects_interpolated.values():
    print(obj)
    obj = interpolate(obj, 0.1)

for i in range(0, len(keys)):
    for j in range(i+1, len(keys)):
        collision = find_collision(objects[keys[i]], objects[keys[j]], 1)
        if collision:
            print("ALARM ALARM ALARM")
            print(keys[i], "and", keys[j], "collide")
