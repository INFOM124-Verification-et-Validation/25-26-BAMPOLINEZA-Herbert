package nl.tudelft.jpacman.board;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test suite to confirm that {@link Unit}s correctly (de)occupy squares.
 *
 * @author Jeroen Roosen 
 *
 */
class OccupantTest {

    /**
     * The unit under test.
     */
    private Unit unit;

    /**
     * Resets the unit under test.
     */
    @BeforeEach
    void setUp() {
        unit = new BasicUnit();
    }

    /**
     * Asserts that a unit has no square to start with.
     */
    @Test
    void noStartSquare() {
        // TODO - Done
        assertThat(this.unit.hasSquare()).isFalse();
    }

    /**
     * Tests that the unit indeed has the target square as its base after
     * occupation.
     */
    @Test
    void testOccupy() {
        // TODO - Done
        Square target = new BasicSquare();
        target.put(this.unit);
        assertThat(this.unit.getSquare()).isEqualTo(target);
    }

    /**
     * Test that the unit indeed has the target square as its base after
     * double occupation.
     */
    @Test
    void testReoccupy() {
        // TODO - Done
        // Place the unit on the first square
        Square target1 = new BasicSquare();
        Square target2 = new BasicSquare();
        target1.put(this.unit);
        assertThat(this.unit.getSquare()).isEqualTo(target1);

        // Move the unit to the second square
        target2.put(this.unit);
        assertThat(this.unit.getSquare()).isEqualTo(target2);
        assertThat(target1.getOccupants()).doesNotContain(this.unit);
        assertThat(target2.getOccupants()).contains(this.unit);
    }
    
}
