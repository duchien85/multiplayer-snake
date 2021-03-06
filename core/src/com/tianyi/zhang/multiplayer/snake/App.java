package com.tianyi.zhang.multiplayer.snake;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.esotericsoftware.minlog.Log;
import com.kotcrab.vis.ui.VisUI;
import com.tianyi.zhang.multiplayer.snake.agents.Client;
import com.tianyi.zhang.multiplayer.snake.agents.IAgent;
import com.tianyi.zhang.multiplayer.snake.agents.Server;
import com.tianyi.zhang.multiplayer.snake.states.ErrorState;
import com.tianyi.zhang.multiplayer.snake.states.GameState;
import com.tianyi.zhang.multiplayer.snake.states.TitleScreenState;

import java.util.Stack;

public class App extends Game {
    protected Stack<GameState> stateStack;
    protected IAgent agent;

    public IAgent getAgent() {
        return agent;
    }

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_NONE);
        Log.set(Log.LEVEL_NONE);
        VisUI.load(VisUI.SkinScale.X2);
        VisUI.setDefaultTitleAlign(Align.center);

        stateStack = new Stack<GameState>();
        pushState(new TitleScreenState(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        destroyAgent();
        while (!stateStack.empty()) {
            stateStack.pop().dispose();
        }
        VisUI.dispose();
    }

    public void pushState(GameState gameState) {
        if (!stateStack.isEmpty()) {
            stateStack.peek().pause();
        }
        stateStack.push(gameState);
        setScreen(gameState);
    }

    public void popState() {
        stateStack.pop().dispose();
        setScreen(stateStack.peek());
        stateStack.peek().resume();
    }

    public void setState(GameState gameState) {
        stateStack.pop().dispose();
        pushState(gameState);
    }

    public void gotoTitleScreen() {
        while (stateStack.size() > 1) {
            popState();
        }
        destroyAgent();
    }

    public void gotoErrorScreen(String errorMessage) {
        while (stateStack.size() > 1) {
            popState();
        }
        destroyAgent();
        pushState(new ErrorState(this, errorMessage));
    }

    public void initAgent(boolean isServer) {
        if (isServer) {
            agent = new Server();
        } else {
            agent = new Client();
        }
    }

    public void destroyAgent() {
        if (agent != null) {
            agent.destroy();
        }
    }
}
