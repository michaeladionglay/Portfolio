package package_hw8.data;

import org.junit.Test;


import junit.framework.TestCase;
import package_hw8.data.InventorySet;
import package_hw8.data.VideoObj;

// TODO:  complete the tests
public class VideoTEST extends TestCase {
  public VideoTEST(String name) {
    super(name);
  }
  
  @Test
  public void testEquals() { 
	  	 String title = "Love";
	     int year = 2020;
	     String director = "Lemon";
	     VideoObj vid_1 = new VideoObj(title, year, director);
	     VideoObj vid_2 = new VideoObj(title, year, director);
	     VideoObj vid_3 = new VideoObj(director, year, title);
	     VideoObj vid_1copy = new VideoObj(title, year, director);
	     InventorySet set = new InventorySet();
	     set.addNumOwned(vid_1, 3);
	     //Record vid1 = (Record) set.get(vid_1);
	     try 
		    {
		      vid_1.compareTo(set);
		      fail();
		    } 
		    catch ( ClassCastException e ) {}
	     assertTrue(vid_1.equals(vid_1));
	     assertTrue(vid_1.equals(vid_2));
	     assertTrue(vid_1.equals(new VideoObj(title, year, director)));
	     assertTrue(vid_1.equals(new VideoObj("Love", 2020, "Lemon")));
	     assertTrue(vid_1.equals(vid_1copy));
	     assertFalse(vid_1.equals(vid_3));
	     assertFalse(vid_1.equals(null));
	     assertFalse(vid_1.equals(new VideoObj("Harry", 2000, "Potter")));
	     assertFalse(vid_1.equals(new VideoObj("Cherry", year, director)));
	     assertFalse(vid_1.equals(new VideoObj(title, 2019, director)));
	     assertFalse(vid_1.equals(new VideoObj(title, year, "Lime")));
	     assertFalse(vid_1.equals(new VideoObj("Cherry", year, "john")));
	     assertFalse(vid_1.equals(new VideoObj("Cherry", 2019, director)));
	     assertFalse(vid_1.equals(new VideoObj(title, 2019, "Lime")));
	     
	     title = "A";
	     year = 2009;
	     director = "Zebra";
	     VideoObj a = new VideoObj(title,year,director);
	     assertTrue( a.equals(a) );
	     assertTrue( a.equals( new VideoObj(title, year, director) ) );
	     assertTrue( a.equals( new VideoObj(new String(title), year, director) ) );
	     assertTrue( a.equals( new VideoObj(title, year, new String(director)) ) );
	     assertFalse( a.equals( new VideoObj(title+"1", year, director) ) );
	     assertFalse( a.equals( new VideoObj(title, year+1, director) ) );
	     assertFalse( a.equals( new VideoObj(title, year, director+"1") ) );
	     assertFalse( a.equals( new Object() ) );
	     assertFalse( a.equals( null ) );
  }

  @Test
  public void testCompareTo() { 
	  String title = "Love";
	  int year = 2020;
	  String director = "Lemon";
	  VideoObj vid_1 = new VideoObj(title, year, director);//change title
	  VideoObj vid_2 = new VideoObj("War", year, director);
	  assertTrue(vid_1.compareTo(vid_1) == 0);
	  assertTrue(vid_1.compareTo(vid_2) < 0);
	  assertTrue(vid_1.compareTo(vid_2) == -vid_2.compareTo(vid_1));//vice versa check
	  vid_2 = new VideoObj(title, 2019, director);//change year
	  assertTrue(vid_1.compareTo(vid_2) > 0);
	  assertTrue(vid_1.compareTo(vid_2) == -vid_2.compareTo(vid_1));
	  vid_2 = new VideoObj(title, year, "Potter");//change director
	  assertTrue(vid_1.compareTo(vid_2) < 0);
	  assertTrue(vid_1.compareTo(vid_2) == -vid_2.compareTo(vid_1));
	  
	  try 
	  {
		  vid_1.compareTo(null);
		  fail();
	  } 
	  catch ( NullPointerException e ) {}
		catch ( ClassCastException e ) {}
	  
	  
	  	title = "A"; 
	  	String title2 = "B";
	    year = 2009;
	    int year2 = 2010;
	    director = "Zebra"; 
	    String director2 = "Zzz";
	    VideoObj a = new VideoObj(title,year,director);
	    VideoObj b = new VideoObj(title2,year,director);
	    assertTrue( a.compareTo(b) < 0 );
	    assertTrue( a.compareTo(b) == -b.compareTo(a) );
	    assertTrue( a.compareTo(a) == 0 );

	    b = new VideoObj(title,year2,director);
	    assertTrue( a.compareTo(b) < 0 );
	    assertTrue( a.compareTo(b) == -b.compareTo(a) );

	    b = new VideoObj(title,year,director2);
	    assertTrue( a.compareTo(b) < 0 );
	    assertTrue( a.compareTo(b) == -b.compareTo(a) );

	    b = new VideoObj(title2,year2,director2);
	    assertTrue( a.compareTo(b) < 0 );
	    assertTrue( a.compareTo(b) == -b.compareTo(a) );

	    try 
	    {
	      a.compareTo(null);
	      fail();
	    } 
	    catch ( NullPointerException e ) {}
  }

  @Test
  public void testToString() { 
	  String title = "Love";
	  int year = 2020;
	  String director = "Lemon";
	  VideoObj vid_1 = new VideoObj(title, year, director);
	  assertEquals(vid_1.toString(), "Love (2020) : Lemon");
	  
	  VideoObj vid_2 = new VideoObj("War", 2019, "Hemming");
	  assertEquals(vid_2.toString(), "War (2019) : Hemming");
	  
	  
  }

  @Test
  public void testHashCode() {
	
    assertEquals
    (-875826552,
     new VideoObj("None", 2009, "Zebra").hashCode());
    assertEquals
    (-1391078111,
     new VideoObj("Blah", 1954, "Cante").hashCode());
  }

}
