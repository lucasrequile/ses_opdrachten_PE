package be.kuleuven.candycrush.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CandyCrushModelTest{
    private CandyCrushModel model;

    @BeforeEach
    public void setUp() {
        model = new CandyCrushModel(10, 10, "Player1");
    }

    @Test
    public void testCandyGeneration_RightSize() {
        model.generateCandyArray(10, 10);
        assertEquals(100, model.getCandyArray().size());
    }

    @Test
    public void testCandyGeneration_Between1And5(){
        model.generateCandyArray(10, 10);
        for (int candy : model.getCandyArray()) {
            assertTrue(candy >= 1 && candy <= 5);
        }
    }
    @Test
    public void testScoreIncreasing() {
        model.increaseScore(10);
        assertEquals(10, model.getScore());
        model.increaseScore(5);
        assertEquals(15, model.getScore());
    }


    @Test
    public void testRemoveSameNeighbours_under3() {
        model.generateCandyArray(10, 10);
        model.getCandyArray().set(0,3);
        model.getCandyArray().set(1,3);
        model.getCandyArray().set(2,3);
        model.getCandyArray().set(10,0);
        model.getCandyArray().set(11,0);
        model.getCandyArray().set(12,0);
        model.removeSameNeighbours(0);
        assertEquals(3, model.getCandyArray().get(2));
        assertEquals(0, model.getCandyArray().get(10));
        assertEquals(0, model.getCandyArray().get(11));
        assertEquals(0, model.getCandyArray().get(12));
        assertEquals(0,model.getScore());
    }

    @Test
    public void testRemoveSameNeighbours_above3() {
        model.generateCandyArray(10, 10);
        model.getCandyArray().set(0,3);
        model.getCandyArray().set(1,3);
        model.getCandyArray().set(2,3);
        model.getCandyArray().set(10,3);
        model.getCandyArray().set(11,3);
        model.getCandyArray().set(12,3);
        model.removeSameNeighbours(0);
        assertEquals(3, model.getCandyArray().get(2));
        assertEquals(3, model.getCandyArray().get(12));
        assertEquals(4,model.getScore());
    }

    @Test
    public void testGameReset() {
        model.generateCandyArray(10, 10);
        model.setScore(50);
        model.removeSameNeighbours(0);
        model.reset(10,10);
        assertEquals(0, model.getScore());
        assertEquals(100, model.getCandyArray().size());
    }
    @Test
    public void testName() {
        assertEquals("Player1", model.getName());
    }

    @Test
    public void testInvalidInputsWhenMakingNewModel() {
        assertThrows(IllegalArgumentException.class, () -> new CandyCrushModel(-10, 10, "Player1"));
        assertThrows(IllegalArgumentException.class, () -> new CandyCrushModel(10, -10, "Player1"));
        assertThrows(IllegalArgumentException.class, () -> new CandyCrushModel(0, 10, "Player1"));
        assertThrows(IllegalArgumentException.class, () -> new CandyCrushModel(10, 0, "Player1"));
    }
    @Test
    public void testInvalidInputsWhenMakingNewCandyArray() {
        assertThrows(IllegalArgumentException.class, () -> model.generateCandyArray(-10, 10));
        assertThrows(IllegalArgumentException.class, () -> model.generateCandyArray(10, -10));
        assertThrows(IllegalArgumentException.class, () -> model.generateCandyArray(0, 10));
        assertThrows(IllegalArgumentException.class, () -> model.generateCandyArray(10, 0));
    }

    @Test
    public void testGetWidthAndHeight() {
        CandyCrushModel model = new CandyCrushModel(5, 7, "Player1");
        assertEquals(5, model.getHeight());
        assertEquals(7, model.getWidth());
    }

}