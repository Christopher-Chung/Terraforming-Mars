import java.util.*;

public class MarsGame{

  protected int numPlayers;
  protected int[][] board; //board[location][id]
  protected int temperature;
  protected int generation;
  protected int oxygen;
  protected int oceans;

  final int maxTemp = 8;
  final int maxOxygen = 14;
  final int maxOcean = 9;
  MarsDeck deck;
  Queue<MarsCard> drawPile;
  Vector<MarsCard> discardPile;
  Vector<MarsPlayer> players;

  HashMap<String,boolean> awards;
  HashMap<String,Integer> milestones;
  int[] awardCost;
  int numAward;
  int numMilestone;

  public MarsGame(int playerNumber){
    numPlayers = playerNumber;
    board = new int[!!][2];
    temperature = -24;
    generation = 1;
    oxygen = 0;
    ocean = 0;
    numAward = 0;
    numMilestone = 0;
    milestones = new HashMap<String,Integer>;
    milestones.put("Terraformer",0);
    milestones.put("Builder",0);
    milestones.put("Mayor",0);
    milestones.put("Gardner",0);
    milestones.put("Planner",0);
    awards = new HashMap<String,boolean>;
    awards.put("Landlord",false);
    awards.put("Banker",false);
    awards.put("Scientist",false);
    awards.put("Miner",false);
    awards.put("Thermalist",false);
    players = new Vector<MarsPlayer>();
    awardCost = new int[]{8,14,20};
    deck = new MarsDeck(true);
    drawPile = new ConcurrentLinkedQueue<MarsCard>();
    MarsDeck.initial(deck,drawPile);
    discardPile = new Vector<MarsCard>();
  }

  private void shuffle(){
    Random random = new Random();
    int size = discardPile.size();
    for (int i = 0; i < size; i ++){
      int j = random.nextInt(size - i);
      MarsCard card = discardPile.get(j);
      discardPile.remove(card);
      drawPile.offer(card);
    }
  }

  private void playGeneration(){
    //Generation 1 starts from id = 0 player
    int id = (generation - 1) % numPlayers;
    playTurn(id);
    while (findNextPlayer(id) != null){
      id = findNextPlayer(id);
      playTurn(id);
    }
  }

  private int findNextPlayer(int currentId){
    int i = 1;
    while (players.get((currentId + i) % numPlayers).hardPass == true && i % numPlayers != 0){
      i ++;
    }
    if (i == numPlayers){
      if (players.get(currentId).hardPass){
        return null;
      }else{
        return currentId;
      }
    }else{
      return (currentId + i) % numPlayers;
    }
  }

  private void singleAction(int id, Scanner sc){
    System.out.println("Now is " + id + " turn!");
    System.out.println("What would you like to do?");
    String action = sc.nextLine().trim();
    switch (action) {
      case "Standard project":
        System.out.println("Which standard project are you doing?");
        String mlstn = sc.nextLine().trim();
        standardProjects(mlstn,id);
        break;
      case "Play card":
        System.out.println("Which card do you want to play?");
        String mlstn = sc.nextLine().trim();
        playCard(mlstn,id);
        break;
      case "Fund award":
        System.out.println("Which award do you want to fund?");
        String mlstn = sc.nextLine().trim();
        fundAward(mlstn,id);
        break;
      case "Claim milestone":
        System.out.println("Which milestone do you want to claim?");
        String mlstn = sc.nextLine().trim();
        claimMilestone(mlstn,id);
        break;
      case "Raise temperature":
        MarsPlayer plyr = players.get(id);
        if (plyr.heat >= 8){
          plyr.heat -= 8;
          putGreenery(); ///
        }else{
          System.out.println("Not enough heat.");
        }
        break;
      case "Plant greenery":
        MarsPlayer plyr = players.get(id);
        boolean check = (plyr.corp == "Ecoline")? true : false;
        if (check){
          if (plyr.plant >= 7){
            plyr.plant -= 7;
            putGreenery();
          }else{
            System.out.println("Not enough plants.");
          }
        }else{
          if (plyr.plant >= 8){
            plyr.plant -= 8;
            putGreenery();
          }else{
            System.out.println("Not enough plants.");
          }
        }
        break;
      case "Hard Pass":
        hdPass(id);
        break;
      case "Activate":
        //Do stuff
        break;
      default:
        System.out.println("Invalid action.");
  }

  private void playTurn(int id){
    //Do any two actions
    Scanner scanner = new Scanner(System.in);
    singleAction(id, scanner);
    String response = "";
    if (players.get(id).hardPass) return;
    while (!response.equals("y") || !response.equals("n")){
      System.out.println("Second action? (y/n)");
      response = scanner.nextLine().trim();
    }
    if (response.equals(y)){
      singleAction(id, scanner);
    }
  }

  private void endGame(){

  }

  private void draft(){

  }

  private MarsCard drawCard(){
    return drawPile.poll();
  }

  private void hdPass(int id){
    MarsPlayer plyr = players.get(id);
    plyr.hardPass = true;
  }

  private void endGeneration(){
    if (temperature == maxTemp && oxygen == maxOxygen && oceans == maxOcean){
      endGame();
    }else{
      generation ++;
      for (MarsPlayer plyr : players){
        plyr.produce();
      }
      if (discardPile.size() > 35) shuffle();
      draft();
      playGeneration();
    }
  }

  private void tempIncrease(MarsPlayer plyr){
    if (temperature < maxTemp){
      temperature += 2;
      plyr.ratingIncrement();
    }
  }

  private void oxygenIncrease(MarsPlayer plyr){
    if (oxygen < maxOxygen){
      oxygen ++;
      plyr.ratingIncrement();
    }
  }

  private void putGreenery(int id, int location){

  }

  private void putOnBoard(int id, String item){
    int location = -1;
    while (location < 0 || board[location][0] != 0){
      System.out.println("Choose location: ");
      location = Scanner.nextInt();
    }
    board[location] = 2;
  }

  //Takes a legal card and check global requirements.
  private boolean checkReqs(String cardName){
    MarsCard card = deck.get(cardName);
    switch (card.cardReqs){
      case 0:
        return true;
      case 1:
        if (card.oxygenReqs > 0){
          return oxygen >= card.oxygenReqs;
        }else{
          return oxygen <= -card.oxygenReqs;
        }
      case 2:
        if (card.tempReqUpperBound){
          return temperature <= card.tempReqs;
        }else{
          return temperature >= card.tempReqs;
        }
      case 3:
        if (card.oceanReqs > 0){
          return oceans >= card.oceanReqs;
        }else{
          return oceans <= -card.oceanReqs;
        }
      case 4,5!!!
    }
  }

  //Not complete!
  public boolean standardProjects(String action, int id){
    MarsPlayer plyr = players.get(id);
    int reduction = 0; //Standard projects card?
    switch (action){
      case "Sell Patents":
        //Do stuff;
        return true;
      case "Power Plant":
        if (plyr.money >= 11 - reduction){
          plyr.moneyChange(-11 + reduction);
          plyr.energyProduction ++;
          return true;
        }else{
          System.out.println("Not enough money!");
          return false;
        }
      case "Asteroid":
        if (plyr.money >= 14 - reduction){
          plyr.moneyChange(-14 + reduction);
          tempIncrease(plyr);
          return true;
        }else{
          System.out.println("Not enough money!");
          return false;
        }
      case "Aquafer": //Finish this
        if (plyr.money >= 14 - reduction){
          plyr.moneyChange(-14 + reduction);
          tempIncrease(plyr);
          return true;
        }else{
          System.out.println("Not enough money!");
          return false;
        }
      case "Greenery":
        if (plyr.money >= 23 - reduction){
          plyr.moneyChange(-23 + reduction);
          putGreenery(id); !!!!!!!!!
          return true;
        }else{
          System.out.println("Not enough money!");
          return false;
        }
      case "City":
        if (plyr.money >= 25 - reduction){
          plyr.moneyChange(-25 + reduction);
          putCity(plyr);
          plyr.moneyProduction ++;
          return true;
        }else{
          System.out.println("Not enough money!");
          return false;
        }
      default:
        System.out.println("Action does not exist.");
        return false;
    }
  }

  public boolean playCard(String cardName, int id){
    MarsPlayer plyr = players.get(id);
    if (deck.searchCard(cardName) != null && plyr.hand.contains(cardName)){
      MarsCard card = deck.searchCard(cardName);
      //Check for money reduction
      int reduction = 0;
      int cost = card.cost - reduction;
      plyr.moneyChange(-cost);
      effect!;
      discardPile.add(card);
    }else{
      return false;
    }
  }

  //Status: Complete
  public boolean fundAward(String awardName, int id){
    MarsPlayer plyr = player.get(id);
    if (numAward >= 3){
      System.out.println("No more awards.");
      return false;
    }
    if (awards.containsKey(awardName) == false){
      System.out.println("Wrong award name.");
      return false;
    }
    if (awards.get(awardName)){
      System.out.println("Award already funded.");
      return false;
    }
    if (plyr.money >= awardCost[numAward]){
      plyr.moneyChange(-awardCost[numAward]);
      awards.replace(awardName,true);
      numAward ++;
      return true;
    }else{
      System.out.println("Not enough money!");
      return false;
    }
  }

  //Status: Complete
  public boolean claimMilestone(String name, int id){
    MarsPlayer plyr = player.get(id);
    if (numMilestone >= 3){
      System.out.println("No more milestones.");
      return false;
    }
    if (milestones.containsKey(name) == false){
      System.out.println("Wrong milestone name.");
      return false;
    }
    if (milestones.get(name) != 0){
      System.out.println("Milestone already claimed.");
      return false;
    }
    if (plyr.money >= 8){
      switch (name){
        case "Terraformer":
          if (plyr.rating < 35){
            System.out.println("Not qualified.");
            return false;
          }
        case "Builder":
          if (plyr.builderTags < 8){
            System.out.println("Not qualified.");
            return false;
          }
        case "Mayor":
          if (plyr.cities.size() < 3){
            System.out.println("Not qualified.");
            return false;
          }
        case "Gardner":
          if (plyr.greens.size() < 3){
            System.out.println("Not qualified.");
            return false;
          }
        case "Planner":
          if (plyr.hand.size() < 16){
            System.out.println("Not qualified.");
            return false;
          }
      }
      plyr.moneyChange(-8);
      milestones.replace(name,id);
      plyr.vp += 5;
      numMilestone ++;
      return true;
    }else{
      System.out.println("Not enough money!");
      return false;
    }
  }

}
