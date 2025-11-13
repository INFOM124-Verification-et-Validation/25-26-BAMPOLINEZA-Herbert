package nl.tudelft.jpacman.board;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.*;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.npc.ghost.GhostMapParser;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Unit.squaresAheadOf(...) following a specification-based approach.
 * - Good weather: 0 ahead returns same square.
 * - Good weather: 1 ahead into adjacent wall returns that (non-null, different) square.
 * - Bad weather: looking far ahead past board border returns null.
 */
class UnitTest {
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

    @Test
    void zeroAheadReturnsSameSquare() {
        List<String> map = Arrays.asList(
            "#####",
            "#P  #",
            "#####"
        );

        Level level = mapParser.parseMap(map);
        Player player = playerFactory.createPacMan();
        level.registerPlayer(player);

        assertTrue(player.hasSquare());
        // 0 ahead should return the same square the unit occupies
        Square result = player.squaresAheadOf(0);
        assertSame(player.getSquare(), result);
    }

    @Test
    void oneAheadIntoWallReturnsAdjacentSquare() {
        List<String> map = Arrays.asList(
            "######",
            "#P####", // player at col 1, immediate east (col 2) is a wall square
            "######"
        );

        Level level = mapParser.parseMap(map);
        Player player = playerFactory.createPacMan();
        level.registerPlayer(player);

        player.setDirection(Direction.EAST);
        assertTrue(player.hasSquare());

        Square ahead = player.squaresAheadOf(1);
        // adjacent square exists (a wall square), so non-null and not the same as current square
        assertNotNull(ahead);
        assertNotSame(player.getSquare(), ahead);
    }

    @Test
    void farAheadBeyondBoardReturnsNull() {
        List<String> map = Arrays.asList(
            "######",
            "#P   #", // some open squares to the east, then border; requesting a large lookahead should hit null
            "######"
        );

        Level level = mapParser.parseMap(map);
        Player player = playerFactory.createPacMan();
        level.registerPlayer(player);

        player.setDirection(Direction.EAST);
        assertTrue(player.hasSquare());

        // large lookahead should eventually step off the board and yield null
        Square far = player.squaresAheadOf(10);
        assertNull(far);
    }
}
