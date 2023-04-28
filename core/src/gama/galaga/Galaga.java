package gama.galaga;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.SnapshotArray;

public class Galaga extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;

    private Player player;

    private Killable[][] enemies;

    private SnapshotArray<Missile> missiles;

    private final int enemyRow = 4;
    private final int enemyColumn = 6;

    private float windowWidth;
    private float windowHeight;

    private float horizontalDistance;

    private float verticalDistance;

    private final int radius = 10;

    private final float delay = 0.2f;

    private float fireDeltaTime;

    private boolean finished;

    private float waitingTime;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        windowWidth = Gdx.graphics.getWidth();
        windowHeight = Gdx.graphics.getHeight();
        player = new Player(new Vector2(windowWidth / 2, radius * 4), radius);
        enemies = new Killable[4][6];
        horizontalDistance = windowWidth / (enemyColumn + 1);
        verticalDistance = windowHeight / 2 / (enemyRow + 1);
        for (int i = 0; i < enemyRow; i++) {
            for (int j = 0; j < enemyColumn; j++) {
                enemies[i][j] = new Killable(
                        new Vector2(
                                (j + 1) * horizontalDistance,
                                windowHeight - ((1 + i) * verticalDistance)), radius);
            }
        }
        missiles = new SnapshotArray<>();
        finished = false;
        fireDeltaTime = waitingTime = 0;
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        if (!finished) update();
        else {
            waitingTime += Gdx.graphics.getDeltaTime();
            if (waitingTime > 3) create();
        }
        draw();
    }

    private void update() {
        handleInput();
        moveEnemies();
        moveMissiles();
        checkCollision();
    }

    private void checkCollision() {
        for (Killable[] killers : enemies)
            for (Killable enemy : killers) {
                if (!enemy.isDead() && enemy.overlaps(player)) {
                    finished = true;
                }
                for (Missile missile : missiles) {
                    if (!enemy.isDead() && Intersector.overlaps(enemy, missile)) {
                        enemy.setDead();
                        missiles.removeValue(missile, true);
                    }
                }
            }
        boolean atLeastOneLiving = false;
        for (Killable[] killers : enemies)
            for (Killable enemy : killers)
                atLeastOneLiving |= !enemy.isDead();
        if (!atLeastOneLiving) finished = true;

    }

    private void moveMissiles() {
        for (Missile missile : missiles) missile.y++;
    }

    private void moveEnemies() {
        float move = horizontalDistance / 60;
        if (Killable.toLeft) move *= -1;
        float moveDown;
        if (enemies[0][0].x < horizontalDistance / 2) {
            Killable.toLeft = false;
            moveDown = verticalDistance / 30;
        } else if (enemies[0][enemyColumn - 1].x > windowWidth - horizontalDistance / 2) {
            Killable.toLeft = true;
            moveDown = verticalDistance / 30;
        } else {
            moveDown = 0;
        }
        for (int i = 0; i < enemyRow; i++) {
            for (int j = 0; j < enemyColumn; j++) {
                enemies[i][j].setPosition(new Vector2(enemies[i][j].x + move, enemies[i][j].y - moveDown));
            }
        }
    }

    private void handleInput() {
        fireDeltaTime += Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            if (player.x > radius) player.setPosition(new Vector2(player.x - 2, player.y));
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            if (player.x < windowWidth - radius)
                player.setPosition(new Vector2(player.x + 2, player.y));
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
            if (fireDeltaTime > delay) {
                missiles.add(new Missile(player.x - 1, player.y + radius, 2, 9));
                fireDeltaTime = 0;
            }
    }

    private void draw() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(player.color);
        player.draw(shapeRenderer);
        shapeRenderer.setColor(Killable.color);
        for (Killable[] enemyRow : enemies)
            for (Killable enemy : enemyRow) enemy.draw(shapeRenderer);
        shapeRenderer.setColor(Missile.color);
        for (Missile missile : missiles)
            missile.draw(shapeRenderer);
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
