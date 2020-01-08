import java.util.*;

public class MarsGame{

  protected int numPlayers;
  MarsBoard board;
  protected int temperature;
  protected int generation;
  protected int oxygen;
  protected int oceans;

  final int maxTemp = 8;
  final int maxOxygen = 14;
  final int maxOcean = 9;
  MarsDeck deck;
  LinkedList<MarsCard> drawPile;
  Vector<MarsCard> discardPile;
  Vector<MarsPlayer> players;

  HashMap<String,Boolean> awards;
  HashMap<String,Integer> milestones;
  int[] awardCost;
  int numAward;
  int numMilestone;

  public MarsGame(int playerNumber){
    numPlayers = playerNumber;
    board = new MarsBoard();
    temperature = -24;
    generation = 1;
    oxygen = 0;
    oceans = 0;
    numAward = 0;
    numMilestone = 0;
    milestones = new HashMap<String,Integer>();
    milestones.put("Terraformer",0);
    milestones.put("Builder",0);
    milestones.put("Mayor",0);
    milestones.put("Gardner",0);
    milestones.put("Planner",0);
    awards = new HashMap<String,Boolean>();
    awards.put("Landlord",false);
    awards.put("Banker",false);
    awards.put("Scientist",false);
    awards.put("Miner",false);
    awards.put("Thermalist",false);
    players = new Vector<MarsPlayer>();
    awardCost = new int[]{8,14,20};
    deck = new MarsDeck(true);
    drawPile = new LinkedList<MarsCard>();
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
    while (findNextPlayer(id) != -1){
      id = findNextPlayer(id);
      playTurn(id);
    }
    endGeneration();
  }

  private int findNextPlayer(int currentId){
    int i = 1;
    while (players.get((currentId + i) % numPlayers).hardPass == true && i % numPlayers != 0){
      i ++;
    }
    if (i == numPlayers){
      if (players.get(currentId).hardPass){
        return -1;
      }else{
        return currentId;
      }
    }else{
      return (currentId + i) % numPlayers;
    }
  }

  private void singleAction(int id, Scanner sc){
    MarsPlayer plyr = players.get(id);
    System.out.println("Now is " + id + " turn!");
    System.out.println("What would you like to do?");
    String action = sc.nextLine().trim();
    switch (action) {
      case "Standard project":
        System.out.println("Which standard project are you doing?");
        String prj = sc.nextLine().trim();
        standardProjects(prj,id);
        break;
      case "Play card":
        System.out.println("Which card do you want to play?");
        String cd = sc.nextLine().trim();
        playCard(cd,id);
        break;
      case "Fund award":
        System.out.println("Which award do you want to fund?");
        String awd = sc.nextLine().trim();
        fundAward(awd,id);
        break;
      case "Claim milestone":
        System.out.println("Which milestone do you want to claim?");
        String mlstn = sc.nextLine().trim();
        claimMilestone(mlstn,id);
        break;
      case "Raise temperature":
        if (plyr.heat >= 8){
          plyr.heat -= 8;
          tempIncrease(plyr);
        }else{
          System.out.println("Not enough heat.");
        }
        break;
      case "Plant greenery":
        boolean check = (plyr.corp == "Ecoline")? true : false;
        if (check){
          if (plyr.plant >= 7){
            plyr.plant -= 7;
            putGreenery(id);
          }else{
            System.out.println("Not enough plants.");
          }
        }else{
          if (plyr.plant >= 8){
            plyr.plant -= 8;
            putGreenery(id);
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
    if (response.equals("y")){
      singleAction(id, scanner);
    }
  }

  private void endGame(){
    //milestone already counted for
    for (MarsPlayer plyr : players){
      plyr.generationProduce();
      //Add terraforming rating
      plyr.vp += plyr.rating;
    }
    int id = generation % numPlayers;
    //Put greeneries

    //awards
    endAward();

    //card points
    for (MarsPlayer plyr : players){
      for (MarsCard card: plyr.playedEvents) plyr.vp += card.vp;
      for (MarsCard card: plyr.playedPermanents){
        if (card.constantVp){
          plyr.vp += card.vp;
        }else{

        }
      }
      for (MarsCard card: plyr.playedProjects){
        if (card.constantVp){
          plyr.vp += card.vp;
        }else{

        }
      }
    }

    //board points
    for (MarsPlayer plyr : players){
      //Greenery points
      for (Boardspace space : board.getCoordinates.values()){
        if (space.item == 1 && space.id == plyr.id) plyr.vp ++;
      }
      //Greeneries around cities
      for (Boardspace space : board.getCoordinates.values()){
        if (space.item == 3 && space.id == plyr.id) plyr.vp += board.adjacentGreeneries(space.i,space.j);
      }
    }

  }

  private void endAward(){
    if (awards.get("Miner")){
      int[] m = new int[numPlayers];
      for (int i = 0; i < numPlayers; i ++){
        m[i] = players.get(i).titanium + players.get(i).steel;
      }
      countAward(m);
    }
    if (awards.get("Thermalist")){
      int[] t = new int[numPlayers];
      for (int i = 0; i < numPlayers; i ++){
        t[i] = players.get(i).heat;
      }
      countAward(t);
    }
    if (awards.get("Banker")){
      int[] b = new int[numPlayers];
      for (int i = 0; i < numPlayers; i ++){
        b[i] = players.get(i).moneyProduction;
      }
      countAward(b);
    }
    if (awards.get("Scientist")){
      int[] s = new int[numPlayers];
      for (int i = 0; i < numPlayers; i ++){
        s[i] = players.get(i).scienceTags;
      }
      countAward(s);
    }
    if (awards.get("Landlord")){
      int[] l = new int[numPlayers];
      for (Boardspace space : board.getCoordinates.values()){
        if (space.item != 0) l[space.id] ++;
      }
      countAward(l);
    }
  }

  //Status: completed
  private void countAward(int[] score){
    int max = -1;
    int occur = 0;
    for (int i = 0; i < score.length; i ++){
      if (score[i] > max){
        max = score[i];
        occur = 1;
      }else if (score[i] == max){
        occur ++;
      }
    }
    for (int i = 0; i < score.length; i ++){
      if (score[i] == max) players.get(i).vp += 5;
    }
    if (occur == 1){
      int secondMax = -1;
      for (int i = 0; i < score.length; i ++){
        if (score[i] > secondMax && score[i] < max) secondMax = score[i];
      }
      for (int i = 0; i < score.length; i ++){
        if (score[i] == secondMax) players.get(i).vp += 2;
      }
    }
  }

  private void draft(){
    for (int i = 0; i < 4; i ++){
      for (MarsPlayer plyr : players){
        plyr.tempDraftHand.add(drawPile.poll());
      }
    }
    Scanner scanner = new Scanner(System.in);
    for (int i = 0; i < 4; i ++){
      for (MarsPlayer plyr : players){
        System.out.println("What card do you want to draft?");
        String card = scanner.nextLine().trim();

      }
    }
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
        plyr.generationProduce();
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
      if (temperature == -20 || temperature == -24){
        plyr.heatProduction ++;
      }
      if (temperature == 0 && oceans < maxOcean){
        putOcean(plyr.id);
      }
    }
  }

  private void oxygenIncrease(MarsPlayer plyr){
    if (oxygen < maxOxygen){
      oxygen ++;
      plyr.ratingIncrement();
      if (oxygen == 8){
        tempIncrease(plyr);
      }
    }
  }

  private void putGreenery(int id){
    putOnBoard(id,1);
    oxygenIncrease(players.get(id));
  }

  private void putCity(int id){
    putOnBoard(id,3);
  }

  private void putOcean(int id){
    putOnBoard(id,2);
    players.get(id).ratingIncrement();
    oceans ++;
  }

  private void putOnBoard(int id, int itemNumber){
    int locX = 5;
    int locY = 5;
    Scanner scanner = new Scanner(System.in);
    while (board.checkLocation(locX,locY,itemNumber,id) == false){
      System.out.println("Choose location (x): (Press 9 to quit)");
      locX = scanner.nextInt();
      if (locX == 9) return;
      System.out.println("Choose location (y): ");
      locY = scanner.nextInt();
    }
    int location = 100 * locX + locY;
    board.getCoordinates.replace(location,board.getCoordinates.get(location).modify(id,itemNumber));
    MarsPlayer plyr = players.get(id);
    int[] placementRewards = board.getCoordinates.get(location).bonus;
    plyr.plant += placementRewards[0];
    plyr.steel += placementRewards[2];
    plyr.titanium += placementRewards[3];
    for (int i = 0; i < placementRewards[1]; i ++){
      plyr.addCard(drawCard());
    }
    plyr.money += 2 * board.adjacentOceans(locX,locY);
  }

  //Takes a legal card and check global requirements.
  private boolean checkReqs(String cardName, MarsPlayer plyr){
    MarsCard card = deck.deck.get(cardName);
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
      //More cases and default case
      default:
        return false;
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
      case "Aquifer":
        if (oceans >= maxOcean){
          System.out.println("No more oceans.");
          return false;
        }
        if (plyr.money >= 18 - reduction){
          plyr.moneyChange(-18 + reduction);
          putOcean(id);
          return true;
        }else{
          System.out.println("Not enough money!");
          return false;
        }
      case "Greenery":
        if (plyr.money >= 23 - reduction){
          plyr.moneyChange(-23 + reduction);
          putGreenery(id); //FIX!!!!
          return true;
        }else{
          System.out.println("Not enough money!");
          return false;
        }
      case "City":
        if (plyr.money >= 25 - reduction){
          plyr.moneyChange(-25 + reduction);
          putCity(id);
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
    if (deck.searchCard(cardName) != null && plyr.hand.contains(cardName) && checkReqs(cardName,plyr)){
      MarsCard card = deck.searchCard(cardName);
      //Check for money reduction
      int reduction = 0;
      int cost = card.cost - reduction;
      plyr.moneyChange(-cost);
      String type = card.type;
      if (type.equals("Events")) plyr.playedEvents.add(card);
      if (type.equals("Permanents")) plyr.playedPermanents.add(card);
      if (type.equals("Projects")) plyr.playedProjects.add(card);
      //CARD EFFECT!!!
      return true;
    }else{
      System.out.println("Play card: failed");
      return false;
    }
  }

  //Status: Complete
  public boolean fundAward(String awardName, int id){
    MarsPlayer plyr = players.get(id);
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
    MarsPlayer plyr = players.get(id);
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

  public static void main(String args[]){

  }

}
