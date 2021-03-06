package util.enums;

public enum Tags {
    // grouped first by argsCount and then alphabetically
    INSTRUCTION('i', 1, "instruction"),
    TEXT('t', 1, "plaintext"),
    DOWNLOAD_REQUEST('r', 2, "download request"),
    DOWNLOADABLE_FILE('d', 3, "downloadable file"),
    ;

    private final char value;
    private final int argsCount;

    private final String consoleID;

    public char getValue(){
        return value;
    }

    public int getRequiredArgsCount(){
        return argsCount;
    }

    public String getConsoleID() {
        return consoleID;
    }

    public static Tags fromChar(char value){
        for(Tags index : Tags.values()){
            if(index.getValue() == value){
                return index;
            }
        }
        System.err.printf("Unknown char tag '%s' passed to fromChar() call", value);
        return null;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "value=" + value +
                '}';
    }

    Tags(char value, int argsCount, String consoleID){
        this.value = value;
        this.argsCount = argsCount;
        this.consoleID = consoleID;
    }
}
