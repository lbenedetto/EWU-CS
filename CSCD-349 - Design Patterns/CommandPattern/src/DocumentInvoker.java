import java.util.ArrayList;

public class DocumentInvoker {
	  private ArrayList commands = new ArrayList();
      private Document currentdoc;
      
      public DocumentInvoker(String name) {
    	  currentdoc = new Document(name);
      }
      
      public void Redo(int level)
      {
          Command tmp;
    	  System.out.println("---- Redo level " + level);
          tmp = (Command) commands.get(level);
          tmp.redo();
      }

      public void Undo(int level)
      {
    	  Command tmp;
    	  System.out.println("---- Undo level " + level);
    	  tmp = (Command) commands.get(level);
          tmp.undo();
      }

    
      public void Write(String text)
      {
          DocumentWriteCommand cmd = new 
          DocumentWriteCommand(currentdoc,text);
          commands.add(cmd);
      }
      
      public void Erase(String text)
      {
          DocumentEraseCommand cmd = new 
          DocumentEraseCommand(currentdoc,text);
          commands.add(cmd);
      }
      
      
      public void Bold(int line)
      {
          DocumentBoldCommand cmd = new 
          DocumentBoldCommand(currentdoc,line);
          commands.add(cmd);
      }
      
      public void RemoveBold(int line)
      {
          DocumentRemoveBoldCommand cmd = new 
          DocumentRemoveBoldCommand(currentdoc,line);
          commands.add(cmd);
      }
      
      public String Read()
      {
          return currentdoc.Read();
      }
      
      public void printCommandBuffer() {
    		StringBuffer txt = new StringBuffer();
    		txt.append("---- Commands issued----\n");
    		for (int i = 0; i < commands.size(); i++) {
    			txt.append( i +":"+ (Command) commands.get(i) + "\n");
    			}
       System.out.println(txt.toString());
      }
}
