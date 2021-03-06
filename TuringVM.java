package turring;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

public class TuringVM {
    private int State = 0;
    private int Head = 0;
    private Boolean DoAdvTrace = false;
    private Boolean DoTrace = false;
    private String Tape = "";
    private HashMap<String,tuple> Tuples;
    private int Counter = 0;
    
    public void Run(){
        if (DoAdvTrace){
            PrintHeader();
        }
        while (true){
            if (Head + 1 > Tape.length()){
                break;
            }
            char IC = Tape.charAt(Head);
            tuple current = Tuples.get(tuple.makeKey(State,IC));
            if (current == null || (Head > 0 && IC == '_')){
                break;
            }
            Counter += 1;
            if (DoTrace){
                PrintTrace(current.toString(),IC);
            }
            String Inter = Tape.substring(0,Head) + current.getOutput() 
                    + Tape.substring(Head + 1);
            this.Tape = Inter;
            this.State = current.getNewState();
           
            if (current.GetDirection() == 'l'){
                Head -= 1;
            }else{
                Head += 1;
            }       
        }
        if (DoAdvTrace){
            PrintTailer();
        }
    }
    
    public void SetTrace(Boolean Newtrace,Boolean NewAdvTrace){
        DoTrace = Newtrace;
        DoAdvTrace = NewAdvTrace;
    }
    
    public void WriteTape(String Tape){
        this.Tape = Tape;
    }
    
    public void LoadFromDisc(String File){
        Tuples = GetTuples(File);
    }
    
    private void PrintHeader(){
        System.out.println("Begin execution with settings:");
        System.out.println("    Initial state: " + State);
        System.out.println("    Initial head location: " + Head);
        System.out.println("    Initial tape: " + Tape);
    }
    
    private void PrintTailer(){
        System.out.println("Finished exec with final tape: " + Tape);
        System.out.println("Execution took " + Counter + " steps.");
    }
    
    private void PrintTrace(String tuple,char in){
        System.out.println(Tape.substring(0, Head) +
                "[" + in + "]" + Tape.substring(Head + 1) + " " + tuple);

    }
    
    private HashMap GetTuples(String File){
        Stream<String> fileInst = null;
        HashMap<String,tuple> Tuples = new HashMap();
        try{
            fileInst = Files.lines(Paths.get(File));
        }catch(Exception FileNotFound){
            System.out.println("Exception : " + FileNotFound.toString());
        }
        if (fileInst != null){
          
            String[] STArray = fileInst.toArray(String[]::new);
            int Iter = 0;
            for (String Tuple : STArray){
                Iter += 1;
                String[] ItemSet = Tuple.split(" ");
                
                if (ItemSet.length != 5){
                    System.out.println("Check tuple file at line " + (Iter + 1));
                }else{
                    tuple Item = new tuple(Integer.parseInt(ItemSet[0]),
                            ItemSet[1].charAt(0),
                            Integer.parseInt(ItemSet[2]),
                            ItemSet[3].charAt(0),
                            ItemSet[4].charAt(0));
                    Tuples.put(Item.getKey(),Item);
                }
            }
        }
        
        return Tuples;
    }
    
    public TuringVM(){  
    }
}
