package com.td.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ScreenManager {//это особый класс(синголтон). и он управляет всеми экранами
    public enum ScreenType { //класс - список
        MENU, GAME //пока что у нас два экрана - меню и игра
    }

    public static final int WORLD_WIDTH = 1280; // размер нашего мира всегда 1280*720
    public static final int WORLD_HEIGHT = 720; //независимо от растяжения

    private TowerDefenseGame game;
    private SpriteBatch batch;
    private GameScreen gameScreen; //ниже три экрана. они создаются сразу
    private LoadingScreen loadingScreen;
    private MenuScreen menuScreen;
    private Screen targetScreen;
    private Viewport viewport;
    private Camera camera;

    private static ScreenManager ourInstance = new ScreenManager();//реализация синголтона

    public static ScreenManager getInstance() {//реализация синголтона
        return ourInstance;
    }

    public Viewport getViewport() {
        return viewport;
    }

    private ScreenManager() {
    }

    public void init(TowerDefenseGame game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
        this.camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT); //создает некую камеру
        //задача viewport влепить игру в нужный экран
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera); //создает некий viewport
        this.gameScreen = new GameScreen(batch);
        this.menuScreen = new MenuScreen(batch);
        this.loadingScreen = new LoadingScreen(batch);
    }

    public void resize(int width, int height) {//метод. который при растяжении пересчитывает пропорции
        viewport.update(width, height);//мересчет матрицы преобразования
        viewport.apply();//применяем новые параметры пересчета.но разрешение такое же, хотя окно больше\меньше
    }

    public void resetCamera() {//сброс камеры
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);//камера центруеться по центру.куда мы хотитм смотреть
        camera.update();//камера пересчитывает коофиценты
        batch.setProjectionMatrix(camera.combined);//применяем параметра(перемешаем камеру)
    }

    public void changeScreen(ScreenType type) {
        Screen screen = game.getScreen(); //запоминаем эеран на котором мы на хходились.если на менюскрине, то запомнили
        Assets.getInstance().clear();//мы говорим, что все ресурсы были загружены(музыка,звуки, картинки) мы чистим
        if (screen != null) {//если экран какой то был,
            screen.dispose();//то мы освобождаем ресусы этого экрана
        }
        resetCamera();
        //говорим, что накоой экран мы не хоте ли бы перейти, сразу на него не попадаем
        game.setScreen(loadingScreen);//сначала попадаем на loadingScreen(экран загрузки).чтобы загрузить ресурсы
        switch (type) {
            case MENU:
                targetScreen = menuScreen;//несмотря на то что мы сейчас на экране загрузке, мы запоминаем, что мы хотели на экран меню
                Assets.getInstance().loadAssets(ScreenType.MENU);//обращаемся к классу Assert, чтобы он загрузил нам ресурсы для экрана меню
                break;
            case GAME:
                targetScreen = gameScreen;
                Assets.getInstance().loadAssets(ScreenType.GAME);
                break;
        }
    }

    public void goToTarget() {//метод, для перехода из экрана загрузки на тот экран, который мы запоминали выше(куда мы хотели)
        game.setScreen(targetScreen);
    }
}
