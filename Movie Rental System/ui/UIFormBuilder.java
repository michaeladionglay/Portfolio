//package package_hw8.ui;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import package_hw8.ui.Pair;
////import package_hw8.ui.UIForm;
//import package_hw8.ui.UIFormTest;
//
//final class UIFormBuilder implements UIFormBuilderInterface{
//	private final List<Pair<String, UIFormTest>> _menu;
//	  public UIFormBuilder() {
//		  _menu = new ArrayList();
//	  }
//	  public Common toUIForm(String heading) {
//	  //public UIForm toUIForm(String heading) {
//	    if (null == heading)
//	      throw new IllegalArgumentException();
//	    if (_menu.size() < 1)
//	      throw new IllegalStateException();
//	    Pair<String, UIFormTest>[] array = new Pair[_menu.size()];
//	    //UIForm.Pair[] array = new UIForm.Pair[_menu.size()];
//	    for (int i = 0; i < _menu.size(); i++)
//	      //array[i] = (UIForm.Pair) (_menu.get(i));
//	      array[i] = _menu.get(i);
//	   //return new UIForm(heading, array);
//	    return new Common(heading, array);
//	  }
//	  public void add(String prompt, UIFormTest test) {
//	    _menu.add(new Pair(prompt, test));
//	  }
//}
