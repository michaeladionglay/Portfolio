package package_hw8.ui;

import java.util.ArrayList;
import java.util.List;

class CommonBuilder<T, U> implements CommonBuilderInterface{
	private final List<Pair<T, U>> _menu;
	public CommonBuilder() {
	    this._menu = new ArrayList();
	  }
	
	public Common toUIMenuForm(String heading) {
		    if (null == heading) {
		      throw new IllegalArgumentException();
		      }
		    if (_menu.size() < 1) {
		      throw new IllegalStateException();
		      } 
		    Pair<T, U>[] array = new Pair[_menu.size()];
		    for (int i = 0; i < _menu.size(); i++) {
		      array[i] = _menu.get(i);
		      }
		      return new Common(heading, array);
		    }
//	
//	public Common toUIMenu(String heading) {
////	    if (null == heading) {
////	      throw new IllegalArgumentException();
////	      }
////	    if (_menu.size() <= 1) {
////	      throw new IllegalStateException();
////	      } 
////	    Pair<T, U>[] array = new Pair[_menu.size()];
////	    for (int i = 0; i < _menu.size(); i++) {
////	      array[i] = _menu.get(i);
////	      }
////	      return new Common(heading, array);
//		return toUIMenu(heading);
//	    }
	public <M> void add(String prompt, M testAction) {
		if (null == testAction) {
		      throw new IllegalArgumentException();
		    }
		    _menu.add(new Pair(prompt, testAction));
	
	}
//	public void add(String prompt, UIFormTest testAction) {
//		    if (null == testAction) {
//		      throw new IllegalArgumentException();
//		    }
//		    _menu.add(new Pair(prompt, testAction));
//	}
//	
//	public void add(String prompt, UIMenuAction testAction) {
//	    	if (null == testAction) {
//	    		throw new IllegalArgumentException();
//	    	}
//	    	_menu.add(new Pair(prompt, testAction));
//}
}
