import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MyTest {
	
	static Node n1;
	static Node n2;
	static Node n3;
	static Node n4;
	static int[] board1 = {14,8,3,7,6,9,0,2,12,10,15,5,4,11,1,13};
	static int[] board2 = {1,2,0,3,4,5,6,7,8,9,10,11,12,13,14,15};
	static int[] board3 = {0,1,7,6,5,3,2,15,4,9,13,11,8,12,14,10};
	static int[] board4 = {14,8,3,7,6,9,2,0,12,10,15,5,4,11,1,13};
	DB_Solver2 solver = new DB_Solver2(n1, "heuristicOne");
	A_IDS_A_15solver ids = new A_IDS_A_15solver();
	
	@BeforeAll
	static void setup() {
		n1 = new Node(board1);
		n2 = new Node(board2);
		n3 = new Node(board3);
		n4 = new Node(board4);
	}

	@Test
	void nodeConstructorKeyTest() {
		assertEquals(6, n1.getKey()[4], "node key constructed improperly");
	}
	
	@Test
	void nodeConstructorKey2Test() {
		assertEquals("[14, 8, 3, 7, 6, 9, 0, 2, 12, 10, 15, 5, 4, 11, 1, 13]", n1.getKey2(), "node key2 constructed improperly");
	}
	
	@Test
	void nodeConstructorParentTest() {
		assertEquals(null, n3.getParent(), "node parent constructed improperly");
	}
	
	@Test
	void DB_Solver2_goalStateTest() {
		assertFalse(solver.goalTest(board1), "DB_Solver2 identified the goal state improperly");
	}
	
	@Test
	void DB_Solver2_copyArrayTest() {
		int temp = solver.copyArray(board1)[6];
		assertEquals(0, temp, "DB_Solver2 copied array improperly");
	}
	
	@Test
	void nodeParentTest() {
		n1.setParent(n4);
		int temp = n1.getParent().getKey()[2];
		assertEquals(3, temp, "node assigned parent node improperly");
	}
	
	@Test
	void nodeDepthTest() {
		n3.setDepth(5);
		assertEquals(5, n3.getDepth(), "node assigned depth improperly");
	}
	
	@Test
	void node_hValueTest() {
		n3.set_hValue(11);
		assertEquals(11, n3.get_hValue(), "node assigned hValue improperly");
	}
	
	@Test
	void idsLengthTestHeuristicTwo() {
		int temp = ids.A_Star(n2, "heuristicTwo").size();
		assertEquals(3, temp, "A_IDS solver solved for incorrent number of turns for heuristic two");
	}
	
	@Test
	void idsLengthTestHeuristicOne() {
		int temp = ids.A_Star(n2, "heuristicOne").size();
		assertEquals(3, temp, "A_IDS solver solved for incorrent number of turns for heuristic one");
	}
}
