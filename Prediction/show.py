import matplotlib.pyplot as plt
from read import read_data


def show(objects, points=[]):
    if len(objects) == 0:
        print("Nothing to show")
    for obj in objects.values():
        obj_interpolated = obj  # interpolate(obj, 100)
        xs = list(map(lambda d: d[0].x, obj_interpolated))
        ys = list(map(lambda d: d[0].y, obj_interpolated))

        plt.axis("equal")
        plt.scatter(xs, ys)
        plt.plot(xs, ys)

    plt.show()


if __name__ == "__main__":
    objects = read_data()
    show(objects)
