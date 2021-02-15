package com.brent.mario.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.brent.mario.MarioBros;
import com.brent.mario.Scenes.Hud;
import com.brent.mario.Screens.PlayScreen;

public class Coin extends InteractiveTileObject{
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 109;
    public Coin(PlayScreen screen, Rectangle bounds){
        super(screen,bounds);
        tileSet= map.getTileSets().getTileSet("terrain_2");
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.COIN_BIT);
    }
    @Override
    public void onHeadHit() {
        Gdx.app.log("Coin", "Collision");
        getcell().setTile(tileSet.getTile(BLANK_COIN));
        Hud.addScore(100);
    }
}
