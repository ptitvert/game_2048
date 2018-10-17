package org.perucchi.games.game_2048;

import org.perucchi.games.game_2048.datamodel.Grid;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Game2048Application implements CommandLineRunner {

    private static String ttyConfig;

    public static void main(String[] args) {
        SpringApplication.run(Game2048Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Scores score = new Scores();
        Grid gameGrid = GameRules.initialBoard();
        HistoryGrid historyGrid = new HistoryGrid();
        historyGrid.addToHistory(gameGrid, score);
        clearScreen();
        System.out.println(DisplayGrid.tableDisplay(gameGrid));
        System.out.println("Score      = " + score.getCurrentScore());
        System.out.println("High Score = " + score.getHighScore());


        try {
            setTerminalToCBreak();

            boolean continuePlay = true;
            byte arrowKey = -1;
            while (continuePlay) {
                if (arrowKey == -1)
                    TimeUnit.MILLISECONDS.sleep(100);
                if (System.in.available() != 0) {
                    int c = System.in.read();

                    switch (c) {
                        case 'q': { // QUIT
                            continuePlay = false;
                            break;
                        }

                        case 'U': { // Toggle UNDO
                            historyGrid.toggleUndo();
                            break;
                        }

                        case 68: { // LEFT
                            if (arrowKey != 1)
                                break;
                            arrowKey = -1;
                        }
                        case 'h': { // LEFT
                            clearScreen();
                            Grid newGameGrid = GameRules.move(gameGrid, score, Directions.LEFT);
                            if (!newGameGrid.equals(gameGrid)) {
                                gameGrid = GameRules.randomPlacement(newGameGrid);
                                historyGrid.addToHistory(gameGrid, score);
                            }
                            System.out.println(DisplayGrid.tableDisplay(gameGrid));
                            System.out.println("Score      = " + score.getCurrentScore());
                            System.out.println("High Score = " + score.getHighScore());
                            break;
                        }

                        case 67: { // RIGHT
                            if (arrowKey != 1)
                                break;
                            arrowKey = -1;
                        }
                        case 'l': { // RIGHT
                            clearScreen();
                            Grid newGameGrid = GameRules.move(gameGrid, score, Directions.RIGHT);
                            if (!newGameGrid.equals(gameGrid)) {
                                gameGrid = GameRules.randomPlacement(newGameGrid);
                                historyGrid.addToHistory(gameGrid, score);
                            }
                            System.out.println(DisplayGrid.tableDisplay(gameGrid));
                            System.out.println("Score      = " + score.getCurrentScore());
                            System.out.println("High Score = " + score.getHighScore());
                            break;
                        }

                        case 65: { // UP
                            if (arrowKey != 1)
                                break;
                            arrowKey = -1;
                        }
                        case 'k': { // UP
                            clearScreen();
                            Grid newGameGrid = GameRules.move(gameGrid, score, Directions.UP);
                            if (!newGameGrid.equals(gameGrid)) {
                                gameGrid = GameRules.randomPlacement(newGameGrid);
                                historyGrid.addToHistory(gameGrid, score);
                            }
                            System.out.println(DisplayGrid.tableDisplay(gameGrid));
                            System.out.println("Score      = " + score.getCurrentScore());
                            System.out.println("High Score = " + score.getHighScore());
                            break;
                        }

                        case 66: { // DOWN
                            if (arrowKey != 1)
                                break;
                            arrowKey = -1;
                        }
                        case 'j': { // DOWN
                            clearScreen();
                            Grid newGameGrid = GameRules.move(gameGrid, score, Directions.DOWN);
                            if (!newGameGrid.equals(gameGrid)) {
                                gameGrid = GameRules.randomPlacement(newGameGrid);
                                historyGrid.addToHistory(gameGrid, score);
                            }
                            System.out.println(DisplayGrid.tableDisplay(gameGrid));
                            System.out.println("Score      = " + score.getCurrentScore());
                            System.out.println("High Score = " + score.getHighScore());
                            break;
                        }

                        case 'u': { // UNDO
                            clearScreen();
                            historyGrid.undo();
                            gameGrid = historyGrid.getCurrentGrid();
                            score = historyGrid.getCurrentScore();
                            System.out.println(DisplayGrid.tableDisplay(gameGrid));
                            System.out.println("Score      = " + score.getCurrentScore());
                            System.out.println("High Score = " + score.getHighScore());
                            break;
                        }

                        case 's': { // Set checkPoint
                            clearScreen();
                            historyGrid.setCheckPoint();
                            System.out.println(DisplayGrid.tableDisplay(gameGrid));
                            System.out.println("Score      = " + score.getCurrentScore());
                            System.out.println("High Score = " + score.getHighScore());
                            break;
                        }

                        case 'r': { // Go back to CheckPoint
                            clearScreen();
                            historyGrid.restoreCheckPoint();
                            gameGrid = historyGrid.getCurrentGrid();
                            score = historyGrid.getCurrentScore();
                            System.out.println(DisplayGrid.tableDisplay(gameGrid));
                            System.out.println("Score      = " + score.getCurrentScore());
                            System.out.println("High Score = " + score.getHighScore());
                            break;
                        }

                        case 27: {
                            if (arrowKey == -1)
                                arrowKey = 0;
                            break;
                        }

                        case 91: {
                            if (arrowKey == 0)
                                arrowKey = 1;
                            break;
                        }

                        default:
                            System.out.println(c);
                    }
                }
            } // end while
        } catch (IOException e) {
            System.err.println("IOException");
        } catch (InterruptedException e) {
            System.err.println("InterruptedException");
        } finally {
            try {
                stty(ttyConfig.trim());
            } catch (Exception e) {
                System.err.println("Exception restoring tty config");
            }
        }

    }

    private static void clearScreen() {
        System.out.println("\033[H\033[2J");
    }

    private static void setTerminalToCBreak() throws IOException, InterruptedException {

        ttyConfig = stty("-g");

        // set the console to be character-buffered instead of line-buffered
        stty("-icanon min 1");

        // disable character echoing
        stty("-echo");
    }

    /**
     * Execute the stty command with the specified arguments
     * against the current active terminal.
     */
    private static String stty(final String args)
            throws IOException, InterruptedException {
        String cmd = "stty " + args + " < /dev/tty";

        return exec(new String[]{
                "sh",
                "-c",
                cmd
        });
    }

    /**
     * Execute the specified command and return the output
     * (both stdout and stderr).
     */
    private static String exec(final String[] cmd)
            throws IOException, InterruptedException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        Process p = Runtime.getRuntime().exec(cmd);
        int c;
        InputStream in = p.getInputStream();

        while ((c = in.read()) != -1) {
            bout.write(c);
        }

        in = p.getErrorStream();

        while ((c = in.read()) != -1) {
            bout.write(c);
        }

        p.waitFor();

        String result = new String(bout.toByteArray());
        return result;
    }


}
