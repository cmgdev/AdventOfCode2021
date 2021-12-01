from pathlib import Path
import itertools

def getNumIncreases(input):
    current = 0
    previous = 1000
    increases = 0

    for i in input:
        current = int(i)
        if current > previous:
            increases = increases + 1
        previous = current
    return increases

IS_TEST = False

inputFile = "example.txt" if IS_TEST else "input.txt"
fileContents = Path(inputFile).read_text().splitlines()

input = list(
    filter(lambda l: not l.startswith("answer") and not l.startswith("#"),
           fileContents))

expected1 = int(list(
    filter(lambda l: l.startswith("answer1:"), fileContents))
    [0].removeprefix("answer1:"))

increases = getNumIncreases(input)
    
print(increases)
print(increases == expected1)



expected2 = int(list(
    filter(lambda l: l.startswith("answer2:"), fileContents))
    [0].removeprefix("answer2:"))

windows = []

idx = 0

while idx + 2 < len(input):
    windows.append(int(input[idx]) + int(input[idx + 1]) + int(input[idx + 2]))
    idx = idx + 1
    
# print(windows)
increases = getNumIncreases(windows)
print(increases)
print(increases == expected2)
    