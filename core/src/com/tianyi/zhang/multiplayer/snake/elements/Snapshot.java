package com.tianyi.zhang.multiplayer.snake.elements;

import java.util.*;

import static com.tianyi.zhang.multiplayer.snake.helpers.Constants.RIGHT;

public class Snapshot {
    private final Map<Integer, Snake> snakes;
    private final int step;

    public Snapshot(int[] snakeIds) {
        snakes = new HashMap<Integer, Snake>(snakeIds.length);
        // TODO: Coordinates of snake are hard-coded for now
        int[] coords = {3, 3, 2, 3};
        for (int i : snakeIds) {
            snakes.put(new Integer(i), new Snake(i, coords, RIGHT, 0));
        }
        step = 0;
    }

    private Snapshot(Map<Integer, Snake> snakes, int step) {
        this.snakes = snakes;
        this.step = step;
    }

    public synchronized void updateDirection(int snakeId, int direction, int inputId) {
        snakes.put(new Integer(snakeId), snakes.get(new Integer(snakeId)).changeDirection(direction, inputId));
    }

    public synchronized Set<Integer> getSnakeIds() {
        return snakes.keySet();
    }

    public synchronized Snake getSnakeById(int id) {
        return snakes.get(new Integer(id));
    }

    public synchronized Snake getSnakeById(Integer id) {
        return snakes.get(id);
    }

    public int getStep() {
        return step;
    }

    public synchronized Snapshot next() {
        Map<Integer, Snake> nextSnakes = new HashMap<Integer, Snake>(snakes.size());
        for (Map.Entry<Integer, Snake> entry : snakes.entrySet()) {
            nextSnakes.put(new Integer(entry.getKey()), entry.getValue().next());
        }
        return new Snapshot(nextSnakes, step+1);
    }
}
