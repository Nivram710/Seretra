from Vec2 import Vec2
import matplotlib.pyplot as plt


ROTATION_PREDICTION_WEIGHTS = [0.6, 0.4]

# Input Data; last is newest, difference in time is constant
data = [Vec2(0, 0),
        Vec2(1, 0),
        Vec2(1, 1),
        Vec2(0, 1)]


def predict(data):
    # Find out relative offset between each point
    relative_data = []

    # Loop through each data point and calculate offset
    for i, _ in enumerate(data[1:]):
        relative_data.append(data[i+1] - data[i])

    #print("relative_data\t", relative_data)
    angle_data = list(map(Vec2.getAngle, relative_data))

    relative_angles = []

    # Loop through each angle and calculate relative angles
    for i, _ in enumerate(angle_data[1:]):
        relative_angles.append(angle_data[i+1] - angle_data[i])

    #print("relative_angles\t", relative_angles)

    weigthed_angles = [a * b for a, b in zip(relative_angles[::-1],
                                             ROTATION_PREDICTION_WEIGHTS)]
    #print("weighted_angles\t", weigthed_angles)
    predicted_angle_change = sum(weigthed_angles)
    predicted_angle = angle_data[-1] + predicted_angle_change
    predicted_speed = relative_data[-1].getMagnitude()

    #print("predictions\t", predicted_angle, predicted_speed)

    predicted_offset = Vec2(predicted_angle, predicted_speed, True)
    #print()

    return data[-1] + predicted_offset


if __name__ == "__main__":
    data.append(predict(data))
    print(data[-1])

    fig, ax = plt.subplots()

    xs = list(map(lambda v: v.x, data))
    ys = list(map(lambda v: v.y, data))

    plt.plot(xs, ys)
    plt.scatter(xs[-1], ys[-1])
    plt.scatter(xs[-2], ys[-2])
    plt.show()
