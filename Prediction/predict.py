from vec2 import Vec2
from interpolate import interpolate

ROTATION_PREDICTION_WEIGHTS = [x for x in range(100, 1, -1)]


# Return information about the positions by analysing them
def unpack(positions):
    # Subtract every position from the previous position to calculate the
    # change of the position
    position_changes = [positions[i+1] - positions[i]
                        for i in range(len(positions)-1)]

    # Get the angle of all position changes
    angles = list(map(Vec2.getAngle, position_changes))

    # Calculate the changes of the angles
    angle_changes = [angles[i+1] - angles[i] for i in range(len(angles)-1)]

    # Rotate all movements so that they are in line with the x axis. This makes
    # it easy to see how the next movement varies from the current one
    movements = [position_changes[i+1].rotated(-angles[i])
                 for i in range(len(angle_changes))]

    # Return everything as a tuple
    return movements, angles, angle_changes, position_changes


# Turn a movement back into a position
def pack(movement, angles, positions):
    return positions[-1] + movement.rotated(angles[-1])


def applyWeights(data, weights):
    # Trim weights to the size of the angles
    weights = weights[:len(data)]

    # Make the sum of the weights equal to 1
    adjusted_weights = list(map(lambda w: w/sum(weights),
                                weights))
    # Apply weights to angles
    weighted_data = [a * b for a, b in zip(data,
                                           adjusted_weights)]
    return weighted_data


def predict(data, repeats=1):
    results = []
    for i in range(repeats):
        positions = list(map(lambda d: d[0], data))
        times = list(map(lambda d: d[1], data))
        time_step = times[-1] - times[-2]

        # Get information about the positions
        movements, angles, _, _ = unpack(positions)

        # Use weighted average rotation of last movements and the current speed
        # for the next position
        predicted_angle = sum(applyWeights(
                movements, ROTATION_PREDICTION_WEIGHTS),
                Vec2(0, 0)).getAngle()

        predicted_magnitude = movements[-1].getMagnitude()

        predicted_movement = Vec2(predicted_angle,
                                  predicted_magnitude,
                                  True)

        # Dampen rotation
        predicted_movement = predicted_movement.rotated(
                -predicted_movement.getAngle() * 0.95)

        position = (pack(predicted_movement, angles, positions),
                    times[-1] + time_step)

        results.append(position)
        data.append(position)

    return results
