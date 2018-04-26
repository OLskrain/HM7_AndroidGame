package com.td.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MenuScreen implements Screen {
    private SpriteBatch batch;
    private Stage stage;//это сцена, на которую добавляються актеры(кнопки, бигуньки и т.д)
    private Skin skin;//это как будут выглядить актеры(их параметры)
    private Music music;
    private BitmapFont font32;
    private BitmapFont font96;

    public MenuScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        //как грузяться фонты.запрашиваем серез AssetManager
        font32 = Assets.getInstance().getAssetManager().get("zorque32.ttf", BitmapFont.class);
        font96 = Assets.getInstance().getAssetManager().get("zorque96.ttf", BitmapFont.class);
//        music = Gdx.audio.newMusic(Gdx.files.internal("Jumping bat.wav"));
//        music.setLooping(true);
//        music.play();
        createGUI();//весь интерфейс вынесен
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0.4f, 0.4f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        //ниже мы отрисовываем текст,Halign - это центровка надписи, wrap- это перенос текста
        font96.draw(batch, "Java Tower Defense", 0, 600, 1280, 1, false);
        batch.end();
        stage.draw();//чтобы нарисовать интерфейс
    }

    public void update(float dt) {
        stage.act(dt);
    }

    public void createGUI() {
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);//создаем сцену. обязательно Viewport и batch
        Gdx.input.setInputProcessor(stage);//обрабодчик событий. который отправляет в сцену
        skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());//скин взаимодействует с атлосом
        skin.add("font32", font32);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();//стиль кнопки
        textButtonStyle.up = skin.getDrawable("simpleButton");//если кнопка отжата
        textButtonStyle.font = font32;//шрифт текста
        skin.add("simpleSkin", textButtonStyle);

        Button btnNewGame = new TextButton("Start New Game", skin, "simpleSkin");//кнопка GDX
        Button btnExitGame = new TextButton("Exit Game", skin, "simpleSkin");
        btnNewGame.setPosition(640 - 160, 180);//координаты кнопок
        btnExitGame.setPosition(640 - 160, 60);
        stage.addActor(btnNewGame);//добавляем в сцену
        stage.addActor(btnExitGame);
        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {//если кто то нажал играть, переходим в GAME
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });
        btnExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {//если нажали выход, то выходим
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void resize(int width, int height) { //пересчет матрицы соотношений расзмеров
        ScreenManager.getInstance().resize(width, height);
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
    }
}
