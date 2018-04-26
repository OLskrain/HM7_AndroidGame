package com.td.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

/**
 * Created by FlameXander on 12.03.2018.
 */

public class Assets {//это синголтон. класс который хранит и генерит ресурсы для игры(картинки, шрифт, музыка, звуки и т.д)
    private static final Assets ourInstance = new Assets();

    public static Assets getInstance() {
        return ourInstance;
    }

    private AssetManager assetManager;//есть менеджер ресурсов. это LibGDX
    private TextureAtlas textureAtlas;//есть атлас,ссылка на основной атлс

    public TextureAtlas getAtlas() {
        return textureAtlas;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    private Assets() {
        assetManager = new AssetManager();
    }

    public void loadAssets(ScreenManager.ScreenType type) {//укажите тип экрана, а я уже загружу
        switch (type) {
            case MENU:
                assetManager.load("game.pack", TextureAtlas.class);//грузим атлас.какой файл загрузить и какого класса
                createStandardFont(32);//генерим нужные шрифты
                createStandardFont(96);
                break;
            case GAME:
                assetManager.load("game.pack", TextureAtlas.class);
                createStandardFont(24);
                createStandardFont(48);
                break;
        }
    }

    public void createStandardFont(int size) {//метод для отображения шрифтов. подгружаем файл и говорим как эго отрисовывать
        FileHandleResolver resolver = new InternalFileHandleResolver();//3 строчки ниже. мы учим прогу цеплять ttf файлы
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();//фондпараметры
        fontParameter.fontFileName = "zorque.ttf";//берем фал с шрифтами
        fontParameter.fontParameters.size = size;//размер шрифта.приходит извне
        fontParameter.fontParameters.color = Color.WHITE;//цвет букв
        fontParameter.fontParameters.borderWidth = 1;//границы 1 пиксель
        fontParameter.fontParameters.borderColor = Color.BLACK;//цвет границы черный
        fontParameter.fontParameters.shadowOffsetX = 1;//тени
        fontParameter.fontParameters.shadowOffsetY = 1;//тени
        fontParameter.fontParameters.shadowColor = Color.BLACK;//цвет тени
        assetManager.load("zorque" + size + ".ttf", BitmapFont.class, fontParameter);//в менеджер загружаем все что получилось
    }

    public void makeLinks() {//метод котоый оворит, что он ссылается на атлас , который в хранилище
        textureAtlas = assetManager.get("game.pack", TextureAtlas.class);
    }

    public void clear() {//метод для очистки всех ресурсов в менеджере хранилища
        assetManager.clear();
    }
}
