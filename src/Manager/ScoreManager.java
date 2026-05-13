package src.Manager;
public class ScoreManager {
    private int score;
    private int level;
    private int lines;
    private boolean levelUp = false;

    public ScoreManager() {
        reset();
    }

    public int getScore() { return score; }
    public int getLevel() { return level; }
    public int getLines() { return lines; }

    public void addLines(int cleared) {
        if (cleared <= 0) return;
        int oldLevel = level;
        score += calculateScore(cleared);
        lines += cleared;
        level = lines / 5 + 1; // cứ clear 5 rows tăng 1 level
        if (level > oldLevel) {
            levelUp = true;
        }
    }

    private int calculateScore(int lines) {
        switch (lines) {
            case 1: return 100 * level;
            case 2: return 300 * level;
            case 3: return 500 * level;
            case 4: return 800 * level;
            default: return 0;
        }
    }

    public boolean isLevelUp() { return levelUp; }
    public void resetLevelUp() { levelUp = false; }
    
    public void reset() {
        score = 0;
        level = 1;
        lines = 0;
    }
}