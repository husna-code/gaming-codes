package com.brent.mario.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.brent.mario.MarioBros;
import com.brent.mario.Scenes.Hud;
import com.brent.mario.Sprites.Goomba;
import com.brent.mario.Sprites.Mario;
import com.brent.mario.Tools.B2WorldCreator;
import com.brent.mario.Tools.WorldContactListener;

public class PlayScreen implements Screen {
    private MarioBros game;
    private TextureAtlas atlas;
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    private Mario player;
    private Goomba goomba;

    public PlayScreen(MarioBros game){
        atlas = new TextureAtlas("C:\\Users\\user1\\OneDrive\\MarioBROS\\android\\assets\\TP.txt");
        this.game=game;
        gamecam=new OrthographicCamera();

        gamePort=new FitViewport(MarioBros.V_WIDTH/MarioBros.PPM, MarioBros.V_HEIGHT/MarioBros.PPM, gamecam);

        hud= new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("C:\\Users\\user1\\OneDrive\\MarioBROS\\m1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/MarioBros.PPM);

        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/ 2, 0);

        world = new World(new Vector2(0,-10),true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(this);

        //create mario in our game world
        player=new Mario( this);

        world.setContactListener(new WorldContactListener());

        goomba = new Goomba(this, .32f/MarioBros.PPM, .32f/MarioBros.PPM);

    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    public void show() {

    }
    public void handleInput(float dt)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            player.b2body.applyLinearImpulse(new Vector2(0,5f), player.b2body.getWorldCenter(),true);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)&& player.b2body.getLinearVelocity().x <=2)
            player.b2body.applyLinearImpulse(new Vector2(0.1f,0),player.b2body.getWorldCenter(),true);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)&& player.b2body.getLinearVelocity().x >= -2)
            player.b2body.applyLinearImpulse(new Vector2(-0.1f,0),player.b2body.getWorldCenter(),true);
    }
     public void update(float dt)
     {
         handleInput(dt);

         world.step(1/60f, 6, 2);

         player.update(dt);
         goomba.update(dt);
         hud.update(dt);

         gamecam.position.x=player.b2body.getPosition().x;

         gamecam.update();

         renderer.setView(gamecam);

         game.batch.setProjectionMatrix(gamecam.combined);
         game.batch.begin();
         player.draw(game.batch);
         game.batch.end();

     }
    @Override
    public void render(float delta) {
        update(delta);


        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        b2dr.render(world, gamecam.combined);
        
        renderer.render();
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        goomba.draw(game.batch);
        game.batch.end();

        //render box2ddebuglines
        b2dr.render(world, gamecam.combined);



        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);

    }
    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
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
      map.dispose();
      renderer.dispose();
      world.dispose();
      b2dr.dispose();
      hud.dispose();
    }
}
