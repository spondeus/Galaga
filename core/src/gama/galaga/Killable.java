package gama.galaga;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Killable extends Circle {

    static Color color = Color.BLUE;

    private boolean dead = false;

    public static boolean toLeft = true;

    public Killable(Vector2 position, int radius) {
        super(position.x, position.y, radius);
    }

    public void draw(ShapeRenderer shapeRenderer) {
        if (!dead) shapeRenderer.circle(this.x, this.y, this.radius);
    }

    public void setPosition(Vector2 position) {
        this.x = position.x;
        this.y = position.y;
    }


    public boolean isDead() {
        return dead;
    }

    public void setDead() {
        dead = true;
    }
}
