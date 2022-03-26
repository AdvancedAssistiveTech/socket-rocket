package util;

import util.enums.Tags;

public class Message{
    String[] contents;
    Tags tag;

    public Message(Tags tag, String... contents) {
        this.contents = contents;
        this.tag = tag;
    }

    public Message(String toParse){
        tag = Tags.fromChar(toParse.charAt(1));
        toParse = toParse.substring(2);
        contents = tag == Tags.TEXT ? new String[]{toParse} : toParse.split(","); //don't split by comma on plaintext
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

    @Override
    public String toString() {
        StringBuilder stringExpr = new StringBuilder();
        stringExpr.append("\\").append(tag.getValue());
        for(String index : contents){
            stringExpr.append(", ").append(index);
        }
        stringExpr.setLength(stringExpr.length() - 1); //truncate trailing comma
        return stringExpr.toString();
    }
}
