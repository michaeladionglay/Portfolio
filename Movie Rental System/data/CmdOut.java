package package_hw8.data;

import package_hw8.command.UndoableCommand;

/**
 * Implementation of command to check out a video.
 * @see Data
 */
final class CmdOut implements UndoableCommand {
  private InventorySet _inventory;
  private Record _oldvalue;
  private Video _video;
  CmdOut(InventorySet inventory, Video video) {
    this._inventory = inventory;
    this._video = video;
  }
  public boolean run() {
	  try 
	  {
		  _oldvalue = _inventory.checkOut(_video);
		  UndoableCommand c = this;
		  _inventory.getHistory().add(c);
		  return true;
	  }
	  catch (ClassCastException e) {return false;}
	  catch (IllegalArgumentException e) {return false;}
  }
  
  public void undo() {
	  _inventory.replaceEntry(_video, _oldvalue);
	  }
  public void redo() {
	  _oldvalue = _inventory.checkOut(_video);
	  }
}
