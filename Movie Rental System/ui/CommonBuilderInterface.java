package package_hw8.ui;

public interface CommonBuilderInterface {
	
	
	public Common toUIMenuForm(String heading);

    public <M>void add(String prompt, M testAction);

//    public void add(String prompt, UIMenuAction testAction);

}
