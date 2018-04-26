package com.td.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;

public class TowerDefenseGame extends Game {//наследуемся от класса. для того чтобы управлять экранами
    private SpriteBatch batch;

    // План работ:
    // Система экранов +
    // Система ресурсов +
    // Работа с интерфейсом +
    // Работа со текстом(вывод текста, генерация шрифтов) +
    // Подгонка под разные экраны +
    // Звуки/музыка +
    // --------------------
    // TurretEmitter
    // Подкорректировать систему частиц
    // Добавить домик
    // Добавить монетки
    // Улучшение пушек
    // Портирование на андроид

    @Override
    public void create() {
        batch = new SpriteBatch();
        ScreenManager.getInstance().init(this, batch);//отдаем ссылку на игру и batch(экземпляр объекта в этом)
        ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);//чтобы переключать экраны.сначало меню
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();//дельта д
        getScreen().render(dt);//спрашиваем какой экран активен и спрашиваем у него рендер
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
