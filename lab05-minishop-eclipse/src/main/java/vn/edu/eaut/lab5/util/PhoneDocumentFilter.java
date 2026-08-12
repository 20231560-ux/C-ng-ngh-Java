package vn.edu.eaut.lab5.util;
import javax.swing.text.*;
public class PhoneDocumentFilter extends DocumentFilter {
    private boolean ok(FilterBypass fb,int off,int len,String text)throws BadLocationException{
        String cur=fb.getDocument().getText(0,fb.getDocument().getLength());
        String t=cur.substring(0,off)+(text==null?"":text)+cur.substring(off+len);
        return t.matches("\\d{0,10}");
    }
    @Override public void insertString(FilterBypass fb,int off,String text,AttributeSet a)throws BadLocationException{if(ok(fb,off,0,text))super.insertString(fb,off,text,a);}
    @Override public void replace(FilterBypass fb,int off,int len,String text,AttributeSet a)throws BadLocationException{if(ok(fb,off,len,text))super.replace(fb,off,len,text,a);}
}
