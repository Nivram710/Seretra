from predict import predict
from Vec2 import Vec2

PREDICT_AMOUNT = 5
ACCURACY_CONSTANT = 0.1


class Bogey:
    def __init__(self, transportation_type, bogey_id, points):
        self.transportation_type = transportation_type
        self.bogey_id = bogey_id
        self.real_points = points
        self.predicted_points = []

    def predict(self):
        self.predicted_points = []
        for i in range(PREDICT_AMOUNT):
            self.predicted_points.append(predict(self.real_points + self.predicted_points))
        return self.predicted_points


b1 = Bogey("car", 234, [
    Vec2(0, 1),
    Vec2(0, 2),
    Vec2(0, 3)
    ])

for point, i in enumerate(b1.predict()):
    accuracy = i * 0.1
