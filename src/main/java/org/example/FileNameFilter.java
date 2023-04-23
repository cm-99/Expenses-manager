package org.example;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

class FileNameFilter extends DocumentFilter
{
    private static final char[] ILLEGAL_CHARACTERS = {'/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':', '.'};

    @Override
    public void insertString (DocumentFilter.FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException
    {
        fb.insertString (offset, fixText(text), attr);
    }

    @Override
    public void replace (DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attr) throws BadLocationException
    {
        fb.replace(offset, length, fixText(text), attr);
    }

    private String fixText (String s)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < s.length(); ++i)
        {
            if (!isIllegalFileNameChar (s.charAt (i)))
                sb.append (s.charAt (i));
        }
        return sb.toString();
    }

    private boolean isIllegalFileNameChar (char c)
    {
        boolean isIllegal = false;
        for (char illegalCharacter : ILLEGAL_CHARACTERS) {
            if (c == illegalCharacter){
                isIllegal = true;
                break;
            }
        }
        return isIllegal;
    }
}
