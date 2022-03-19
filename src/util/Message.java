package util;

import util.enums.Tags;

public class Message{
    String[] contents;
    Tags tag;

    public Message(String contents, Tags tag) {
        this.contents = tag == Tags.TEXT ? new String[]{contents} : contents.split(","); // prevent splitting of plaintext messages
        this.tag = tag;
    }

    public String getContents(int index) {
        return contents[index];
    }

    public String getContents(){
        return getContents(0);
    }

    public Tags getTag() {
        return tag;
    }
}
