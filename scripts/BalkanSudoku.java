import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.*; 
import java.net.*; 
import javax.swing.JOptionPane; 
import javax.swing.JFileChooser;  
import javax.imageio.ImageIO;

import org.rsbot.bot.Bot;
import org.rsbot.bot.input.Mouse;
import org.rsbot.event.listeners.PaintListener;

import org.rsbot.event.events.ServerMessageEvent;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.event.listeners.ServerMessageListener;
import org.rsbot.script.Script;

import org.rsbot.script.ScriptManifest;

import java.util.Map; //import java.util.Timer;
//import java.util.TimerTask;

import org.rsbot.script.wrappers.RSNPC;

import org.rsbot.script.wrappers.RSTile;

@ScriptManifest(authors = { "Balkan" }, 
        category = "Money", 
        name = "BalkanSudokuSolver", 
        version = 1.5, 
        description = "<style type='text/css'>body {background:url('http://i68.servimg.com/u/f68/11/28/85/87/bss10.png') no-repeat}</style><html><head><center><head></td><td><center>" +
		"<html><body>" +
                "<br><br><br><br><br><br><br><br><br><B>Please go to this link for help:</B><br>" +
                "<p>http://www.rsbot.org/vb/showthread.php?t=265904<br>" +
                "<br><br><br><br><br><br>All updates will be sent via email! So stay alert! :)" +
                "</body></html>")
public class BalkanSudoku extends Script implements PaintListener,
        ServerMessageListener {
    // Variables
    private int naturePrice;
    private int cosmicPrice;
    private int bloodPrice;
    private int deathPrice;
    private int chaosPrice;
    private int mindPrice;//231 3
    private int soulPrice;
    private int bodyPrice;
    private int lawPrice;

    private int natureInvID = 561;//
    private int cosmicInvID = 564;//
    private int bloodInvID = 565;//
    private int deathInvID = 560;//
    private int chaosInvID = 562;//
    private int mindInvID = 558;//
    private int soulInvID = 566;//
    private int bodyInvID = 559;//
    private int lawInvID = 563;//

    private int profit = 0;
    private int startMoney = 0;
    private int startCash = 0;
    private int startRunes = 0;

    private int currentMoney = 0;
    private int currentCash = 0;
    private int currentRunes = 0;

    private int solved = 0;
    private int failed = 0;

    // Mouse speed
    boolean messedUp = false;
    boolean sudokuOpen = false;
    boolean sudokuSolved = false;
    boolean talked = false;

    private int[][] sudokuGrid = new int[9][9];

    private int mindID = 8982; // 1 205
    private int fireID = 8980; // 2 206
    private int airID = 8975; // 3 204
    private int waterID = 8987; // 4 203
    private int earthID = 8979; // 5 202
    private int bodyID = 8976; // 6 207
    private int deathID = 8978; // 7 208
    private int chaosID = 8977;// 8 209
    private int lawID = 8981; // 9 210

    long scriptStartTIME = 0;

    // Npcs
    int aliID = 1862;

    // Items
    int moneyID = 995;

    // Objects

    // Areas
    private final int[] aliArea = { 3300, 3208, 3308, 3214 };
    private RSTile aliTile = new RSTile(3305, 3211);

    // walk


    private enum State {
        buyRunes, openSudoku, doSudoku, toAli, end, error;
    }

    	int mSpeed = 1;
    	@Override
    	protected int getMouseSpeed() {
        	return mSpeed;
    	
	}

	@Override
    	public void clickMouse(final int x, final int y, final int width,
			final int height, final boolean leftClick) {
		moveMouse(x, y, width, height);
		wait(random(20, 50));
		clickMouse(leftClick, 0);
	}
	@Override
	public void clickMouse(final boolean leftClick, final int moveAfterDist) {
		input.clickMouse(leftClick);
	}

	public boolean setMouseSpeed(int mouseSpeed) {
		mSpeed=mouseSpeed;
		getMouseSpeed();
		return true;
	}
	private double getVersion(){
		return getClass().getAnnotation(ScriptManifest.class).version();
	}

    private State getState() {
        try {
            if (inArea(aliArea)) {
                if (getInventoryCount(moneyID) < 70000) {
                    return State.end;
                }
                if (getInventoryCount(moneyID) >= 70000) {
                    if (sudokuOpen == false) {
                        sudokuSolved = false;
                        if (getInterface(228).getChild(2).containsText(
                                "Buy all")) {
                            return State.buyRunes;
                        }
                        if (!getInterface(228).getChild(2).containsText(
                                "Buy all")) {
                            return State.openSudoku;
                        }
                    }
                    if (sudokuOpen == true) {
                        return State.doSudoku;
                    }
                }
            }
            if (!inArea(aliArea)) {
                return State.toAli;
            }

        } catch (Exception e) {
        }
        return State.error;
    }

    @Override
    public int loop() {
        // log("started");
        // log("startMoney = "+startMoney);
        // log("startCash = "+startCash);
        // log("startRunes = "+startRunes);

        // log("currentMoney = "+currentMoney);
        // log("currentCash = "+currentCash);
        // log("currentRunes = "+currentRunes);

      //  log("profit = " + profit);

        if (getInterface(288).getChild(9).containsText("open casket")) {
            sudokuOpen = true;
        }
        if (!getInterface(288).getChild(9).containsText("open casket")) {
            sudokuOpen = false;
        }
        exitGeneralStore();
        if (messedUp == true) {
       //     log("closing interface");
            talked = false;
            sudokuOpen = false;
            messedUp = false;
            failed++;
            resetGrid();
            atInterface(288, 212);
            wait(random(500, 750));

        }

        try {
            switch (getState()) {
            case toAli:

                toAli();
                break;

            case openSudoku:

                openSudoku();

                break;
            case doSudoku:
                doSudoku();

                break;

            case buyRunes:
                buyRunes();
                break;
            case end:
                stopScript();
                break;
            }
        } catch (Exception e) {
        }
        calculateProfit();
      //  log("" + getState());
      //  log("sudokuOpen = " + sudokuOpen);
      //  log("sudokuSolved = " + sudokuSolved);
      //  log("messedUp = " + messedUp);

        return 200;
    }
 public void antiBan(){
		int randomNum = random(1, 30);
		int r = random(1,35);
		if (randomNum == 6){
			if (r == 1){
				setCameraRotation(random(100, 360));
				wait(random(200, 400));
			}
			if (r == 2){
				setCameraRotation(random(100, 360));
				wait(random(200, 400));
			}
			if (r == 3){
				setCameraRotation(random(100, 360));
				wait(random(200, 400));
			}
			if (r == 4){
				setCameraRotation(random(100, 360));
				wait(random(200, 400));
			}
		
			}
		}
    

    private void getPrices() {
        naturePrice = grandExchange.loadItemInfo(natureInvID).getMarketPrice();
        cosmicPrice = grandExchange.loadItemInfo(cosmicInvID).getMarketPrice();
        bloodPrice = grandExchange.loadItemInfo(bloodInvID).getMarketPrice();
        deathPrice = grandExchange.loadItemInfo(deathInvID).getMarketPrice();
        chaosPrice = grandExchange.loadItemInfo(chaosInvID).getMarketPrice();
        mindPrice = grandExchange.loadItemInfo(mindInvID).getMarketPrice();
        soulPrice = grandExchange.loadItemInfo(soulInvID).getMarketPrice();
        bodyPrice = grandExchange.loadItemInfo(bodyInvID).getMarketPrice();
        lawPrice = grandExchange.loadItemInfo(lawInvID).getMarketPrice();

        startCash = getInventoryCount(995);
        startRunes = (naturePrice * getInventoryCount(natureInvID)
                + cosmicPrice * getInventoryCount(cosmicInvID) + bloodPrice
                * getInventoryCount(bloodInvID) + deathPrice
                * getInventoryCount(deathInvID) + chaosPrice
                * getInventoryCount(chaosInvID) + mindPrice
                * getInventoryCount(mindInvID) + soulPrice
                * getInventoryCount(soulInvID) + bodyPrice
                * getInventoryCount(bodyInvID) + lawPrice
                * getInventoryCount(lawInvID));
        startMoney = startCash + startRunes;

    }

   	 private void calculateProfit() {

        currentCash = getInventoryCount(995);
        currentRunes = (naturePrice * getInventoryCount(natureInvID)
                + cosmicPrice * getInventoryCount(cosmicInvID) + bloodPrice
                * getInventoryCount(bloodInvID) + deathPrice
                * getInventoryCount(deathInvID) + chaosPrice
                * getInventoryCount(chaosInvID) + mindPrice
                * getInventoryCount(mindInvID) + soulPrice
                * getInventoryCount(soulInvID) + bodyPrice
                * getInventoryCount(bodyInvID) + lawPrice
                * getInventoryCount(lawInvID));

        currentMoney = currentCash + currentRunes; // actual =

        profit = (int) ((currentMoney - startMoney));

    }

    private void toAli() {
        walkTo(aliTile, 2, 2);
    }

    private void openSudoku() {
        while (getNearestNPCByID(aliID) != null && talked == false) {
            RSNPC npc = (getNearestNPCByID(aliID));
            	setMouseSpeed(6);
		atNPC(npc, "Talk-to");
		antiBan();
		setMouseSpeed(6);
		talked = true;
	wait(250+random(250,500));
        }
        while ((getInterface(241).getChild(4)
                .containsText("How's the adventuring"))) {
            atInterface(241, 5);
	wait(250+random(250,500));
        }
        while ((getInterface(228).getChild(2).containsText("Hi, Ali. Not bad"))) {
            atInterface(228, 2);
	wait(250+random(250,500));
        }
            
        while ((getInterface(64).getChild(4).containsText("Hi Ali"))) {
            atInterface(64, 5);
	wait(250+random(250,500));
        }
        while ((getInterface(241).getChild(4).containsText("Still selling"))) {
            atInterface(241, 5);            
	wait(250+random(250,500));

        }
        while ((getInterface(241).getChild(4).containsText("Sigh"))) {
            atInterface(241, 5);
	wait(250+random(250,500)); 
        }
        while ((getInterface(64).getChild(4).containsText("What's up with you"))) {
            atInterface(64, 5);
	wait(250+random(250,500)); 
        }
        while ((getInterface(243).getChild(4)
                .containsText("Well, I always fancied"))) {
            atInterface(243, 7);
	wait(250+random(250,500)); 
        }
        while ((getInterface(64).getChild(4)
                .containsText("Is there anything I could do"))) {
            atInterface(64, 5);
	wait(250+random(250,500)); 

        }
        while ((getInterface(241).getChild(4)
                .containsText("How's the adventuring"))) {
            atInterface(241, 5);
	wait(250+random(250,500)); 

        }
        while ((getInterface(244).getChild(4).containsText("I have a friend"))) {
            atInterface(244, 8);
	wait(250+random(250,500)); 

        }
        while ((getInterface(64).getChild(4)
                .containsText("Maybe I could give it a look"))) {
            atInterface(64, 5);
	wait(250+random(250,500)); 
        }
        while ((getInterface(232).getChild(4)
                .containsText("selection of runes"))) {
            atInterface(232, 4);
	wait(250+random(250,500)); 
        }
        while ((getInterface(232).getChild(3)
                .containsText("selection of runes"))) {
            atInterface(232, 3);
	wait(250+random(250,500)); 
        }
        while ((getInterface(234).getChild(4)
                .containsText("selection of runes"))) {
            atInterface(234, 4);
	wait(250+random(250,500)); 
        }
        while ((getInterface(230).getChild(2)
                .containsText("selection of runes"))) {
            atInterface(230, 2);
	wait(250+random(250,500)); 
        }
        while ((getInterface(241).getChild(4).containsText("Hang on"))) {
            atInterface(241, 5);
	wait(250+random(250,500)); ;
        }
        while ((getInterface(233).getChild(3)
                .containsText("large casket of runes"))) {
            atInterface(233, 3);
	wait(250+random(250,500)); 

        }
        while ((getInterface(231).getChild(3)
                .containsText("large casket of runes"))) {
            atInterface(231, 3);
	wait(250+random(250,500)); 

        }
        while ((getInterface(235).getChild(3)
                .containsText("large casket of runes"))) {
            atInterface(235, 3);
	wait(250+random(250,500)); 
        }
        while ((getInterface(64).getChild(4).containsText("On second thoughts"))) {
            talked = false;
            atInterface(64, 5);
  	wait(250+random(250,500)); 

        }
        while ((getInterface(65).getChild(4)
		.containsText("I'm feeling lucky"))) {
            atInterface(65, 6);
	wait(250+random(250,500)); 
        }
        while ((getInterface(64).getChild(4).containsText("Who did you want me"))) {
            atInterface(64, 5);
	wait(250+random(250,500));   
	}
        while ((getInterface(64).getChild(4).containsText("Right, so I'll go"))) {
            atInterface(64, 5);
	wait(250+random(250,500));   
	}
        while ((getInterface(241).getChild(4).containsText("Good, Good"))) {
            atInterface(241, 5);
	wait(250+random(250,500)); 
	}
        while ((getInterface(64).getChild(4).containsText("I'll get to it right"))) {
            atInterface(64, 5);
	wait(250+random(250,500)); 
	}
        while ((getInterface(241).getChild(4).containsText("Go see Siamun in"))) {
            atInterface(241, 5);
	wait(250+random(250,500));  
	}
        while ((getInterface(242).getChild(3).containsText("Ali Morrisane"))) {
            atInterface(242, 6);
	wait(250+random(250,500));   
	}
        while ((getInterface(230).getChild(4).containsText("Examine lock"))) {
            atInterface(230, 4);
	wait(250+random(250,500)); 
            sudokuOpen = true;
		antiBan();
             
        }

    }



    public void solveSudoku() {
	setMouseSpeed(0);
        for (int k = 0; k < 9; k++) {
            switch (k) {
            case 0:
                atInterface(288, 205);
                for (int r = 0; r < 9; r++) {
                    for (int c = 0; c < 9; c++) {
                        int i = (11 + c * 2 + r * 18);
                        if ((sudokuGrid[r][c] == 1)
                                && !(getInterface(288).getChild(i).getModelID() == mindID)) {
                            atInterface(288, i);
                         //wait(random(0, 0));                                                                          

                        }
                    }
                }
                break;
            case 1:
                atInterface(288, 206);
                for (int r = 0; r < 9; r++) {
                    for (int c = 0; c < 9; c++) {
                        int i = (11 + c * 2 + r * 18);
                        if (sudokuGrid[r][c] == 2
                                && !(getInterface(288).getChild(i).getModelID() == fireID)) {
                            atInterface(288, i);
                         //wait(random(0, 0));                                              

                        }
                    }
                }
                break;
            case 2:
                atInterface(288, 204);
                for (int r = 0; r < 9; r++) {
                    for (int c = 0; c < 9; c++) {
                        int i = (11 + c * 2 + r * 18);
                        if (sudokuGrid[r][c] == 3
                                && !(getInterface(288).getChild(i).getModelID() == airID)) {
                            atInterface(288, i);
                         //wait(random(0, 0));                                                                          

                        }
                    }
                }
                break;
            case 3:
                atInterface(288, 203);
                for (int r = 0; r < 9; r++) {
                    for (int c = 0; c < 9; c++) {
                        int i = (11 + c * 2 + r * 18);
                        if (sudokuGrid[r][c] == 4
                                && !(getInterface(288).getChild(i).getModelID() == waterID)) {
                            atInterface(288, i);
                         //wait(random(0, 0));                                                                          

                        }
                    }
                }
                break;
            case 4:
                atInterface(288, 202);
                for (int r = 0; r < 9; r++) {
                    for (int c = 0; c < 9; c++) {
                        int i = (11 + c * 2 + r * 18);
                        if (sudokuGrid[r][c] == 5
                                && !(getInterface(288).getChild(i).getModelID() == earthID)) {
                            atInterface(288, i);
                         //wait(random(0, 0));                                                                          

                        }
                    }
                }
                break;
            case 5:
                atInterface(288, 207);
                for (int r = 0; r < 9; r++) {
                    for (int c = 0; c < 9; c++) {
                        int i = (11 + c * 2 + r * 18);
                        if (sudokuGrid[r][c] == 6
                                && !(getInterface(288).getChild(i).getModelID() == bodyID)) {
                            atInterface(288, i);
                          //wait(random(0, 0));                                                                         

                        }
                    }
                }
                break;
            case 6:
                atInterface(288, 208);
                for (int r = 0; r < 9; r++) {
                    for (int c = 0; c < 9; c++) {
                        int i = (11 + c * 2 + r * 18);
                        if (sudokuGrid[r][c] == 7
                                && !(getInterface(288).getChild(i).getModelID() == deathID)) {
                            atInterface(288, i);
                         //wait(random(0, 0));                                                                          

                        }
                    }
                }
                break;
            case 7:
                atInterface(288, 209);
                for (int r = 0; r < 9; r++) {
                    for (int c = 0; c < 9; c++) {
                        int i = (11 + c * 2 + r * 18);
                        if (sudokuGrid[r][c] == 8
                                && !(getInterface(288).getChild(i).getModelID() == chaosID)) {
                            atInterface(288, i);
                          //wait(random(0, 0));                                                                         

                        }
                    }
                }
                break;
            case 8:
                atInterface(288, 210);
                for (int r = 0; r < 9; r++) {
                    for (int c = 0; c < 9; c++) {
                        int i = (11 + c * 2 + r * 18);
                        if (sudokuGrid[r][c] == 9
                                && !(getInterface(288).getChild(i).getModelID() == lawID)) {
                            atInterface(288, i);
                         //wait(random(0, 0));                                                                          

                        }
                    }
                }
                break;
            }

        }
   	setMouseSpeed(0);
    }

    private void doSudoku() {

        if (sudokuOpen == true && sudokuSolved == false
                && !getInterface(228).getChild(2).containsText("Buy all")) {
            makeGrid();
            if (solve(0, 0, sudokuGrid)) {
                //log("solved, waiting 10-20 secs");
                if (sudokuOpen == true
                        && !getInterface(228).getChild(2).containsText(
                                "Buy all")) {
                    for (int i = 0; i < 9; i++) {
                        wait(random(10, 15));
		antiBan();
                    }
                }

                solveSudoku();
                wait(random(8,10));
                sudokuSolved = true;
                if (getInterface(288).getChild(9).containsText("open casket")) {
                    atInterface(288, 9);
                    wait(3500+(random(175,250)));
                    sudokuOpen = false;
                    sudokuSolved = false;
                }
            }
        }
    }

    private void buyRunes() {
        atInterface(228, 2);
        resetGrid();
        talked = false;
        sudokuOpen = false;
        sudokuSolved = false;
        wait(random(75, 90));
		antiBan();
        resetGrid();
    }

    private boolean inArea(final int[] area) {
        final int x = getMyPlayer().getLocation().getX();
        final int y = getMyPlayer().getLocation().getY();
        if (x >= area[0] && x <= area[2] && y >= area[1] && y <= area[3]) {
            return true;
        }
        return false;

    }
    private boolean inGeneralStore(){
        if(getInterface(620).getChild(20).containsText("Ali's Discount Wares")){
            return true;
        }
        else{
            return false;
            }
    }
    private void exitGeneralStore(){
        if(inGeneralStore()==true){
            if(getInterface(620).getChild(18).containsAction("Close")){
                log("missclicked, closing general store");
                atInterface(620,18);
                talked = false;
                sudokuOpen = false;
                
            }
            
        }
    }	


    static boolean solve(int i, int j, int[][] cells) {
	 if (i == 9) {
            i = 0;
            if (++j == 9)
                return true;
        }
        if (cells[i][j] != 0) // skip filled cells
            return solve(i + 1, j, cells);

        for (int val = 1; val <= 9; ++val) {
            if (legal(i, j, val, cells)) {
                cells[i][j] = val;
                if (solve(i + 1, j, cells))
                    return true;
            }
        }
        cells[i][j] = 0; // reset on backtrack
        return false;
    }

    static boolean legal(int i, int j, int val, int[][] cells) {
        for (int k = 0; k < 9; ++k)
            // row
            if (val == cells[k][j])
                return false;

        for (int k = 0; k < 9; ++k)
            // col
            if (val == cells[i][k])
                return false;

        int boxRowOffset = (i / 3) * 3;
        int boxColOffset = (j / 3) * 3;
        for (int k = 0; k < 3; ++k)
            // box
            for (int m = 0; m < 3; ++m)
                if (val == cells[boxRowOffset + k][boxColOffset + m])
                    return false;

        return true; // no violations, so it's legal
    }

    BufferedImage normal = null;
    BufferedImage clicked = null;

    public boolean onStart(Map<String, String> args) {
	//AUTO UPDATER
	URLConnection url = null;
    BufferedReader in = null;
    BufferedWriter out = null;
    if(JOptionPane.showConfirmDialog(null, "Would you like to check for updates?\nNote: This connects to the internet and will write files!") == 0){
    	try{
    		url = new URL("http://balkanssudokusolver.webs.com/Scripts/BalkanSudokuVERSION.txt").openConnection();
    		in = new BufferedReader(new InputStreamReader(url.getInputStream()));
    		if(Double.parseDouble(in.readLine()) > getVersion()) {
    			if(JOptionPane.showConfirmDialog(null, "Update found! Do you want to update?") == 0){
    				JOptionPane.showMessageDialog(null, "Please choose 'BalkanSudoku.java' in your scripts folder and hit 'Open'");
    				JFileChooser fc = new JFileChooser();
                    	if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                    		url = new URL("http://balkanssudokusolver.webs.com/Scripts/BalkanSudoku.java").openConnection();
                    		in = new BufferedReader(new InputStreamReader(url.getInputStream()));
                    		out = new BufferedWriter(new FileWriter(fc.getSelectedFile().getPath()));
                    		String inp;
                    		while((inp = in.readLine()) != null){
                    			out.write(inp);
                    			out.newLine();
                    			out.flush();
                            }
                    		log("You've downloaded the latest version. Please recompile your bot!");
                    		return false;
                    	} else log("Update canceled.");
                } else log("Update canceled.");
            } else
                JOptionPane.showMessageDialog(null, "You have the latest version.");
                if(in != null)
                	in.close();
                if(out != null)
                	out.close();
        } catch (IOException e){
        	log("Problem getting latest version. Try going on the forums and getting it.");
        	return false;
        }
    }
        scriptStartTIME = System.currentTimeMillis();
	try {
            final URL cursorURL = new URL(
                    "http://i44.tinypic.com/52j4lh.jpg");
            final URL cursor80URL = new URL(
                    "http://i44.tinypic.com/52j4lh.jpg");
            normal = ImageIO.read(cursorURL);
            clicked = ImageIO.read(cursor80URL);
        } catch (MalformedURLException e) {
            log("Unable to buffer cursor.");
        } catch (IOException e) {
            log("Unable to open cursor image.");
	}
        resetGrid();
        getPrices();
        return true;
	}


    private void makeGrid() {

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int i = (11 + c * 2 + r * 18);

                if (getInterface(288).getChild(i).getModelID() == mindID) {
                    sudokuGrid[r][c] = 1;
                }
                if (getInterface(288).getChild(i).getModelID() == fireID) {
                    sudokuGrid[r][c] = 2;
                }
                if (getInterface(288).getChild(i).getModelID() == airID) {
                    sudokuGrid[r][c] = 3;
                }
                if (getInterface(288).getChild(i).getModelID() == waterID) {
                    sudokuGrid[r][c] = 4;
                }
                if (getInterface(288).getChild(i).getModelID() == earthID) {
                    sudokuGrid[r][c] = 5;
                }
                if (getInterface(288).getChild(i).getModelID() == bodyID) {
                    sudokuGrid[r][c] = 6;
                }
                if (getInterface(288).getChild(i).getModelID() == deathID) {
                    sudokuGrid[r][c] = 7;
                }
                if (getInterface(288).getChild(i).getModelID() == chaosID) {
                    sudokuGrid[r][c] = 8;
                }
                if (getInterface(288).getChild(i).getModelID() == lawID) {
                    sudokuGrid[r][c] = 9;
                }
            }
        }
    }

    private void resetGrid() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                sudokuGrid[r][c] = 0;

            }
        }

    }

    @Override
    public void onRepaint(Graphics render) {
	((Graphics2D) render).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        long runTime = 0;
        long seconds = 0;
        long minutes = 0;
        long hours = 0;
        runTime = System.currentTimeMillis() - scriptStartTIME;
        seconds = runTime / 1000;
        if (seconds >= 60) {
            minutes = seconds / 60;
            seconds -= (minutes * 60);
        }
        if (minutes >= 60) {
            hours = minutes / 60;
            minutes -= (hours * 60);
        }
        // variables
        int solvedPerHour = 0;
        int failedPerHour = 0;
        int profitPerHour = 0;

        if (runTime / 1000 > 0) {
            solvedPerHour = (int) ((3600000.0 / (double) runTime) * solved);
            failedPerHour = (int) ((3600000.0 / (double) runTime) * failed);
            profitPerHour = (int) ((3600000.0 / (double) runTime) * profit);

        }
    if (normal != null) {
        final Mouse mouse = Bot.getClient().getMouse();
        final int mouse_x = mouse.getMouseX();
        final int mouse_y = mouse.getMouseY();
        final int mouse_x2 = mouse.getMousePressX();
        final int mouse_y2 = mouse.getMousePressY();
        final long mpt = System.currentTimeMillis()
                - mouse.getMousePressTime();
        if (mouse.getMousePressTime() == -1 || mpt >= 1000) {
            render.drawImage(normal, mouse_x - 8, mouse_y - 8, null); //this show the mouse when its not clicked
        }
        if (mpt < 1000) {
            render.drawImage(clicked, mouse_x2 - 8, mouse_y2 - 8, null); //this show the four squares where you clicked.
            render.drawImage(normal, mouse_x - 8, mouse_y - 8, null); //this show the mouse as normal when its/just clicked
        }
    }

        // the paint!

        render.setColor(new Color(0, 0, 0, 175));
        render.fillRoundRect(312, 4, 203, 100, 0, 0);
        render.setColor(Color.white);
        render.draw3DRect(312, 4, 203, 100, true);
        render.drawString("Sudoku Solver", 370, 23);
        render.setColor(Color.yellow);
        render.drawString("Runtime: " + hours + ":" + minutes + ":" + seconds, 365, 41);
        render.setColor(Color.white);
        render.drawString("profit: " + profit, 317, 59);
        render.drawString("profit/hr: " + profitPerHour, 317, 77);
        render.setColor(Color.red);
        render.drawString("solved: " + solved, 430, 59);
        render.drawString("solved/hr: " + solvedPerHour, 430, 77);

    }
	public void onFinish(){
		Bot.getEventManager().removeListener(ServerMessageListener.class, this);
		Bot.getEventManager().removeListener(PaintListener.class, this);
		log("Thanks for using my script - Balkan.");
		log("Profit Made: " + profit);
	}

    public void serverMessageRecieved(final ServerMessageEvent arg0) {

        final String message = arg0.getMessage();
        if (message.contains("remove this rune")) {
            messedUp = true;
        }
        if (message.contains("locked")) {
            messedUp = true;
        }

        if (message.contains("hear the locking") && sudokuOpen == true) {
            solved++;
            resetGrid();
            sudokuOpen = false;
            // log("open");
        }

    }
}
