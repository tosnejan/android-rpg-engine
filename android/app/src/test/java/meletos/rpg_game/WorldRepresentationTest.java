package meletos.rpg_game;


import com.google.gson.Gson;

import org.junit.Test;

import meletos.rpg_game.file_io.WorldRepresentation;

import static org.junit.Assert.*;

/**
 * Not real tests :D
 */
public class WorldRepresentationTest {
    @Test
    public void test1() {
        WorldRepresentation wr = new WorldRepresentation();
        wr.generateWorldRepresentation("C:\\melecmat\\android\\app\\src\\main\\assets\\lvl");
    }

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