package package_hw8.ui;

public interface UI {
  //public void processMenu(UIMenu menu);
  public void processMenu(CommonInterface m);
  //public void processMenu(CommonInterface menu);
  //public String[] processForm(UIForm form);
  public String[] processForm(CommonInterface _getVideoForm);
 // public String[] processForm(UIFormInterface form);
  public void displayMessage(String message);
  public void displayError(String message);
}
