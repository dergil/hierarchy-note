package com.example.roomwordsample;

public class TestUtil {
    private Long id1 = 424242L;
    private String name1 = "name";
    private String text1 = "text";
    private String directory_name1 = "MYDIR";
    private Boolean isDir1 = Boolean.FALSE;
    private Boolean isSynced1 = Boolean.FALSE;

    private Long id2 = 434343L;
    private String name2 = "other name";
    private String text2 = "other text";
    private String directory_name2 = "MYDIR2";
    private Boolean isDir2 = Boolean.FALSE;
    private Boolean isSynced2 = Boolean.FALSE;

    public Note createNote1(){
        Note note = new Note(name1, text1, directory_name1, isDir1, isSynced1);
        note.setId(id1);
        return note;
    }

    public Note createNote2(){
        Note note = new Note(name2, text2, directory_name2, isDir2, isSynced2);
        note.setId(id2);
        return note;
    }

    public boolean compareNotes (Note note1, Note note2) {
        boolean sameValues = true;
        if (!note1.getId().equals(note2.getId()))
            sameValues = false;
        if (!note1.getName().equals(note2.getName()))
            sameValues = false;
        if (!note1.getText().equals(note2.getText()))
            sameValues = false;
        if (!note1.isDir().equals(note2.isDir()))
            sameValues = false;
        return sameValues;
    }

    public boolean compareNotes (ResponseDto note1, Note note2) {
        boolean sameValues = true;
        if (!note1.getId().equals(note2.getId()))
            sameValues = false;
        if (!note1.getName().equals(note2.getName()))
            sameValues = false;
        if (!note1.getText().equals(note2.getText()))
            sameValues = false;
        if (!note1.getDir().equals(note2.isDir()))
            sameValues = false;
        return sameValues;
    }
}
