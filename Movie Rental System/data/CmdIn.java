package package_hw8.data;

import package_hw8.command.UndoableCommand;

/**
 * Implementation of command to check in a video.
 * @see Data
 */
final class CmdIn implements UndoableCommand {
  private InventorySet _inventory;
  private Record _oldvalue;
  private Video _video;
  CmdIn(InventorySet inventory, Video video) {
    this._inventory = inventory;
    this._video = video;
  }
  public boolean run() {
	  try 
	  {
		  _oldvalue = _inventory.checkIn(_video);
		  _inventory.getHistory().add(this);
		  return true;
	  }
	  catch (ClassCastException e) {return false;}
	  catch (IllegalArgumentException e) {return false;}
  }
  
  public void undo() {
	    _inventory.replaceEntry(_video, _oldvalue);
	  }
  public void redo() {
	    _oldvalue = _inventory.checkIn(_video);
	  }
}
//use the checkin method of the inventory set 