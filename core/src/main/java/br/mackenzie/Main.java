package br.mackenzie;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    Texture bucketTexture;
    SpriteBatch spriteBatch;
    FitViewport viewport;
    Sprite bucketSprite;

    Texture backgroundTexture;
    Vector2 touchPos;


    @Override
    public void create() {
        bucketTexture = new Texture("bucket.png");
        backgroundTexture = new Texture("background.png");

        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8,5);

        bucketSprite = new Sprite(bucketTexture);
        bucketSprite.setSize(1,1);

        touchPos = new Vector2();



        // Prepare your application here.
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); //true centers the camera
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        // Resize your application here. The parameters represent the new window size.
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
        // Draw your application here.
    }

    private void input() {
        float speed =  4f;
        float delta = Gdx.graphics.getDeltaTime();

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            bucketSprite.translateX(speed * delta); //Move o bucket right
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bucketSprite.translateX(-speed * delta);

        }
        if (Gdx.input.isTouched()){
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);
            bucketSprite.setCenterX(touchPos.x);


        }

    }

    private void logic(){
        float worldWidth = viewport.getWorldWidth();
        float bucketWidth = bucketSprite.getWidth();
        bucketSprite.setX(MathUtils.clamp(bucketSprite.getX(), 0,worldWidth));





    }

    private void draw(){
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        spriteBatch.draw(backgroundTexture, 0,0,worldWidth,worldHeight);


        bucketSprite.draw(spriteBatch);
        spriteBatch.end();

    }
    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void dispose() {
        // Destroy application's resources here.
    }
}
