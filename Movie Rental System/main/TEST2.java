package package_hw8.main;


import org.junit.Test;

import junit.framework.TestCase;
import package_hw8.command.RerunnableCommand;
import package_hw8.command.UndoableCommand;
import package_hw8.data.Data;
import package_hw8.data.Video;
import package_hw8.data.Inventory;


public class TEST2 extends TestCase {
  public TEST2(String name) {
    super(name);
  }
  
  @Test
  public void test1() {
    final Inventory inventory = Data.newInventory();
    final Video v1 = Data.newVideo("K", 2003, "S");
    final Video v2 = Data.newVideo("S", 2002, "K");
    final RerunnableCommand UNDO = (RerunnableCommand) Data.newUndoCmd(inventory);
    final RerunnableCommand REDO = (RerunnableCommand) Data.newRedoCmd(inventory);
	
    UndoableCommand c = (UndoableCommand) Data.newAddCmd(inventory, v1, 2);
    assertTrue  ( c.run() );
    assertEquals( 1, inventory.size() );
    assertTrue  (!c.run() );     // cannot run an undoable command twice
    assertTrue  (!Data.newAddCmd(inventory, null, 3).run()); // can't add null!
    assertTrue  (!Data.newAddCmd(inventory, v2, 0).run());   // can't add zero copies!
    assertEquals( 1, inventory.size() );
    assertTrue  ( UNDO.run() );
    assertEquals( 0, inventory.size() );
    assertTrue  (!UNDO.run() );  // nothing to undo!
    assertEquals( 0, inventory.size() );
    assertTrue  ( REDO.run() );
    assertEquals( 1, inventory.size() );
    assertTrue  (!REDO.run() );  // nothing to redo!
    assertEquals( 1, inventory.size() );
    assertTrue  ( Data.newAddCmd(inventory, v1, -2).run());   // delete!
    assertEquals( 0, inventory.size() );
    assertTrue  (!Data.newOutCmd(inventory, v1).run());       // can't check out
    assertEquals( 0, inventory.size() );
    assertTrue  ( UNDO.run() );  // should undo the AddCmd, not the OutCmd
    assertEquals( 1, inventory.size() ); 
    assertTrue  (!Data.newAddCmd(inventory, v1, -3).run());   // can't delete 3
    assertTrue  ( Data.newAddCmd(inventory, v1, -2).run());   // delete 2
    assertEquals( 0, inventory.size() );
    assertTrue  ( UNDO.run() ); 
    assertEquals( 1, inventory.size() ); 

    assertEquals( "K (2003) : S [2,0,0]", inventory.get(v1).toString() );	
    assertTrue  ( Data.newAddCmd(inventory, v1, 2).run());
    assertEquals( "K (2003) : S [4,0,0]", inventory.get(v1).toString() );	
    assertTrue  ( Data.newAddCmd(inventory, v1, 2).run());
    assertEquals( "K (2003) : S [6,0,0]", inventory.get(v1).toString() );	
    assertTrue  ( UNDO.run() );
    assertEquals( "K (2003) : S [4,0,0]", inventory.get(v1).toString() );	
    assertTrue  ( UNDO.run() );
    assertEquals( "K (2003) : S [2,0,0]", inventory.get(v1).toString() );	

    assertTrue  ( Data.newOutCmd(inventory, v1).run());
    assertEquals( "K (2003) : S [2,1,1]", inventory.get(v1).toString() );
    assertTrue  ( Data.newOutCmd(inventory, v1).run());
    assertEquals( "K (2003) : S [2,2,2]", inventory.get(v1).toString() );
    assertTrue  (!Data.newOutCmd(inventory, v1).run());
    assertEquals( "K (2003) : S [2,2,2]", inventory.get(v1).toString() );
    assertTrue  ( UNDO.run() );
    assertEquals( "K (2003) : S [2,1,1]", inventory.get(v1).toString() );
    assertTrue  ( UNDO.run() );
    assertEquals( "K (2003) : S [2,0,0]", inventory.get(v1).toString() );
    assertTrue  ( REDO.run() );
    assertEquals( "K (2003) : S [2,1,1]", inventory.get(v1).toString() );
    assertTrue  ( REDO.run() );
    assertEquals( "K (2003) : S [2,2,2]", inventory.get(v1).toString() );
    
    assertTrue  ( Data.newInCmd(inventory, v1).run() );
    assertEquals( "K (2003) : S [2,1,2]", inventory.get(v1).toString() );	
    assertTrue  ( Data.newInCmd(inventory, v1).run() );
    assertEquals( "K (2003) : S [2,0,2]", inventory.get(v1).toString() );
    assertTrue  (!Data.newInCmd(inventory, v1).run() );
     assertEquals( "K (2003) : S [2,0,2]", inventory.get(v1).toString() );
     assertTrue  ( UNDO.run() );
     assertEquals( "K (2003) : S [2,1,2]", inventory.get(v1).toString() );
     assertTrue  ( UNDO.run() );
     assertEquals( "K (2003) : S [2,2,2]", inventory.get(v1).toString() );
     assertTrue  ( REDO.run() );
     assertEquals( "K (2003) : S [2,1,2]", inventory.get(v1).toString() );
     assertTrue  ( REDO.run() );
     assertEquals( "K (2003) : S [2,0,2]", inventory.get(v1).toString() );

     assertTrue  ( Data.newAddCmd(inventory, v2, 4).run());
     assertEquals( 2, inventory.size() );
     assertTrue  ( Data.newClearCmd(inventory).run());
     assertEquals( 0, inventory.size() );
     assertTrue  ( UNDO.run() );
     assertEquals( 2, inventory.size() );
     assertTrue  ( REDO.run() );
     assertEquals( 0, inventory.size() );
     
     
     Video video1 = Data.newVideo("A", 2000, "B");
     Video video2 = Data.newVideo("A", 2000, "B");
     assertTrue(video1.equals(video2));
     assertTrue(video1 == video2);
     //assertEquals(video1.equals(video2), (video1 == video2));
  }

  @Test
  public void test2() {
    final Inventory inventory = Data.newInventory();
    final Video v1 = Data.newVideo("K", 2003, "S");
    final RerunnableCommand UNDO = (RerunnableCommand) Data.newUndoCmd(inventory);
    final RerunnableCommand REDO = (RerunnableCommand) Data.newRedoCmd(inventory);
     assertTrue  ( Data.newAddCmd(inventory, v1,2).run());
     assertEquals( "K (2003) : S [2,0,0]", inventory.get(v1).toString() );
     assertTrue  ( Data.newOutCmd(inventory, v1).run());
     assertEquals( "K (2003) : S [2,1,1]", inventory.get(v1).toString() );
     assertTrue  ( UNDO.run() );
     assertEquals( "K (2003) : S [2,0,0]", inventory.get(v1).toString() );
     assertTrue  ( REDO.run() );
     assertEquals( "K (2003) : S [2,1,1]", inventory.get(v1).toString() );
     assertTrue  ( Data.newOutCmd(inventory, v1).run());
     assertEquals( "K (2003) : S [2,2,2]", inventory.get(v1).toString() );
     assertTrue  ( UNDO.run() );
     assertEquals( "K (2003) : S [2,1,1]", inventory.get(v1).toString() );
     assertTrue  ( UNDO.run() );
     assertEquals( "K (2003) : S [2,0,0]", inventory.get(v1).toString() );
  }
  
  @Test
  public void testVideo() {
	  Video v1 = Data.newVideo("K", 2000, "S");
	  Video v2 = Data.newVideo("K", 2000, "S");
	  
	  assertTrue(v1==v2);
  }
}
