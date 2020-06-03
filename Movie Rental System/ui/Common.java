package package_hw8.ui;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

class Common<T, U> implements CommonInterface{
	private final String _heading;
	private final Pair<T, U>[] _menuForm;
	private final Pair<String, UIFormTest>[] _Form;
	private final Pair<String, UIMenuAction>[] _Menu;
	
	Common(String heading, Pair<T, U>[] menu) {
	    _heading = heading;
	    _menuForm = menu;
	    _Form = (Pair<String, UIFormTest>[]) menu;
	    _Menu = (Pair<String, UIMenuAction>[]) menu;
	  }
	
	public int size() {
	    return _menuForm.length;
	  }
	  public String getHeading() {
	    return _heading;
	  }
	  public String getPrompt(int i) {
	    return (String) _menuForm[i].prompt;
	  }
	  
	  public boolean checkInput(int i, String input) {
		 if (null == _menuForm[i]) {
			 return true;
		 	 }
		 //Pair<String, UIFormTest>[] _Form = new Pair<String, UIFormTest>[] {;
		 assertThat(_menuForm, instanceOf(_Form.getClass()));
		 return ((Pair<String, UIFormTest>)_menuForm[i]).test.run(input);//.test.run(input);
		 }
	  
	  public void runAction(int i) {
		  //Pair<String, UIMenuAction>[] _Menu = new Pair<String, UIMenuAction>(_heading, null);
		  assertThat(_menuForm, instanceOf(_Menu.getClass()));
		((Pair<String, UIMenuAction>)_menuForm[i]).test.run();
	    }
	
}
