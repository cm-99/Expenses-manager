package org.example;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * DocumentLengthFilter is a subclass of DocumentFilter implemented for the purpose of limiting number of characters
 * which can be entered in a document using this filter.
 */
public class DocumentLengthFilter extends DocumentFilter{
    /**
     * Maximum number of characters in a document permitted by the filter.
     */
    private final int maxLength;

    public DocumentLengthFilter(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if ((fb.getDocument().getLength() + string.length()) <= maxLength) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if ((fb.getDocument().getLength() - length + text.length()) <= maxLength) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}