package com.javarush.games.game2048;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;

public class Game2048 extends Game {
    private static final int SIDE = 4;

    private int[][] gameField = new int[SIDE][SIDE];

    private int score;
    private boolean isGameStopped = false;

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        if (!canUserMove() && !isGameStopped) {
            gameOver();
            return;
        }

        switch (key) {
            case UP:
                if (isGameStopped) break;
                moveUp();
                drawScene();
                break;
            case DOWN:
                if (isGameStopped) break;
                moveDown();
                drawScene();
                break;
            case LEFT:
                if (isGameStopped) break;
                moveLeft();
                drawScene();
                break;
            case RIGHT:
                if (isGameStopped) break;
                moveRight();
                drawScene();
                break;
            case SPACE:
                isGameStopped = false;
                createGame();
                drawScene();
                break;
        }
    }

    private void createGame() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                gameField[y][x] = 0;
            }
        }
        score = 0;
        setScore(score);

        createNewNumber();
        createNewNumber();
    }

    private void drawScene() {
        for (int y = 0; y < gameField.length; y++) {
            for (int x = 0; x < gameField[y].length; x++) {
                setCellColoredNumber(x, y, gameField[y][x]);
            }
        }
    }

    private void createNewNumber() {
        if (getMaxTileValue() == 2048) {
            win();
            return;
        }

        int randX;
        int randY;
        do {
            randX = getRandomNumber(SIDE);
            randY = getRandomNumber(SIDE);
        } while (gameField[randY][randX] != 0);

        gameField[randY][randX] = getRandomNumber(10) == 9 ? 4 : 2;
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.BISQUE, "You're winner! Yeah baby!!", Color.BLACK, 40);
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.BISQUE, "IMPOSTER", Color.RED, 80);
    }

    private boolean canUserMove() {
        for (int y = 0; y < SIDE - 1; y++) {
            for (int x = 0; x < SIDE - 1; x++) {
                if (gameField[y][x] == 0 ||
                        gameField[y][x] == gameField[y+1][x] ||
                        gameField[y][x] == gameField[y][x+1])
                    return true;
            }
        }
        for (int y = 0; y < SIDE - 1; y++) {
            if (gameField[y][SIDE-1] == 0 ||
                    gameField[y][SIDE-1] == gameField[y+1][SIDE-1])
                return true;
        }
        for (int x = 0; x < SIDE - 1; x++) {
            if (gameField[SIDE-1][x] == 0 ||
                    gameField[SIDE-1][x] == gameField[SIDE-1][x+1])
                return true;
        }
        return gameField[SIDE - 1][SIDE - 1] == 0;
    }

    private void setCellColoredNumber(int x, int y, int value) {
        String num = null;

        if (value != 0 && value != 1) {
            int t = value;
            while (t > 1) {
                if (t % 2 != 0) {
                    num = "";
                    break;
                }
                t /= 2;
            }
        } else
            num = "";

        if (num == null) num = String.valueOf(value);

        setCellValueEx(x, y, getColorByValue(value), num);
    }

    private Color getColorByValue(int value) {
        switch (value) {
            case 0: return Color.WHITESMOKE;
            case 2: return Color.TURQUOISE;
            case 4: return Color.LIGHTSEAGREEN;
            case 8: return Color.DEEPSKYBLUE;
            case 16: return Color.DODGERBLUE;
            case 32: return Color.ROYALBLUE;
            case 64: return Color.AQUA;
            case 128: return Color.AQUAMARINE;
            case 256: return Color.PALEGREEN;
            case 512: return Color.MEDIUMSEAGREEN;
            case 1024: return Color.FORESTGREEN;
            case 2048: return Color.VIOLET;

            default: return Color.WHITE;
        }
    }

    private int getMaxTileValue() {
        int maxValue = 0;
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                if (maxValue < gameField[y][x])
                    maxValue = gameField[y][x];
            }
        }
        return maxValue;
    }

    private boolean compressRow(int[] row) {
        boolean res = false;
        int indexZero = -1;
        for (int i = 0; i < row.length; i++) {
            if (row[i] == 0 && indexZero == -1) {
                indexZero = i;
            } else if (row[i] != 0 && indexZero != -1) {
                row[indexZero++] = row[i];
                row[i] = 0;
                res = true;
            }
        }
        return res;
    }

    private boolean mergeRow(int[] row) {
        boolean res = false;
        int index = -1;
        int val = 0;
        for (int i = 0; i < row.length; i++) {
            if (row[i] == 0) {
                index = -1;
                val = 0;
            } else if (row[i] != val){
                index = i;
                val = row[i];
            } else if (row[i] == val) {
                score += 2*val;
                setScore(score);
                row[index] = 2*val;
                row[i] = 0;
                val = 0;
                res = true;
            }
        }
        return res;
    }

    private void moveLeft() {
        boolean wasCompress = false;
        boolean wasMerge = false;
        boolean wasMove = false;
        for (int y = 0; y < SIDE; y++) {
            wasCompress = compressRow(gameField[y]);
            wasMerge = mergeRow(gameField[y]);
            if (wasMerge) compressRow(gameField[y]);
            if (!wasMove) wasMove = wasCompress || wasMerge;
        }
        if (wasMove) createNewNumber();
    }

    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }
    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }
    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private void rotateClockwise() {
        int newArr[][] = new int[SIDE][SIDE];
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                newArr[y][x] = gameField[SIDE-x-1][y];
            }
        }
        gameField = newArr;
    }
}
