import java.io.File;
import java.io.IOException;
import java.util.*;

enum DomainTag{
    IDENT,
    STRING,
    END_OF_PROGRAM
}
class MyCompiler{
    private SortedMap<Position, Message> messages;
    public MyCompiler(){
        messages = new TreeMap<>();
    }

    public void AddMessage(boolean isError, Position pos, String txt){
        messages.put(pos, new Message(isError, txt));
    }

    public void OutputMessages(){
        for (Map.Entry<Position,Message> entry : messages.entrySet()){
            System.out.print("Error" + " "+ entry.getKey() + " ");
            System.out.println(entry.getValue().getText());
        }
    }
}
class MyScanner {
    public String Program;
    private MyCompiler compiler;
    private Position cur;

    public MyScanner(String program, MyCompiler compiler){
        this.compiler =compiler;
        cur = new Position(Program = program);
    }

    public Token NextToken(){
        while (cur.Cp() != -1){
            while (cur.isWhitespace()) cur.add();

            Position start = new Position(cur);


            switch (cur.Cp()){
                case'\'':
                    cur.add();
                    while (cur.Cp() != -1) {
                        if (cur.isNewLine()) {
                            compiler.AddMessage(true,  new Position(cur), "Newline inside string literal");
                        }
                        if(cur.Cp() == '\''){
                            cur.add();
                            if (cur.Cp() == '\''){
                                cur.add();

                            } else{
                                return new StringToken(Program.substring(start.getIndex(),
                                        cur.getIndex()),start, new Position(cur));
                            }
                        } else cur.add();
                    }
                    compiler.AddMessage(true,  new Position(cur), "End of file inside string literal");
                    return new StringToken(Program.substring(start.getIndex(),
                            cur.getIndex()),start,  new Position(cur));
                case '#':
                    cur.add();
                    if (!cur.isDigit()){
                        compiler.AddMessage(true,  new Position(cur), "Empty string literal");
                        return new StringToken(Program.substring(start.getIndex(),
                                cur.getIndex()),start,  new Position(cur));
                    }
                    while(cur.isDigit() && cur.Cp() != -1){
                        cur.add();
                    }
                    return new StringToken(Program.substring(start.getIndex(),
                            cur.getIndex()),start,  new Position(cur));
                default:
                    if(cur.isLetter()){
                        cur.add();
                        while(cur.isLetterOrDigit()){
                            cur.add();
                        }
                        return new IdentToken(Program.substring(start.getIndex(),
                                cur.getIndex()),start,  new Position(cur));
                    }
                    else{
                        cur.add();
                        compiler.AddMessage(true,  new Position(cur),"Unexpected symbol " + Program.substring(start.getIndex(),
                                cur.getIndex()));
                        break;
                    }
            }
        }
        return new EOFToken(cur, cur);
    }

}
abstract class Token{
    public DomainTag tag;
    public Fragment coords;

    public Token(DomainTag tag, Position starting, Position following){
        this.tag = tag;
        coords = new Fragment(starting,following);
    }
    @Override
    public String toString() {
        return (coords + " " + tag.toString());
    }

}

class IdentToken extends Token{
    public String Ident;
    public IdentToken(String ident, Position starting, Position following){
        super(DomainTag.IDENT, starting, following);
        Ident = ident;
    }

    @Override
    public String toString() {
        return (coords + " "+ tag.toString()+ " " + Ident);
    }
}

class EOFToken extends Token{
    public EOFToken(Position starting, Position following){
        super(DomainTag.END_OF_PROGRAM, starting, following);
    }
    @Override
    public String toString() {
        return super.toString();
    }
}

class StringToken extends Token{
    public String Str;
    public StringToken(String str, Position starting, Position following){
        super(DomainTag.STRING, starting, following);
        Str = str;
    }
    @Override
    public String toString() {
        return (coords + " " + tag.toString()+ " " + Str);
    }
}
public class Lexer {
    public static void main(String[] args) {
       // String file = args[0];
        String file = "C:\\Users\\Татьяна\\IdeaProjects\\compiler_labs\\src\\input.txt";
        try {
            Scanner scanner = new Scanner(new File(file));
            String text = scanner.useDelimiter("\\A").next();

            MyCompiler compiler = new MyCompiler();
            MyScanner scan = new MyScanner(text, compiler);
            List<Token> tokens = new ArrayList<>();
            Token tok = scan.NextToken();
            while(tok.tag != DomainTag.END_OF_PROGRAM){
                tokens.add(tok);
                tok = scan.NextToken();

            }
            System.out.println("TOKENS:");
            for(Token token: tokens){
                System.out.println(token);
            }
            System.out.println("ERRORS:");
            compiler.OutputMessages();




        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
