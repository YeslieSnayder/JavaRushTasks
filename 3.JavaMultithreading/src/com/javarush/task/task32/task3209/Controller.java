package com.javarush.task.task32.task3209;

import com.javarush.task.task32.task3209.listeners.UndoListener;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.*;
import java.net.URI;

public class Controller {
    private View view;
    private HTMLDocument document;
    private File currentFile;

    public static void main(String... args) {
        View view = new View();
        Controller controller = new Controller(view);
        view.setController(controller);

        view.init();
        controller.init();
    }

    public Controller(View view) {
        this.view = view;
    }

    public void init() {
        createNewDocument();
    }

    public void exit() {
        System.exit(0);
    }

    public void resetDocument() {
        UndoListener listener = view.getUndoListener();
        if (document != null)
            document.removeUndoableEditListener(listener);

        document = (HTMLDocument) new HTMLEditorKit().createDefaultDocument();
        document.addUndoableEditListener(listener);
        
        view.update();
    }

    public void setPlainText(String text) {
        try {
            resetDocument();
            StringReader reader = new StringReader(text);
            HTMLEditorKit editor = new HTMLEditorKit();
            editor.read(reader, document, 0);
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public String getPlainText() {
        StringWriter writer = new StringWriter();

        try {
            HTMLEditorKit editor = new HTMLEditorKit();
            editor.write(writer, document, 0, document.getLength());
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }

        return writer.toString();
    }

    public HTMLDocument getDocument() {
        return document;
    }

    public void createNewDocument() {
        view.selectHtmlTab();
        resetDocument();
        view.setTitle("HTML Редактор");
        view.resetUndo();
        currentFile = null;
    }

    public void openDocument() {
        view.selectHtmlTab();
        JFileChooser fileChooser = new JFileChooser();
        HTMLFileFilter filter = new HTMLFileFilter();
        fileChooser.setFileFilter(filter);

        if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            resetDocument();
            view.setTitle(currentFile.getName());

            try (FileReader reader = new FileReader(currentFile)){
                HTMLEditorKit editor = new HTMLEditorKit();
                editor.read(reader, document, 0);
            } catch (Exception e) {
                ExceptionHandler.log(e);
            }

            view.resetUndo();
        }
    }

    public void saveDocument() {
        if (currentFile == null)
            saveDocumentAs();
        else {
            view.selectHtmlTab();
            view.setTitle(currentFile.getName());

            if (currentFile != null) {
                editorWrite();
            }
        }
    }

    public void saveDocumentAs() {
        view.selectHtmlTab();
        JFileChooser fileChooser = new JFileChooser();
        HTMLFileFilter filter = new HTMLFileFilter();
        fileChooser.setFileFilter(filter);

        if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            view.setTitle(currentFile.getName());

            editorWrite();
        }
    }

    private void editorWrite() {
        try (FileWriter writer = new FileWriter(currentFile)){
            HTMLEditorKit editor = new HTMLEditorKit();
            editor.write(writer, document, 0, document.getLength());
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public void LoadFromInternet() {
        view.selectHtmlTab();

        String URL = view.showInternetDialog();
        if (URL.isEmpty()) return;
        try {
            currentFile = new File(new URI(URL));
            view.setTitle(currentFile.getName());

            editorWrite();
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }
}
