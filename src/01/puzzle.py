from pathlib import Path
import itertools

IS_TEST = False

inputFile = "example.txt" if IS_TEST else "input.txt"
fileContents = Path(inputFile).read_text().splitlines()

input = list(
    filter(lambda l: not l.startswith("answer") and not l.startswith("#"),
           fileContents))

expected = int(list(
    filter(lambda l: l.startswith("answer1:"), fileContents))
    [0].removeprefix("answer1:"))

current = 0
previous = 1000
increases = 0

for i in input:
    current = int(i)
    if current > previous:
        increases = increases + 1
    previous = current
    
print(increases)
print(increases == expected)