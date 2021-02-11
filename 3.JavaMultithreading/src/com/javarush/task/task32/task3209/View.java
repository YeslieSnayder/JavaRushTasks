package com.javarush.task.task32.task3209;

import com.javarush.task.task32.task3209.listeners.FrameListener;
import com.javarush.task.task32.task3209.listeners.TabbedPaneChangeListener;
import com.javarush.task.task32.task3209.listeners.UndoListener;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame implements ActionListener {
    private Controller controller;

    private UndoManager undoManager = new UndoManager();
    private UndoListener undoListener = new UndoListener(undoManager);

    private JTabbedPane tabbedPane = new JTabbedPane();
    private JTextPane htmlTextPane = new JTextPane();
    private JEditorPane plainTextPane = new JEditorPane();

    private final int TAB_HTML = 0;
    private final int TAB_TEXT = 1;

    public View() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Новый":
                controller.createNewDocument();
                break;
            case "Открыть":
                controller.openDocument();
                break;
            case "Загрузить с интернета":
                controller.LoadFromInternet();
                break;
            case "Сохранить":
                controller.saveDocument();
                break;
            case "Сохранить как...":
                controller.saveDocumentAs();
                break;
            case "Выход":
                controller.exit();
                break;
            case "О программе":
                showAbout();
                break;
        }
    }

    public void init() {
        initGui();
        FrameListener listener = new FrameListener(this);
        addWindowListener(listener);
        setVisible(true);
    }

    public void exit() {
        controller.exit();
    }

    public void initMenuBar() {
        JMenuBar menu = new JMenuBar();
        MenuHelper.initFileMenu (this, menu);
        MenuHelper.initEditMenu (this, menu);
        MenuHelper.initStyleMenu(this, menu);
        MenuHelper.initAlignMenu(this, menu);
        MenuHelper.initColorMenu(this, menu);
        MenuHelper.initFontMenu (this, menu);
        MenuHelper.initHelpMenu (this, menu);

        getContentPane().add(menu, BorderLayout.NORTH);
    }

    public void initEditor() {
        // Устанавливаем тип контента
        htmlTextPane.setContentType("text/html");

        // Добавляем вкладку с текстом {@code 'HTML'} и компонентом {@code JScroolPane},
        // основанным на базе {@code htmlTextPane}
        JScrollPane htmlScrollPane = new JScrollPane(htmlTextPane);
        tabbedPane.add("HTML", htmlScrollPane);

        // Добавляем вкладку с текстом {@code 'Текст'} и компонентом {@code JScroolPane},
        // основанным на базе {@code plainTextPane}
        JScrollPane plainScrollPane = new JScrollPane(plainTextPane);
        tabbedPane.add("Текст", plainScrollPane);

        // Устанавливаем предпочтительный размер панели
        tabbedPane.setPreferredSize(new Dimension(500, 500));

        // Создаём объект класса {@code TabbedPaneChangeListener}
        // и устанавливаем его в качестве слушателя изменений в {@code tabbedPane}
        TabbedPaneChangeListener listener = new TabbedPaneChangeListener(this);
        tabbedPane.addChangeListener(listener);

        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public void initGui() {
        initMenuBar();
        initEditor();
        pack();
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void selectedTabChanged() {
        String text;
        switch (tabbedPane.getSelectedIndex()) {
            case TAB_HTML:
                text = plainTextPane.getText();
                controller.setPlainText(text);
                break;
            case TAB_TEXT:
                text = controller.getPlainText();
                plainTextPane.setText(text);
                break;
        }
        resetUndo();
    }

    public boolean canUndo() {
        return undoManager.canUndo();
    }

    public boolean canRedo() {
        return undoManager.canRedo();
    }

    public boolean isHtmlTabSelected() {
        return tabbedPane.getSelectedIndex() == TAB_HTML;
    }

    public void undo() {
        try {
            undoManager.undo();
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public void redo() {
        try {
            undoManager.redo();
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public void resetUndo() {
        undoManager.discardAllEdits();
    }

    public UndoListener getUndoListener() {
        return undoListener;
    }

    public void selectHtmlTab() {
        tabbedPane.setSelectedIndex(0);
        resetUndo();
    }

    public void update() {
        htmlTextPane.setDocument(controller.getDocument());
    }

    public void showAbout() {
        JOptionPane.showMessageDialog(tabbedPane,
                "This program was created AndreyKey)",
                "About",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public String showInternetDialog() {
        return JOptionPane.showInputDialog(tabbedPane,
                "Введите ссылку на файл",
                null);
    }
}
