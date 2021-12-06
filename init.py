from pathlib import Path

def mkdir(dir:Path):
    if not dir.exists():
        dir.mkdir()

root = Path("./src")
mkdir(root)

for i in range(1, 26):
    dir = root.joinpath(f"{i:02d}")
    mkdir(dir)
    
    puzzleFile = dir.joinpath(f"Puzzle{i:02d}.java")
    if not puzzleFile.exists():
        with puzzleFile.open("a") as f:
            f.write("""import java.nio.file.Files;
            import java.nio.file.Path;

            """)
            f.write(f"   public class Puzzle{i:02d} {{\n")
            f.write("       final static boolean IS_TEST = true;\n")
            f.write(f'      static Path input = Path.of("./src/{i:02d}/" + (IS_TEST ? "example.txt" : "input.txt"));\n')
            f.write("""    
                    public static void main(String... args) {
                        try {
                            List<String> lines = Files.readAllLines(input);

                            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                                    .findFirst().get();
                            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                                    .findFirst().get();
                        }
                        catch(Exception e){
                            System.out.println("Shit! " + e);
                        }
                    }
                }
            """)
        
    exampleFile = dir.joinpath("example.txt")
    if not exampleFile.exists():
        exampleFile.touch()
        
    inputFile = dir.joinpath("input.txt")
    if not inputFile.exists():
        inputFile.touch()
