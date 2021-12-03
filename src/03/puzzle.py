from pathlib import Path

IS_TEST = False

inputFile = "example.txt" if IS_TEST else "input.txt"
fileContents = Path(inputFile).read_text().splitlines()

input = list(
    filter(lambda l: not l.startswith("answer") and not l.startswith("#"),
           fileContents))

expected1 = list(
    filter(lambda l: l.startswith("answer1:"), fileContents))[0].removeprefix("answer1:")

expected2 = list(
    filter(lambda l: l.startswith("answer2:"), fileContents))[0].removeprefix("answer2:")

numLines = len(input)
lineWidth = len(input[0])
counts = []
for i in range(0, lineWidth):
    counts.append(0)

for i in input:
    for j in range(0, lineWidth):
        counts[j] += int(i[j])

gamma = ""
epsilon = ""
for c in counts:
    if c > (numLines / 2):
        gamma += "1"
        epsilon += "0"
    else:
        gamma += "0"
        epsilon += "1"

product = int(gamma, 2) * int(epsilon, 2)
print(product)
print(product == int(expected1))