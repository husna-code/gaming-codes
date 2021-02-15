package com.brent.mario.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.brent.mario.MarioBros;
import com.brent.mario.Screens.PlayScreen;

public class Mario extends Sprite {
    public enum State{FALLING,JUMPING,STANDING,RUNNING};
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion mariostand;
    private Sprite sprite;
    private float stop;
    private Animation <TextureRegion> mariorun;
    private Animation <TextureRegion> mariojump;
    private float statetimer;
    private boolean runningright;

    public Mario( PlayScreen screen){
        super(screen.getAtlas().findRegion("little_mario"));

        this.world= screen.getWorld();
        currentState=State.STANDING;
        previousState=State.STANDING;
        statetimer=0;
        runningright=true;
        Array<TextureRegion>frames=new Array<TextureRegion>();
        for (int i = 1;i < 3; i++)
            frames.add(new TextureRegion(getTexture(),i*144,0,16,16));
        mariorun=new Animation <TextureRegion>(0.12f,frames);
        frames.clear();

        for (int i = 3; i < 5; i++)
            frames.add(new TextureRegion(getTexture(),i*144,0,16,16));
        mariojump=new Animation <TextureRegion>(0.12f,frames);

        mariostand = new TextureRegion(getTexture(),144,0,16,16);

        defineMario();
        sprite=new Sprite(mariostand);
        setBounds(144,0,16/MarioBros.PPM, 16/MarioBros.PPM);
        setRegion(mariostand);

    }
    public void update(float dt)
    {
        setPosition(b2body.getPosition().x-getWidth()/2, b2body.getPosition().y-getHeight()/2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt)
    {
        currentState=getState();
        TextureRegion region;
        switch (currentState)
        {
            case JUMPING:
                region= (TextureRegion) mariojump.getKeyFrame(statetimer);
                break;
            case RUNNING:
                region= (TextureRegion) mariorun.getKeyFrame(statetimer,true);
                break;
            case FALLING:
            case STANDING:
                default:
                    region=mariostand;
                    break;
        }
        if ((b2body.getLinearVelocity().x<0|| !runningright)&& !region.isFlipX()){
            region.flip(true,false);
            runningright=false;
        }
        else if ((b2body.getLinearVelocity().x>0||runningright) && region.isFlipX()){
            region.flip(true,false);
            runningright=true;
        }
        statetimer = currentState == previousState ? statetimer + dt : 0;
        previousState=currentState;
        return region;
    }
    public State getState()
    {
        if(b2body.getLinearVelocity().y>0||(b2body.getLinearVelocity().y<0 && previousState==State.JUMPING))
            return State.JUMPING;
        else if (b2body.getLinearVelocity().y<0)
            return State.FALLING;
        else if (b2body.getLinearVelocity().x!=0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public void defineMario(){
        BodyDef bdef=new BodyDef();
        bdef.position.set(32 / MarioBros.PPM, 32 /MarioBros.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape=new PolygonShape();
        shape.setAsBox(16/2 /MarioBros.PPM,16/2 /MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT | MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT | MarioBros.ENEMY_HEAD_BIT ;

       /* FixtureDef fdef2 = new FixtureDef();
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-2 / MarioBros.PPM, -6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, -6 / MarioBros.PPM));
        fdef2.shape = feet;
        b2body.createFixture(fdef2);*/

        fdef.shape=shape;
        b2body.createFixture(fdef);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 7/MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 7/MarioBros.PPM));
        fdef.shape = head;
        fdef.isSensor=true;

        b2body.createFixture(fdef).setUserData("head");
    }
}
