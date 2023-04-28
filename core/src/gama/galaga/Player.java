package gama.galaga;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Player extends Killable {

    Color color;

    public Player(Vector2 position, int radius) {
        super(position, radius);
        color = Color.PURPLE;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        super.draw(shapeRenderer);
        shapeRenderer.triangle(this.x - this.radius, this.y, this.x + this.radius, this.y, this.x, this.y - this.radius * 3);
    }


}
