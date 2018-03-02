import math


class Vec2:
    def __init__(self, p1, p2, alternate=False):
        if not alternate:
            self.x = p1
            self.y = p2
        else:
            self.__init__(1, 0)
            self.rotated(p1, True)
            self.x *= p2
            self.y *= p2

    def __add__(self, other):
        return Vec2(self.x + other.x,
                    self.y + other.y)

    def __sub__(self, other):
        return Vec2(self.x - other.x,
                    self.y - other.y)

    def __truediv__(self, other):
        return Vec2(self.x / other,
                    self.y / other)

    def __mul__(self, other):
        return Vec2(self.x * other,
                    self.y * other)

    def getAngle(self):
        if self.x == 0 and self.y == 0:
            return -1

        if self.x == 0:
            if self.y < 0:
                return 270
            else:
                return 90
        if self.x < 0:
            return 180 - math.atan(self.y / -self.x) / 2 / math.pi * 360

        return math.atan(self.y / self.x) / 2 / math.pi * 360

    def getMagnitude(self):
        return math.sqrt(self.x**2 + self.y**2)

    def rotated(self, angle, modify=False):
        angle = angle / 360 * 2 * math.pi
        x = self.x * math.cos(angle) - self.y * math.sin(angle)
        y = self.x * math.sin(angle) + self.y * math.cos(angle)
        if modify:
            self.x = x
            self.y = y
        return Vec2(x, y)

    def __repr__(self):
        return f"Vec2({self.x}, {self.y})"

    def toList(self):
        return [self.x, self.y]
