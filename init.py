from pathlib import Path

def mkdir(dir:Path):
    if not dir.exists():
        dir.mkdir()

root = Path("./src")
mkdir(root)

for i in range(1, 26):
    dir = root.joinpath(f"{i:02d}")
    mkdir(dir)
    
    puzzleFile = dir.joinpath("puzzle.py")
    if not puzzleFile.exists():
        puzzleFile.touch()
        
    exampleFile = dir.joinpath("example.txt")
    if not exampleFile.exists():
        exampleFile.touch()
        
    inputFile = dir.joinpath("input.txt")
    if not inputFile.exists():
        inputFile.touch()
