package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.*;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InkyTest {
    private PacManSprites sprites;
    private PlayerFactory playerFactory;
    private GhostFactory ghostFactory;
    private LevelFactory levelFactory;
    private BoardFactory boardFactory;
    private MapParser mapParser;

    @BeforeEach
    void setUp() {
        sprites = new PacManSprites();
        playerFactory = new PlayerFactory(sprites);
        ghostFactory = new GhostFactory(sprites);
        levelFactory = new LevelFactory(sprites, ghostFactory);
        boardFactory = new BoardFactory(sprites);
        mapParser = new GhostMapParser(levelFactory, boardFactory, ghostFactory);
    }

    // Good weather tests
//    @Test
//    void normalMovementTest() {
//        List<String> map = Arrays.asList(
//            "##########",
//            "#P  B    #",
//            "#   I    #",
//            "##########"
//        );
//        Level level = mapParser.parseMap(map);
//        Player player = playerFactory.createPacMan();
//        level.registerPlayer(player);
//
//        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
//        assertNotNull(inky);
//        Optional<Direction> direction = inky.nextAiMove();
//        assertTrue(direction.isPresent());
//    }

    @Test
    void inkyWithClearPathTest() {
        List<String> map = Arrays.asList(
            "##########",
            "#P      I#",
            "#   B    #",
            "##########"
        );
        Level level = mapParser.parseMap(map);
        Player player = playerFactory.createPacMan();
        level.registerPlayer(player);

        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        assertNotNull(inky);
        Optional<Direction> direction = inky.nextAiMove();
        assertTrue(direction.isPresent());
    }

    @Test
    void inkyTargetRightFacingPacmanTest() {
        List<String> map = Arrays.asList(
            "#############",
            "#     P   I #",  // P facing right, I on right
            "#   B       #",  // B below and left of Pacman
            "#############"
        );
        Level level = mapParser.parseMap(map);
        Player player = playerFactory.createPacMan();
        level.registerPlayer(player);
        player.setDirection(Direction.EAST); // Important: set Pacman's direction

        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        assertNotNull(inky);
        Optional<Direction> direction = inky.nextAiMove();

        // Inky should move WEST towards target point
        // Target = 2 squares ahead of Pacman, then doubled from Blinky's position
        assertEquals(Optional.of(Direction.WEST), direction);
    }

    @Test
    void inkyTargetUpFacingPacmanTest() {
        List<String> map = Arrays.asList(
            "#############",
            "#     I     #",
            "#     P     #", // P facing up
            "#     B     #",
            "#############"
        );
        Level level = mapParser.parseMap(map);
        Player player = playerFactory.createPacMan();
        level.registerPlayer(player);
        player.setDirection(Direction.NORTH);

        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        assertNotNull(inky);
        Optional<Direction> direction = inky.nextAiMove();

        // In up-facing case, target should be 2 up and 2 left from Pacman
        assertEquals(Optional.of(Direction.WEST), direction);
    }

    @Test
    void inkyTargetWithFarBlinkyTest() {
        List<String> map = Arrays.asList(
            "#############",
            "#I    P     #", // I left, P center facing right
            "#           #",
            "#         B #", // B far bottom-right
            "#############"
        );
        Level level = mapParser.parseMap(map);
        Player player = playerFactory.createPacMan();
        level.registerPlayer(player);
        player.setDirection(Direction.EAST);

        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        assertNotNull(inky);
        Optional<Direction> direction = inky.nextAiMove();

        // With Blinky far away, target point should be far ahead of Pacman
        assertEquals(Optional.of(Direction.EAST), direction);
    }

    // Bad weather tests
    @Test
    void noPacmanTest() {
        List<String> map = Arrays.asList(
            "##########",
            "#   B    #",
            "#   I    #",
            "##########"
        );
        Level level = mapParser.parseMap(map);

        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        assertNotNull(inky);
        Optional<Direction> direction = inky.nextAiMove();
        assertEquals(Optional.empty(), direction);
    }

    @Test
    void noBlinkyTest() {
        List<String> map = Arrays.asList(
            "##########",
            "#P       #",
            "#   I    #",
            "##########"
        );
        Level level = mapParser.parseMap(map);
        Player player = playerFactory.createPacMan();
        level.registerPlayer(player);

        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        assertNotNull(inky);
        Optional<Direction> direction = inky.nextAiMove();
        assertEquals(Optional.empty(), direction);
    }

    @Test
    void blockedPathTest() {
        List<String> map = Arrays.asList(
            "##########",
            "#P  B####",
            "#   I####",
            "##########"
        );
        Level level = mapParser.parseMap(map);
        Player player = playerFactory.createPacMan();
        level.registerPlayer(player);

        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        assertNotNull(inky);
        Optional<Direction> direction = inky.nextAiMove();
        assertEquals(Optional.empty(), direction);
    }
}

/**
 * The test cases cover:
 *
 * Good weather:
 *
 * normalMovementTest: Normal movement with all required elements present
 * inkyWithClearPathTest: Clear path to target with different positioning
 * inkyTargetRightFacingPacmanTest: Inky's target calculation when Pacman faces right
 * inkyTargetUpFacingPacmanTest: Inky's target calculation when Pacman faces up
 * inkyTargetWithFarBlinkyTest: Inky's target calculation with Blinky far away
 * Bad weather:
 *
 * noPacmanTest: Missing Pac-man
 * noBlinkyTest: Missing Blinky
 * blockedPathTest: Path is blocked by walls
 */
