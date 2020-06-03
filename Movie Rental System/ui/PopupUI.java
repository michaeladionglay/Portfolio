package package_hw8.ui;

import javax.swing.JOptionPane;
//import java.io.IOException;

final class PopupUI implements UI {
  //PopupUI() {}

  public void displayMessage(String message) {
    JOptionPane.showMessageDialog(null,message);
  }

  public void displayError(String message) {
    JOptionPane.showMessageDialog(null,message,"Error",JOptionPane.ERROR_MESSAGE);
  }

    public void processMenu(Common menu) {
  //public void processMenu(UIMenu menu) {
    StringBuffer b = new StringBuffer();
    b.append(menu.getHeading());
    b.append("\n");
    b.append("Enter choice by number:");
    b.append("\n");

    for (int i = 1; i < menu.size(); i++) {
      b.append("  " + i + ". " + menu.getPrompt(i));
      b.append("\n");
    }

    String response = JOptionPane.showInputDialog(b.toString());
    if(response == null) {
    	response = "";
    }
    int selection;
    try {
      selection = Integer.parseInt(response, 10);
      if ((selection < 0) || (selection >= menu.size()))
        selection = 0;
    } catch (NumberFormatException e) {
      selection = 0;
    }

    menu.runAction(selection);
  }
  
  public void processMenu(CommonInterface menu) {
	    StringBuffer b = new StringBuffer();
	    b.append(menu.getHeading());
	    b.append("\n");
	    b.append("Enter choice by number:");
	    b.append("\n");

	    for (int i = 1; i < menu.size(); i++) {
	      b.append("  " + i + ". " + menu.getPrompt(i));
	      b.append("\n");
	    }

	    String response = JOptionPane.showInputDialog(b.toString());
	    if(response == null) {
	    	response = "";
	    }
	    int selection;
	    try {
	      selection = Integer.parseInt(response, 10);
	      if ((selection < 0) || (selection >= menu.size()))
	        selection = 0;
	    } catch (NumberFormatException e) {
	      selection = 0;
	    }

	    menu.runAction(selection);
	  }

    public String[] processForm(Common form) {
  //public String[] processForm(UIForm form) {
    int formSize = form.size();
    String[] result = new String[formSize];
    int i = 0;
    for(i = 0; i<formSize; ++i) {
    	String answer = JOptionPane.showInputDialog(form.getPrompt(i));
    	if(answer == null) {
    		answer = "";
    	}
    	if (form.checkInput(i, answer) == false) {
    		displayError("Invalid Input. Please try again");
    		i=0;
    	}
    	else {
    		result[i] = answer;
    	}
    }
    return result;
    
    }
    
  
  public String[] processForm(CommonInterface form) {
//	    int formSize = form.size();
//	    String[] result = new String[formSize];
//	    int i;
//	    for(i = 0; i<formSize; ++i) {
//	    	String answer = JOptionPane.showInputDialog(form.getPrompt(i));
//	    	if(answer == null) {
//	    		answer = "";
//	    	}
//	    	if (form.checkInput(i, answer) == false) {
//	    		displayError("Invalid Input. Please try again");
//	    	}
//	    	else {
//	    		result[i] = answer;
//	    	}
//	    }
//	    return result;
	  	int formSize = form.size();
	    String[] result = new String[formSize];
	    int i = 0;
	    while(i<form.size()) {
	    	String answer = JOptionPane.showInputDialog(form.getPrompt(i));
	    	if(answer == null) {
	    		answer = "";
	    	}
	    	if (form.checkInput(i, answer) == false) {
	    		displayError("Invalid Input. Please try again");
	    		continue;
	    	}
	    	else {
	    		result[i] = answer;
	    		i++;
	    	}
	    }
	    return result;
	  }
}
