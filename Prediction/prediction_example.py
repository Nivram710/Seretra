from vec2 import Vec2
import random
import matplotlib.pyplot as plt
from predict import predict
from interpolate import interpolate

# Input Data; last is newest, second element is difference in time from start


def generate_random_data(start, number):
    data = [(start, 0)]
    for i in range(1, number):
        angle = random.randint(-50, 50)
        length = random.random()+0.2
        data.append((data[-1][0]+Vec2(data[-1][0].getAngle()+angle, length, True), i))
    return data


# Predict
#for i in range(50):
#    data.append(predict(data, 0.5))

# Needs constant time step
def find_collision(o1, o2, threshold):
    for pos1, pos2 in zip(o1, o2):
        if(Vec2.distance(pos1[0], pos2[0]) < threshold):
            return pos1


random.seed(0.925)

obj1_data = interpolate(generate_random_data(Vec2(0, 0), 10), 0.1)
obj2_data = interpolate(generate_random_data(Vec2(0, 1), 10), 0.1)

for i in range(0, 100):

    collision = find_collision(obj1_data, obj2_data, 0.1)
    """if collision:

        xs = list(map(lambda d: d[0].x, obj1_data))
        ys = list(map(lambda d: d[0].y, obj1_data))

        plt.plot(xs, ys)

        xs = list(map(lambda d: d[0].x, obj2_data))
        ys = list(map(lambda d: d[0].y, obj2_data))
        plt.plot(xs, ys)

        plt.scatter(collision[0].x, collision[0].y)

        plt.show()"""
