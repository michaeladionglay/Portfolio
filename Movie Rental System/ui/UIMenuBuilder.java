//package package_hw8.ui;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import package_hw8.ui.Pair;
////import package_hw8.ui.UIMenu;
//import package_hw8.ui.UIMenuAction;
//
//class UIMenuBuilder implements UIMenuBuilderInterface{
//	private final List<Pair<String, UIMenuAction>> _menu;
//	  public UIMenuBuilder() {
//	    this._menu = new ArrayList();
//	  }
//
//	  public Common toUIMenu(String heading) {
//	  //public UIMenu toUIMenu(String heading) {
//	    if (null == heading)
//	      throw new IllegalArgumentException();
//	    if (_menu.size() <= 1) {
//	      throw new IllegalStateException();
//	    }
//	    Pair<String, UIMenuAction>[] array = new Pair[_menu.size()];
//	    for (int i = 0; i < _menu.size(); i++) {
//	    	array[i] = _menu.get(i);
//	    }
//	    //return new UIMenu(heading, array);
//	    return new Common(heading, array);
//	  }
//	  public void add(String prompt, UIMenuAction action) {
//	    if (null == action) {
//	      throw new IllegalArgumentException();
//	    }
//	    _menu.add(new Pair<String, UIMenuAction>(prompt, action));
//	  }
//}
