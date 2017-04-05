package turring;

public class Turring {
    public static void main(String[] args) {
      TuringVM VM = new TuringVM();
      VM.SetTrace(true,true);
      VM.WriteTape("_XX.YYY+");
      VM.LoadFromDisc("Save.txt");
      VM.Run();
    }
    
}
