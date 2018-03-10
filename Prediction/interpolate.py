import scipy.interpolate
import numpy as np
from vec2 import Vec2
import matplotlib.pyplot as plt

debug = True


def interpolate(data, stepsize):
    x = [d[0].x for d in data]
    y = [d[0].y for d in data]
    time = [d[1] for d in data]
    steps = len(data) / stepsize

    fx_linear = scipy.interpolate.interp1d(time, x)
    fy_linear = scipy.interpolate.interp1d(time, y)
    fx_cubic = scipy.interpolate.interp1d(time, x, kind="cubic")
    fy_cubic = scipy.interpolate.interp1d(time, y, kind="cubic")

    timeNew = np.linspace(data[0][1], data[-1][1], steps)

    if debug:
        plt.subplot(221)
        plt.plot(time, x, 'o',
                 timeNew, fx_linear(timeNew), '-',
                 timeNew, fx_cubic(timeNew), '--')
        plt.xlabel("time")
        plt.ylabel("x")
        plt.legend(['x', 'linear', 'cubic'], loc='best')
        plt.axis("equal")

        plt.subplot(222)
        plt.plot(time, y, 'o',
                 timeNew, fy_linear(timeNew), '-',
                 timeNew, fy_cubic(timeNew), '--')
        plt.xlabel("time")
        plt.ylabel("y")
        plt.legend(['y', 'linear', 'cubic'], loc='best')
        plt.axis("equal")

        plt.subplot(223)
        plt.plot(x, y, 'ro')
        plt.xlabel("x")
        plt.ylabel("y")
        plt.legend(['points'], loc='best')
        plt.axis("equal")

        plt.subplot(224)
        plt.plot(fx_cubic(timeNew), fy_cubic(timeNew))
        plt.xlabel("x")
        plt.ylabel("y")
        plt.legend(['interpolated curve'], loc='best')
        plt.axis("equal")

        plt.show()

    return list(map(lambda x, y, t: (Vec2(x, y), t), fx_cubic(timeNew), fy_cubic(timeNew), timeNew))