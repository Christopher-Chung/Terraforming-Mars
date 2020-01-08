import java.util.*;

public class MarsDeck{
  protected HashMap<String,MarsCard> deck;

  public MarsDeck(boolean add){
    deck = new HashMap<String,MarsCard>();
    if (add){
      addAll();
    }
  }

  public MarsCard searchCard(String name){
    if (deck.containsKey(name)){
      return deck.get(name);
    }else{
      System.out.println("Wrong card name.");
      return null;
    }
  }


  //Shuffles initial pile
  public static void initial(MarsDeck allCard,LinkedList<MarsCard> queue){
    Random random = new Random();
    Vector<MarsCard> temp = new Vector<MarsCard>(250);
    for (MarsCard card : allCard.deck.values()){
      temp.add(card);
    }
    int size = temp.size();
    for (int i = 0; i < size; i ++){
      int j = random.nextInt(size - i);
      MarsCard card = temp.get(j);
      temp.remove(card);
      queue.offer(card);
    }
  }

  public void addAll(){

  }

}
