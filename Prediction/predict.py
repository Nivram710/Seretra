from Vec2 import Vec2
import matplotlib.pyplot as plt
import random


ROTATION_PREDICTION_WEIGHTS = [x ** 2 for x in range(100, 1, -1)]
ROTATION_RESISTENCE = 0.2

def predict(data):
    # Loop through each data point and calculate relative positions
    relative_data = []
    for i, _ in enumerate(data[1:]):
        relative_data.append(data[i+1] - data[i])

    # Get angle of each movement
    angle_data = list(map(Vec2.getAngle, relative_data))

    # Loop through each angle and calculate relative angles
    relative_angles = []
    for i, _ in enumerate(angle_data[1:]):
        solution1 = angle_data[i+1] - angle_data[i]
        # Angle might be negative. If it is, get the correct angle
        if solution1 < 0:
            solution1 += 360
        solution2 = 360 - solution1
        relative_angles.append(min(solution1, solution2))

    weights = ROTATION_PREDICTION_WEIGHTS[:len(relative_angles)]
    print(relative_angles)
    weights = list(map(lambda w: w/sum(weights), weights))
    weighted_angles = [a * b for a, b in zip(relative_angles[::-1],
                                             weights)]
    predicted_angle_change = sum(weighted_angles) * (1 - ROTATION_RESISTENCE)
    predicted_angle = angle_data[-1] + predicted_angle_change
    predicted_speed = relative_data[-1].getMagnitude()

    predicted_offset = Vec2(predicted_angle, predicted_speed, True)

    return data[-1] + predicted_offset


if __name__ == "__main__":
    # Input Data; last is newest, difference in time is constant
    data = []
    random.seed(6)
    for i in range(3):
        x = random.randint(-5, 5)
        y = random.randint(-5, 5)
        data.append(Vec2(x, y))

    for i in range(5):
        data.append(predict(data))

    fig, ax = plt.subplots()

    plt.axis("equal")

    xs = list(map(lambda v: v.x, data))
    ys = list(map(lambda v: v.y, data))

    plt.plot(xs, ys)
    for i, p in enumerate(xs):
        plt.scatter(p, ys[i])
    plt.show()
