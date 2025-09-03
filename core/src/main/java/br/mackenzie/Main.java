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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main implements ApplicationListener {
    Texture bucketTexture;
    SpriteBatch spriteBatch;
    FitViewport viewport;
    Sprite bucketSprite;

    Texture backgroundTexture;
    Vector2 touchPos;
    Texture dropTexture;

    Array<Sprite> dropSprites;

    @Override
    public void create() {
        bucketTexture = new Texture("bucket.png");
        backgroundTexture = new Texture("background.png");
        dropTexture = new Texture("drop.png"); // continua usando drop.png

        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);

        bucketSprite = new Sprite(bucketTexture);
        bucketSprite.setSize(1, 1);

        touchPos = new Vector2();

        dropSprites = new Array<>();
        createDroplet(); // cria o primeiro drop
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        if (width <= 0 || height <= 0) return;
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() {
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime();

        // Movimento horizontal
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucketSprite.translateX(speed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bucketSprite.translateX(-speed * delta);
        }

        // Movimento vertical
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            bucketSprite.translateY(speed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            bucketSprite.translateY(-speed * delta);
        }

        // Movimento por toque na tela
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);
            bucketSprite.setCenterX(touchPos.x);
            bucketSprite.setCenterY(touchPos.y);
        }
    }


    private void logic() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float bucketWidth = bucketSprite.getWidth();
        float bucketHeight = bucketSprite.getHeight();

        // Limite horizontal
        bucketSprite.setX(MathUtils.clamp(bucketSprite.getX(), 0, worldWidth - bucketWidth));

        // Limite vertical
        bucketSprite.setY(MathUtils.clamp(bucketSprite.getY(), 0, worldHeight - bucketHeight));

        float delta = Gdx.graphics.getDeltaTime();

        // mover drops para baixo
        for (Sprite drop : dropSprites) {
            drop.translateY(-2f * delta);
        }

        // colisão e remoção
        for (int i = dropSprites.size - 1; i >= 0; i--) {
            Sprite drop = dropSprites.get(i);
            if (drop.getBoundingRectangle().overlaps(bucketSprite.getBoundingRectangle())) {
                dropSprites.removeIndex(i); // remove se colidir
            } else if (drop.getY() + drop.getHeight() < 0) {
                dropSprites.removeIndex(i); // remove se sair da tela
            }
        }

        // criar novos drops periodicamente
        if (MathUtils.random() < 0.02f) { // chance de spawn a cada frame
            createDroplet();
        }
    }


    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        spriteBatch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);

        bucketSprite.draw(spriteBatch);

        for (Sprite drop : dropSprites) {
            drop.draw(spriteBatch);
        }

        spriteBatch.end();
    }

    private void createDroplet() {
        float dropSize = 1f;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        Sprite drop = new Sprite(dropTexture);
        drop.setSize(dropSize, dropSize);
        drop.setX(MathUtils.random(0f, worldWidth - dropSize));
        drop.setY(worldHeight);

        dropSprites.add(drop);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        spriteBatch.dispose();
        bucketTexture.dispose();
        backgroundTexture.dispose();
        dropTexture.dispose();
    }
}
