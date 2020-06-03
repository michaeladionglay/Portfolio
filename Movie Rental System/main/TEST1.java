package package_hw8.main;

import junit.framework.TestCase;
import package_hw8.command.Command;
import package_hw8.data.Data;
import package_hw8.data.Record;
import package_hw8.data.Video;
import package_hw8.data.Inventory;
import java.util.Iterator;
import org.junit.Test;

// TODO:
// write an integration test that tests the data classes.
// add in some videos, check out, check in, delete videos, etc.
// check that errors are reported when necessary.
// check that things are going as expected.
public class TEST1 extends TestCase {
  private Inventory _inventory = Data.newInventory();
  public TEST1(String name) {
    super(name);
  }
  
  private void expect(Video v, String s) {
    assertEquals(s,_inventory.get(v).toString());
  }
  
  private void expect(Record r, String s) {
    assertEquals(s,r.toString());
  }
  
  
  @Test
  public void test1() {
    Command clearCmd = Data.newClearCmd(_inventory);
    clearCmd.run();
    
    Video vid1 = Data.newVideo("Title1", 2000, "Director1");
    assertEquals(0, _inventory.size());
    assertTrue(Data.newAddCmd(_inventory, vid1, 5).run());
    assertEquals(1, _inventory.size());
    assertTrue(Data.newAddCmd(_inventory, vid1, 5).run());
    assertEquals(1, _inventory.size());
    expect(vid1,"Title1 (2000) : Director1 [10,0,0]");
    assertEquals("Title1 (2000) : Director1 [10,0,0]", _inventory.get(vid1).toString());
    
    Video vid2 = Data.newVideo("Insidious", 2015, "Monster");
	assertTrue(Data.newAddCmd(_inventory, vid2, 24).run());
	assertEquals(2, _inventory.size());
	expect(vid2,"Insidious (2015) : Monster [24,0,0]");
	assertEquals("Insidious (2015) : Monster [24,0,0]", _inventory.get(vid2).toString());
	
	assertFalse(Data.newAddCmd(_inventory, null, 5).run());
	assertEquals(2, _inventory.size());

	assertTrue(Data.newOutCmd(_inventory, vid2).run());
	expect(vid2,"Insidious (2015) : Monster [24,1,1]");

	assertTrue(Data.newInCmd(_inventory, vid2).run());
	expect(vid2,"Insidious (2015) : Monster [24,0,1]");

	assertTrue(Data.newOutCmd(_inventory, vid2).run());
	assertTrue(Data.newOutCmd(_inventory, vid2).run());
	expect(vid2,"Insidious (2015) : Monster [24,2,3]");
	expect(_inventory.get(vid2), "Insidious (2015) : Monster [24,2,3]");

	
	try
	{
		Data.newOutCmd(_inventory, Data.newVideo(null, 2015, "Monster")).run();
		fail();
	}
	catch(IllegalArgumentException e) {};
	
	Video vid3 = Data.newVideo("Title3", 2000, "Director3");
	assertTrue(Data.newAddCmd(_inventory, vid3, 1).run());
	assertTrue(Data.newOutCmd(_inventory, vid3).run());
	expect(vid3,"Title3 (2000) : Director3 [1,1,1]");
	assertFalse(Data.newOutCmd(_inventory, vid3).run());
	
	//return/checkin videos
	assertTrue(Data.newInCmd(_inventory, vid2).run());
	assertTrue(Data.newInCmd(_inventory, vid2).run());
	assertTrue(Data.newInCmd(_inventory, vid3).run());
	
	
	assertTrue(Data.newAddCmd(_inventory, vid1, -10).run());
	assertTrue(Data.newAddCmd(_inventory, vid2, -24).run());
	assertTrue(Data.newAddCmd(_inventory, vid3, -1).run());
	
	
	
	
	Video v1 = Data.newVideo("Title1", 2000, "Director1");
    assertEquals(0, _inventory.size());
    assertTrue(Data.newAddCmd(_inventory, v1, 5).run());
    assertEquals(1, _inventory.size());
    assertTrue(Data.newAddCmd(_inventory, v1, 5).run());
    assertEquals(1, _inventory.size());
    // System.out.println(_inventory.get(v1));
    expect(v1,"Title1 (2000) : Director1 [10,0,0]");
    
    Video v2 = Data.newVideo("Title2", 2001, "Director2");
    assertTrue(Data.newAddCmd(_inventory, v2, 1).run());
    assertEquals(2, _inventory.size());
    expect(v2,"Title2 (2001) : Director2 [1,0,0]");
    
    assertFalse(Data.newAddCmd(_inventory, null, 5).run());
    assertEquals(2, _inventory.size());
    
    assertTrue(Data.newOutCmd(_inventory, v2).run());
    expect(v2,"Title2 (2001) : Director2 [1,1,1]");
    
    assertTrue(Data.newInCmd(_inventory, v2).run());
    expect(v2,"Title2 (2001) : Director2 [1,0,1]");
    
    assertTrue(Data.newAddCmd(_inventory, v2, -1).run());
    assertEquals(1, _inventory.size());
    expect(v1,"Title1 (2000) : Director1 [10,0,0]");
    
    Command outCmd = Data.newOutCmd(_inventory, v1);
    assertTrue(outCmd.run());
    assertTrue(outCmd.run());
    assertTrue(outCmd.run());
    assertTrue(outCmd.run());
    expect(v1,"Title1 (2000) : Director1 [10,4,4]");
    
    assertTrue(Data.newInCmd(_inventory, v1).run());
    expect(v1,"Title1 (2000) : Director1 [10,3,4]");
    
    assertTrue(Data.newAddCmd(_inventory, v2, 5).run());
    assertEquals(2, _inventory.size());
    expect(v2,"Title2 (2001) : Director2 [5,0,0]");
    expect(v1,"Title1 (2000) : Director1 [10,3,4]");

    Iterator<Record> it = _inventory.iterator(new java.util.Comparator<Record>() {
        public int compare (Record r1, Record r2) {
          return r2.numRentals() - r1.numRentals();
        }
      });
    expect(it.next(),"Title1 (2000) : Director1 [10,3,4]");
    expect(it.next(),"Title2 (2001) : Director2 [5,0,0]");
    assertFalse(it.hasNext());
	
  }
}
