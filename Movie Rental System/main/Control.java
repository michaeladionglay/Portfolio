package package_hw8.main;


import package_hw8.ui.CommonBuilderInterface;
import package_hw8.ui.CommonInterface;
import package_hw8.ui.Factory;
import package_hw8.ui.UI;
import package_hw8.ui.UIError;
import package_hw8.ui.UIMenuAction;
import package_hw8.ui.UIFormTest;
import package_hw8.data.Data;
import package_hw8.data.Inventory;
import package_hw8.data.Video;
import package_hw8.data.Record;
import package_hw8.command.Command;
import java.util.Iterator;
import java.util.Comparator;

class Control {

	

	final State EXITED;
	final State EXIT;
	final State START;
    State _state;

  private CommonInterface _getVideoForm;
  private UIFormTest _numberTest;
  private UIFormTest _stringTest;
    
  private Inventory _inventory;
  private UI _ui;
  
  Factory fac;
  
  
 enum ExitMenu {
	 
	 
	  DEF("Default") {
		  //public String prompt = "Default";
		  @Override
		  public UIMenuAction action (Control c) {
			  return new UIMenuAction() { public void run() {} };
		  	  }
	  },
	  
	  YES("Yes") {
		  //public String prompt = "Yes";
		  @Override
		  public UIMenuAction action(Control c){
			  return new UIMenuAction() {
			        public void run() {
			            System.gc();
			            c._state = c.EXITED;
			          	}
			  		};
		  }
	  },
	  
	  NO("No") {
		  //public String prompt = "No";
		  @Override
		  public UIMenuAction action(Control c) {
			  return new UIMenuAction() {
			        public void run() {
				           c._state = c.START;
				          }
				        };
		  }
	  };
	 
	 String prompt;
	 
	 private ExitMenu(String p) {
		 this.prompt = p;
	 }
	  
	  public abstract UIMenuAction action(Control c);
  }
  
 enum StartMenu {
	    
	 DEF("Default") {
		  @Override
		  public UIMenuAction action(Control c) {
			  return new UIMenuAction() {
		        public void run() {
		          c._ui.displayError("doh!");
		        }
		      };
		  }
	  },
	  
	  ADD_REMOVE_VID("Add/Remove copies of a video") {
		  @Override
		  public UIMenuAction action(Control c){
			  return new UIMenuAction() {
		        public void run() {
		          String[] result1 = c._ui.processForm(c._getVideoForm);
		          Video v = Data.newVideo(result1[0], Integer.parseInt(result1[1]), result1[2]);
		          
		          Factory fact = new Factory();
		          //UIFormBuilderInterface f = fact.FactoryUIformBuilder();
		          CommonBuilderInterface f = fact.FactoryCommonBuilder();
		          f.add("Number of copies to add/remove", c._numberTest);
		          
		          String[] result2 = c._ui.processForm(f.toUIMenuForm(""));                     
		          Command com = Data.newAddCmd(c._inventory, v, Integer.parseInt(result2[0]));
		          if (! com.run()) {
		            c._ui.displayError("Command failed");
		          }
		        }
		      };
		  }
	  },
	  
	  CHECK_IN_VID("Check in a video") {
		  @Override
		  public UIMenuAction action(Control c) {
			  return new UIMenuAction() {
		        public void run() {
		        	String[] result1 = c._ui.processForm(c._getVideoForm);
		        	Video vid = Data.newVideo(result1[0], Integer.parseInt(result1[1]), result1[2]);
		        	
		        	Command com = Data.newInCmd(c._inventory, vid);
		        	if(com.run() == false) {
		        		c._ui.displayError("Command failed");
		        	}
		        }
		      };
		  }
	  },
	  
	  CHECK_OUT_VID("Check out a video") {
		  @Override
		  public UIMenuAction action(Control c) {
			  return new UIMenuAction() {
		        public void run() {
		        	String[] result1 = c._ui.processForm(c._getVideoForm);
		        	Video vid = Data.newVideo(result1[0], Integer.parseInt(result1[1]), result1[2]);
		        	
		        	Command com = Data.newOutCmd(c._inventory, vid);
		        	if(com.run() == false) {
		        		c._ui.displayError("Command failed");
		        	}
		        }
		      };
		  }
	  },
	  
	  PRINT_INVENTORY("Print the inventory") {
		  @Override
		  public UIMenuAction action(Control c) {
			  return new UIMenuAction() {
		        public void run() {
		          c._ui.displayMessage(c._inventory.toString());
		        }
		      };
		  }
	  },
	 
		CLEAR_INVENTORY("Clear the inventory") {
	  	      @Override
	  	      public UIMenuAction action(Control c) {
		      return new UIMenuAction() {
		        public void run() {
		          if (!Data.newClearCmd(c._inventory).run()) {
		            c._ui.displayError("Command failed");
		          }
		        }
		      };
	  	    }
	  },
		      
		 UNDO("Undo") {
		  	  @Override
		  	  public UIMenuAction action(Control c){
		      return new UIMenuAction() {
		        public void run() {
		          if (!Data.newUndoCmd(c._inventory).run()) {
		            c._ui.displayError("Command failed");
		          }
		        }
		      };
		  }
	  },
		 
	  	  REDO("Redo") {
  	      @Override
  	      public UIMenuAction action(Control c) {
		      return new UIMenuAction() {
		        public void run() {
		          if (!Data.newRedoCmd(c._inventory).run()) {
		            c._ui.displayError("Command failed");
		          }
		        }
		      };
  	      }  
	  },
	  	  
	  	PRINT_TOP_TEN("Print top ten all time rentals in order") {
  	      @Override
  	      public UIMenuAction action(Control c) {
		      return new UIMenuAction() {
		        public void run() {
		          Comparator<Record> comp = (r1, r2) -> r2.numRentals() - r1.numRentals();
		          Iterator<Record> lst = c._inventory.iterator(comp);
		          int counter = 10;
		          StringBuilder output = new StringBuilder();
		          while((lst.hasNext()) && (counter > 0)) {
		        	  Record rec = lst.next();
		        	  output.append(rec.toString());
		        	  output.append("\n");
		        	  counter--;
		          }
		          c._ui.displayMessage(output.toString());
		        }
		      };
  	      }
	  },
		          
	  	EXIT("Exit") {
  	      @Override
  	      public UIMenuAction action(Control c) {
		      return new UIMenuAction() {
		        public void run() {
		          c._state = c.EXIT;
		        }
		      };
  	      }
	  },
	  	
		    
	  	INITIALIZE_BOGUS_VID("Initialize with bogus contents") {
  	      @Override
  	      public UIMenuAction action(Control c) {
		      return new UIMenuAction() {
		        public void run() {
		          Data.newAddCmd(c._inventory, Data.newVideo("a", 2000, "m"), 1).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("b", 2000, "m"), 2).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("c", 2000, "m"), 3).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("d", 2000, "m"), 4).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("e", 2000, "m"), 5).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("f", 2000, "m"), 6).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("g", 2000, "m"), 7).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("h", 2000, "m"), 8).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("i", 2000, "m"), 9).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("j", 2000, "m"), 10).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("k", 2000, "m"), 11).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("l", 2000, "m"), 12).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("m", 2000, "m"), 13).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("n", 2000, "m"), 14).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("o", 2000, "m"), 15).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("p", 2000, "m"), 16).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("q", 2000, "m"), 17).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("r", 2000, "m"), 18).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("s", 2000, "m"), 19).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("t", 2000, "m"), 20).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("u", 2000, "m"), 21).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("v", 2000, "m"), 22).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("w", 2000, "m"), 23).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("x", 2000, "m"), 24).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("y", 2000, "m"), 25).run();
		          Data.newAddCmd(c._inventory, Data.newVideo("z", 2000, "m"), 26).run();
		        }
		      };
  	      }
	  };
	  
	  String prompt;
		 
		private StartMenu(String p) {
			 this.prompt = p;
		 }
	  
	  public abstract UIMenuAction action(Control c);
	  
 }
 
 enum FormTest {
	 TEST_YEAR {
 	      @Override
 	      public UIFormTest action() {
 	    	 return new UIFormTest() {
 	            public boolean run(String input) {
 	              return ! "".equals(input.trim());
 	            }
 	          };
 	      }  
	  },
	 
	 TEST_NUM {
 	      @Override
 	      public UIFormTest action() {
		      return new UIFormTest() {
		          public boolean run(String input) {
		              try {
		                Integer.parseInt(input);
		                return true;
		              } catch (NumberFormatException e) {
		                return false;
		              }
		            }
		          };
 	      }  
	  },
	 
	 TEST_STRING {
 	      @Override
 	      public UIFormTest action() {
		      return new UIFormTest() {
		          public boolean run(String input) {
		              return ! "".equals(input.trim());
		            }
		          };
 	      }  
	  };
	 
	 public abstract UIFormTest action();
	 
 }
  
  
  Control(Inventory inventory, UI ui) {
    _inventory = inventory;
    _ui = ui;
    
    EXITED = new ExitedState();
    EXIT = new ExitState(this, ui);
    START = new StartState(this, ui);
    
    _state = START;
    
    UIFormTest yearTest = Control.FormTest.TEST_YEAR.action();
    _numberTest = Control.FormTest.TEST_NUM.action();
    _stringTest = Control.FormTest.TEST_STRING.action();

    Factory fact = new Factory();
    //UIFormBuilderInterface f = fact.FactoryUIformBuilder();
    CommonBuilderInterface f = fact.FactoryCommonBuilder();
    f.add("Title", _stringTest);
    f.add("Year", yearTest);
    f.add("Director", _stringTest);
    _getVideoForm = f.toUIMenuForm("Enter Video");
  }
  
  void run() {
    try {
    	_state.run();

    } catch (UIError e) {
      _ui.displayError("UI closed");
    }
  }
  
  interface State{
	  public void run();
  }
  
  final class StartState implements State{
	  Control control;
	  UI ui;
	  Factory fact = new Factory();
	  CommonInterface m = fact.FactoryCommon();
	  StartState(Control control, UI ui){
		  this.control = control;
		  this.ui = ui;
		  
		  CommonBuilderInterface mb = fact.FactoryCommonBuilder();
		  
		  	mb.add(StartMenu.DEF.prompt, StartMenu.DEF);
		  	mb.add(StartMenu.ADD_REMOVE_VID.prompt, StartMenu.ADD_REMOVE_VID.action(this.control));
		    mb.add(StartMenu.CHECK_IN_VID.prompt, StartMenu.CHECK_IN_VID.action(this.control));
		    mb.add(StartMenu.CHECK_OUT_VID.prompt, StartMenu.CHECK_OUT_VID.action(this.control));
		    mb.add(StartMenu.PRINT_INVENTORY.prompt, StartMenu.PRINT_INVENTORY.action(this.control));
		    mb.add(StartMenu.CLEAR_INVENTORY.prompt, StartMenu.CLEAR_INVENTORY.action(this.control));
		    mb.add(StartMenu.UNDO.prompt, StartMenu.UNDO.action(this.control));
		    mb.add(StartMenu.REDO.prompt, StartMenu.REDO.action(this.control));
		    mb.add(StartMenu.PRINT_TOP_TEN.prompt,StartMenu.PRINT_TOP_TEN.action(this.control));  
		    mb.add(StartMenu.EXIT.prompt,StartMenu.EXIT.action(this.control));
		    mb.add(StartMenu.INITIALIZE_BOGUS_VID.prompt, StartMenu.INITIALIZE_BOGUS_VID.action(this.control));
		    
		    this.m = mb.toUIMenuForm("Bob's Video");
	  }
	  
	  public void run() {
		  ui.processMenu(m);
		  _state.run();
		  }
  }
  
  final class ExitedState implements State {
	  public void run() {
		  _ui.displayMessage("Goodbye");
	  }
	}
  
  
  final class ExitState implements State {
	  Control control;
	  UI ui;
	  Factory fact = new Factory();
	  CommonInterface m = fact.FactoryCommon();
	  	  
	  ExitState(Control control, UI ui) {
	    this.control = control;
	    this.ui = ui;

	    CommonBuilderInterface mb = fact.FactoryCommonBuilder();

	    mb.add(ExitMenu.DEF.prompt, ExitMenu.DEF.action(this.control));
	    mb.add(ExitMenu.YES.prompt, ExitMenu.YES.action(this.control));
	    mb.add(ExitMenu.NO.prompt, ExitMenu.NO.action(this.control));
	    this.m = mb.toUIMenuForm("Are you sure you want to exit?");
	  }
	  
	  public void run() {
	    _ui.processMenu(m);
	    _state.run();
	  }
  }
}












