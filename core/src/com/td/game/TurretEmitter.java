package com.td.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class TurretEmitter {
    private GameScreen gameScreen;
    private TextureAtlas atlas;
    private Map map;
    private Turret[] turrets;

    public TurretEmitter(TextureAtlas atlas, GameScreen gameScreen, Map map) {
        this.gameScreen = gameScreen;
        this.map = map;
        this.atlas = atlas;
        this.turrets = new Turret[20];
        for (int i = 0; i < turrets.length; i++) {//создаем пушки
            turrets[i] = new Turret(atlas, gameScreen, map, 0, 0);
        }
    }

    public void render(SpriteBatch batch) {//метод открисовки активных пушек
        for (int i = 0; i < turrets.length; i++) {
            if (turrets[i].isActive()) {
                turrets[i].render(batch);
            }
        }
    }

    public void update(float dt) {//метод апгрейда активных пушек
        for (int i = 0; i < turrets.length; i++) {
            if (turrets[i].isActive()) {
                turrets[i].update(dt);
            }
        }
    }

    public void setTurret(int cellX, int cellY) {//метод постановки пушки
        if (map.isCellEmpty(cellX, cellY)) {//если клетка пуста
            for (int i = 0; i < turrets.length; i++) {
                //проверяем активна турель, и нет ли там туррели
                if (turrets[i].isActive() && turrets[i].getCellX() == cellX && turrets[i].getCellY() == cellY) {
                    return;//если турель есть про сто выходим и ни сего не делаем
                }
            }
            for (int i = 0; i < turrets.length; i++) {
                if (!turrets[i].isActive()) {//если пушка не активна
                    turrets[i].activate(cellX, cellY);//активируем ее и ставим в эту точку
                    break;
                }
            }
        }
    }

    public void destroyTurret(int cellX, int cellY) {//метод уничтожения пушки
        for (int i = 0; i < turrets.length; i++) {
            //если пушка активнаи и мы нашли пушку в указанной клетке(сравнение координат)
            if (turrets[i].isActive() && turrets[i].getCellX() == cellX && turrets[i].getCellY() == cellY) {
                turrets[i].deactivate();//удаляем пушку из этой клетке
            }
        }
    }
}

