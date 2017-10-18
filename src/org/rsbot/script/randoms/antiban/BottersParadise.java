package org.rsbot.script.randoms.antiban;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import org.rsbot.bot.Bot;
import org.rsbot.script.*;
import org.rsbot.script.wrappers.*;

/**
 * Created by IntelliJ IDEA.
 * User: Zenzie
 * Date: 16-nov-2009
 * Time: 18:02:40
 */

@ScriptManifest(authors = { "Zenzie"}, category = "AntiBan", name = "BottersParadise", version = 1.337)
public class BottersParadise extends Random {

    int[] tabs = {TAB_ATTACK,TAB_CLAN,TAB_IGNORE,TAB_FRIENDS,TAB_OPTIONS,TAB_QUESTS,TAB_MAGIC,
                    TAB_MUSIC,TAB_PRAYER,TAB_EQUIPMENT,INTERFACE_TAB_EMOTES};
    int speed = 8;
    int maxYTab = 52;
    int maxXTab = 64;
    int[] stats = { STAT_ATTACK, STAT_DEFENSE, STAT_STRENGTH,
            STAT_HITPOINTS, STAT_RANGE, STAT_PRAYER, STAT_MAGIC, STAT_COOKING, STAT_WOODCUTTING,
            STAT_FLETCHING, STAT_FISHING, STAT_FIREMAKING, STAT_CRAFTING, STAT_SMITHING,
            STAT_MINING, STAT_HERBLORE, STAT_AGILITY, STAT_THIEVING, STAT_SLAYER, STAT_FARMING,
            STAT_RUNECRAFTING, STAT_HUNTER, STAT_CONSTRUCTION, STAT_SUMMONING};

    public int randomTime = random(60000, 180000);
    public long lastCheck = System.currentTimeMillis();


    int randGenerator(int min, int max) {
        return min + (int) (java.lang.Math.random() * (max - min));
    }

    @Override
    protected int getMouseSpeed() {
        return speed;
    }

    public boolean activateCondition() {
        return isLoggedIn() && (System.currentTimeMillis() - lastCheck) >= randomTime;
    }

    public int loop() {
        if (isLoggedIn() && (System.currentTimeMillis() - lastCheck) >= randomTime) {
            lastCheck = System.currentTimeMillis();
            randomTime = random(60000, 180000);

            int wutWutInTheButt = randGenerator(0,11);
            switch(wutWutInTheButt) {
                case 1:
                    speed = (randGenerator(6,13));
                    getMouseSpeed();
                    log("Mouse Speed - Changed mouse speed to: " + speed);
                break;

                case 2:
                    int movedMouse = 0;
                    for(int i= 1; i < 11; i++) {
                        int randomMouse = randGenerator(0,3);
                        if(randomMouse == 1) {
                            movedMouse++;
                            moveMouse(1, 1, 760, 500);
                            wait(random(500,1000));
                            if(movedMouse <= 1) {
                                log("Moved Mouse - " + movedMouse + " Time");
                            } else {
                                log("Moved Mouse - " + movedMouse + " Times");
                            }
                        }
                    }
                break;

                case 3:
                    int randomShit = randGenerator(0,4);
                    if(randomShit == 1) {
                        int randTab = tabs[randGenerator(0,tabs.length)];
                        if(getCurrentTab() != randTab) {
                            openTab(randTab);
                            log("Random Tab - Opened random tab");
                        }
                        int moveMouseOrNot = randGenerator(0,4);
                        if(moveMouseOrNot == 1 || moveMouseOrNot == 2) {
                            moveMouse(550, 209, 730, 465);
                            log("Random Tab - Moved mouse in tab");
                        }
                        int backToInvent = randGenerator(0,4);
                        if(backToInvent == 1 || backToInvent == 2) {
                            openTab(Constants.TAB_INVENTORY);
                            log("Random Tab - Switched back to invent");
                        }
                    }
                break;

                case 4:
                    if(!getMyPlayer().isInCombat() || getMyPlayer().getInteracting() != null) {
                        int randomTime = randGenerator(500,15000);
                        wait(randomTime);
                        log("AFK - " + randomTime + " Ms");
                    } else {
                        log("AFK - We're under attack");
                    }
                break;

                case 5:
                    hoverPlayer();
                    wait(randGenerator(750,3000));
                    while (isMenuOpen()) {
                        moveMouseRandomly(750);
                        wait(random(100, 500));
                }
                break;

                case 6:
                    int randomTurn = randGenerator(0,5);
                    switch(randomTurn) {
                        case 1:
                        case 2:
                            log("Camera - Turned camera");
                            new CameraRotateThread().start();
                        break;

                        case 3:
                            log("Camera - Changed camera height");
                            new CameraHeightThread().start();
                        break;
                        case 4:
                            int randomFormation = randGenerator(0,3);
                            if(randomFormation == 1) {
                                log("Camera - Turned camera and changed height");
                                new CameraRotateThread().start();
                                new CameraHeightThread().start();
                            } else {
                                log("Camera - Changed height and turned camera");
                                new CameraHeightThread().start();
                                new CameraRotateThread().start();
                            }
                        break;
                    }
                break;

                case 7:
                    examineRandomObject(5);
                    wait(randGenerator(750,3000));
                    int moveMouseAfter2 = randGenerator(0,4);
                    wait(randGenerator(200,3000));
                    if(moveMouseAfter2 == 1 && moveMouseAfter2 == 2) {
                        moveMouse(1, 1, 760, 500);
                        log("Examine Object - Moved mouse after");
                    }
                break;

                case 8:
                    if (getCurrentTab() != Constants.TAB_INVENTORY && !RSInterface.getInterface(Constants.INTERFACE_BANK)
                            .isValid() && !RSInterface.getInterface(Constants.INTERFACE_STORE).isValid()) {
                        openTab(Constants.TAB_INVENTORY);
                        log("Hover item - Opened Inventory");
                    }

                    if (getCurrentTab() != Constants.TAB_INVENTORY && !RSInterface.getInterface(Constants.INTERFACE_BANK)
                            .isValid() && !RSInterface.getInterface(Constants.INTERFACE_STORE).isValid()) {
                        openTab(Constants.TAB_INVENTORY);
                        log("Hover item - Opened Inventory");
                    }

                    int[] items = getInventoryArray();
                    java.util.List<Integer> possible = new ArrayList<Integer>();
                    for (int i = 0; i < items.length; i++) {
                        if (items[i] > 1) {
                            possible.add(i);
                        }
                    }
                    if (possible.size() == 0) {
                        log("Hover Item - No items in inventory");
                    }
                    if(possible != null && possible.size() >= 1) {
                    int idx = possible.get(random(0, possible.size()));
                    Point t = getInventoryItemPoint(idx);
                    try {
                        if(idx != -1) {
                            moveMouse(t, 5, 5);
                            int rightClickOrNot = randGenerator(0,3);
                                if(rightClickOrNot == 1 || rightClickOrNot == 2) {
                                    clickMouse(false);
                                    log("Hover item - Right clicked item");
                                } else {
                                    log("Hover item - Hovered item");
                                }
                                int moveAfter = randGenerator(0,3);
                                if(moveAfter == 1 || moveAfter == 2) {
                                    moveMouse(1, 1, 760, 500);
                                    log("Hover item - Moved mouse after");
                                }
                        } else {
                            log("Hover item - No items in inventory");
                        }
                    } catch (final Exception e) {
                        log("Hover item - Error hovering item");
                    }
                    }
                break;

                case 9:
                        if(getCurrentTab() != TAB_STATS) {
                            openTab(TAB_STATS);
                            log("Stats Tab - Opened stats tab");
                            int hoveredSkill = 0;
                            int shouldHover = randGenerator(0,4);
                            for(int i= 1; i < 5; i++) {
                                if(shouldHover == 1 || shouldHover == 2 || shouldHover == 3) {
                                    int randomStat = stats[randGenerator(0, stats.length)];
                                    hoveredSkill++;
                                    moveMouse(getStatX(randomStat), getStatY(randomStat), maxXTab, maxYTab);
                                    if(hoveredSkill <= 1) {
                                        log("Stats Tab - Hovered " + hoveredSkill + " skill");
                                    } else {
                                        log("Stats Tab - Hovered " + hoveredSkill + " skills");
                                    }
                                    wait(randGenerator(500,7000));
                                }
                            }
                        }
                        int backToInvent = randGenerator(0,3);
                        if(backToInvent == 1) {
                            openTab(Constants.TAB_INVENTORY);
                            log("Stats Tab - Switched back to inventory");
                         }
                    break;

            default: //Default, skipping
                    log("BottersParadise - Skipped AntiBan");
                break;
            }
        }
        return -1;
    }

    int getStatX(int id) {
        switch(id) {
            case STAT_ATTACK: return 552;
            case STAT_STRENGTH: return 552;
            case STAT_DEFENSE: return 552;
            case STAT_RANGE: return 552;
            case STAT_PRAYER: return 552;
            case STAT_MAGIC: return 552;
            case STAT_RUNECRAFTING: return 552;
            case STAT_HITPOINTS: return 606;
            case STAT_AGILITY: return 606;
            case STAT_HERBLORE: return 606;
            case STAT_THIEVING: return 606;
            case STAT_CRAFTING: return 606;
            case STAT_FLETCHING: return 606;
            case STAT_SLAYER: return 606;
            case STAT_MINING: return 660;
            case STAT_SMITHING: return 660;
            case STAT_FISHING: return 660;
            case STAT_COOKING: return 660;
            case STAT_FIREMAKING: return 660;
            case STAT_WOODCUTTING: return 660;
            case STAT_FARMING: return 660;
        }
        log("Stats Tab - Error getting stats X-coordinates - Random move on screen");
        return randGenerator(1,760);
    }

    int getStatY(int id) {
        switch(id) {
            case STAT_ATTACK: return 229;
            case STAT_STRENGTH: return 262;
            case STAT_DEFENSE: return 294;
            case STAT_RANGE: return 326;
            case STAT_PRAYER: return 358;
            case STAT_MAGIC: return 390;
            case STAT_RUNECRAFTING: return 422;
            case STAT_HITPOINTS: return 229;
            case STAT_AGILITY: return 262;
            case STAT_HERBLORE: return 294;
            case STAT_THIEVING: return 326;
            case STAT_CRAFTING: return 358;
            case STAT_FLETCHING: return 390;
            case STAT_SLAYER: return 422;
            case STAT_MINING: return 229;
            case STAT_SMITHING: return 262;
            case STAT_FISHING: return 294;
            case STAT_COOKING: return 326;
            case STAT_FIREMAKING: return 358;
            case STAT_WOODCUTTING: return 390;
            case STAT_FARMING: return 422;
        }
        log("Stats Tab - Error getting stats Y-coordinates - Random move on screen");
        return randGenerator(1,500);
    }

@Override
public void onFinish() {
}

    boolean hoverPlayer() {
        RSPlayer player = null;
        int[] validPlayers = Bot.getClient().getRSPlayerIndexArray();
        org.rsbot.accessors.RSPlayer[] players = Bot.getClient().getRSPlayerArray();

        for (int element : validPlayers) {
            if (players[element] == null) {
                continue;
            }

            player = new RSPlayer(players[element]);
            String playerName = player.getName();
            String myPlayerName = getMyPlayer().getName();
            if(playerName.equals(myPlayerName)) {
                continue;
            }
            try {
                RSTile targetLoc = player.getLocation();
                String name = player.getName();
                Point checkPlayer = Calculations.tileToScreen(targetLoc);
                if(pointOnScreen(checkPlayer) && checkPlayer != null) {
                    clickMouse(checkPlayer, 5, 5, false);
                    log("Hover Player - Right click on " + name);
                } else {
                    continue;
                }
            return true;
            } catch (Exception ignored) {
            }
        }
        return player != null;
    }

    public RSTile examineRandomObject(int scans) {
        RSTile start = getMyPlayer().getLocation();
        ArrayList<RSTile> possibleTiles = new ArrayList<RSTile>();
        for(int h = 1; h < scans * scans; h += 2) {
            for(int i = 0; i < h; i++) {
                for(int j = 0; j < h; j++) {
                    int offset = (h + 1)/2 - 1;
                    if(i > 0 && i < h - 1) {
                        j = h - 1;
                    }
                    RSTile tile = new RSTile(start.getX() - offset + i, start.getY() - offset + j);
                    RSObject objectToList = getObjectAt(tile);

                    if(objectToList!= null && objectToList.getType() == 0 && tileOnScreen(objectToList.getLocation())
                            && objectToList.getLocation().isValid()) {
                        possibleTiles.add(objectToList.getLocation());
                    }
                }
            }
        }
        if (possibleTiles.size() == 0) {
            log("Examine Object - Found no object");
            return null;
        }
        if(possibleTiles.size() > 0 && possibleTiles != null) {
            final RSTile objectLoc = possibleTiles.get(randGenerator(0,possibleTiles.size()));
            Point objectPoint = objectLoc.getScreenLocation();
            if(objectPoint != null) {
                log("Examine Object - Found object at: RSTile " + objectLoc.getX() + ", " + objectLoc.getY());
                try {
                    moveMouse(objectPoint);
                    if(atMenu("xamine")) {
                        log("Examine Object - Examined object");
                    } else {
                        log("Examine Object - Error examine");
                    }
                    wait(random(500,1000));
                } catch(NullPointerException ignored) {}
            }
        }
        return null;
    }

    public class CameraRotateThread extends Thread {

        @Override
        public void run() {
        char LR = KeyEvent.VK_RIGHT;
            if (randGenerator(0, 2) == 0) {
                LR = KeyEvent.VK_LEFT;
            }
            Bot.getInputManager().pressKey(LR);
            try {
                Thread.sleep(randGenerator(100, 3000));
            } catch (final Exception ignored) {}
                Bot.getInputManager().releaseKey(LR);
        }
    }

    public class CameraHeightThread extends Thread {

        @Override
        public void run() {
        char UD = KeyEvent.VK_UP;
            if (randGenerator(0, 2) == 0) {
                UD = KeyEvent.VK_DOWN;
            }
            Bot.getInputManager().pressKey(UD);
            try {
                Thread.sleep(randGenerator(100, 2000));
            } catch (final Exception ignored) {}
                Bot.getInputManager().releaseKey(UD);
        }
    }
}