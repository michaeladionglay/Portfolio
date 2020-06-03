package package_hw8.data;


import package_hw8.command.RerunnableCommand;
import package_hw8.command.UndoableCommand;
import java.util.HashSet;
import java.util.WeakHashMap;
import java.util.HashMap;
import java.lang.ref.WeakReference;

/**
 * A static class for accessing data objects.
 */
public class Data {

  //private static HashSet<Video> C = new HashSet<Video>();
  
  private Data() {
  }
  /**
   * Returns a new Inventory.
   */
  private static WeakHashMap<Integer, Video> hashmap = new WeakHashMap<Integer, Video>();
  
  private static int hash3 (Object key1, Object key2, Object key3) {
	  return key1.hashCode() + 5 * key2.hashCode() + 13 * key3.hashCode();
  }
  
  static public final Inventory newInventory() {
	  return new InventorySet(); //inventory is implemented by inventorySet
  }

  
  /**
   * Factory method for Video objects.
   * Title and director are "trimmed" to remove leading and final space.
   * @throws IllegalArgumentException if Video invariant violated.
   */
  static public Video newVideo(String title, int year, String director) {
	  if ( 		(title == null) 
				|| (director == null) 
				|| (year <= 1800) 
				|| (year >= 5000)) {
			throw new IllegalArgumentException();
		}
	    title = title.trim();
	    director = director.trim();
	    
	    
	    if (	("".equals(title))
	    		||	("".equals(director)))
	    	throw new IllegalArgumentException();
	    
	    Integer key = hash3(title, year, director);
	    
	    Video v = hashmap.get(key);
	    
	    WeakReference<Video> weakref = new WeakReference<Video>(v);
	    
	    
	    if ( (weakref.get() ==null) || (!(weakref.get().title().equals(title)) || (weakref.get().year() != year) || (!(weakref.get().title().equals(title))))) {
	    	v = new VideoObj(title, year, director);
	    	hashmap.put(key, v);
	    }
	    return v;
	    
  }

  /**
   * Returns a command to add or remove copies of a video from the inventory.
   * <p>The returned command has the following behavior:</p>
   * <ul>
   * <li>If a video record is not already present (and change is
   * positive), a record is created.</li>
   * <li>If a record is already present, <code>numOwned</code> is
   * modified using <code>change</code>.</li>
   * <li>If <code>change</code> brings the number of copies to be less
   * than one, the record is removed from the inventory.</li>
   * </ul>
   * @param video the video to be added.
   * @param change the number of copies to add (or remove if negative).
   * @throws IllegalArgumentException if <code>inventory<code> not created by a call to <code>newInventory</code>.
   */
  static public UndoableCommand newAddCmd(Inventory inventory, Video video, int change) {
    if (!(inventory instanceof InventorySet))
      throw new IllegalArgumentException();
    	return new CmdAdd((InventorySet) inventory, video, change);
  }

  /**
   * Returns a command to check out a video.
   * @param video the video to be checked out.
   */
  static public UndoableCommand newOutCmd(Inventory inventory, Video video) {
	  if (!(inventory instanceof InventorySet))
	      throw new IllegalArgumentException();
	    return new CmdOut((InventorySet) inventory, video);
  }
  
  /**
   * Returns a command to check in a video.
   * @param video the video to be checked in.
   */
  static public UndoableCommand newInCmd(Inventory inventory, Video video) {
	  if (!(inventory instanceof InventorySet))
	      throw new IllegalArgumentException();
	    return new CmdIn((InventorySet) inventory, video);
  }
  
  /**
   * Returns a command to remove all records from the inventory.
   */
  static public UndoableCommand newClearCmd(Inventory inventory) {
    if (!(inventory instanceof InventorySet))
    		throw new IllegalArgumentException();
    return new CmdClear((InventorySet) inventory);
  }
  
  /**
   * Returns a command to undo that will undo the last successful UndoableCommand. 
   */
  static public RerunnableCommand newUndoCmd(Inventory inventory) {
	  if (!(inventory instanceof InventorySet))
	      throw new IllegalArgumentException();
		  return ((InventorySet) inventory).getHistory().getUndo();

  }

  /**
   * Returns a command to redo that last successfully undone command. 
   */
  static public RerunnableCommand newRedoCmd(Inventory inventory) {
	  if (!(inventory instanceof InventorySet))
	      throw new IllegalArgumentException();
	  return ((InventorySet) inventory).getHistory().getRedo();
  }
}  
