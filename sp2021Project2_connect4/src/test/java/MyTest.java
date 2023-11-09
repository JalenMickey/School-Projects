import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class MyTest {
	/* full transparency, we had a very difficult time authoring this test class, as
	 * almost every single one of our functions uses JavaFX, which cannot be tested in JUnit5
	 * 
	 * Instead, we have pretty much created the most basic test functions, 
	 * which test to ensure that a new game is initialized properly
	 */
	
	// initialize a game variable
	static JavaFXTemplate game1;
	
	// setup a new, unplayed game
	static void setup () {
		game1 = new JavaFXTemplate();
	}

	@Test // test to ensure the game begins with the correct player, player one
	void playerOneBeginTest() {
		assertTrue(JavaFXTemplate.turn == 'r', "started game with incorrect player");
	}
	
	@Test // test to ensure the game begins without the game over condition active
	void gameOverTest() {
		assertFalse(JavaFXTemplate.gameOver, "began with a game over, not good");
	}
	
	@Test // test to ensure the game begins with the default theme
	void originalThemeTest() {
		assertEquals("-fx-background-color: #FF0000", JavaFXTemplate.colorOne, "original theme not utilized at outset, player one color");
		assertEquals("-fx-background-color: #2b02f7", JavaFXTemplate.colorTwo, "original theme not utilized at outset, player two color");
		assertEquals("-fx-background-color: #000000", JavaFXTemplate.colorUnused, "original theme not utilized at outset, unused tile color");
	}
	
	@Test // test to ensure the game begins with its turn count at zero
	void turnCountTest() {
		assertEquals(0, JavaFXTemplate.numTurns, "game started on a turn count other than zero, not good");
	}

}
