from pathlib import Path

IS_TEST = False

inputFile = "example.txt" if IS_TEST else "input.txt"
fileContents = Path(inputFile).read_text().splitlines()

input = list(
    filter(lambda l: not l.startswith("answer") and not l.startswith("#"),
           fileContents))

expected1 = list(
    filter(lambda l: l.startswith("answer1:"), fileContents))[0].removeprefix("answer1:")

hDelta = 0
dDelta = 0

for i in input:
    if i.startswith("forward "):
        hDelta = int(i.removeprefix("forward ")) + hDelta
    elif i.startswith("down "):
        dDelta = int(i.removeprefix("down ")) + dDelta
    elif i.startswith("up "):
        dDelta = dDelta - int(i.removeprefix("up "))

print(hDelta)
print(dDelta)
product = hDelta * dDelta
print(product)
print(product == int(expected1))

expected2 = list(
    filter(lambda l: l.startswith("answer2:"), fileContents))[0].removeprefix("answer2:")