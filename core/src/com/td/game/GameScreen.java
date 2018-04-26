package com.td.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class GameScreen implements Screen {//чтобы класс был экраном имплементим скрин
    private SpriteBatch batch;
    private BitmapFont font24;
    private Map map;
    private TurretEmitter turretEmitter;
    private MonsterEmitter monsterEmitter;
    private ParticleEmitter particleEmitter;
    private TextureAtlas atlas;
    private TextureRegion selectedCellTexture;
    private Stage stage;
    private Skin skin;
    private Group groupTurretAction;

    private Vector2 mousePosition;

    private int selectedCellX, selectedCellY;

    public ParticleEmitter getParticleEmitter() {
        return particleEmitter;
    }

    public MonsterEmitter getMonsterEmitter() {
        return monsterEmitter;
    }

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {//когда переключаемся на экран, то срабатывает метод и грузит все это(подготовка)
        atlas = Assets.getInstance().getAtlas();//ссылаемся на атлас, который лежит в менеджере ресурсов
        selectedCellTexture = atlas.findRegion("cursor");
        map = new Map(atlas);
        font24 = Assets.getInstance().getAssetManager().get("zorque24.ttf", BitmapFont.class);
        turretEmitter = new TurretEmitter(atlas, this, map);
        monsterEmitter = new MonsterEmitter(atlas, map, 60);
        particleEmitter = new ParticleEmitter(atlas.findRegion("star16"));
        Gdx.input.setInputProcessor(null);//когда мы переходим с экрана на экран(в геймскрин) то надо сбрасывать InputProcessor
        //иначе мы будем на экране игры поподать по кнопкам меню.

        mousePosition = new Vector2(0, 0);
        createGUI();
    }

    public void createGUI() {
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);

        InputProcessor myProc = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                selectedCellX = (int) (mousePosition.x / 80);
                selectedCellY = (int) (mousePosition.y / 80);
                return true;
            }
        };

        InputMultiplexer im = new InputMultiplexer(stage, myProc);
        Gdx.input.setInputProcessor(im);

        skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());
        skin.add("font24", font24);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("shortButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        groupTurretAction = new Group();
        groupTurretAction.setPosition(50, 600);

        Button btnSetTurret = new TextButton("Set", skin, "simpleSkin");
        Button btnUpgradeTurret = new TextButton("Upg", skin, "simpleSkin");
        Button btnDestroyTurret = new TextButton("Dst", skin, "simpleSkin");
        Button btnToMenu = new TextButton("Menu", skin, "simpleSkin");
        btnSetTurret.setPosition(10, 10);
        btnUpgradeTurret.setPosition(110, 10);
        btnDestroyTurret.setPosition(210, 10);
        btnToMenu.setPosition(1110,10);
        groupTurretAction.addActor(btnSetTurret);
        groupTurretAction.addActor(btnUpgradeTurret);
        groupTurretAction.addActor(btnDestroyTurret);
        groupTurretAction.addActor(btnToMenu);

        stage.addActor(groupTurretAction);

        btnSetTurret.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                turretEmitter.setTurret(selectedCellX, selectedCellY);
            }
        });
        btnDestroyTurret.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
           turretEmitter.destroyTurret(selectedCellX,selectedCellY);
            }
        });
        btnToMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
           ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        map.render(batch);
        turretEmitter.render(batch);
        monsterEmitter.render(batch, font24);
        particleEmitter.render(batch);
        batch.setColor(1, 1, 0, 0.5f);
        batch.draw(selectedCellTexture, selectedCellX * 80, selectedCellY * 80);
        batch.setColor(1, 1, 1, 1);
        batch.end();
        stage.draw();
    }

    public void update(float dt) {
        mousePosition.set(Gdx.input.getX(), Gdx.input.getY());//постоянно запрашиваем координаты
        ScreenManager.getInstance().getViewport().unproject(mousePosition);//преобразуем их в мировые, чтобы знать куда ставим при смене размера экрана
        monsterEmitter.update(dt);
        turretEmitter.update(dt);
        particleEmitter.update(dt);
        particleEmitter.checkPool();
        stage.act(dt);
    }

    @Override
    public void resize(int width, int height) {//на каком бы экране мы не были.вызываем метод пересчета коофицентов экрана(для всех экранов сразу)
        ScreenManager.getInstance().resize(width, height);// передаем размеры нового окна(сжатого, увеличинного)
    }

    @Override
    public void pause() {//если адройд перешел на другое приложение. экран в игре застывает и ни чего не делает

    }

    @Override
    public void resume() {//когда мы вернулись из состояния пайзы

    }

    @Override
    public void hide() {//когда экран был закрыт

    }

    @Override
    public void dispose() {//когда кто то решил его уничтожить(полностью закрыть)
    }
}
