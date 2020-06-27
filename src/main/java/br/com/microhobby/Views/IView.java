package br.com.microhobby.Views;

import com.totalcross.knowcode.parse.XmlContainerLayout;

public interface IView {
    public XmlContainerLayout getView();
    public void init();
    public void clean();
    public void show();
}
