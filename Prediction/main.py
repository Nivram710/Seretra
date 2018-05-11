from collision import find_collision
from predict import predict
from read import read_data
from show import show

danger_path = ".danger/"

objects = read_data()
keys = list(objects.keys())

objects_interpolated = objects
for obj in objects_interpolated.values():
    obj.extend(predict(obj, 1, 1))

for i in range(0, len(keys)):
    for j in range(i+1, len(keys)):
        collision = find_collision(objects[keys[i]], objects[keys[j]], 0.0003)
        if collision:
            print("COLLISION FOUND")
            print(collision[1])
            with open(danger_path + keys[i], "a") as myfile:
                myfile.write(keys[i] + " " + str(collision[1]))
            with open(danger_path + keys[j], "a") as myfile:
                myfile.write(keys[i] + " " + str(collision[1]))

show(objects)
