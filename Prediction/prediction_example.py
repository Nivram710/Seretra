from vec2 import Vec2
import random
import matplotlib.pyplot as plt
from predict import predict

# Input Data; last is newest, difference in time is constant
data = [Vec2(0, 0),
        Vec2(1, 0),
        Vec2(1.75, 0.5),
        Vec2(2.2, 2),
        Vec2(2.2, 3.5),
        Vec2(1.75, 5)]
random.seed(16)

for i in range(0):
    x = random.randint(-5, 5)
    y = random.randint(-5, 5)
    data.append(Vec2(x, y))

for i in range(10):
    new = predict(data)
    data.append(new)
    # print("-> ", data)

fig, ax = plt.subplots()

plt.axis("equal")

xs = list(map(lambda v: v.x, data))
ys = list(map(lambda v: v.y, data))

plt.plot(xs, ys)
for i, p in enumerate(xs):
    plt.scatter(p, ys[i])
plt.show()
