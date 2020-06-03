package package_hw8.data;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import package_hw8.command.CommandHistory;
import package_hw8.command.CommandHistoryFactory;

/**
 * Implementation of Inventory interface.
 * @see Data
 */
final class InventorySet implements Inventory {
  // Chose to use Map of Record, rather than RecordObj, because of
  // Java's broken generic types.  The story is too sad to retell, but
  // involves the fact that Iterable<? extends Record> is not a valid
  // type, and that Iterator<RecordObj> is not a subtype of
  // Iterator<Record>.
  //
  // Seems like the best approach for Java generics is to use the
  // external representation internally and downcast when necessary.
  private Map<Video,Record> _data;
  private final CommandHistory _history;

  InventorySet() {
    _data = new WeakHashMap<Video,Record>();
    _history = CommandHistoryFactory.newCommandHistory();
  }
  
  /**
   * If <code>record</code> is null, then delete record for <code>video</code>;
   * otherwise replace record for <code>video</code>.
   */
  void replaceEntry(Video video, Record record) {
    _data.remove(video);
    if (record != null)
      _data.put(video,((RecordObj)record).copy());
  }
  
  /**
   * Overwrite the map.
   */
  void replaceMap(Map<Video,Record> data) {
    _data = data;
  }
  
  public int size() {
	  return _data.size();
  }

  public Record get(Video v) {
	  if ((v == null)) {
		  throw new IllegalArgumentException();
	  }
	  
	  return _data.get(v);
  }

  public Iterator<Record> iterator() {
    return Collections.unmodifiableCollection(_data.values()).iterator();
  }

  public Iterator<Record> iterator(Comparator<Record> comparator) {
	  List<Record> listCopy = new ArrayList<Record>(_data.values());
	  Collections.sort(listCopy, comparator);
	  return Collections.unmodifiableList(listCopy).iterator();
  }

  /**
   * Add or remove copies of a video from the inventory.
   * If a video record is not already present (and change is
   * positive), a record is created. 
   * If a record is already present, <code>numOwned</code> is
   * modified using <code>change</code>.
   * If <code>change</code> brings the number of copies to be less
   * than one, the record is removed from the inventory.
   * @param video the video to be added.
   * @param change the number of copies to add (or remove if negative).
   * @return A copy of the previous record for this video (if any)
   * @throws IllegalArgumentException if video null or change is zero
   */
  Record addNumOwned(Video video, int change) {
	  if (	(video == null))
  	{
  	throw new IllegalArgumentException();
  	}
  
	  RecordObj rec = (RecordObj)_data.get(video);
	  if (	(rec == null)
			  &&	(change < 1)) {
      		throw new IllegalArgumentException();
  			}
	  else if (rec == null) {
		  	_data.put(video, new RecordObj(video, change, 0, 0));
	  		}
	  else if (rec.numOwned+change < rec.numOut) {
		  	throw new IllegalArgumentException();
	  		}
	  else if (rec.numOwned+change < 1) {
		  	_data.remove(video);
	  		}
	  else {
		  	//rec.numOwned += change;
		  _data.put(video, new RecordObj(video, rec.numOwned + change, rec.numOut, rec.numRentals));
	  		}
	  return rec;
  		}

  /**
   * Check out a video.
   * @param video the video to be checked out.
   * @return A copy of the previous record for this video
   * @throws IllegalArgumentException if video has no record or numOut
   * equals numOwned.
   */
  Record checkOut(Video video) {
	  Record rec = _data.get(video);
	  if ((rec == null) || (rec.numOut() == rec.numOwned() || (video == null))) {
		  throw new IllegalArgumentException();
	  }
	  else {
		  _data.put(video, new RecordObj(video, rec.numOwned(), rec.numOut()+1, rec.numRentals()+1));
		  return rec;
	  }
  }
  
  /**
   * Check in a video.
   * @param video the video to be checked in.
   * @return A copy of the previous record for this video
   * @throws IllegalArgumentException if video has no record or numOut
   * non-positive.
   */
  Record checkIn(Video video) {
	  Record rec = _data.get(video);
	  if ((rec == null) || (rec.numOut() == 0) /*|| (video == null)*/) {
		  throw new IllegalArgumentException();
	  }
	  else {
		  _data.put(video, new RecordObj(video, rec.numOwned(), rec.numOut()-1, rec.numRentals()));
		  return rec;
	  }
  }
  
  /**
	 * Remove all records from the inventory.
	 * @return A copy of the previous inventory as a Map
	 */
  Map<Video,Record> clear() {
    
	  Map<Video,Record> clr = _data;
	  _data = new HashMap<Video,Record>();
	  return clr;
	  }
  
  /**
   * Return a reference to the history.
   */
  CommandHistory getHistory() {
    return _history;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("Database:\n");
    for (Record r : _data.values()) {
      buffer.append("  ");
      buffer.append(r);
      buffer.append("\n");
    }
    return buffer.toString();
  }


  /**
   * Implementation of Record interface.
   *
   * <p>This is a utility class for Inventory.  Fields are mutable and
   * package-private.</p>
   *
   * <p><b>Class Invariant:</b> No two instances may reference the same Video.</p>
   *
   * @see Record
   */
  private static final class RecordObj implements Record {
    Video video; // the video
    int numOwned;   // copies owned
    int numOut;     // copies currently rented
    int numRentals; // total times video has been rented
    
    RecordObj(Video video, int numOwned, int numOut, int numRentals) {
      this.video = video;
      this.numOwned = numOwned;
      this.numOut = numOut;
      this.numRentals = numRentals;
    }
    public Video video() {
      return video;
    }
    public int numOwned() {
      return numOwned;
    }
    public int numOut() {
      return numOut;
    }
    public int numRentals() {
      return numRentals;
    }
    public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append(video);
      buffer.append(" [");
      buffer.append(numOwned);
      buffer.append(",");
      buffer.append(numOut);
      buffer.append(",");
      buffer.append(numRentals);
      buffer.append("]");
      return buffer.toString();
    }
    
    public Record copy() {
    	Record rec = new RecordObj(this.video, this.numOwned, this.numOut, this.numRentals);
        return rec;
      }
  }
}

