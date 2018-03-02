from Vec2 import Vec2
import matplotlib.pyplot as plt
import random


ROTATION_PREDICTION_WEIGHTS = [x ** 2 for x in range(100, 1, -1)]
ACCELERATION_PREDICTION_WEIGHTS = [0.8, 0.2]
ROTATION_RESISTENCE = 0.5


def getPositiveAngle(angle):
    if angle >= 0:
        return angle
    return angle+360


def applyWeights(data, weights):
    # Trim ROTATION_PREDICTION_WEIGHTS to the size of the angles
    weights = ROTATION_PREDICTION_WEIGHTS[:len(data)]

    # Make the sum of the weights equal to 1
    adjusted_weights = list(map(lambda w: w/sum(weights),
                                weights))
    # Apply weights to angles
    weighted_data = [a * b for a, b in zip(data,
                                           adjusted_weights)]
    return weighted_data


def predict(data):
    # Calculate relative positions
    data_change = [data[i+1] - data[i] for i in range(len(data)-1)]

    # Get angles and calculate relative_angles
    angle_data = list(map(Vec2.getAngle, data_change))
    speed = list(map(Vec2.getMagnitude, data_change))

    angle_change = [getPositiveAngle(angle_data[i+1] - angle_data[i])
                    for i in range(len(angle_data) - 1)]
    speed_change = [speed[i+1] - speed[i]
                    for i in range(len(angle_data) - 1)]

    weighted_angle_change = applyWeights(angle_change,
                                         ROTATION_PREDICTION_WEIGHTS)

    weighted_speed_change = applyWeights(speed_change,
                                         ACCELERATION_PREDICTION_WEIGHTS)

    predicted_angle_change = sum(weighted_angle_change) - 10
    predicted_speed_change = 0 #sum(weighted_speed_change) * (1-0)
    predicted_angle = angle_data[-1] + predicted_angle_change
    predicted_speed = data_change[-1].getMagnitude() + predicted_speed_change
    predicted_movement = Vec2(predicted_angle, predicted_speed, True)
    print(predicted_angle_change)
    print(predicted_angle)
    print(predicted_movement)
    print(data[-1]+predicted_movement)

    return data[-1] + predicted_movement


if __name__ == "__main__":
    # Input Data; last is newest, difference in time is constant
    data = []
    random.seed(16)
    for i in range(3):
        x = random.randint(-5, 5)
        y = random.randint(-5, 5)
        data.append(Vec2(x, y))

    for i in range(5):
        new = predict(data)
        data.append(new)
        print(data)

    fig, ax = plt.subplots()

    plt.axis("equal")

    xs = list(map(lambda v: v.x, data))
    ys = list(map(lambda v: v.y, data))

    plt.plot(xs, ys)
    for i, p in enumerate(xs):
        plt.scatter(p, ys[i])
    plt.show()
