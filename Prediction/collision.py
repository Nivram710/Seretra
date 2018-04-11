from vec2 import Vec2


# Needs constant time step
def find_collision(o1, o2, threshold):
    for pos1, pos2 in zip(o1, o2):
        if(Vec2.distance(pos1[0], pos2[0]) < threshold):
            return pos1
