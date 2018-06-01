import scipy.interpolate
import numpy as np
from vec2 import Vec2
import matplotlib.pyplot as plt

def interpolate(data, stepsize):
    x = [d[0].x for d in data]
    y = [d[0].y for d in data]
    time = [d[1] for d in data]
    steps = len(data) / stepsize

    #fx_linear = scipy.interpolate.interp1d(time, x)
    #fy_linear = scipy.interpolate.interp1d(time, y)
    fx_cubic = scipy.interpolate.interp1d(time, x, kind="cubic")
    fy_cubic = scipy.interpolate.interp1d(time, y, kind="cubic")

    time_new = np.linspace(data[0][1], data[-1][1], steps)

    # Debug
    """
    plt.subplot(221)
    plt.plot(time, x, 'o',
             time_new, fx_cubic(time_new), '--')
    plt.xlabel("time")
    plt.ylabel("x")
    plt.legend(['x', 'cubic'], loc='best')

    plt.subplot(222)
    plt.plot(time, y, 'o',
             time_new, fy_cubic(time_new), '--')
    plt.xlabel("time")
    plt.ylabel("y")
    plt.legend(['y', 'cubic'], loc='best')

    plt.subplot(223)
    plt.plot(x, y, 'ro')
    plt.xlabel("x")
    plt.ylabel("y")
    plt.legend(['points'], loc='best')
    plt.axis("equal")

    plt.subplot(224)
    plt.plot(fx_cubic(time_new), fy_cubic(time_new))
    plt.xlabel("x")
    plt.ylabel("y")
    plt.legend(['interpolated curve'], loc='best')
    plt.axis("equal")

    plt.show()
    """

    return list(map(lambda x, y, t: (Vec2(x, y), t), fx_cubic(time_new), fy_cubic(time_new), time_new))
