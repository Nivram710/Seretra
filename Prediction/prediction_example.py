from vec2 import Vec2
import random
import matplotlib.pyplot as plt
from predict import unpack, predict

# Input Data; last is newest, difference in time is constant
data = [(Vec2(0, 0), 0)]

random.seed(22)

for i in range(1, 10):
    angle = random.randint(-45, 45)
    length = random.random()+0.2
    data.append((data[-1][0]+Vec2(data[-1][0].getAngle()+angle, length, True), i))

for i in range(10):
    new = predict(data, 1)
    data.append(new)
    # print("-> ", data)

fig, ax = plt.subplots()

plt.axis("equal")

xs = list(map(lambda d: d[0].x, data))
ys = list(map(lambda d: d[0].y, data))

plt.plot(xs, ys)
for i, p in enumerate(xs):
    plt.scatter(p, ys[i])
# plt.plot(range(len(data)-2), list(map(lambda d: d.getAngle()/360*2*3.141, unpack(data)[0])))
plt.show()
