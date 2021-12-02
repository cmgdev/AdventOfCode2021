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
        hDelta += int(i.removeprefix("forward "))
    elif i.startswith("down "):
        dDelta += int(i.removeprefix("down "))
    elif i.startswith("up "):
        dDelta -= int(i.removeprefix("up "))

print(hDelta)
print(dDelta)
product = hDelta * dDelta
print(product)
print(product == int(expected1))

expected2 = list(
    filter(lambda l: l.startswith("answer2:"), fileContents))[0].removeprefix("answer2:")

hDelta = 0
dDelta = 0
aim = 0

for i in input:
    if i.startswith("forward "):
        x = int(i.removeprefix("forward "))
        hDelta += x
        dDelta += (aim * x)
    elif i.startswith("down "):
        aim += int(i.removeprefix("down "))
    elif i.startswith("up "):
        aim -= int(i.removeprefix("up "))
        
print(hDelta)
print(dDelta)
print(aim)
product = hDelta * dDelta
print(product)
print(product == int(expected2))