from collision import find_collision
from predict import predict
from read import read_data
from interpolate import interpolate
from show import show
from parser import parse
import time

DANGER_PATH = ".danger/"

while True:
    parse("Seretra/Prediction/main.srt")
    print("done")
    time.sleep(1)
