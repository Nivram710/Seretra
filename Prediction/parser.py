from collision import find_collision
from predict import predict
from read import read_data
from interpolate import interpolate
from show import show

def parse(path):
    objects = {}
    keys = []

    for line in open(path):
        parts = line.strip().split()

        command = parts[0]
        arg = None 
        if(len(parts) > 1):
            arg = parts[1]
        
        if command == "reset":
            objects = {}
            keys = []

        elif command == "text":
            print(objects)

        elif command == "show":
            show(objects)

        elif command == "read":
            objects = read_data()
            keys = list(objects.keys())

        elif command == "intp":
            step = float(arg)
            for key in keys:
                objects[key] = interpolate(objects[key], step)

        elif command == "pred":
            repeats = int(arg)
            for obj in objects.values():
                predict(obj, repeats)

        elif command == "coll":
            threshold = float(arg)
            for i in range(0, len(keys)):
                for j in range(i+1, len(keys)):
                    collision = find_collision(objects[keys[i]], objects[keys[j]], threshold)
                    if collision:
                        print("COLLISION FOUND")
                        print(collision[1])
                        with open(".danger/" + keys[i], "a") as myfile:
                            myfile.write(keys[i] + " " + str(collision[1]))
                        with open(".danger/" + keys[j], "a") as myfile:
                            myfile.write(keys[i] + " " + str(collision[1]))
                    else: print("no collision")
        else:
            print("Command '" + command + "' not found")
