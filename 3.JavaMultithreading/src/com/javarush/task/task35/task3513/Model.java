package com.javarush.task.task35.task3513;

import java.util.*;

public class Model {
    int score;
    int maxTile;

    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;

    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    private Stack<Tile[][]> previousStates = new Stack<>();
    private Stack<Integer> previousScores = new Stack<>();
    private boolean isSaveNeeded = true;

    public Model() {
        resetGameTiles();
        score = 0;
        maxTile = 0;
    }

    public void left() {
        if (isSaveNeeded)
            saveState(gameTiles);
        move(LEFT);
        isSaveNeeded = true;
    }

    public void right() {
        saveState(gameTiles);
        move(RIGHT);
    }

    public void up() {
        saveState(gameTiles);
        move(UP);
    }

    public void down() {
        saveState(gameTiles);
        move(DOWN);
    }

    public void rollback() {
        if (previousScores.empty() || previousStates.empty()) return;

        gameTiles = previousStates.pop();
        score = previousScores.pop();
    }

    public void randomMove() {
        int moveIndex = ((int) (Math.random() * 100)) % 4;
        switch (moveIndex) {
            case LEFT:  left();break;
            case RIGHT: right();break;
            case UP:    up();break;
            case DOWN:  down();break;
        }
    }

    public void autoMove() {
        PriorityQueue<MoveEfficiency> queue = new PriorityQueue<>(4, Collections.reverseOrder());
        queue.add(getMoveEfficiency(this::left));
        queue.add(getMoveEfficiency(this::right));
        queue.add(getMoveEfficiency(this::down));
        queue.add(getMoveEfficiency(this::up));

        queue.peek().getMove().move();
    }

    boolean hasBoardChanged() {
        Tile[][] prevTiles = previousStates.peek();
        for (int x = 0; x < prevTiles.length; x++) {
            for (int y = 0; y < prevTiles.length; y++) {
                if (prevTiles[y][x].value != gameTiles[y][x].value)
                    return true;
            }
        }
        return false;
    }

    boolean canMove() {
        for (int y = 0; y < FIELD_WIDTH; y++) {
            for (int x = 0; x < FIELD_WIDTH; x++) {
                try {
                    if (gameTiles[y][x].value == 0 ||
                        gameTiles[y][x].value == gameTiles[y][x+1].value ||
                        gameTiles[y][x].value == gameTiles[y+1][x].value)

                        return true;
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }
            }
        }
        return false;
    }

    MoveEfficiency getMoveEfficiency(Move move) {
        MoveEfficiency efficiency;
        move.move();
        if (hasBoardChanged())
            efficiency = new MoveEfficiency(getEmptyTiles().size(), score, move);
        else
            efficiency = new MoveEfficiency(-1, 0, move);
        rollback();

        return efficiency;
    }

    void resetGameTiles() {
        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];

        for (int y = 0; y < FIELD_WIDTH; y++) {
            for (int x = 0; x < FIELD_WIDTH; x++) {
                gameTiles[y][x] = new Tile();
            }
        }
        addTile();
        addTile();
    }

//    public static void main(String args[]) {
//        int[][] ints = {
//                {0, 2, 2, 4},
//                {2, 2, 4, 0},
//                {0, 4, 0, 4},
//                {4, 2, 4, 2}
//        };
//        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
//        for (int x = 0; x < FIELD_WIDTH; x++) {
//            for (int y = 0; y < FIELD_WIDTH; y++) {
//                gameTiles[y][x] = new Tile(ints[y][x]);
//            }
//        }
//
//        for (Tile[] tile1 : gameTiles) {
//            for (Tile tile : tile1) {
//                System.out.print(tile + " ");
//            }
//            System.out.println();
//        }
//        System.out.println("-------------------------------------------------------");
//        down();
//        for (Tile[] tile1 : gameTiles) {
//            for (Tile tile : tile1) {
//                System.out.print(tile + " ");
//            }
//            System.out.println();
//        }
//    }

    private void move(int dir) {
        boolean isChanged = false;
        Tile[] tiles = new Tile[FIELD_WIDTH];

        for (int i = 0; i < FIELD_WIDTH; i++) {
            int t = 0;
            switch (dir) {
                case UP:
                    for (int j = 0; j < FIELD_WIDTH; j++) {
                        tiles[t++] = gameTiles[j][i];
                    }break;
                case DOWN:
                    for (int j = FIELD_WIDTH - 1; j >= 0; j--) {
                        tiles[t++] = gameTiles[j][i];
                    }break;
                case LEFT:
                    for (int j = 0; j < FIELD_WIDTH; j++) {
                        tiles[t++] = gameTiles[i][j];
                    }break;
                case RIGHT:
                    for (int j = FIELD_WIDTH - 1; j >= 0; j--) {
                        tiles[t++] = gameTiles[i][j];
                    }break;
            }
            if (compressTiles(tiles) | mergeTiles(tiles))
                isChanged = true;
        }

        if (isChanged) addTile();
    }

    private void saveState(Tile[][]tiles) {
        Tile[][] tilesForSave = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int x = 0; x < FIELD_WIDTH; x++) {
            for (int y = 0; y < FIELD_WIDTH; y++) {
                tilesForSave[y][x] = new Tile(tiles[y][x].value);
            }
        }
        isSaveNeeded = false;
        previousStates.push(tilesForSave);
        previousScores.push(score);
    }

    private void addTile() {
        List<Tile> emptyTiles = getEmptyTiles();
        if (emptyTiles.size() > 0)
            emptyTiles.get((int) (emptyTiles.size() * Math.random())).value = (Math.random() < 0.9) ? 2 : 4;
    }

    private List<Tile> getEmptyTiles() {
        List<Tile> emptyTiles = new ArrayList<>();
        for (int y = 0; y < FIELD_WIDTH; y++) {
            for (int x = 0; x < FIELD_WIDTH; x++) {
                Tile tile = gameTiles[y][x];
                if (tile.isEmpty())
                    emptyTiles.add(tile);
            }
        }
        return emptyTiles;
    }

    private boolean compressTiles(Tile[] tiles) {
        boolean isChanged = false;
        for (int i = 1; i < tiles.length; i++) {
            if (tiles[i-1].value == 0 && tiles[i].value != 0) {
                tiles[i-1].value = tiles[i].value;
                tiles[i].value = 0;
                i = 0;
                isChanged = true;
            }
        }
        return isChanged;
    }

    private boolean mergeTiles(Tile[] tiles) {
        boolean isChanged = false;
        for (int j = 0; j < 3; j++) {
            if (tiles[j].value != 0 && tiles[j].value == tiles[j+1].value) {
                tiles[j].value = tiles[j].value * 2;
                tiles[j + 1].value = 0;
                if (tiles[j].value > maxTile) maxTile = tiles[j].value;
                score += tiles[j].value;
                isChanged = true;

            }
        }
        compressTiles(tiles);
        return isChanged;
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }
}
