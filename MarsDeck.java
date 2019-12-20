import java.util.HashMap;

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

  public static ConcurrentLinkedQueue<MarsCard> initial(MarsDeck allCard,ConcurrentLinkedQueue<MarsCard> queue){
    Random random = new Random();
    Set set = allCard.keySet();
    Vector<MarsCard> temp = new Vector<MarsCard>(250);
    for (MarsCard card : set){
      temp.add(card);
    }
    int size = temp.size();
    for (int i = 0; i < size; i ++){
      int j = random.nextInt(size - i);
      MarsCard card = temp.get(j);
      temp.remove(card);
      queue.offer(card);
    }
    return queue;
  }

  public void addAll(){

  }

}
