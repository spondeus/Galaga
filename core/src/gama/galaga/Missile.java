package gama.galaga;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;


public class Missile extends Rectangle {

    static Color color = Color.GREEN;

    public Missile(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(this.x, this.y, this.width, this.height);
    }
}
