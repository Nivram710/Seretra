from vec2 import Vec2
import random
import matplotlib.pyplot as plt
from predict import predict

# Input Data; last is newest, second element is difference in time from start
data = [(Vec2(0, 0), 0)]

random.seed(28)

for i in range(1, 10):
    angle = random.randint(-50, 50)
    length = random.random()+0.2
    data.append((data[-1][0]+Vec2(data[-1][0].getAngle()+angle, length, True), i))

for i in range(50):
    data.append(predict(data, 0.5))

fig, ax = plt.subplots()

plt.axis("equal")

xs = list(map(lambda d: d[0].x, data))
ys = list(map(lambda d: d[0].y, data))

plt.plot(xs, ys)
for i, p in enumerate(xs):
    plt.scatter(p, ys[i])
plt.show()
