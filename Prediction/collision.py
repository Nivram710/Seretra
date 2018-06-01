from vec2 import Vec2
import time


# Needs constant time step
def find_collision(o1, o2, threshold):
    time = int(round(time.time() * 1000))
    for pos1, pos2 in zip(o1, o2):
        if pos1[1] < time and pos2[1] < time:
            continue
        if Vec2.distance(pos1[0], pos2[0]) < threshold:
            return pos1
