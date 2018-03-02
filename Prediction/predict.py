from vec2 import Vec2


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
def pack(movement, positions, angles):
    return positions[-1] + movement.rotated(angles[-1])


def predict(positions):
    movement_changes, angles, _, _ = unpack(positions)

    # TODO detect change in movement pattern
    predicted_movement_change = movement_changes[-1]

    # Dampen rotation
    predicted_movement_change = predicted_movement_change.rotated(
            -predicted_movement_change.getAngle() * 0.15)

    return pack(predicted_movement_change, positions, angles)
