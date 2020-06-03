package package_hw8.main;

import package_hw8.ui.UIFactory;
import package_hw8.data.Data;

public class Main {
  private Main() {}
  public static void main(String[] args) {
    Control control = new Control(Data.newInventory(), UIFactory.ui());
    control.run();
  }
}
