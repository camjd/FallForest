import java.util.Random;
import java.io.*;
import java.lang.Thread;
 
class Branch {
    private int leaves;
    private Branch[] subBranches;

    private static Random rand = new Random();
 
    public Branch(int leaves, int subCount) {
        this.leaves = leaves;
        subBranches = new Branch[subCount];

        for (int i = 0; i < subCount; i++) {
            subBranches[i] =
                new Branch(rand.nextInt(3) + 1, rand.nextInt(2));
        }
    }
 
    public void fallLeaves(int level, int maxWindStrength, FileWriter logWriter) throws IOException, InterruptedException {
        if (leaves <= 0 && subBranches.length == 0) return;
 
        if (leaves > 0) {
            int windGust = rand.nextInt(maxWindStrength) + 1;
            int fallCount = Math.min(windGust, leaves);

            int baseDelay = 750;
            int gustDelay = Math.max(100, baseDelay - (windGust - 1) * 200);

            for (int i = 0; i < fallCount; i++) {
                String msg = " ".repeat(level * 2) + "🍁  A leaf falls from branch level " + level;
                System.out.println(msg);
                logWriter.write(msg + "\n");

                leaves--;
                Thread.sleep(gustDelay);
            }
           fallLeaves(level, maxWindStrength, logWriter); 
        }
 
        for (Branch b : subBranches) {  // recurse into sub-branches
            b.fallLeaves(level + 1, maxWindStrength, logWriter);
        }
    }

    public void growLeaves(int level, FileWriter logWriter) throws IOException, InterruptedException {
        leaves = rand.nextInt(3) + 1;
        String msg = " ".repeat(level * 2) + "🌿  A leaf grows on branch level " + level;
        System.out.println(msg);
        logWriter.write(msg + "\n");

        int baseGrowthDelay = 750;
        int growthDelay = Math.max(100, baseGrowthDelay - (leaves - 1) * 200);
        Thread.sleep(growthDelay);

        for (Branch b : subBranches) {
            b.growLeaves(level + 1, logWriter);
        }
    }
}

public class FallForest {
    public static void main(String[] args) {
        System.out.println("🍂  The forest prepares for autumn...");

        try (FileWriter logWriter = new FileWriter("leaf_fall_log.txt")) {
            Branch tree = new Branch(3, 2);
            int maxWindStrength = 3;

            tree.fallLeaves(0, maxWindStrength, logWriter);
            System.out.println("❄️  The forest sleeps for winter...");

            System.out.println("🪴  Spring has returned!");
            tree.growLeaves(0, logWriter);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}