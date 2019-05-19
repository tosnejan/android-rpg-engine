package meletos.rpg_game;


import com.google.gson.Gson;

import org.junit.Test;

/**
 * Not real tests :D
 */
public class WorldRepresentationTest {


    @Test
    public void test2() {
        Coordinates[] coordinates = {
                new Coordinates(100, 200),
                new Coordinates(300, 400),
                new Coordinates(400, 500),
                new Coordinates(700, 600),
                new Coordinates(300, 400),
                new Coordinates(1000, 200)
        };
        System.out.println(new Gson().toJson(coordinates));
    }

    @Test
    public void test3() {
        Coordinates[] coordinates = {
                new Coordinates(100, 200),
                new Coordinates(300, 400),
                new Coordinates(400, 500),
                new Coordinates(700, 600),
                new Coordinates(300, 400),
                new Coordinates(1000, 200)
        };
        System.out.println(new Gson().toJson(coordinates));
    }


}
