package package_hw8.data;

import org.junit.Test;
import junit.framework.TestCase;
import package_hw8.data.Record;
import package_hw8.data.InventorySet;
import package_hw8.data.VideoObj;
import java.util.Set;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

// TODO:  complete the tests
public class InventoryTEST extends TestCase {
  public InventoryTEST(String name) {
    super(name);
  }

  InventorySet set = new InventorySet();
  final VideoObj video1 = new VideoObj( "A", 2000, "B" );
  final VideoObj video1copy = new VideoObj( "A", 2000, "B" );
  final VideoObj video2 = new VideoObj( "C", 2000, "D" );
  final VideoObj video3 = new VideoObj( "E", 2000, "F" );
  final Video v1 = new VideoObj( "A", 2000, "B" );
  final Video v1copy = new VideoObj( "A", 2000, "B" );
  final Video v2 = new VideoObj( "B", 2000, "B" );
  
  @Test
  public void testSize() {
	  assertTrue(set.size() == 0);
	  set.addNumOwned(video1, 1);
	  assertTrue(set.size() == 1);
	  set.addNumOwned(video1, 2);//since one is present, its still considered 1
	  assertTrue(set.size() == 1);
	  set.addNumOwned(video2, 2);//new so 1 is added
	  assertTrue(set.size() == 2);
	  set.addNumOwned(video2, -1);
	  assertTrue(set.size() == 2);
	  set.addNumOwned(video1, -1);//removes what's left
	  assertTrue(set.size() == 2);
	  
	  try { 
		  set.addNumOwned(video1, -5);
	      fail();
	    } catch (IllegalArgumentException e) { } 
  }

  @Test
  public void testAddNumOwned() {
		  assertTrue(set.size() == 0);
		  try { 
			  set.addNumOwned(video1, 0);
		      fail();
		    } catch (IllegalArgumentException e) { }
		  try { 
			  VideoObj vidNull = null;
			  set.addNumOwned(vidNull, 0);
		      fail();
		    } catch (IllegalArgumentException e) { }
		  set.addNumOwned(video3, 5);
		  Record rec3 = set.get(video3);
		  assertTrue(rec3.numOwned() == 5);
		  set.addNumOwned(video3, -1);
		  rec3 = set.get(video3);//have to get record again
		  assertTrue(rec3.numOwned() == 4);
		  set.addNumOwned(video3, -4);
		  assertTrue(set.size() == 0);
		  
		  InventorySet s = new InventorySet();
          assertEquals( null, s.get(v1) );
          s.addNumOwned(v1, 1);    		assertEquals( v1, s.get(v1).video() );
             							assertEquals( 1, s.get(v1).numOwned());
          s.addNumOwned(v1, 2);     	assertEquals( 3, s.get(v1).numOwned());
          s.addNumOwned(v1, -1);    	assertEquals( 2, s.get(v1).numOwned());
          s.addNumOwned(v2, 1);     	assertEquals( 2, s.get(v1).numOwned());
          s.addNumOwned(v1copy, 1); 	assertEquals( 3, s.get(v1).numOwned());
          s.addNumOwned(v1, -3);    	assertEquals( null, s.get(v1) );
          try { s.addNumOwned(null, 1);  fail(); } catch ( IllegalArgumentException e ) {}
  }

  @Test
  public void testCheckOutCheckIn() {
	  try { 
		  set.checkOut(null);
	      fail();
	    } catch (IllegalArgumentException e) { }
	  try { 
		  set.checkIn(null);
	      fail();
	    } catch (IllegalArgumentException e) { }
	  
	  set.addNumOwned(video1, 2);
	  assertTrue(set.get(video1).numOut() == 0); 
	  set.checkOut(video1);
	  assertTrue(set.get(video1).numOut() == 1);
	  assertTrue(set.get(video1).numRentals() == 1);
	  try { 
		  set.addNumOwned(video1, -2);
	      fail();
	    } catch (IllegalArgumentException e) { }
	  
	  set.checkOut(video1);
	  assertTrue(set.get(video1).numOut() == 2);
	  
	  
	  
	  InventorySet s = new InventorySet();
	  try { s.checkOut(null);     fail(); } catch ( IllegalArgumentException e ) {}
	    try { s.checkIn(null);      fail(); } catch ( IllegalArgumentException e ) {}
	          s.addNumOwned(v1, 2); assertTrue( s.get(v1).numOut() == 0 && s.get(v1).numRentals() == 0 );
	          s.checkOut(v1);       assertTrue( s.get(v1).numOut() == 1 && s.get(v1).numRentals() == 1 );
	    try { s.addNumOwned(v1,-3); fail(); } catch ( IllegalArgumentException e ) {}
	    try { s.addNumOwned(v1,-2); fail(); } catch ( IllegalArgumentException e ) {}
	          s.addNumOwned(v1,-1); assertTrue( s.get(v1).numOut() == 1 && s.get(v1).numRentals() == 1 );
	          s.addNumOwned(v1, 1); assertTrue( s.get(v1).numOut() == 1 && s.get(v1).numRentals() == 1 );
	          s.checkOut(v1);       assertTrue( s.get(v1).numOut() == 2 && s.get(v1).numRentals() == 2 );
	    try { s.checkOut(v1);       fail(); } catch ( IllegalArgumentException e ) {}
	          s.checkIn(v1);        assertTrue( s.get(v1).numOut() == 1 && s.get(v1).numRentals() == 2 );
	          s.checkIn(v1copy);    assertTrue( s.get(v1).numOut() == 0 && s.get(v1).numRentals() == 2 );
	    try { s.checkIn(v1);        fail(); } catch ( IllegalArgumentException e ) {}
	    try { s.checkOut(v2);       fail(); } catch ( IllegalArgumentException e ) {}
	          s.checkOut(v1);       assertTrue( s.get(v1).numOut() == 1 && s.get(v1).numRentals() == 3 );
  }

  @Test
  public void testClear() {
	  set.addNumOwned(video1, 4);
	  set.clear();
	  assertTrue(set.size() == 0);
  }

  @Test
  public void testGet() {
	  set.addNumOwned(video2, 1);
	  Record vid1 = set.get(video2);
	  Record vid2 = set.get(video2);
	  assertTrue(vid1.getClass() == vid2.getClass());
	  try { 
		  VideoObj vidNull = null;
		  set.get(vidNull);
	      fail();
	    } catch (IllegalArgumentException e) { }
	  
	  InventorySet s = new InventorySet();
	  s.addNumOwned(v1, 1);
	  Record r1 = s.get(v1);
	  Record r2 = s.get(v1);
	  assertTrue( r1.equals(r2) );
	  assertTrue( r1 == r2 );
  }

  @Test
  public void testIterator1() {
	  	Set<Video> exp = new HashSet<Video>();
		InventorySet inventory = new InventorySet();
		VideoObj A = new VideoObj("Rose", 2019, "Dr. Seuss");
		VideoObj B = new VideoObj("Lemon", 2020, "Gordon Ramsay");
		inventory.addNumOwned(A, 3);
		inventory.addNumOwned(B, 4);
		exp.add(A);
		exp.add(B);
		
		assertTrue(inventory.size() == 2);

		Iterator<Record> iteratorInventory = inventory.iterator();
		try 
		{ 
			iteratorInventory.remove(); 
			fail(); 
		}
		catch (UnsupportedOperationException e) { }
		
		while(iteratorInventory.hasNext()) {
			Record currentRec = iteratorInventory.next();
			assertTrue(exp.contains(currentRec.video()));
			exp.remove(currentRec.video());
		}
		assertTrue(exp.isEmpty());
  }
  
  @Test
  public void testIterator2() {
	  	List<Video> exp = new ArrayList<Video>();
		InventorySet inventory = new InventorySet();
		VideoObj A = new VideoObj("Rose", 2019, "Dr. Seuss");
		VideoObj B = new VideoObj("Lemon", 2020, "Gordon Ramsay");
		exp.add(A);
		exp.add(B);
		inventory.addNumOwned(A, 3);
		inventory.addNumOwned(B, 4);

		Comparator<Record> comparatorRecord = (record1, record2) -> record1.video().year() - record2.video().year();
		Iterator<Record> iteratorRecord = inventory.iterator(comparatorRecord);
		try 
		{ 
			iteratorRecord.remove(); 
			fail(); 
		}
		catch (UnsupportedOperationException e) { }
		
		Iterator<Video> iteratorVideo = exp.iterator();
		while (iteratorRecord.hasNext()) {
			assertSame(iteratorVideo.next(), iteratorRecord.next().video());
			iteratorVideo.remove();
		}
		assertTrue(exp.isEmpty());
  }

}
