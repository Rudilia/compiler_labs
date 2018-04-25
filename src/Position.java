class Position implements  Comparable<Position> {
    private String text;
    private int line, pos, index;

    public Position(String text){
        this.text = text;
        line = pos = 1;
        index = 0;
    }

    public Position(String text, int line, int pos, int index){
        this.text = text;
        this.line = line;
        this.pos = pos;
        this.index = index;
    }

    public Position(Position other) {
        this(other.text, other.line, other.pos, other.index);
    }


    public int getIndex() {
        return index;
    }

    public int getLine() {
        return line;
    }

    public int getPos() {
        return pos;
    }

    public Position add(){
        if(this.index < this.text.length()){
            if(this.isNewLine()){
                if (this.text.charAt(this.index)=='\r') this.index++;
                this.line++;
                this.pos = 1;
            } else this.pos++;
            this.index++;
        }
        return this;
    }

    @Override
    public int compareTo(Position o) {
        return Integer.compare(index, o.index);
    }
    @Override
    public String toString(){
        return "(" + line + "," + pos + ")";
    }

    public int Cp(){
            return (index == text.length()) ? -1 : text.charAt(index);
    }

    public boolean isWhitespace(){
        return (index != text.length()) && Character.isWhitespace(text.charAt(index));
    }
    public boolean isLetter(){
        return (index != text.length()) && Character.isLetter(text.charAt(index));
    }
    public boolean isLetterOrDigit(){
        return (index != text.length()) && Character.isLetterOrDigit(text.charAt(index));
    }
    public boolean isDigit(){
        return (index != text.length()) && text.charAt(index) >= '0' && text.charAt(index) <= '9';
    }

    public boolean isNewLine(){
        if (index == text.length()) return true;
        if (text.charAt(index) =='\r' && index + 1 < text.length())
            return (text.charAt(index+1) == '\n');
        return (text.charAt(index) == '\n');

    }
}
