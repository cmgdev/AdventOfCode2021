from pathlib import Path
import itertools

inputFile = "example.txt"
fileContents = Path(inputFile).read_text().splitlines()

sumTarget = 2020
input = list(
    filter(lambda l: not l.startswith("answer") and not l.startswith("#"),
           fileContents))

combos = itertools.permutations(input, 2)
product = 0

for c in combos:
    sum = int(c[0]) + int(c[1])
    if sum == sumTarget:
        product = int(c[0]) * int(c[1])
        break

print(product)
expected = int(list(
    filter(lambda l: l.startswith("answer1:"), fileContents))
    [0].removeprefix("answer1:"))

print(product == expected)
