package br.com.microhobby.Views;

import java.util.Stack;

import com.totalcross.knowcode.parse.XmlContainerFactory;
import com.totalcross.knowcode.parse.XmlContainerLayout;
import totalcross.ui.MainWindow;

public abstract class View implements IView {
    
    private static final Stack<View> HISTORY = new Stack<>();
    protected XmlContainerLayout View;
    private MainWindow mainWindow;

    public View (MainWindow mainWindow)
    {
        String viewXMLPath = "xml/" + this.getClass().getSimpleName() + ".xml";
        View = (XmlContainerLayout) XmlContainerFactory.create(viewXMLPath);
        
        this.mainWindow = mainWindow;
    }

    public void show()
    {
        // clean the previuos
        if (!HISTORY.empty()) {
            HISTORY.firstElement().clean();
        }

        // stack if we are new
        if (HISTORY.empty() || !HISTORY.firstElement().equals(this))
            HISTORY.push(this);
        
        mainWindow.swap(View);
        init();
    }

    public void back()
    {
        if (!HISTORY.empty() && HISTORY.size() > 1) {
            // out and clean
            HISTORY.pop().clean();
            if (!HISTORY.empty()) {
                // show the back
                HISTORY.firstElement().show();
            }
        }
    }

    public XmlContainerLayout getView()
    {
        return View;
    }

    public MainWindow getMainWindow()
    {
        return mainWindow;
    }

    public abstract void init();
    public abstract void clean();
}
