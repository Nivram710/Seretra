from collision import find_collision
from predict import predict
from read import read_data
from interpolate import interpolate
from show import show

objects = {}
keys = []

print("show: show data as graphs")
print("text: show data as text")
print("read: read data")
print("intp: interpolate")
print("pred: predict next step")
print("coll: find collision")
print()

while True:
    command = input("> ")
    
    if command == "show":
        show(objects)

    elif command == "text":
        print(objects)

    elif command == "read":
        objects = read_data()
        keys = list(objects.keys())

    elif command == "intp":
        step = float(input("Stepsize: "))
        for key in keys:
            objects[key] = interpolate(objects[key], step)

    elif command == "pred":
        repeats = int(input("Repeats: "))
        for obj in objects.values():
            predict(obj, repeats)

    elif command == "coll":
        threshold = float(input("Threshold: "))
        for i in range(0, len(keys)):
            for j in range(i+1, len(keys)):
                collision = find_collision(objects[keys[i]], objects[keys[j]], threshold)
                if collision:
                    print("COLLISION FOUND")
                    print(collision[1])
                    with open(danger_path + keys[i], "a") as myfile:
                        myfile.write(keys[i] + " " + str(collision[1]))
                    with open(danger_path + keys[j], "a") as myfile:
                        myfile.write(keys[i] + " " + str(collision[1]))

    else:
        print("Command '" + command + "' not found")
