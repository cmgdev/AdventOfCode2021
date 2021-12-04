from pathlib import Path

IS_TEST = True

inputFile = "example.txt" if IS_TEST else "input.txt"
fileContents = Path(inputFile).read_text().splitlines()

input = list(
    filter(lambda l: not l.startswith("answer") and not l.startswith("#"),
           fileContents))

expected1 = list(
    filter(lambda l: l.startswith("answer1:"), fileContents))[0].removeprefix("answer1:")

expected2 = list(
    filter(lambda l: l.startswith("answer2:"), fileContents))[0].removeprefix("answer2:")

calledNumbers = input[0].split(',')
boards = []
for i in range(2,len(input),6):
    lines = list( map( lambda m: 
                    list( filter(lambda l: len(l.strip()) > 0 , m.split(' '))), 
                    input[i:i+5]))
    # lines = input[i:i+5]
    # numDict = {}
    # for line in lines:
    #     for num in line.split(' '):
    #         if len(num.strip()) > 0:
    #             numDict[int(num)] = False
                
    boards.append(lines)

print(boards)
