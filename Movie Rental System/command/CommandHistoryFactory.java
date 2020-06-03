package package_hw8.command;


public class CommandHistoryFactory {
	  private CommandHistoryFactory() {}
	  static public CommandHistory newCommandHistory() {
	    CommandHistoryObj a = new CommandHistoryObj();
	    return ((CommandHistory) a);
	  }
	}
