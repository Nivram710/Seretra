from Vec2 import Vec2

data = [Vec2(0, 0), Vec2(1, 1), Vec2(2, 4), Vec2(3, 5), Vec2(4, 5), Vec2(5, 4), Vec2(6, 1), Vec2(7, 0), Vec2(8, 0)]


def getRelativeData(data):
    relative_data = []
    for index in range(len(data) - 1):
        relative_data.append(data[index+1] - data[index])
        print(relative_data[-1])
    return relative_data


def getRelativeAngles(relative_data):
    relative_angles = []
    for index in range(len(relative_data) - 1):
        print(relative_data[index+1].getAngle() - relative_data[index].getAngle())

    return relative_angles


def predict(data):
    getRelativeAngles(getRelativeData(data))
    return Vec2(0, 0)


print(predict(data))
