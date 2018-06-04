package com.tianyi.zhang.multiplayer.snake.states.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.tianyi.zhang.multiplayer.snake.App;
import com.tianyi.zhang.multiplayer.snake.states.GameState;

import java.io.IOException;

public class BroadcastState extends GameState {
    private volatile int clientCount = 0;
    private static final String TAG = BroadcastState.class.getCanonicalName();

    // UI elements
    private Stage stage;
    private VisTable table;
    private VisTextButton btnStart;

    public BroadcastState(App app) {
        super(app);

        // Set up UI element layout
        stage = new Stage();

        table = new VisTable(true);
        table.setFillParent(true);
        stage.addActor(table);

        btnStart = new VisTextButton("Start the game");
        btnStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.debug(TAG, "START GAME button clicked");

                synchronized (BroadcastState.this) {
                    while (clientCount <= 0) {
                        try {
                            BroadcastState.this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                BroadcastState.this.app.pushState(new SVMainGameState(BroadcastState.this.app));
            }
        });
        table.row();
        table.add(btnStart);
        Gdx.input.setInputProcessor(stage);

        // Start broadcasting
        try {
            this.app.getAgent().broadcast(new Listener() {
                @Override
                public void connected(Connection connection) {
                    Gdx.app.debug(TAG, "connected");
                    synchronized (BroadcastState.this) {
                        clientCount += 1;
                        BroadcastState.this.notify();
                    }
                }

                @Override
                public void disconnected(Connection connection) {
                    Gdx.app.debug(TAG, "disconnected");
                    synchronized (BroadcastState.this) {
                        clientCount -= 1;
                    }
                }
            });
        } catch (IOException e) {
            // TODO: Display error message
            Gdx.app.error(TAG, e.getMessage());
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act();
        stage.draw();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
