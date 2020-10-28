package client.guiandpresenter.mainscreen;

import client.guiandpresenter.SystemPresenter;

public class MainScreenPresenter extends SystemPresenter {
    String[] buttonList() {
        return new String[]{"Login", "Registration", "Credits", "Exit"};
    }

    @Override
    public String getTitle() {
        return "Main Menu";
    }

    String creditPage() {
        return ("<html>Jerry (Ziyuan) Zhang<br>Rachel (Yuchen) Zeng<br>Andrew Feng<br>Mark (Hehan) Zhao<br>Qiyun Pan" +
                "<br>David (Ruijia) Wang<br>Gloria (Jiaming) Yang<br>Nero (Wenzhi) Lin<br>2020-08-11");
    }
}
