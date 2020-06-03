package package_hw8.ui;

public class Factory {

	public Factory(){}
	
//	public UIFormBuilder FactoryUIformBuilder() {
//		return new UIFormBuilder();
//	}
//	
//	public UIMenuBuilder FactoryMenuBuilder() {
//		return new UIMenuBuilder();
//	}
	public Common FactoryCommon() {
		return new Common(null, null);
	}
	
	public CommonBuilder FactoryCommonBuilder() {
		return new CommonBuilder();
	}
	
	public UIError FactoryUIError() {
		return new UIError();
	}
}
